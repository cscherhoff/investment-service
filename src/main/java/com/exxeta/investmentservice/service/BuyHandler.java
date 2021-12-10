package com.exxeta.investmentservice.service;

import com.exxeta.investmentservice.entities.DepotEntry;
import com.exxeta.investmentservice.entities.Transaction;
import com.exxeta.investmentservice.repositories.DepotEntryRepository;
import com.exxeta.investmentservice.service.exceptions.DepotEntryNotFound;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BuyHandler {

    private final DepotEntryRepository depotEntryRepository;

    public BuyHandler(DepotEntryRepository depotEntryRepository) {
        this.depotEntryRepository = depotEntryRepository;
    }

    public void processBuy(Transaction transaction) throws IllegalStateException {
        DepotEntry depotEntry;
        try {
            depotEntry = getDepotEntryFromDatabase(transaction);
            updateDepotEntry(depotEntry, transaction);
        } catch (DepotEntryNotFound depotEntryNotFound) {
            depotEntry = createNewDepotEntry(transaction);
        }
        depotEntryRepository.save(depotEntry);
    }

    private DepotEntry getDepotEntryFromDatabase(Transaction transaction) throws DepotEntryNotFound {
        List<DepotEntry> depotEntryList = depotEntryRepository.findDepotEntriesByUserIdAndDepotNameAndSecurityName(
                transaction.getUserId(), transaction.getDepotName(), transaction.getSecurityName());
        if(depotEntryList.size()==0) {
            throw new DepotEntryNotFound("There is no depot entry for the user with the ID " + transaction.getUserId() +
                ", the depot name + " + transaction.getDepotName() + " and the security name " +
                transaction.getSecurityName());
        } else if (depotEntryList.size()>1) {
            throw new IllegalStateException("There is more than one depot entry for the user with the ID " +
                transaction.getUserId() + ", the depot name + " + transaction.getDepotName() + " and the security name " +
                transaction.getSecurityName());
        }
        return depotEntryList.get(0);
    }

    private DepotEntry createNewDepotEntry(Transaction transaction) {
        return new DepotEntry(transaction.getUserId(), transaction.getDepotName(), transaction.getSecurityName(),
            transaction.getNumber(), transaction.getPrice(), transaction.getExpenses());
    }

    private void updateDepotEntry(DepotEntry depotEntry, Transaction transaction) {
        depotEntry.updatePrice(transaction.getNumber(), transaction.getPrice());
        depotEntry.updateCosts(transaction.getExpenses());
        depotEntry.updateNumber(transaction.getNumber());
    }
//    public void processBuy(Transaction transaction) {
//        List<DepotEntry> depotEntryList = depotEntryRepository.findDepotEntriesByUserIdAndDepotNameAndSecurityName(
//            transaction.getUserId(), transaction.getDepotName(), transaction.getSecurityName());
//        DepotEntry depotEntry;
//        if (depotEntryList.size() == 0) {
//            depotEntry = null;//new DepotEntry(transaction.getUserId(), transaction.getDepotName(), transaction.getSecurityName(), transaction.getNumber(), transaction.getPrice(), transaction.getExpenses());
//        }
//        else if (depotEntryList.size() == 1) {
//            depotEntry = depotEntryList.get(0);
//
//            BigDecimal costs = depotEntry.getCosts().add(transaction.getExpenses());
//            BigDecimal number = depotEntry.getNumber().add(transaction.getNumber());
//            BigDecimal price = calculatePrice(transaction.getNumber(), transaction.getPrice(), depotEntry.getNumber(),
//                depotEntry.getSinglePrice());
//
//            depotEntry.setCosts(costs);
//            depotEntry.setNumber(number);
//            depotEntry.setSinglePrice(price);
//        } else {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The number of depot entries in the database for "
//                + "the user ID " + transaction.getUserId() + ", the depot with the name " + transaction.getDepotName()
//                + " and the security name " + transaction.getSecurityName() + " should be zero or one, but was " + depotEntryList.size());
//        }
//        depotEntryRepository.save(depotEntry);
//    }
}
