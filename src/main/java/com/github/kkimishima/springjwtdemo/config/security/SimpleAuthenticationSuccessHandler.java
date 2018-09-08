package com.github.kkimishima.springjwtdemo.config.security;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class SimpleAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
  private final Algorithm algorithm;
  private final Long EXPIRATION_TIME = TimeUnit.MINUTES.toMillis(10L);

  public SimpleAuthenticationSuccessHandler(String secretKey) throws Exception {
    Objects.requireNonNull(secretKey, "secret key must be not null");
    this.algorithm = Algorithm.HMAC512(secretKey);
  }

  @Override
  public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) {
    if (httpServletResponse.isCommitted()) {
      return;
    }
    httpServletResponse.setHeader("Authorization", String.format("Bearer %s", generateToken(authentication)));
    httpServletResponse.setStatus(HttpStatus.OK.value());
    clearAuthenticationAttributes(httpServletRequest);

  }

  private String generateToken(Authentication auth) {
    SimpleLoginUser simpleLoginUser = (SimpleLoginUser) auth.getPrincipal();
    Date issuedAt = new Date();
    Date notBefore = new Date(issuedAt.getTime());
    Date expiresAt = new Date(issuedAt.getTime() + EXPIRATION_TIME);
    return JWT.create()
        .withIssuedAt(issuedAt)
        .withNotBefore(notBefore)
        .withExpiresAt(expiresAt)
        .withSubject(simpleLoginUser.getUsers().getId().toString())
        .sign(this.algorithm);
  }

  private void clearAuthenticationAttributes(HttpServletRequest request) {
    HttpSession session = request.getSession(false);
    if (session == null) {
      return;
    }
    session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
  }
}
