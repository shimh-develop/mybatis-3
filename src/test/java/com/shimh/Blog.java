package com.shimh;

/**
 * @author: shiminghui
 * @create: 2020年06月
 **/
public class Blog {

  private String title;

  private Integer id;

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
