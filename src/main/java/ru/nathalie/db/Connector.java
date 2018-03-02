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
    private final static String GET_USERS = "SELECT * FROM users";
    private final static Logger log = LoggerFactory.getLogger(Connector.class.getName());
    private final DataSource dataSource;

    public Connector(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public JSONObject getUsers() {
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(GET_USERS);

            ResultSet rs = statement.executeQuery();
            JSONObject jsonObject = new JSONObject();

            while (rs.next()) {

                Map<String, String> map = new HashMap<>();
                map.put("user_id", rs.getString("user_id"));
                map.put("user_name", rs.getString("user_name"));

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
        return null;
    }
}
