package com.github.kkimishima.springjwtdemo.repository;

import com.github.kkimishima.springjwtdemo.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {
  Users findByName(String name);
//  public Users findById(Long id);
}
