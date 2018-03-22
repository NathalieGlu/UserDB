package ru.nathalie.db;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Connector {
    private static final String GET_USERS = "SELECT user_id, user_name FROM users";
    private static final String GET_BALANCE_BY_ID = "SELECT user_balance FROM users WHERE user_id = ?";
    private static final String GET_MAX_ID = "SELECT user_id FROM users ORDER BY user_id DESC LIMIT 1";
    private static final String CHECK_PHONE = "SELECT user_phone FROM users WHERE user_phone = ?";
    private static final String CHECK_MAIL = "SELECT user_email FROM users WHERE user_email = ?";
    private static final String INSERT_USER = "INSERT INTO users (user_id, user_name, user_phone, user_email) VALUES(?, ?, ?, ?)";
    private static final String USER_ID = "user_id";
    private static final String USER_NAME = "user_name";
    private static final String USER_BALANCE = "user_balance";
    private static final String USER_BALANCE_PREV = "user_balance_prev";
    private static final Logger log = LoggerFactory.getLogger(Connector.class.getName());
    private static final String UPDATE_BALANCES_TRANSACTION = "BEGIN;" +
            "UPDATE users SET last_trc = ? WHERE user_id IN (?, ?);" +
            "UPDATE users SET user_balance_prev = user_balance WHERE user_id = ?;" +
            "UPDATE users SET user_balance = user_balance - ? WHERE user_id = ?;" +
            "UPDATE users SET user_balance_prev = user_balance WHERE user_id = ?;" +
            "UPDATE users SET user_balance = user_balance + ? WHERE user_id = ?;";
    private static final String GET_BALANCE = "SELECT user_balance FROM users WHERE user_id = ?;";
    private static final String GET_PREVIOUS_BALANCE = "SELECT user_balance_prev FROM users WHERE user_id = ?;";
    private static final String COMMIT = "COMMIT;";
    private final ConnectionPool connectionPool;

    public Connector(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    public JSONObject getUsers() {
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_USERS)) {

            ResultSet rs = statement.executeQuery();
            JSONObject jsonObject = new JSONObject();

            while (rs.next()) {
                Map<String, String> map = new HashMap<>();
                map.put(USER_ID, rs.getString(USER_ID));
                map.put(USER_NAME, rs.getString(USER_NAME));

                jsonObject.put(String.valueOf(rs.getRow()), map);
            }
            return jsonObject;
        } catch (SQLException e) {
            log.error("Cannot get users: ", e);
        } catch (Exception e) {
            log.error("Exception during connection: ", e);
        }
        return null;
    }

    public String getBalanceById(Integer id) {
        try (Connection connection = connectionPool.getConnection()) {

            PreparedStatement statement = connection.prepareStatement(GET_BALANCE_BY_ID);
            statement.setInt(1, id);

            ResultSet rs = statement.executeQuery();
            rs.next();
            return Double.toString(rs.getDouble(USER_BALANCE));
        } catch (SQLException e) {
            log.error("Cannot get balance: ", e);
            return null;
        }
    }

    public boolean createUser(String name, String phone, String mail) {
        if (notExists(phone, CHECK_PHONE) && notExists(mail, CHECK_MAIL)) {
            try (Connection connection = connectionPool.getConnection()) {
                PreparedStatement statement = connection.prepareStatement(GET_MAX_ID);
                ResultSet rs = statement.executeQuery();
                rs.next();
                Integer id = rs.getInt(USER_ID) + 1;

                statement = connection.prepareStatement(INSERT_USER);
                statement.setInt(1, id);
                statement.setString(2, name);
                statement.setString(3, phone);
                statement.setString(4, mail);
                statement.executeUpdate();
            } catch (Exception e) {
                log.error("Cannot create user: ", e);
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    public String makeTransaction(Integer fromId, Integer toId, Double amount) {
        Double fromBalance = Double.parseDouble(getBalanceById(fromId));

        if (fromBalance < amount) {
            return "Not enough money on balance\n";
        } else {
            if (commitTransaction(fromId, toId, amount)) {
                JSONObject jsonFrom = new JSONObject()
                        .put("before", getBalance(fromId, GET_PREVIOUS_BALANCE, USER_BALANCE_PREV))
                        .put("after", getBalance(fromId, GET_BALANCE, USER_BALANCE));

                JSONObject jsonTo = new JSONObject()
                        .put("before", getBalance(toId, GET_PREVIOUS_BALANCE, USER_BALANCE_PREV))
                        .put("after", getBalance(toId, GET_BALANCE, USER_BALANCE));

                JSONObject js = new JSONObject()
                        .put(fromId.toString(), jsonFrom)
                        .put(toId.toString(), jsonTo);
                commit();
                return js.toString();
            } else {
                return "Error during transaction";
            }
        }
    }

    private boolean notExists(String data, String state) {
        try (Connection connection = connectionPool.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(state);
            statement.setString(1, data);
            ResultSet rs = statement.executeQuery();
            return !rs.next();

        } catch (Exception e) {
            log.error("User doesn't exist: ", e);
            return true;
        }
    }

    private Double getBalance(Integer id, String query, String balanceType) {
        try (Connection connection = connectionPool.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            rs.next();
            return rs.getDouble(balanceType);
        } catch (SQLException e) {
            log.error("Cannot get balance: ", e);
            return null;
        }
    }

    private void commit() {
        try (Connection connection = connectionPool.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(COMMIT);
            statement.executeUpdate();
        } catch (SQLException e) {
            log.error("Cannot get balance: ", e);
        }
    }

    private boolean commitTransaction(Integer fromId, Integer toId, Double amount) {
        try (Connection connection = connectionPool.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(UPDATE_BALANCES_TRANSACTION);
            statement.setTimestamp(1, new Timestamp(new Date().getTime()));
            statement.setInt(2, fromId);
            statement.setInt(3, toId);
            statement.setInt(4, fromId);
            statement.setDouble(5, amount);
            statement.setInt(6, fromId);
            statement.setInt(7, toId);
            statement.setDouble(8, amount);
            statement.setInt(9, toId);
            statement.executeUpdate();
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }
}
