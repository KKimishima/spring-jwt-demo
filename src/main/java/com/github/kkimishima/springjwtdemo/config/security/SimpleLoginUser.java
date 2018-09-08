package com.github.kkimishima.springjwtdemo.config.security;


import com.github.kkimishima.springjwtdemo.domain.Users;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;

public class SimpleLoginUser extends User {
  private Users users;

  public SimpleLoginUser(Users users) {
    super(users.getName(), users.getPass(), AuthorityUtils.createAuthorityList("ROLE_ADMIN", "ROLE_USER"));
    this.users = users;
  }

  public Users getUsers() {
    return users;
  }
}
