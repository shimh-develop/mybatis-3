package com.shimh;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Reader;
import java.sql.*;

/**
 * @author: shiminghui
 * @create: 2021年02月
 **/
public class TransactionTest {

  private static Logger LOG = LoggerFactory.getLogger(TransactionTest.class);

  @Test
  public void jdbcTransactionTest() throws Exception {

    Class.forName("com.mysql.cj.jdbc.Driver");

    Connection conn = null;

    Statement statement = null;

    try {

      conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/mybatis_blog?useUnicode=true&characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull&autoReconnect=true&serverTimezone=Asia/Shanghai", "root", "123456");

      // 开启事务
      conn.setAutoCommit(false);

      statement = conn.createStatement();

      String sql = "INSERT INTO blog(title) VALUES('胖虎')";

      statement.executeUpdate(sql);

      //错误
      int a = 1 / 0;
      // 提交事务
      conn.commit();
    } catch (SQLException e) {
      // 回滚事务
      conn.rollback();

      LOG.error(e.getMessage(), e);
    } finally {
      close(conn, statement);
    }
  }

  @Test
  public void mybatisTransactionTest() throws Exception {

    final String resource = "com/shimh/mybatis-config.xml";
    final Reader reader = Resources.getResourceAsReader(resource);

    SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
    // 开启事务 默认是开启的
    SqlSession sqlSession = sqlSessionFactory.openSession(false);

    try {
      Blog blog = new Blog("小虎");

      BlogMapper mapper = sqlSession.getMapper(BlogMapper.class);
      mapper.insert(blog);
      //2
      sqlSession.insert("com.shimh.BlogMapper.insert", blog);

      //错误
      int a = 1 / 0;
      // 提交事务
      sqlSession.commit();
    } catch (Exception e) {
      // 回滚事务
      sqlSession.rollback();
      LOG.error(e.getMessage(), e);
    } finally {
      sqlSession.close();
    }
  }

  private void close(Connection conn, Statement statement) {
    if (statement != null)
      try {
        statement.close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    if (conn != null)
      try {
        conn.close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
  }
}
