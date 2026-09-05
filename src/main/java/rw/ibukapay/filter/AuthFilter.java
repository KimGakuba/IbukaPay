package rw.ibukapay.filter;

import rw.ibukapay.model.User;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * The Class AuthFilter - least privilege gate: nothing is reachable before
 * signing in, except the public login and registration pages (and static
 * Faces resources).
 *
 * @version 1.0
 */
@WebFilter(filterName = "AuthFilter", urlPatterns = "/*")
public class AuthFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String path = httpRequest.getRequestURI();

        boolean publicPage = path.endsWith("login.xhtml")
                || path.endsWith("register.xhtml")
                || path.contains("/javax.faces.resource/");

        HttpSession session = httpRequest.getSession(false);
        User currentUser = session == null ? null : (User) session.getAttribute("currentUser");

        if (!publicPage && currentUser == null) {
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/login.xhtml");
            return;
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}