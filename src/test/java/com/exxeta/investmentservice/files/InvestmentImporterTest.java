package com.exxeta.investmentservice.files;

import com.exxeta.investmentservice.entities.*;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InvestmentImporterTest {

    @Test
    @Disabled
    public void importerTest() throws IOException {
//        exportTest();

//        InvestmentImporter investmentImporter = new InvestmentImporter(null);
//        List<Transaction> transactionList = investmentImporter.importTransactionList();
//        assertEquals(234, transactionList.size(), "The number of lines in the file should be 1");
    }

    private void exportTest() throws IOException {
        InvestmentExporter investmentExporter = new InvestmentExporter();
        List<Transaction> transactionList = List.of(new Transaction("testDepot", LocalDate.now(),
                "Kauf", "BMW", BigDecimal.valueOf(20.99), BigDecimal.valueOf(100.00),
                BigDecimal.valueOf(1.0), BigDecimal.valueOf(2100.00)));
        List<AccountMovement> accountMovementList = List.of(new AccountMovement("6", LocalDate.now(),
                "testDepot", "TransferToDepot", BigDecimal.valueOf(123.45)));
        List<Investment> investmentList = List.of(new Investment("6", LocalDate.now(), 9876.54));
        try {
            investmentExporter.export(transactionList, accountMovementList, investmentList, false);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
