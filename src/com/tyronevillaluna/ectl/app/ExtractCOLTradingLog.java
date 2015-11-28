package com.tyronevillaluna.ectl.app;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import com.tyronevillaluna.ectl.model.TradeLog;
import com.tyronevillaluna.ectl.model.TransactionLine;
import com.tyronevillaluna.ectl.util.PrintUtil;

public class ExtractCOLTradingLog {

	public static void main(String[] args) throws Exception {
		if (args.length < 3) {
			System.out.println("Missing arguments");
			System.out.println("1st argument: file location of historical trades from COL Financial");
			System.out.println("2nd argument: file location to save transaction line CSV");
			System.out.println("3rd argument: file location to save trade log CSV");
			System.exit(1);
		}
		
		// Read trading log
		Scanner scanner = new Scanner(new File(args[0]));
		
		Boolean isTransactionLines = false;
		Boolean findNextInvoice = true;
		List<String> invoiceTxnLines = new ArrayList<>();
		Set<String> invoiceHistory = new TreeSet<>();
		String invoiceNumber = null;
		String stockCode = null;
		String txnType = null;
		Date txnDate = null;
		Double result = 0.0;
		Double transactionExpenses = 0.0;
		Map<String, Long> holdingsMap = new TreeMap<>();
		Map<String, TradeLog> tradeLogMap = new TreeMap<>();
		List<TradeLog> completedTradeLog = new ArrayList<>();
		List<TransactionLine> allTransactionLines = new ArrayList<>();
		
		// Read each line on trading log
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine().replace("|", "").trim();

			// Process transactions per invoice
			if (findNextInvoice) {
				if (line.startsWith("INVOICE")) {
					invoiceNumber = line.split(":")[1].split("PRINT NO")[0].trim();
					
					// Skip duplicate invoices
					if (!invoiceHistory.contains(invoiceNumber)) {
						invoiceHistory.add(invoiceNumber);
						findNextInvoice = false;
					}
				}				
			}
			else if (!isTransactionLines) {

				// Read transaction date
				if (line.startsWith("TRX DATE")) {
					String txnDateStr  = line.split(":")[1].trim();
					SimpleDateFormat df = new SimpleDateFormat("MMMM dd, yyyy");
					txnDate = df.parse(txnDateStr);
				}
				// Read transaction type (eg. BOUGHT or SOLD)
				else if (line.startsWith("TRANSACTION")) {
					txnType  = line.split(":")[1].split("-")[0].trim();
				}
				// Read stock code
				else if (line.startsWith("STOCK")) {
					stockCode  = line.split(":")[1].split("-")[0].trim();
				} 
				// Inspect if succeeding lines means the transaction lines
				else if (line.startsWith("NO. OF SHARES")) {
					isTransactionLines  = true;
					invoiceTxnLines = new ArrayList<>();
				}				
			} else {
				// This mark the line prior the transction lines
				if (line.startsWith("------") && invoiceTxnLines.isEmpty()) {
					// do nothing
				} 
				// Read transaction lines
				else if (!line.startsWith("------")) {
					invoiceTxnLines.add(line);
					String[] transactionDetails = line.replaceAll("\\s+"," ").split(" ");
					
					// Read transaction line details
					TransactionLine tl = new TransactionLine();
					tl.setNumberOfShares(Long.valueOf(transactionDetails[0].replaceAll(",", "")));
					tl.setPrice(Double.valueOf(transactionDetails[1].replaceAll(",", "")));
					tl.setGrossAmount(Double.valueOf(transactionDetails[2].replaceAll(",", "")));
					tl.setCommissionAndVat(Double.valueOf(transactionDetails[3].replaceAll(",", "")));
					tl.setOtherCharges(Double.valueOf(transactionDetails[4].replaceAll(",", "")));
					tl.setDstOrSalesTax(Double.valueOf(transactionDetails[5].replaceAll(",", "")));
					tl.setTransactionDate(txnDate);
					tl.setStockCode(stockCode);
					tl.setTransactionType(txnType);
					tl.setInvoiceNumber(invoiceNumber);

					// Add to all transaction lines
					allTransactionLines.add(tl);
					
					// When transaction type is BOUGHT
					if (tl.getTransactionType().equals("BOUGHT")) {
						// Subtract net amount to trading result
						result = result - tl.getGrossAmount() - tl.getCommissionAndVat() - tl.getOtherCharges() - tl.getDstOrSalesTax();
						// Add transaction cost
						transactionExpenses = transactionExpenses + tl.getCommissionAndVat() + tl.getOtherCharges() + tl.getDstOrSalesTax();
						
						// Add shares to holdings
						addShareCount(holdingsMap, tl.getStockCode(), tl.getNumberOfShares());
						
						// Add or update corresponding trading log
						TradeLog trl = tradeLogMap.get(stockCode);
						if (trl == null) {
							trl  = new TradeLog();
							trl.setStockCode(stockCode);
							trl.setStartDate(txnDate);
							tradeLogMap.put(stockCode, trl);
						}
						// Check for minimum and maximum entry price
						if (tl.getPrice() > trl.getMaximumEntryPrice()) {
							trl.setMaximumEntryPrice(tl.getPrice());
						}
						if (tl.getPrice() < trl.getMinimumEntryPrice()) {
							trl.setMinimumEntryPrice(tl.getPrice());
						}
						// Update shares count
						trl.setCurrentShares(trl.getCurrentShares() + tl.getNumberOfShares());
						trl.setTotalShares(trl.getTotalShares() + tl.getNumberOfShares());
						
						// Update amount for the trade
						trl.setBuyAmount(trl.getBuyAmount() + tl.getGrossAmount() + tl.getCommissionAndVat() + tl.getOtherCharges() + tl.getDstOrSalesTax());
						trl.setEntryAmount(trl.getEntryAmount() + tl.getNumberOfShares()*tl.getPrice());
						trl.setTransactionCost(trl.getTransactionCost() + tl.getCommissionAndVat() + tl.getOtherCharges() + tl.getDstOrSalesTax());
						
					} 
					// When transaction type is SOLD
					else if (tl.getTransactionType().equals("SOLD")) {
						// Add net amount to trading result
						result = result + tl.getGrossAmount() - tl.getCommissionAndVat() - tl.getOtherCharges() - tl.getDstOrSalesTax();
						// Add transaction cost
						transactionExpenses = transactionExpenses + tl.getCommissionAndVat() + tl.getOtherCharges() + tl.getDstOrSalesTax();
						// Subtract shares to holdings
						addShareCount(holdingsMap, tl.getStockCode(), tl.getNumberOfShares()*-1);
						
						// Update corresponding trading log
						TradeLog trl = tradeLogMap.get(stockCode);
						// Check for minimum and maximum exit price
						if (tl.getPrice() > trl.getMaximumExitPrice()) {
							trl.setMaximumExitPrice(tl.getPrice());
						}
						if (tl.getPrice() < trl.getMinimumExitPrice()) {
							trl.setMinimumExitPrice(tl.getPrice());
						}
						// Update shares count
						trl.setCurrentShares(trl.getCurrentShares() - tl.getNumberOfShares());
						
						// Update amount for the trade
						trl.setSellAmount(trl.getSellAmount() + tl.getGrossAmount() - tl.getCommissionAndVat() - tl.getOtherCharges() - tl.getDstOrSalesTax());
						trl.setExitAmount(trl.getExitAmount() + tl.getNumberOfShares()*tl.getPrice());
						trl.setTransactionCost(trl.getTransactionCost() + tl.getCommissionAndVat() + tl.getOtherCharges() + tl.getDstOrSalesTax());
						
						// Check if trade (buy and sell) is already completed
						if (trl.getCurrentShares() == 0) {
							trl.setEndDate(txnDate);
							completedTradeLog.add(trl);
							tradeLogMap.remove(tl.getStockCode());
						}
					}
				} 
				// This marks that transaction lines are done
				else if (line.startsWith("------") && !invoiceTxnLines.isEmpty()) {
					isTransactionLines  = false;
					findNextInvoice = true;
				}
			}
		}
		
