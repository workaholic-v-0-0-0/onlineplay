/**
 * FILE: Logout.java
 * CONTENT: Servlet implementation for handling the logout process in the Onlineplay application.
 *          This class terminates user sessions and redirects users to the previous page they visited
 *          or to the home page if no referrer URL is found.
 * AUTHOR: Sylvain Labopin
 * DESCRIPTION:
 * The Logout servlet is responsible for:
 * - Invalidating the current session to clear all session attributes.
 * - Redirecting the user to the 'Referer' page if available, otherwise to the 'home' page.
 *   This ensures that users do not stay on the logout page after session termination.
 * USAGE:
 * Typically invoked through a "Log Out" link or button on various pages within the application.
 * This servlet ensures a secure logout process by clearing the session and managing redirection properly.*
 * KEY METHODS:
 * - doGet(HttpServletRequest request, HttpServletResponse response): Handles the GET request for logging out the user.
 * EXTERNAL INTERACTIONS:
 * - Interacts with UserManager for disconnecting the user and possibly updating user-related logs or statistics.
 * SECURITY CONSIDERATIONS:
 * - Ensures that the session is completely invalidated upon logout to prevent session hijacking.
 */

package online.caltuli.webapp.servlet.gui;

import online.caltuli.business.UserManager;
import online.caltuli.model.User;

import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.Serial;

public class Logout extends HttpServlet {
    @Serial
    private static final long serialVersionUID = 1L;
    @Inject
    private UserManager userManager;

    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        // Retrieve the current session, don't create if it doesn't exist
        HttpSession session = request.getSession(false);

        // Calls UserManager to handle the logic for disconnecting the user
        userManager.disconnectUser(((User) session.getAttribute(
                "user")).getId()
        );

        // invalidate the session, clearing all session attributes
        request.getSession().invalidate();

        // try to retrieve the URL of the previous page
        String referer = request.getHeader("Referer");

        // redirect to the referer or home page if no referer exists
        response.sendRedirect(referer != null ? referer : "home");
    }
}