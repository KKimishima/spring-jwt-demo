package com.github.kkimishima.springjwtdemo.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Users")
public class Users {

  @Id
  private Long id;

  @Column
  private String name;

  @Column
  private String pass;

  @CreationTimestamp
  @Column(name = "created_time", updatable = false)
  private Date createdTime;

  @UpdateTimestamp
  @Column(name = "updated_time")
  private Date updatedTime;

}