		scanner.close();
		
		// Print overall result of trading
		System.out.println("Result of trading: " + result);
		// Print total transaction expenses
		System.out.println("Transaction expenses: " + transactionExpenses);
		
		// Print current holdings
		System.out.println("Current holdings: ");
		for (Map.Entry<String, Long> entry : holdingsMap.entrySet()) {
			if (entry.getValue() != 0) {
				System.out.println(entry.getKey() + ": " + entry.getValue());
			}
		}
		
		// Write the transaction lines to file
		BufferedWriter bw = new BufferedWriter(new FileWriter(args[1]));
		bw.write(PrintUtil.tradeLogCsvHeaderLine());
		bw.write("\n");
		for (TransactionLine tl : allTransactionLines) {
			bw.write(PrintUtil.toCsvLine(tl, "yyyy-MM-dd"));
			bw.write("\n");
		}
		bw.close();

		// Write the trade logs to file
		bw = new BufferedWriter(new FileWriter(args[2]));
		bw.write(PrintUtil.transactionLogCsvHeaderLine());
		bw.write("\n");
		for (TradeLog log : completedTradeLog) {
			bw.write(PrintUtil.toCsvLine(log, "yyyy-MM-dd"));
			bw.write("\n");
		}
	}

	public static void addShareCount(Map<String, Long> holdingsMap, String code, Long shares) {
		Long totalShares = holdingsMap.get(code);
		if (totalShares == null) {
			totalShares = 0L;
		}
		totalShares = totalShares + shares;
		holdingsMap.put(code, totalShares);
	}
}
