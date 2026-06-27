package com.luisgustavo.vaultbank.repository;

import com.luisgustavo.vaultbank.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

    @Query("SELECT t FROM Transaction t WHERE t.sourceAccount.id = :accountId OR t.destinationAccount.id = :accountId ORDER BY t.createdAt DESC")
    List<Transaction> findHistoryByAccountId(@Param("accountId") UUID accountId);

    @Query("SELECT t FROM Transaction t WHERE (t.sourceAccount.id = :accountId OR t.destinationAccount.id = :accountId) " +
           "AND t.createdAt >= :startDate AND t.createdAt <= :endDate " +
           "ORDER BY t.createdAt DESC")
    List<Transaction> findStatementByDates(
            @Param("accountId") UUID accountId,
            @Param("startDate") java.time.LocalDateTime startDate,
            @Param("endDate") java.time.LocalDateTime endDate
    );

    @Query("SELECT t FROM Transaction t WHERE (t.sourceAccount.id = :accountId OR t.destinationAccount.id = :accountId) " +
           "AND t.createdAt >= :startDate AND t.createdAt <= :endDate " +
           "AND t.type = :type " +
           "ORDER BY t.createdAt DESC")
    List<Transaction> findStatementByDatesAndType(
            @Param("accountId") UUID accountId,
            @Param("startDate") java.time.LocalDateTime startDate,
            @Param("endDate") java.time.LocalDateTime endDate,
            @Param("type") com.luisgustavo.vaultbank.enums.TransactionType type
    );
}
