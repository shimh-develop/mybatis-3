package com.shimh;

import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.cache.decorators.LoggingCache;
import org.apache.ibatis.cache.decorators.ScheduledCache;
import org.apache.ibatis.cache.impl.PerpetualCache;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Reader;

/**
 * @author: shiminghui
 * @create: 2021年02月
 **/
public class CacheTest {

  private static Logger LOG = LoggerFactory.getLogger(CacheTest.class);

  /**
   *
   * 当 同一个 SqlSession执行两次相同的sql语句时，第一次执行完毕将会写入缓存，第二次不再去数据库查询
   * 一级缓存是 SqlSession会话级别的缓存
   *
   * @throws Exception
   */
  @Test
  public void sqlSessionCacheTest1() throws Exception {

    final String resource = "com/shimh/mybatis-config.xml";
    final Reader reader = Resources.getResourceAsReader(resource);

    SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);

    SqlSession sqlSession = sqlSessionFactory.openSession(true);

    BlogMapper mapper = sqlSession.getMapper(BlogMapper.class);

    LOG.info("sqlSession1: 第一次查询开始");
    System.out.println(mapper.getById(1));

    sqlSession.selectOne("com.shimh.BlogMapper.getById", 1);

    LOG.info("sqlSession1: 第二次查询开始");
    System.out.println(mapper.getById(1));

    SqlSession sqlSession2 = sqlSessionFactory.openSession(true);

    BlogMapper mapper2 = sqlSession2.getMapper(BlogMapper.class);

    LOG.info("sqlSession2: 第一次查询开始");
    System.out.println(mapper2.getById(1));
  }

  /**
   *
   * 如果SqlSession执行了DML操作（insert、update和delete），那么MyBatis会清空SqlSession中的缓存
   *
   * @throws Exception
   */
  @Test
  public void sqlSessionCacheTest2() throws Exception {

    final String resource = "com/shimh/mybatis-config.xml";
    final Reader reader = Resources.getResourceAsReader(resource);

    SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);

    SqlSession sqlSession = sqlSessionFactory.openSession(true);

    BlogMapper mapper = sqlSession.getMapper(BlogMapper.class);

    LOG.info("第一次查询开始");
    LOG.info("{}", mapper.getById(1));

    mapper.deleteById(2);

    LOG.info("第二次查询开始");
    LOG.info("{}", mapper.getById(1));
  }

  /**
   *
   * 修改返回对象时出现问题
   *
   * @throws Exception
   */
  @Test
  public void sqlSessionCacheTest3() throws Exception {

    final String resource = "com/shimh/mybatis-config.xml";
    final Reader reader = Resources.getResourceAsReader(resource);
    SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);

    SqlSession sqlSession = sqlSessionFactory.openSession(true);
    BlogMapper mapper = sqlSession.getMapper(BlogMapper.class);
    LOG.info("第一次查询开始");
    Blog blog = mapper.getById(1);
    System.out.println(blog);
    blog.setTitle("小虎new");

    LOG.info("第二次查询开始");
    System.out.println(mapper.getById(1));
  }

  /**
   *
   * 数据写入导致读取脏数据
   *
   * @throws Exception
   */
  @Test
  public void sqlSessionCacheTest4() throws Exception {

    final String resource = "com/shimh/mybatis-config.xml";
    final Reader reader = Resources.getResourceAsReader(resource);
    SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);

    SqlSession sqlSession = sqlSessionFactory.openSession(true);
    BlogMapper mapper = sqlSession.getMapper(BlogMapper.class);

    LOG.info("第一次查询开始");
    System.out.println(mapper.getById(1));

    SqlSession sqlSession2 = sqlSessionFactory.openSession(true);
    BlogMapper mapper2 = sqlSession2.getMapper(BlogMapper.class);

    mapper2.update(new Blog(1, "小虎new"));

    Thread.sleep(10000);

    LOG.info("第二次查询开始");
    System.out.println(mapper.getById(1));
  }

  /**
   * 二级缓存
   *
   * @throws Exception
   */
  @Test
  public void GlobalCacheTest1() throws Exception {

    // 打开 BlogMapper.xml：<cache/>
    final String resource = "com/shimh/mybatis-config.xml";
    final Reader reader = Resources.getResourceAsReader(resource);
    SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);

    SqlSession sqlSession = sqlSessionFactory.openSession(true);
    BlogMapper mapper = sqlSession.getMapper(BlogMapper.class);

    LOG.info("第一次查询开始");
    System.out.println(mapper.getById(1));

    // 必须提交事务
    sqlSession.commit();

    SqlSession sqlSession2 = sqlSessionFactory.openSession(true);
    BlogMapper mapper2 = sqlSession2.getMapper(BlogMapper.class);

    LOG.info("第二次查询开始");
    System.out.println(mapper2.getById(1));
  }

  public void aa() {
    // 简单的缓存
    Cache cache = new PerpetualCache("cache1");

    //打印命中率的缓存
    Cache cache2 = new LoggingCache(new PerpetualCache("cache1"));

    //定时清空的额缓存
    Cache cache3 = new ScheduledCache(new PerpetualCache("cache1"));

    //定时清空 打印命中率的缓存
    Cache cache4 = new ScheduledCache(new LoggingCache(new PerpetualCache("cache1")));


  }


}
