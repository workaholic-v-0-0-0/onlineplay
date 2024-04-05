package online.caltuli.webapp.servlet;

import jakarta.inject.Inject;
import online.caltuli.business.UserManager;
import online.caltuli.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;

public class AjaxUserActivities extends HttpServlet {

    @Inject
    private UserManager userManager;

    private static final Logger logger = LogManager.getLogger(AjaxUserActivities.class);

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        logger.info("here");

        List<User> connectedUsers = new ArrayList<>(userManager.getConnectedUserList().values());

        String json = new Gson().toJson(connectedUsers);

        PrintWriter out = response.getWriter();
        out.print(json);
        logger.info("json: " + json);
        out.flush();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

}
