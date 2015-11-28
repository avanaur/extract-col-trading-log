package com.tyronevillaluna.ectl.util;

import java.text.SimpleDateFormat;

import com.tyronevillaluna.ectl.model.TradeLog;
import com.tyronevillaluna.ectl.model.TransactionLine;

public class PrintUtil {
	public static String tradeLogCsvHeaderLine() {
		return String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s", 
				"Start Date",
				"End Date",
				"Duration in days" ,
				"Stock code",
				"Total shares traded",
				"Average entry price",
				"Entry price range",
				"Average exit price",
				"Exit price range",
				"Net buy amount",
				"Net sell amount",
				"Gain/Loss percentage - excl cost",
				"Gain/Loss amount",
				"Transaction cost");
	}
	
	public static String toCsvLine(TradeLog log, String dateFormat) {
		SimpleDateFormat df = new SimpleDateFormat(dateFormat);
		
		return String.format("%s,%s,%d,%s,%d,%.5f,%.5f-%.5f,%.5f,%.5f-%.5f,%.2f,%.2f,%.2f,%.2f,%.2f", 
				df.format(log.getStartDate()),
				df.format(log.getEndDate()),
				(log.getEndDate().getTime() - log.getStartDate().getTime())/ (1000 * 60 * 60 * 24) ,
				log.getStockCode(),
				log.getTotalShares(),
				log.getAverageEntryPrice(),
				log.getMinimumEntryPrice(),
				log.getMaximumEntryPrice(),
				log.getAverageExitPrice(),
				log.getMinimumExitPrice(),
				log.getMaximumExitPrice(),
				log.getBuyAmount(),
				log.getSellAmount(),
				100*(log.getAverageExitPrice()-log.getAverageEntryPrice())/log.getAverageEntryPrice(),
				log.getGainLossAmount(),
				log.getTransactionCost());
	
	}
	
	public static String transactionLogCsvHeaderLine() {
		return String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s,%s", 
				"Invoice nuber",
				"Transaction Date",
				"Stock code",
				"Transaction Type",
				"Price",
				"Number of shares",
				"Gross Amount",
				"Commission and VAT",
				"Other Charges",
				"DST or Sales Tax");
	}
	public static String toCsvLine(TransactionLine txn, String dateFormat) {
		SimpleDateFormat df = new SimpleDateFormat(dateFormat);

		return String.format("%s,%s,%s,%s,%.5f,%d,%.2f,%.2f,%.2f,%.2f", 
				txn.getInvoiceNumber(),
				df.format(txn.getTransactionDate()),
				txn.getStockCode(),
				txn.getTransactionType(),
				txn.getPrice(),
				txn.getNumberOfShares(),
				txn.getGrossAmount(),
				txn.getCommissionAndVat(),
				txn.getOtherCharges(),
				txn.getDstOrSalesTax());
	
	
	}
}
