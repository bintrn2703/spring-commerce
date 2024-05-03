package vn.edu.tdtu.springcommerce.configuration;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // Get email from request parameter
        String email = request.getParameter("email");

        // Get session object
        HttpSession session = request.getSession();

        // Set email to session
        session.setAttribute("email", email);

        // Redirect user to home page
        response.sendRedirect(request.getContextPath() + "/");
    }
}
