package online.caltuli.webapp.servlet;

import jakarta.inject.Inject;
import online.caltuli.business.exception.BusinessException;
import online.caltuli.business.UserManager;
import online.caltuli.model.UserConnection;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Home extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Inject
	private UserManager userManager;

	private Logger logger = LogManager.getLogger(Home.class); // to debug
       
    public Home() {
		super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setAttribute("connectedUserList", userManager.getConnectedUserList());
		this.getServletContext().getRequestDispatcher("/WEB-INF/home.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
