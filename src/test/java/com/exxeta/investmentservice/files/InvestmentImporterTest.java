package com.exxeta.investmentservice.files;

import com.exxeta.investmentservice.entities.DepotEntry;
import com.exxeta.investmentservice.entities.Profit;
import com.exxeta.investmentservice.entities.Transaction;
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
        List<DepotEntry> depotEntryList = List.of(new DepotEntry("testDepot", "HUGO", BigDecimal.valueOf(100.00),
            BigDecimal.valueOf(20.99), BigDecimal.valueOf(1.00)));
        List<Profit> profitList = List.of(new Profit(6, "testDepot", "BlaBla", BigDecimal.valueOf(250.85),
            BigDecimal.valueOf(0.21), BigDecimal.valueOf(249.33), BigDecimal.valueOf(0.18)));
        investmentExporter.export(transactionList, depotEntryList, profitList, false);
    }
}
