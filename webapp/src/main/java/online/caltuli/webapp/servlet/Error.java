package online.caltuli.webapp.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class Error extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String errorMessage = request.getParameter("errorMessage");
        if (errorMessage == null || errorMessage.isEmpty()) {
            errorMessage = "undefined cause";
        }
        request.setAttribute("errorMessage", errorMessage);
        request.getRequestDispatcher("/WEB-INF/error.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        doGet(request, response);
    }
}
