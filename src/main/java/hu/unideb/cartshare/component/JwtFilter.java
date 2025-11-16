package hu.unideb.cartshare.component;

import hu.unideb.cartshare.model.UserDetailsImpl;
import hu.unideb.cartshare.service.user.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

/**
 * Component filter class for handling JWT token verification
 * using Spring Security.
 */
@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    /**
     * Constant for the Bearer prefix length in Authorization header.
     */
    private static final int BEARER_PREFIX_LENGTH = 7;

    /**
     * JWT utilities for token operations.
     */
    private final JwtUtils jwtUtils;

    /**
     * Service for loading user details.
     */
    private final UserDetailsServiceImpl userDetailsService;

    /**
     * Filters each request to validate JWT token and set authentication.
     *
     * <p>This method is called by the framework for each request to validate
     * the JWT token in the Authorization header and set up the security context
     * if the token is valid.</p>
     *
     * <p>When extending this class, ensure to call super.doFilterInternal()
     * or properly handle the token validation and authentication setup.</p>
     *
     * @param request the HTTP request
     * @param response the HTTP response
     * @param filterChain the filter chain for the request
     * @throws ServletException if a servlet exception occurs
     * @throws IOException if an I/O exception occurs
     */
    @Override
    protected void doFilterInternal(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final FilterChain filterChain)
            throws ServletException, IOException {
        String token = parseToken(request);
        if (token != null
                && SecurityContextHolder.getContext().getAuthentication()
                == null) {
            UUID id = UUID.fromString(jwtUtils.extractSubject(token, false));
            UserDetailsImpl userDetails =
                    (UserDetailsImpl) userDetailsService.loadUserById(id);
            if (jwtUtils.validateToken(token, userDetails, false)) {
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );
                authentication.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(
                                request));
                SecurityContextHolder.getContext()
                        .setAuthentication(authentication);
            }
        }
        filterChain.doFilter(request, response);
    }

    /**
     * Extracts the JWT token from the Authorization header.
     *
     * @param request the HTTP request containing the Authorization header
     * @return the JWT token without the Bearer prefix, or null if not found
     */
    private String parseToken(final HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        if (headerAuth != null && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(BEARER_PREFIX_LENGTH);
        }
        return null;
    }
}
