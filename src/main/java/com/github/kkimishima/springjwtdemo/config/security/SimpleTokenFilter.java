package com.github.kkimishima.springjwtdemo.config.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.github.kkimishima.springjwtdemo.repository.UsersRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

public class SimpleTokenFilter extends GenericFilterBean {

  final private UsersRepository usersRepository;
  final private Algorithm algorithm;

  public SimpleTokenFilter(UsersRepository usersRepository, String secretKey) throws Exception {
    Objects.requireNonNull(secretKey, "secret key must be not null");
    this.usersRepository = usersRepository;
    this.algorithm = Algorithm.HMAC512(secretKey);
  }

  @Override
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
    String token = resolveToken(servletRequest);
    if (token == null) {
      filterChain.doFilter(servletRequest, servletResponse);
      return;
    }

    try {
      authentication(verifyToken(token));
    } catch (Exception e) {
      SecurityContextHolder.clearContext();
      ((HttpServletResponse) servletResponse).sendError(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase());
    }
    filterChain.doFilter(servletRequest, servletResponse);
  }

  private String resolveToken(ServletRequest request) {
    String token = ((HttpServletRequest) request).getHeader("Authorization");
    if (token == null || !token.startsWith("Bearer ")) {
      return null;
    }
    // remove "Bearer "
    return token.substring(7);
  }

  private void authentication(DecodedJWT jwt) {
    Long userId = Long.valueOf(jwt.getSubject());

    usersRepository.findById(userId).ifPresent(users -> {
      SimpleLoginUser simpleLoginUser = new SimpleLoginUser(users);
      SecurityContextHolder
          .getContext()
          .setAuthentication(new UsernamePasswordAuthenticationToken(simpleLoginUser, null, simpleLoginUser.getAuthorities()));
    });
    System.out.println(SecurityContextHolder.getContext());
  }

  private DecodedJWT verifyToken(String token) {
    JWTVerifier verifier = JWT.require(algorithm).build();
    return verifier.verify(token);
  }

}
