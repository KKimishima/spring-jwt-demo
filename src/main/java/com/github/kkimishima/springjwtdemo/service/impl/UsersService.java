package com.github.kkimishima.springjwtdemo.service.impl;


import com.github.kkimishima.springjwtdemo.domain.Users;
import com.github.kkimishima.springjwtdemo.repository.UsersRepository;
import com.github.kkimishima.springjwtdemo.service.UsersServiceInterFace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsersService implements UsersServiceInterFace {
  private UsersRepository usersRepository;

  @Autowired
  public UsersService(UsersRepository usersRepository) {
    this.usersRepository = usersRepository;
  }

  @Override
  public Users getUser(String name) {
    return usersRepository.findByName(name);
  }
}
