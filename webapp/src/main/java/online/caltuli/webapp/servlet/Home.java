package online.caltuli.webapp.servlet;

import online.caltuli.business.BusinessException;
import online.caltuli.business.UserManager;
import online.caltuli.model.UserConnection;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Timestamp;

// \begin{todebug}
import java.io.PrintWriter;
import java.util.Date;
import java.util.Enumeration;

// \end{todebug}

public class Home extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public Home() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// if no session is defined ;
		// fetch information related to connection, record them into connections tables
		// so that the related primary key value is automatically generated ;
		// fetch them from connections table and record them into a userConnection instance
		HttpSession session = request.getSession(false);
		UserConnection userConnection = null;
		if (session == null) {
			session = request.getSession(true);
			String ipAddress = request.getRemoteAddr();
			Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			try {
				userConnection = UserManager.logUserConnection(ipAddress, timestamp, null);
				session.setAttribute("userConnection", userConnection);
			} catch (BusinessException e) {
				// can't register information related to the user connection in database
			}
		}

		// if a session is defined
		else {
			userConnection = (UserConnection) session.getAttribute("userConnection");
			// if it's an authenticated user
			if (userConnection != null && userConnection.getUserId() != -1) {
				// what to do with an authenticated user
			}

		}

		// propose log in and sign up in JSP home and ASAP a GUI to play
		this.getServletContext().getRequestDispatcher("/WEB-INF/home.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
