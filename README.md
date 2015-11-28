# extract-col-trading-log
Java project to extract trade logs from COL's trading history

# Procedure

### Download historical invoices from COL website 
1. Go to portfolio page (Trade -> Portfolio)
2. Look for Transaction Invoices - Historica
3. Save content to a text file - note flocation


### Run ExtractCOLTradingLog.java
1. Compile all java files
2. Run ExtractCOLTradingLog and pass 3 argumetns (location of invoices, target transation lines csv file, target trade log csv file)