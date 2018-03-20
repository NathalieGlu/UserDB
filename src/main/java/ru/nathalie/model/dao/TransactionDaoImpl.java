package ru.nathalie.model.dao;

import ru.nathalie.db.Connector;

public class TransactionDaoImpl implements TransactionDao {
    private final Connector connector;

    public TransactionDaoImpl(Connector connector) {
        this.connector = connector;
    }

    @Override
    public String makeTransaction(Integer fromId, Integer toId, Double amount) {
        return this.connector.makeTransaction(fromId, toId, amount);
    }
}
