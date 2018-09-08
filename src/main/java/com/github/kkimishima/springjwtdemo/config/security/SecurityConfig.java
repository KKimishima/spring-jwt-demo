package com.github.kkimishima.springjwtdemo.config.security;


import com.github.kkimishima.springjwtdemo.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.GenericFilterBean;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
  private final String secretKey = "hoge";
  @Autowired
  private UsersRepository usersRepository;

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
        // 許可の設定
        .authorizeRequests()
        .mvcMatchers("/hello/**")
        .hasRole("USER")
        .mvcMatchers("/hello/**")
        .hasRole("ADMIN")
        .anyRequest().authenticated()

        // ログイン
        .and()
        .formLogin()
        .loginProcessingUrl("/login").permitAll()
        .usernameParameter("username")
        .passwordParameter("password")
        .successHandler(authenticationSuccessHandler())
        .failureHandler(authenticationFailureHandler())

        // エラー
        .and()
        .exceptionHandling()
        .authenticationEntryPoint(authenticationEntryPoint())
        .accessDeniedHandler(accessDeniedHandler())


        // ログアウト
        .and()
        .logout()
        .logoutUrl("/logout")

        // csrf
        .and()
        .csrf()
        .disable()

        // JWT認証用フィルタ
        .addFilterBefore(tokenFilter(), UsernamePasswordAuthenticationFilter.class)

        // セッション設定
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

  }

  @Bean("UserMyDetailsService")
  UserDetailsService userMyDetailsService() {
    return new UserMyDetailsService(usersRepository);
  }

  @Autowired
  public void configureAuth(AuthenticationManagerBuilder auth) throws Exception {
    auth
        .userDetailsService(userMyDetailsService())
        .passwordEncoder(passwordEncoder());
  }

  @Bean
  PasswordEncoder passwordEncoder() {
    return new Pbkdf2PasswordEncoder();
  }

  GenericFilterBean tokenFilter() throws Exception {
    return new SimpleTokenFilter(usersRepository, secretKey);
  }

  AuthenticationEntryPoint authenticationEntryPoint() {
    return new SimpleAuthenticationEntryPoint();
  }

  AccessDeniedHandler accessDeniedHandler() {
    return new SimpleAccessDeniedHandler();
  }

  AuthenticationSuccessHandler authenticationSuccessHandler() throws Exception {
    return new SimpleAuthenticationSuccessHandler(secretKey);
  }

  AuthenticationFailureHandler authenticationFailureHandler() {
    return new SimpleAuthenticationFailureHandler();
  }
}
