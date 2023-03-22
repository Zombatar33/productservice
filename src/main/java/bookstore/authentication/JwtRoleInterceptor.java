package bookstore.authentication;

import bookstore.productservice.port.product.exception.NotAuthorizedException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@AllArgsConstructor
public class JwtRoleInterceptor implements HandlerInterceptor {

    private Environment environment;
    private String role;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // Always allow GET
        if (request.getMethod().equals("GET")) {
            return HandlerInterceptor.super.preHandle(request, response, handler);
        }

        // other requests need certain role
        if (!JwtUtil.allowRequest(request, environment.getProperty("jwt.secret"), role)) {
            throw new NotAuthorizedException();
        }
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}
