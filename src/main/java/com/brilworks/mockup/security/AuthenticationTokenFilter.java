package com.brilworks.mockup.security;

import com.brilworks.mockup.dto.RestError;
import com.brilworks.mockup.dto.RestResponse;
import com.brilworks.mockup.exceptions.RateLimitExceededException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.ConsumptionProbe;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class AuthenticationTokenFilter extends OncePerRequestFilter {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final TokenUtils tokenUtils;
    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();
    private UserDetailsService userDetailsService;
    @Value("${rate.limit.enabled}")
    private boolean rateLimitEnabled;
    @Value("${rate.limit.request.limit}")
    private Integer rateLimitRequestLimit;
    @Value("${rate.limit.request.time}")
    private Integer rateLimitRequestTime;

    public void setUserDetailsService(UserDetailsService userDetailsService){
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpRequest, HttpServletResponse httResponse, FilterChain chain) throws IOException {
        try {
            String authToken = null;
            String userName = null;

            // If Regular CRM User
            String header = httpRequest.getHeader("Authorization");
            if (header != null && header.startsWith("Bearer ")) {
                authToken = header.substring(7);
                //get client id
                userName = tokenUtils.getUserNameFromToken(authToken);
            }
            boolean applyRateLimit = applyRateLimit(header, httpRequest);
            if (applyRateLimit) {
                if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(userName);

                    if (tokenUtils.validateToken(authToken, userDetails)) {
                        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());
                        usernamePasswordAuthenticationToken
                                .setDetails(new WebAuthenticationDetailsSource().buildDetails(httpRequest));
                        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                    }
                }
                chain.doFilter(httpRequest, httResponse);
            }

        } catch (RateLimitExceededException ex) {
            RestResponse res = new RestResponse(false);
            res.setError(new RestError(429, ex.getMessage()));

            httResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
            httResponse.getWriter().append(objectMapper.writeValueAsString(res));
            httResponse.setStatus(200);
        } catch (Exception ex) {
            RestResponse res = new RestResponse(false);
            res.setError(new RestError(401, "Access Denied [" + ex.getMessage() + "]"));
            httResponse.setContentType("application/json");
            httResponse.getWriter().append(objectMapper.writeValueAsString(res));
            httResponse.setStatus(200);
        }
    }

    private boolean applyRateLimit(String header, HttpServletRequest httpRequest) throws URISyntaxException {
        if (!rateLimitEnabled) {
            return true;
        }
        if (null == header)
            header = getUrlKey(httpRequest.getRequestURI());
        Bucket bucket = buckets.computeIfAbsent(header, key -> createNewBucket());
        ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);
        if (probe.isConsumed())
            return true;
        else
            throw new RateLimitExceededException("Too many requests");
    }

    private String getUrlKey(String url) throws URISyntaxException {
        URI uri = new URI(url);
        String path = uri.getPath();
        // Remove slashes and split the path into segments
        String[] pathSegments = path.split("/");

        // Construct the new format
        StringBuilder newFormat = new StringBuilder();
        for (String segment : pathSegments) {
            if (!segment.isEmpty()) {
                newFormat.append(segment).append("-");
            }
        }
        newFormat.append("key");
        return newFormat.toString();
    }

    private Bucket createNewBucket() {
        Bandwidth limit = Bandwidth.simple(rateLimitRequestLimit, Duration.ofSeconds(rateLimitRequestTime));
        return Bucket4j.builder()
                .addLimit(limit)
                .build();
    }

}
