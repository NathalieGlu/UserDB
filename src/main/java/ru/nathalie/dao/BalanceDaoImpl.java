package ru.nathalie.dao;

import ru.nathalie.db.Connector;

public class BalanceDaoImpl implements BalanceDao {
    private final Connector connector;

    public BalanceDaoImpl(Connector connector) {
        this.connector = connector;
    }

    @Override
    public String getBalance(int id) {
        return connector.getBalance(id);
    }
}
