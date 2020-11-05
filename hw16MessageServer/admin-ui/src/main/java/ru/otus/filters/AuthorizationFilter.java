package ru.otus.filters;

import org.springframework.stereotype.Component;
import ru.otus.messagesystem.rest.uri.MsClientUriDictionary;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Component
public class AuthorizationFilter implements Filter {

    private ServletContext context;

    @Override
    public void init(FilterConfig filterConfig) {
        this.context = filterConfig.getServletContext();
    }

    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String uri = request.getRequestURI();
        this.context.log("Requested Resource:" + uri);

        HttpSession session = request.getSession(false);

        if ((session == null) && (!uri.equals(request.getContextPath() + "/login")) && (!uri.equals(request.getContextPath() + MsClientUriDictionary.API_CLIENT_MESSAGE_SEND))) {
            response.sendRedirect(request.getContextPath() + "/login");
        } else {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    @Override
    public void destroy() {

    }
}
