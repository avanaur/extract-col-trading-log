package com.tyronevillaluna.ectl.model;

import java.util.Date;

public class TradeLog {
	private String stockCode;
	private Long currentShares = 0L;
	private Long totalShares = 0L;
	private Double minimumEntryPrice = Double.MAX_VALUE;
	private Double maximumEntryPrice = Double.MIN_VALUE;
	private Double minimumExitPrice = Double.MAX_VALUE;
	private Double maximumExitPrice = Double.MIN_VALUE;
	private Double entryAmount = 0.0;
	private Double exitAmount = 0.0;
	private Double sellAmount = 0.0;
	private Double buyAmount = 0.0;
	private Double transactionCost = 0.0;
	private Date startDate;
	private Date endDate;

	public String getStockCode() {
		return stockCode;
	}

	public void setStockCode(String stockCode) {
		this.stockCode = stockCode;
	}

	public Long getCurrentShares() {
		return currentShares;
	}

	public void setCurrentShares(Long currentShares) {
		this.currentShares = currentShares;
	}

	public Double getSellAmount() {
		return sellAmount;
	}

	public void setSellAmount(Double sellAmount) {
		this.sellAmount = sellAmount;
	}

	public Double getBuyAmount() {
		return buyAmount;
	}

	public void setBuyAmount(Double buyAmount) {
		this.buyAmount = buyAmount;
	}

	public Double getTransactionCost() {
		return transactionCost;
	}

	public void setTransactionCost(Double transactionCost) {
		this.transactionCost = transactionCost;
	}

	public Double getGainLossAmount() {
		return sellAmount - buyAmount;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Long getTotalShares() {
		return totalShares;
	}

	public void setTotalShares(Long totalShares) {
		this.totalShares = totalShares;
	}

	public Double getEntryAmount() {
		return entryAmount;
	}

	public void setEntryAmount(Double entryAmount) {
		this.entryAmount = entryAmount;
	}

	public Double getExitAmount() {
		return exitAmount;
	}

	public void setExitAmount(Double exitAmount) {
		this.exitAmount = exitAmount;
	}

	public Double getAverageEntryPrice() {
		return entryAmount / totalShares;
	}

	public Double getAverageExitPrice() {
		return exitAmount / totalShares;
	}

	public Double getMinimumEntryPrice() {
		return minimumEntryPrice;
	}

	public void setMinimumEntryPrice(Double minimumEntryPrice) {
		this.minimumEntryPrice = minimumEntryPrice;
	}

	public Double getMaximumEntryPrice() {
		return maximumEntryPrice;
	}

	public void setMaximumEntryPrice(Double maximumEntryPrice) {
		this.maximumEntryPrice = maximumEntryPrice;
	}

	public Double getMinimumExitPrice() {
		return minimumExitPrice;
	}

	public void setMinimumExitPrice(Double minimumExitPrice) {
		this.minimumExitPrice = minimumExitPrice;
	}

	public Double getMaximumExitPrice() {
		return maximumExitPrice;
	}

	public void setMaximumExitPrice(Double maximumExitPrice) {
		this.maximumExitPrice = maximumExitPrice;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

}
