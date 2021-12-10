package com.exxeta.investmentservice.service;

import com.exxeta.investmentservice.entities.DepotEntry;
import com.exxeta.investmentservice.entities.Transaction;
import com.exxeta.investmentservice.repositories.DepotEntryRepository;
import com.exxeta.investmentservice.service.profits.ProfitHandler;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class SalesHandler {

    private final DepotEntryRepository depotEntryRepository;
    private final ProfitHandler profitHandler;

    public SalesHandler(DepotEntryRepository depotEntryRepository,
        ProfitHandler profitHandler) {
        this.depotEntryRepository = depotEntryRepository;
        this.profitHandler = profitHandler;
    }

    protected void processSale(Transaction transaction) throws ResponseStatusException{
        DepotEntry depotEntryFromDatabase = getDepotEntryFromDatabase(transaction);
        profitHandler.createProfit(transaction, depotEntryFromDatabase);
        handleDepotEntry(transaction, depotEntryFromDatabase);
        profitHandler.saveProfit();
    }

    private DepotEntry getDepotEntryFromDatabase(Transaction transaction) {
        List<DepotEntry> depotEntryList = depotEntryRepository.findDepotEntriesByUserIdAndDepotNameAndSecurityName(
            transaction.getUserId(), transaction.getDepotName(), transaction.getSecurityName());

        if (depotEntryList.size() == 1) {
            return depotEntryList.get(0);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The number of depot entries in the database for "
                + "the user ID " + transaction.getUserId() + ", the depot with the name " + transaction.getDepotName()
                + " and the security name " + transaction.getSecurityName() + " must be one, but was " + depotEntryList.size());
        }
    }

    private void handleDepotEntry(Transaction transaction, DepotEntry depotEntry) {
//        BigDecimal oldNumberFromDatabase = depotEntry.getNumber();
        if (depotEntry.getNumber().doubleValue()<transaction.getNumber().doubleValue()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "It is not possible to sell more securities (" +
                transaction.getNumber() + ") than there are in the database (" + depotEntry.getNumber() + ")!");
        } else if (depotEntry.getNumber().doubleValue()==transaction.getNumber().doubleValue()) {
            depotEntryRepository.delete(depotEntry);
            return;
        }
        depotEntry.updateCosts(transaction.getExpenses(), transaction.getNumber());
        depotEntry.updateNumber(transaction.getNumber().negate());
        depotEntryRepository.save(depotEntry);
    }
}
