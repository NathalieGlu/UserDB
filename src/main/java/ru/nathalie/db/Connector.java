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
    private static final String GET_USERS = "SELECT user_id, user_name FROM users";
    private static final String GET_BALANCE_BY_ID = "SELECT user_balance FROM users WHERE user_id = ?";
    private static final String CHECK_PHONE = "SELECT user_phone FROM users WHERE user_phone = ?";
    private static final String CHECK_MAIL = "SELECT user_email FROM users WHERE user_email = ?";
    private static final String GET_MAX_ID = "SELECT user_id FROM users ORDER BY user_id DESC LIMIT 1";
    private static final String INSERT_USER = "INSERT INTO users (user_id, user_name, user_phone, user_email) VALUES(?, ?, ?, ?)";
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

    public String getBalance(Integer id) {
        try (Connection connection = dataSource.getConnection()) {

            PreparedStatement statement = connection.prepareStatement(GET_BALANCE_BY_ID);
            statement.setInt(1, id);

            ResultSet rs = statement.executeQuery();
            rs.next();
            return Double.toString(rs.getDouble(USER_BALANCE));
        } catch (SQLException e) {
            log.error("Exception during statement execution: ", e);
            return null;
        }
    }

    public boolean createUser(String name, String phone, String mail) {
        if (!exists(phone, CHECK_PHONE) && !exists(mail, CHECK_MAIL)) {
            try (Connection connection = dataSource.getConnection()) {
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
                log.error("Exception during statement execution: ", e);
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    private boolean exists(String data, String state) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(state);
            statement.setString(1, data);
            ResultSet rs = statement.executeQuery();
            return rs.next();

        } catch (Exception e) {
            log.error("Exception during statement execution: ", e);
            return false;
        }
    }
}
