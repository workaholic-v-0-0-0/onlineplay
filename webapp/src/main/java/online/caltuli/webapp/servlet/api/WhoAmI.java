package online.caltuli.webapp.servlet.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import online.caltuli.model.User;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class WhoAmI extends HttpServlet {
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        ObjectMapper mapper = new ObjectMapper();

        Map<String, Object> data = new HashMap<>();

        if (session != null) {
            User user = (User) session.getAttribute("user");
            if (user != null) {
                data.put("user", user);
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

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

}
