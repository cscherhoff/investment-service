package com.exxeta.investmentservice.files;

import com.exxeta.investmentservice.entities.Investment;
import com.exxeta.investmentservice.entities.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class InvestmentImporter {

    private final String transactionImportFileName = "transactions.csv";
    private final String investmentImportFileName = "investitionsListe.csv";

    private final Logger logger = LoggerFactory.getLogger(InvestmentImporter.class);

    public List<Transaction> loadTransactionList() {
        logger.info("Importing transactions:");
        List<String> stringList = getListFromCsvFile(transactionImportFileName);
        logger.info("converting strings into transactions.");
        return convertStringListIntoTransactionList(stringList);
    }

    public List<Investment> loadInvestmentList() {
        logger.info("importing investments");
        List<String> stringList = getListFromCsvFile(investmentImportFileName);
        logger.info("converting strings into investments");
        return converStringListIntoInvestmentList(stringList);
    }

    private List<String> getListFromCsvFile(String fileName) {
        logger.info("getting the lines from the file " + fileName);
        BufferedReader in = null;
        List<String> stringList = new ArrayList<>();
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            File file = new File(classLoader.getResource(fileName).getFile());
            String csvEntry;
            in = new BufferedReader(new FileReader(file.getAbsolutePath()));
            csvEntry = in.readLine();
            while (csvEntry != null) {
                logger.info("reading line " + csvEntry);
                stringList.add(csvEntry);
                csvEntry = in.readLine();
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return stringList;
    }

    private List<Transaction> convertStringListIntoTransactionList(List<String> stringList) {
        List<Transaction> transactionList = new ArrayList<>();
        stringList.forEach(eintrag -> transactionList.add(convertStringToTransaction(eintrag)));
        return transactionList;
    }

    private Transaction convertStringToTransaction(String transactionString) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String[] row_string = transactionString.split(";");
        return new Transaction(row_string[1], LocalDate.parse(row_string[0], dateTimeFormatter), row_string[2], row_string[3],
                BigDecimal.valueOf(Double.parseDouble(row_string[5])),
                BigDecimal.valueOf(Double.parseDouble(row_string[4])),
                BigDecimal.valueOf(Double.parseDouble(row_string[6])),
                BigDecimal.valueOf(Double.parseDouble(row_string[7]))
        );
    }

    private List<Investment> converStringListIntoInvestmentList(List<String> stringList) {
        List<Investment> investmentList = new ArrayList<>();
        stringList.forEach(entry -> investmentList.add(convertStringToInvestment(entry)));
        return investmentList;
    }

    private Investment convertStringToInvestment(String investmentString) {
        String[] row_string = investmentString.split(";");
        return null;
//        return new Investment(0, row_string[0], Double.parseDouble(row_string[1]));
    }
}
