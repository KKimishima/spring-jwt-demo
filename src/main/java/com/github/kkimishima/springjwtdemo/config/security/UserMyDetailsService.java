package com.github.kkimishima.springjwtdemo.config.security;

import com.github.kkimishima.springjwtdemo.domain.Users;
import com.github.kkimishima.springjwtdemo.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserMyDetailsService implements UserDetailsService {
  private UsersRepository usersRepository;

  @Autowired
  public UserMyDetailsService(UsersRepository usersRepository) {
    this.usersRepository = usersRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
    Users users = usersRepository.findByName(s);
    if (users == null) {
      throw new UsernameNotFoundException("ユーザが見つかりません");
    }
    return new SimpleLoginUser(users);
  }
}
