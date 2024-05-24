package online.caltuli.webapp.servlet.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import online.caltuli.business.GameManager;
import online.caltuli.model.Game;
import online.caltuli.model.User;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class HowIsMyGame  extends HttpServlet {

    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        ObjectMapper mapper = new ObjectMapper();

        Map<String, Object> data = new HashMap<>();

        User user = null;
        Game game = null;

        if (session != null) {
            user = (User) session.getAttribute("user");
            if (user != null) {
                game =
                        ((GameManager) session.getAttribute("gameManager"))
                                .getGame();
                if (game != null) {
                    data.put("game", game);
                } else {
                    data.put("error", "User found but he or she are not playing a game");
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                }
            } else {
                data.put("error", "No user found in session");
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        } else {
            data.put("error", "No session found");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }

        try {
            String json = mapper.writeValueAsString(data);
            PrintWriter out = response.getWriter();
            out.print(json);
            out.flush();
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\":\"Error serializing data to JSON\"}");
        }
    }

}