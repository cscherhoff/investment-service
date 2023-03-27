package com.exxeta.investmentservice.files;

import com.exxeta.investmentservice.entities.*;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class InvestmentExporterTest {

    private final String path = "src\\test\\resources\\testFiles\\";

    @Test
    @Disabled
    public void exportTest() {
        InvestmentExporter investmentExporter = new InvestmentExporter(path);
        List<Transaction> transactionList = List.of(new Transaction("testDepot", LocalDate.now(),
            "Kauf", "BMW", BigDecimal.valueOf(20.99), BigDecimal.valueOf(100.00),
            BigDecimal.valueOf(1.0), BigDecimal.valueOf(2100.00)));
        List<AccountMovement> accountMovementList = List.of(new AccountMovement(6, LocalDate.now(),
                "testDepot", "TransferToDepot", BigDecimal.valueOf(123.45)));
        List<Investment> investmentList = List.of(new Investment(6, LocalDate.now(), 9876.54));
        try {
            investmentExporter.export(transactionList, accountMovementList, investmentList, false);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
