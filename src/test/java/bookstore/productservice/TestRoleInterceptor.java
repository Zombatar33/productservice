package bookstore.productservice;

import bookstore.authentication.JwtUtil;
import bookstore.productservice.port.product.exception.NotAuthorizedException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@AllArgsConstructor
public class TestRoleInterceptor implements HandlerInterceptor {

    private String role;
    private String key;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // Always allow GET
        if (request.getMethod().equals("GET")) {
            return HandlerInterceptor.super.preHandle(request, response, handler);
        }

        System.out.println(request.getHeader("Authorization"));
        System.out.println(role + key);
        // other requests need certain role
        if (!JwtUtil.allowRequest(request, key, role)) {
            throw new NotAuthorizedException();
        }
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

}
