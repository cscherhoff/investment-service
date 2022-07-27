package com.exxeta.investmentservice.files;

import com.exxeta.investmentservice.entities.DepotEntry;
import com.exxeta.investmentservice.entities.Profit;
import com.exxeta.investmentservice.entities.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class InvestmentExporter {

    private final String exportPath;
    private final Logger logger = LoggerFactory.getLogger(InvestmentExporter.class);

    public InvestmentExporter() {
        this(System.getProperty("user.dir") + System.getProperty("file.separator") + "export");
    }

    public InvestmentExporter(String exportPath) {
        Assert.notNull(exportPath, "The export path is null! "
                + "Either the property couldn't be read or it wasn't specified.");
        this.exportPath = exportPath;
        logger.info("The exportPath for the expenses is: " + this.exportPath);
    }

    public void export(List<Transaction> transactionList, List<DepotEntry> depotEntryList, List<Profit> profitList, boolean append) throws IOException {
        if (!transactionList.isEmpty()) {
            long userId = transactionList.get(0).getUserId();
            final String separator = System.getProperty("file.separator");
            final String pathToIdFolder = exportPath + separator + userId;
            if (!folderExists(pathToIdFolder)) {
                createFolder(pathToIdFolder);
            }
            logger.info("The folder exists and the expenses will be exported now:");
            logger.info("exporting the transactions:");
            export(createListOfTransactionStrings(transactionList), pathToIdFolder + separator + "transactions.csv", append);
            logger.info("exporting the depotEntries:");
            export(createListOfDepotStrings(depotEntryList), pathToIdFolder + separator + "depotEntries.csv", append);
            logger.info("exporting the profits:");
            export(createListOfProfitStrings(profitList), pathToIdFolder + separator + "profits.csv", append);
        }
    }

    private boolean folderExists(String pathToFolder) {
        logger.info("Check if the folder '" + pathToFolder + "' exists.");
        File file = new File(pathToFolder);
        return file.exists();
    }

    private void createFolder(String pathToFolder) throws IOException {
        logger.info("The folder doesn't exist and is going to be created now");
        File file = new File(pathToFolder);
        boolean successfullyCreated = file.mkdir();
        if(!successfullyCreated){
            throw new IOException("The folder " + pathToFolder + " couldn't be created");
        }
    }

    /**
     * Exports the given list into the given csv file
     * @param objectsList A list of String objects - every string represents one line in the csv file
     * @param filePath    The complete path to the csv file
     * @param append      Indicates if given content should be overwritten or if the new content will be appended
     */
    private void export(List<String> objectsList, String filePath, boolean append) {
        FileOutputStream fileOutputStream = null;
        try {
            logger.info("Trying to export to \"" + filePath + "\".");
            fileOutputStream = new FileOutputStream(new File(filePath), append);
            for (String line : objectsList) {
                logger.info("exporting: " + line);
                fileOutputStream.write((line + "\n").getBytes());
            }
            logger.info("... export successfully completed!");
        } catch (IOException e) {
            logger.error("There was an error while trying to export to \"" + filePath + "\"");
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    private List<String> createListOfTransactionStrings(List<Transaction> transactionList) {
        List<String> listOfStrings = new ArrayList<>();
        transactionList.forEach(transaction -> listOfStrings.add(transactionToString(transaction)));
        return listOfStrings;
    }

    private List<String> createListOfDepotStrings(List<DepotEntry> depotEntryList) {
        List<String> listOfStrings = new ArrayList<>();
        depotEntryList.forEach(depotEntry -> listOfStrings.add(depotEntryToString(depotEntry)));
        return listOfStrings;
    }

    private List<String> createListOfProfitStrings(List<Profit> profitList) {
        List<String> listOfString = new ArrayList<>();
        profitList.forEach(profit -> listOfString.add(profitToString(profit)));
        return listOfString;
    }

    private String transactionToString(Transaction transaction) {
        return transaction.getDate() + ";" + transaction.getDepotName() + ";" + transaction.getType() + ";"
                + transaction.getIsin() + ";" + transaction.getSecurityName() + ";" + transaction.getNumber() + ";"
                + transaction.getPrice() + ";" + transaction.getExpenses() + ";" + transaction.getTotalPrice();
    }

    private String depotEntryToString(DepotEntry depotEntry) {
        return depotEntry.getDepotName() + ";" + depotEntry.getSecurityName() + ";" + depotEntry.getNumber() + ";" +
            depotEntry.getSinglePrice() + ";" + depotEntry.getCosts();
    }

    private String profitToString(Profit profit) {
        return profit.getDepotName() + ";" + profit.getSecurityName() + ";" + profit.getNetAbsoluteProfit() + ";"
            + profit.getNetPercentageProfit() + ";" + profit.getGrossAbsoluteProfit() + ";" + profit.getGrossPercentageProfit();
    }
}
