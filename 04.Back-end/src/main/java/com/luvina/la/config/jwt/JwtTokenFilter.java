package com.luvina.la.config.jwt;

import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.luvina.la.common.ErrorCodeConstants;
import com.luvina.la.common.HttpStatusConstants;
import com.luvina.la.exception.BusinessException;
import com.luvina.la.payload.MessageResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

public class JwtTokenFilter extends OncePerRequestFilter {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final JwtTokenProvider tokenProvider;
    private final UserDetailsService userDetailsService;

    public JwtTokenFilter(JwtTokenProvider tokenProvider, UserDetailsService userDetailsService) {
        this.tokenProvider = tokenProvider;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {

        try {
            String jwt = this.getJwtFromRequest(request);
            UsernamePasswordAuthenticationToken authentication = null;
            if (StringUtils.hasText(jwt) && this.tokenProvider.validateToken(jwt)) {

                if (SecurityContextHolder.getContext().getAuthentication() == null) {
                    String username = this.tokenProvider.getUsernameFromJWT(jwt);
                    UserDetails userDetail = this.userDetailsService.loadUserByUsername(username);

                    if (StringUtils.hasText(userDetail.getUsername())) {
                        authentication = new UsernamePasswordAuthenticationToken(userDetail, null, userDetail.getAuthorities());
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                }
            } else {
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception ex) {
            log.error("failed on set user authentication", ex);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            MessageResponse msg = new MessageResponse();
            msg.setCode(ErrorCodeConstants.ER015);
            msg.setParams(new ArrayList<>());

            response.getWriter().write(new ObjectMapper().writeValueAsString(msg));
            return;
        }

        chain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);

        // check and get token from request header
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
            return bearerToken.substring(7);
        }
        return null;
    }

}
