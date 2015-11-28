package com.tyronevillaluna.ectl.model;

import java.util.Date;

public class TransactionLine {
	private Long numberOfShares;
	private Double price;
	private Double grossAmount;
	private Double commissionAndVat;
	private Double otherCharges;
	private Double dstOrSalesTax;
	private Date transactionDate;
	private String transactionType;
	private String stockCode;
	private String invoiceNumber;

	public Long getNumberOfShares() {
		return numberOfShares;
	}

	public void setNumberOfShares(Long numberOfShares) {
		this.numberOfShares = numberOfShares;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Double getGrossAmount() {
		return grossAmount;
	}

	public void setGrossAmount(Double grossAmount) {
		this.grossAmount = grossAmount;
	}

	public Double getCommissionAndVat() {
		return commissionAndVat;
	}

	public void setCommissionAndVat(Double commissionAndVat) {
		this.commissionAndVat = commissionAndVat;
	}

	public Double getOtherCharges() {
		return otherCharges;
	}

	public void setOtherCharges(Double otherCharges) {
		this.otherCharges = otherCharges;
	}

	public Double getDstOrSalesTax() {
		return dstOrSalesTax;
	}

	public void setDstOrSalesTax(Double dstOrSalesTax) {
		this.dstOrSalesTax = dstOrSalesTax;
	}

	public Date getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public String getStockCode() {
		return stockCode;
	}

	public void setStockCode(String stockCode) {
		this.stockCode = stockCode;
	}

	public String getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}
}
