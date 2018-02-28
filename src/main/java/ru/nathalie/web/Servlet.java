package ru.nathalie.web;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.nathalie.dao.UserDaoImpl;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/json")
public class Servlet extends HttpServlet {

    private UserDaoImpl userDao;
    private final Logger log = LoggerFactory.getLogger(Servlet.class.getName());

    public Servlet() {
        super();

        this.userDao = new UserDaoImpl();
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) {

        JSONObject jsonObject = null;

        response.setContentType("application/json");
        try {
            response.getWriter().print(jsonObject);
        } catch (IOException e) {
            log.error("Error during response", e);
        }
    }
}
