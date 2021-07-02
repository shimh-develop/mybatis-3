package com.shimh;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author: shiminghui
 * @create: 2020年06月
 **/
public interface BlogMapper {

  Blog getById(Integer id);

  Blog getById(@Param("id") Integer id, int a);
  Blog getById(@Param("id") Integer id, int a, String c);

  void insert(Blog blog);

  void update(Blog blog);

  void deleteById(Integer id);

  List<Blog> listBlogs(@Param("ids") List<Integer> ids);
}
