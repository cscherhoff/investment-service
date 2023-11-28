package com.exxeta.investmentservice.repositories;

import com.exxeta.investmentservice.entities.DepotEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepotEntryRepository  extends JpaRepository<DepotEntry, Long> {
    List<DepotEntry> findDepotEntriesByUserId(String userId);
    List<DepotEntry> findDepotEntriesByUserIdAndDepotNameAndIsin(String userId, String depotName, String isin);
    List<DepotEntry> findDepotEntriesByUserIdAndDepotName(String userId, String depotName);
}
