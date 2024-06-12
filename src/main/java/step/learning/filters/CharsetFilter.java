package step.learning.filters;

import com.google.inject.Singleton;

import javax.servlet.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Singleton
public class CharsetFilter implements Filter {

    private FilterConfig filterConfig;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        servletRequest.setCharacterEncoding(StandardCharsets.UTF_8.name() );
        servletResponse.setCharacterEncoding(StandardCharsets.UTF_8.name() );

        filterChain.doFilter( servletRequest, servletResponse );
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
