package ru.nathalie.model.dao;

import ru.nathalie.db.Connector;

public class BalanceDaoImpl implements BalanceDao {
    private final Connector connector;

    public BalanceDaoImpl(Connector connector) {
        this.connector = connector;
    }

    @Override
    public String getBalance(Integer id) {
        return connector.getBalanceById(id);
    }
}
