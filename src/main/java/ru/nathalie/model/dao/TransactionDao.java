package ru.nathalie.model.dao;

public interface TransactionDao {

    String makeTransaction(Integer fromId, Integer toId, Double amount);
}
