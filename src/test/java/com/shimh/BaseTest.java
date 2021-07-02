package com.shimh;

import org.apache.ibatis.binding.MapperRegistry;
import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.cache.decorators.BlockingCache;
import org.apache.ibatis.cache.impl.PerpetualCache;
import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.datasource.unpooled.UnpooledDataSource;
import org.apache.ibatis.executor.keygen.Jdbc3KeyGenerator;
import org.apache.ibatis.executor.keygen.KeyGenerator;
import org.apache.ibatis.io.ClassLoaderWrapper;
import org.apache.ibatis.io.ResolverUtil;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.io.VFS;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.logging.jdbc.ConnectionLogger;
import org.apache.ibatis.reflection.*;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.reflection.invoker.GetFieldInvoker;
import org.apache.ibatis.reflection.invoker.Invoker;
import org.apache.ibatis.reflection.invoker.MethodInvoker;
import org.apache.ibatis.reflection.invoker.SetFieldInvoker;
import org.apache.ibatis.reflection.property.PropertyCopier;
import org.apache.ibatis.reflection.property.PropertyNamer;
import org.apache.ibatis.reflection.property.PropertyTokenizer;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.Transaction;
import org.apache.ibatis.transaction.jdbc.JdbcTransaction;
import org.apache.ibatis.type.*;
import org.junit.Test;

import java.io.Reader;
import java.lang.reflect.Type;
import java.sql.*;
import java.util.Arrays;
import java.util.Properties;

/**
 * @author: shiminghui
 * @create: 2020年06月
 **/
public class BaseTest {

  @Test
  public void testReflection() {
    /**
     * Reflector：Reflector 对象都对应一个类，在 Reflector 缓存了反射操作需要使用的类的元信息
     * DefaultReflectorFactory：创建 Reflector 并缓存
     */
    ReflectorFactory reflectorFactory;
    DefaultReflectorFactory defaultReflectorFactory;
    Reflector reflector;

    /**
     * 封装了 Field 和 Method 反射的调用
     */
    Invoker invoker;
    GetFieldInvoker getFieldInvoker;
    SetFieldInvoker setFieldInvoker;
    MethodInvoker methodInvoker;


    TypeParameterResolver resolver;


    /**
     * 创建指定类型的对象
     * 配置：mybatis-config.xml
     * <objectFactory type="org.mybatis.example.ExampleObjectFactory">
     *   <property name="someProperty" value="100"/>
     * </objectFactory>
     */
    ObjectFactory objectFactory;
    DefaultObjectFactory defaultObjectFactory;

    String str = "orders[O].items[O].name";
    PropertyTokenizer propertyTokenizer = new PropertyTokenizer(str);
    while (propertyTokenizer.hasNext()) {
      System.out.println("name: " + propertyTokenizer.getName() + " indexedName: " + propertyTokenizer.getIndexedName()+ " index: " + propertyTokenizer.getIndex());
      propertyTokenizer = propertyTokenizer.next();
    }
    //name: orders indexedName: orders[O] index: O
    //name: items indexedName: items[O] index: O
    /**
     * 根据方法名获取属性|判断是否是 getter setter 方法
    */
    PropertyNamer propertyNamer;
    /**
     * 两个对象属性拷贝
     */
    PropertyCopier propertyCopier;


    MetaClass metaClass;

  }

  @Test
  public void testTypeHandler() {
    /**
     * TypeHandler：Java类型 与 JdbcType类型 互相转换
     * BaseTypeHandler：抽象类，基础实现 处理了null值，具体交由子类实现
     * TypeHandlerRegistry：管理众多的TypeHandler
     * 配置：在 mybatis-config.xml 配置文件中的 typeHandlers 节点下，配置自定义的 TypeHandler
     * <typeHandlers>
     *   <typeHandler handler="org.mybatis.example.ExampleTypeHandler"/>
     * </typeHandlers>
     *
     */
    JdbcType jdbcType; // SQL类型映射 枚举类用法 提前整个map


    TypeHandler typeHandler;
    BaseTypeHandler baseTypeHandler;
    TypeHandlerRegistry typeHandlerRegistry;

    TypeReference t = new TypeReference<Double>(){};
    System.out.println(Double.class == t.getRawType());
  }

