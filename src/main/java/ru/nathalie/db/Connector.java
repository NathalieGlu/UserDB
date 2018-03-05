package ru.nathalie.db;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class Connector {
    private static final String GET_USERS = "SELECT * FROM users";
    private static final String GET_BALANCE_BY_ID = "SELECT user_balance FROM users WHERE user_id = ?";
    private static final String USER_ID = "user_id";
    private static final String USER_NAME = "user_name";
    private static final String USER_BALANCE = "user_balance";
    private static final Logger log = LoggerFactory.getLogger(Connector.class.getName());
    private final DataSource dataSource;

    public Connector(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public JSONObject getUsers() {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_USERS);) {

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
            log.error("Exception during statement execution: ", e);
        } catch (Exception e) {
            log.error("Exception during connection: ", e);
        }
        return null;
    }

    public String getBalance(int id) {
        try (Connection connection = dataSource.getConnection()) {

            PreparedStatement statement = connection.prepareStatement(GET_BALANCE_BY_ID);
            statement.setInt(1, id);

            ResultSet rs = statement.executeQuery();
            rs.next();
            return Double.toString(rs.getDouble(USER_BALANCE));
        } catch (SQLException e) {
            log.error("Exception during statement execution: ", e);
        }
        return null;
    }
}
