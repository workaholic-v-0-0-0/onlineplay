package online.caltuli.webapp.servlet.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.inject.Inject;
import online.caltuli.business.PlayerManager;
import online.caltuli.model.CurrentModel;
import online.caltuli.model.GameSummary;
import online.caltuli.model.Player;
import online.caltuli.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import online.caltuli.model.exceptions.user.UserException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;

public class UserActivities extends HttpServlet {

    @Inject
    private PlayerManager playerManager;

    private static final Logger logger = LogManager.getLogger(UserActivities.class);

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> data = new HashMap<>();

        data.put(
                "authenticatedUsers",
                playerManager
                        .getAuthenticatedUsers()
                        .values()
                        .stream()
                        .map(user -> (Player) user)
                        .collect(Collectors.toList())
        );

        data.put(
                "waitingToPlayUsers",
                playerManager
                        .getWaitingToPlayUser()
                        .values()
                        .stream()
                        .map(user -> (Player) user)
                        .collect(Collectors.toList())
        );

        data.put(
                "games",
                playerManager
                        .getGames()
                        .values()
                        .stream()
                        .map(game -> game
                        )
                        .collect(Collectors.toList())
        );

        try {
            String json = mapper.writeValueAsString(data);
            PrintWriter out = response.getWriter();
            out.print(json);
            logger.info("json: " + json);
            out.flush();
        } catch (Exception e) {
            logger.error("Error serializing data to JSON", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

    }
}