  @Test
  public void testTypeAlias() {
    /**

     * TypeAliasRegistry：别名注册
     * 默认为 Java 基本类型及其数组类型、基本类型的封
     * 装类及其数组类型 Date BigDecimal Biglnt ger Map HashMap List ArrayList Collection
     * Iterator ResultSet 等类型添加了别名
     *
     * 配置：在配置文件中的 typeAlias 节点下 配置 <typeAlias alias="Author" type="domain.blog.Author"/>
     * 或在类上 标注 @Alias("Author") public class Author {}
     */

    TypeAliasRegistry typeAliasRegistry;
  }

  @Test
  public void testLog() {
    /**
     * 适配器模式：将别的日志 适配成 自己的 org.apache.ibatis.logging.Log
     *
     * ConnectionLogger：动态代理了 Connection 可以打印日志
     *
     * 配置：在 mybatis-config.xml 配置文件
     * <settings>
     *  <setting name="logImpl" value="SLF4J | LOG4J | LOG4J2 | JDK_LOGGING | COMMONS_LOGGING | STDOUT_LOGGING | NO_LOGGING"/>
     * </settings>
    */
    LogFactory logFactory;
    ConnectionLogger connectionLogger;
  }

  @Test
  public void testIo() {
    //
    Resources resources;
    ClassLoaderWrapper wrapper;

    ResolverUtil resolverUtil;

    VFS vfs;
  }

  @Test
  public void testDataSource() {
    /**
     * 工厂方法模式：DataSourceFactory PooledDataSourceFactory UnpooledDataSourceFactory
     *
     *
     *
     */
    PooledDataSource pooledDataSource;
    UnpooledDataSource unpooledDataSource;
  }

  @Test
  public void testTransaction() {
    /**
     * 工厂方法：TransactionFactory JdbcTransactionFactory
     *
     *
     */
    Transaction transaction;
    JdbcTransaction jdbcTransaction;
  }

  @Test
  public void testBinding() {
    /**
     * 返回Mapper的代理对象，其内部还是操作SqlSession
     *
     * 使用：
     *  XXMapper xxMapper = sqlSession.getMapper(XXMapper.class)
     *
     *
     *
     */
    MapperRegistry registry;

  }

  @Test
  public void testCache() {
    /**
     * 装饰模式：添加新功能 继承不大可行
     *
     *
     */
    Cache cache;
    PerpetualCache perpetualCache; //s 具体实现
    BlockingCache blockingCache; //s 阻塞版装饰

    CacheKey cacheKey; //s mybatis用的缓存的key
  }

  @Test
  public void testKeyGenerator() throws Exception{
    KeyGenerator keyGenerator;
    Jdbc3KeyGenerator jdbc3KeyGenerator;

    final String resource = "com/shimh/jdbc.properties";
    final Reader reader = Resources.getResourceAsReader(resource);
    Properties properties = new Properties();
    properties.load(reader);

    Class.forName(properties.getProperty("driver"));
    Connection conn = DriverManager.getConnection(properties.getProperty("url"), properties.getProperty("username"), properties.getProperty("password"));
    conn.setAutoCommit(false);
    PreparedStatement pstm = conn.prepareStatement("INSERT INTO blog (title) VALUES (?)",
      Statement.RETURN_GENERATED_KEYS);

    pstm.setString(1, "title1");
    pstm.addBatch();
    pstm.setString(1, "title2");
    pstm.addBatch();
    pstm.executeBatch();
    // 返回自增主键值
    ResultSet rs = pstm.getGeneratedKeys();
    while (rs.next()) {
      Object value = rs.getObject(1);
      System.out.println(value);
    }
    conn.commit();
    rs.close();
    pstm.close();
    conn.close();
  }


  /**
   * 执行流程
   * @throws Exception
   */
  @Test
  public void testMybatis() throws Exception{
    final String resource = "com/shimh/mybatis-config.xml";
    final Reader reader = Resources.getResourceAsReader(resource);
    //s 源码进入
    SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);

    SqlSession sqlSession = sqlSessionFactory.openSession();

    BlogMapper mapper = sqlSession.getMapper(BlogMapper.class);
    //System.out.println(mapper.getById(1));
    System.out.println((Blog)sqlSession.selectOne("getById", 1));

    System.out.println(mapper.listBlogs(Arrays.asList(1,2)));
    sqlSession.commit();
    sqlSession.rollback();
    sqlSession.close();
  }
}
