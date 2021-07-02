/**
 *    Copyright 2009-2019 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.apache.ibatis.scripting.xmltags;

import org.apache.ibatis.builder.SqlSourceBuilder;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.session.Configuration;

/**
 * @author Clinton Begin
 */
public class DynamicSqlSource implements SqlSource {

  private final Configuration configuration;
  private final SqlNode rootSqlNode;

  public DynamicSqlSource(Configuration configuration, SqlNode rootSqlNode) {
    this.configuration = configuration;
    this.rootSqlNode = rootSqlNode;
  }

  @Override
  public BoundSql getBoundSql(Object parameterObject) {
    DynamicContext context = new DynamicContext(configuration, parameterObject);
    //s MixedSqlNode 对SQL中的动态标签进行解析 ${} <if> <foreach>   将解析后的SQL添加到Context中

    /**
     *
     * select ＝” selectDyn2 result Type=” Blog” >
     * select * from Blog B where id IN
     * ！－－为了防止混淆，这里将工ndex 属性位设置为 idx ， itern 生位设置为工itm
     * <foreach collection=”ids” index=” idx” item=”itm” open=” (” separator=”,” close=”)”>
     * #{itm}
     * </foreach>
     * </select>
     * => select * from Bl 。q B where id IN
     * ( # { _frch_itm_ 0) , # { _frch_itm_1})
     *
     * DynamicContext.bindings
     *
     * ” _frch_itm_O”->.’1”
     * ” _frch_itm_1”->.’2”
     *
     */
    //s 这里会解析 1 ${} （TextSqlNode） 直接替换 2 动态标签 foreach 生成变量 同时将变量对应的值添加到Context的bindings中
    rootSqlNode.apply(context);

    //s 对#{name,jdbcType} 进行解析 替换成 ？ 生成parameterMappings 对应？的属性名 放到SqlSource中parameterMappings（List）
    SqlSourceBuilder sqlSourceParser = new SqlSourceBuilder(configuration);
    Class<?> parameterType = parameterObject == null ? Object.class : parameterObject.getClass();
    SqlSource sqlSource = sqlSourceParser.parse(context.getSql(), parameterType, context.getBindings());

    //s BoundSql：包含 ？ 占位符的SQL 和 参数映射
    BoundSql boundSql = sqlSource.getBoundSql(parameterObject);
    //s 运行时 实际的参数 属性和对应的值  foreach的属性是动态生成的
    context.getBindings().forEach(boundSql::setAdditionalParameter);
    return boundSql;
  }

}
