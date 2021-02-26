package com.shimh;

/**
 * @author: shiminghui
 * @create: 2020年06月
 **/
public interface BlogMapper {

  Blog getById(Integer id);

  void insert(Blog blog);

  void update(Blog blog);

  void deleteById(Integer id);
}
