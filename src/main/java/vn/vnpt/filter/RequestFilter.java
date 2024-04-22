package vn.vnpt.filter;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.HttpMethod;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import vn.vnpt.api.repository.checker.RoleRepo;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
public class RequestFilter implements Filter {

    @Autowired
    ApplicationContext applicationContext;
    @Autowired
    RoleRepo roleRepo;


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest originalRequest = (HttpServletRequest) request;

        var uri = "/" + applicationContext.getId() + originalRequest.getRequestURI();

        var method = originalRequest.getMethod();

        List<String> uriPublic = List.of(
                "/management-service/swagger-ui/**",
                "/management-service/v3/api-docs",
                "/management-service/v3/api-docs.yaml",
                "/management-service/v3/api-docs/swagger-config",
                "/v3/api-docs/swagger-config",
                "/v3/api-docs/**",
                "/v3/api-docs.yaml"
        );

        if (uriPublic.stream().anyMatch(u -> uri.matches(u.replace("**", ".*")))) {
            chain.doFilter(request, response);
            return;
        }

        var jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var userId = jwt.getClaims().get("sub").toString();
        var username = jwt.getClaims().get("preferred_username").toString();

        switch (method) {
            case HttpMethod.GET -> {
                var roles = roleRepo.check(userId, uri);
                var authorities = roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).toList();
                SecurityContextHolder.getContext().setAuthentication(new JwtAuthenticationToken(jwt, authorities, username));
                chain.doFilter(request, response);
            }
            case HttpMethod.POST -> {
                var roles = roleRepo.check(userId, uri);
                var authorities = roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).toList();
                SecurityContextHolder.getContext().setAuthentication(new JwtAuthenticationToken(jwt, authorities, username));

                if(originalRequest.getContentType().contains(MediaType.MULTIPART_FORM_DATA_VALUE)){
                    chain.doFilter(originalRequest, response);
                    return;
                }

                var requestBody = new String(StreamUtils.copyToByteArray(request.getInputStream()));
                var objectMapper = new ObjectMapper();
                var jsonNode = objectMapper.readTree(requestBody);

                chain.doFilter(new RequestWrapper(originalRequest, jsonNode), response);
            }
            default -> {
                SecurityContextHolder.getContext().setAuthentication(new JwtAuthenticationToken(jwt, List.of(), username));
                chain.doFilter(request, response);
            }
        }
    }

}
