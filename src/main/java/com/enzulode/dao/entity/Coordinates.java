package com.enzulode.dao.entity;

import com.enzulode.dao.entity.common.BusinessEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Coordinates extends BusinessEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Float x;

  private int y;

  public Coordinates() {}

  public Coordinates(Float x, int y) {
    this.x = x;
    this.y = y;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Float getX() {
    return x;
  }

  public void setX(Float x) {
    this.x = x;
  }

  public int getY() {
    return y;
  }

  public void setY(int y) {
    this.y = y;
  }
}
