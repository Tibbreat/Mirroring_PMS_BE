package sep490.g13.pms_be.jwt;


import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import sep490.g13.pms_be.exception.jwt.JwtTokenException;
import sep490.g13.pms_be.exception.jwt.JwtTokenExpiredException;
import sep490.g13.pms_be.exception.jwt.JwtTokenMalformedException;
import sep490.g13.pms_be.exception.jwt.JwtTokenUnsupportedException;

import java.io.IOException;

@Component
@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String token = jwtTokenProvider.getTokenFromRequest(request);

        if (token != null) {
            try {
                if (jwtTokenProvider.validateToken(token)) {
                    String emailId = jwtTokenProvider.getUserName(token);
                    UserDetails userDetails = userDetailsService.loadUserByUsername(emailId);
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (ExpiredJwtException e) {
                throw new JwtTokenExpiredException("Token đã hết hạn.");
            } catch (MalformedJwtException e) {
                throw new JwtTokenMalformedException("Token không hợp lệ.");
            } catch (UnsupportedJwtException e) {
                throw new JwtTokenUnsupportedException("Token không được hỗ trợ.");
            } catch (Exception e) {
                throw new JwtTokenException( "Token không hợp lệ.");
            }
        }
        filterChain.doFilter(request, response);
    }


}
