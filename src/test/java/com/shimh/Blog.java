package com.shimh;

import java.io.Serializable;

/**
 * @author: shiminghui
 * @create: 2020年06月
 **/
public class Blog implements Serializable {

  private String title;

  private Integer id;

  public Blog() {
  }


  public Blog(String title) {
    this.title = title;
  }

  public Blog(Integer id, String title) {
    this.id = id;
    this.title = title;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  @Override
  public String toString() {
    return "Blog{" +
      "title='" + title + '\'' +
      ", id=" + id +
      '}';
  }
}
