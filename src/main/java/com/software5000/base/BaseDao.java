package com.software5000.base;

import com.github.pagehelper.Page;
import com.google.common.collect.Iterables;
import com.software5000.base.jsql.AndExpressionList;
import com.software5000.base.jsql.ConditionWrapper;
import com.software5000.util.BpMybatisException;
import com.software5000.util.ClassUtil;
import com.software5000.util.JsqlUtils;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.operators.relational.MultiExpressionList;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.AllColumns;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.update.Update;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;


/**
 * 抽象基础的Dao封装类
 *
 * @author matuobasyouca@gmail.com
 */
public abstract class BaseDao {

    private Logger logger = LoggerFactory.getLogger(BaseDao.class);

    /**
     * 默认的数据库结构为 snake (a_b_c),代码中会将驼峰转换为蛇形
     * 如果数据库结果默认为 camel,则本变量设置为false
     */
    public static boolean DB_SCHEMES_SNAKE_TYPE = true;


    /**
     * 最终数据库中的表/字段名大小写规格
     * true为全部小写，false为全部大写
     */
    public static boolean DB_SCHEMES_ALL_LOWER_CASE = false;

    /**
     * 全局忽略的字符串
     */
    public static String IGNORE_FILEDNAMES = "";

    /**
     * 允许用户在继承时可以重新覆盖默认配置
     *
     * @param dbSchemesSnakeType 默认数据库的结构类型
     * @param dbSchemesAllLowerCase 数据库结构大小写情况
     * @param ignoreFiledNames 需要固定忽略的字段名称
     */
    public void initConfig(boolean dbSchemesSnakeType, boolean dbSchemesAllLowerCase, String ignoreFiledNames) {
        BaseDao.DB_SCHEMES_SNAKE_TYPE = dbSchemesSnakeType;
        BaseDao.DB_SCHEMES_ALL_LOWER_CASE = dbSchemesAllLowerCase;
        BaseDao.IGNORE_FILEDNAMES = ignoreFiledNames;
    }


    /**
     * 获取mybatis中的SqlSession，用于基础增删改查操作
     *
     * @return mybatis中的SqlSession
     */
    public abstract SqlSession getSqlSession();

    // region insert 方法块

    /**
     * 根据sql方法名称和对象插入数据库
     *
     * @param sqlName xml中的sql id
     * @param obj     传入操作对象
     * @return 影响行数
     */
    public int insert(String sqlName, Object obj) {
        return getSqlSession().insert(sqlName, obj);
    }

    /**
     * 简单插入实体对象
     *
     * @param entity 实体对象
     * @return 带id的插入对象
     */
    public <T> T insertEntity(T entity) {
        Insert insert = new Insert();
        insert.setTable(new Table(JsqlUtils.transDbSchemesType(entity.getClass().getSimpleName())));
        insert.setColumns(JsqlUtils.getAllColumnNamesFromEntity(entity.getClass()));
        insert.setItemsList(JsqlUtils.getAllColumnValueFromEntity(entity, insert.getColumns()));

        Map<String, Object> param = new HashMap<>(2);
        param.put("baseSql", insert.toString());
        param.put("entity", entity);
        this.insert("BaseDao.insertEntity", param);

        return entity;
    }


    /**
     * 简单批量插入实体对象
     *
     * @param entities 待插入的实体列表
     * @return 带id的插入对象列表
     */
    public <T> List<T> insertEntities(List<T> entities) {
        if (entities == null || entities.size() == 0) {
            return null;
        }

        Insert insert = new Insert();
        insert.setTable(new Table(JsqlUtils.transDbSchemesType(entities.get(0).getClass().getSimpleName())));
        insert.setColumns(JsqlUtils.getAllColumnNamesFromEntity(entities.get(0).getClass()));
        MultiExpressionList multiExpressionList = new MultiExpressionList();
        entities.stream().map(e -> JsqlUtils.getAllColumnValueFromEntity(e, insert.getColumns())).forEach(multiExpressionList::addExpressionList);
        insert.setItemsList(multiExpressionList);

        Map<String, Object> param = new MapperMethod.ParamMap<>();
        param.put("baseSql", insert.toString());
        param.put("list", entities);
        this.insert("BaseDao.insertEntityList", param);
        return entities;
    }
    // endregion

    // region delete 方法块

    /**
     * 根据sql方法名称和对象id
     *
     * @param sqlName xml中的sql id
     * @param obj     传入操作对象
     * @return 影响行数
     */
    public int delete(String sqlName, Object obj) {
        return getSqlSession().delete(sqlName, obj);
    }


    /**
     * 简单删除实体对象
     *
     * @param entity      实体对象
     * @param queryFields 作为查询条件的类属性名称，如<code>ID,codeDesc</code>
     * @return 影响行数
     */
    public int deleteEntity(Object entity, String queryFields) {
        List<Column> conditionCols = JsqlUtils.getAllColumnNamesFromEntityWithNames(entity.getClass(), Arrays.asList(queryFields.split(",")));

        if (Iterables.isEmpty(conditionCols)) {
            throw new BpMybatisException("can't update data without value of condition columns.");
        }

        Delete delete = new Delete();
        delete.setTables(Arrays.asList(new Table(JsqlUtils.transDbSchemesType(entity.getClass().getSimpleName()))));

        AndExpressionList andExpressionList = new AndExpressionList();
        conditionCols
                .forEach(e -> andExpressionList.append(JsqlUtils.equalTo(e, JsqlUtils.getColumnValueFromEntity(entity, e.getColumnName()))));

        delete.setWhere(andExpressionList.get());

        return this.delete("BaseDao.deleteEntity", new HashMap<String, String>() {{
            put("baseSql", delete.toString());
        }});
    }

    // endregion

    // region update 方法块

    /**
     * 根据sql方法名称和对象修改数据库
     *
     * @param sqlName xml中的sql id
     * @param obj     传入操作对象
     * @return 影响行数
     */
    public int update(String sqlName, Object obj) {
        return getSqlSession().update(sqlName, obj);
    }

    /**
     * 简单更新实体对象
     *
     * @param entity      实体对象
     * @param queryFields 作为查询条件的类属性名称，如<code>ID,codeDesc</code>
     * @return 影响行数
     */
    public int updateEntity(Object entity, String queryFields) {
        return updateEntity(entity, queryFields, ValueUpdatePolicy.NOT_EMPTY_NOT_NULL);
    }

    /**
     * 简单更新实体对象
     *
     * @param entity         实体对象
     * @param queryFields    作为查询条件的类属性名称，如<code>ID,codeDesc</code>
     * @param valueUpdatePolicy 更新策略
     * @return 影响行数
     */
    public int updateEntity(Object entity, String queryFields, ValueUpdatePolicy valueUpdatePolicy) {
        List<Column> valueColumns = JsqlUtils.getAllColumnNamesFromEntityExceptSome(entity.getClass(), Arrays.asList(queryFields.split(",")));
        List<Column> conditionCols = JsqlUtils.getAllColumnNamesFromEntityWithNames(entity.getClass(), Arrays.asList(queryFields.split(",")));
        return updateEntityWithNamedColumns(valueColumns, conditionCols,
                entity, valueUpdatePolicy);
    }


    /**
     * 简单更新实体对象
     *
     * @param entities    实体对象
     * @param queryFields 作为查询条件的类属性名称，如<code>ID,codeDesc</code>
     */
    public void updateEntities(List<?> entities, String queryFields) {
        entities.forEach(entity -> updateEntity(entity, queryFields, ValueUpdatePolicy.NOT_EMPTY_NOT_NULL));
    }


    /**
     * 简单更新实体对象
     *
     * @param entities       实体对象
     * @param queryFields    作为查询条件的类属性名称，如<code>ID,codeDesc</code>
     * @param valueUpdatePolicy 更新策略
     */
    public void updateEntities(List<?> entities, String queryFields, ValueUpdatePolicy valueUpdatePolicy) {
        entities.forEach(entity -> updateEntity(entity, queryFields, valueUpdatePolicy));
    }

    /**
     * 根据指定字段设置过滤条件，指定字段更新值
     *
     * @param valueCols      指定的更新值的字段
     * @param conditionCols  指定的条件列字段
     * @param entity         待更新实体
     * @param valueUpdatePolicy 更新策略
     * @return 影响行数
     */
    private int updateEntityWithNamedColumns(List<Column> valueCols, List<Column> conditionCols, Object entity, ValueUpdatePolicy valueUpdatePolicy) {
        if (Iterables.isEmpty(conditionCols)) {
            throw new BpMybatisException("can't update data without value of condition columns.");
        }

        Update update = new Update();
        update.setTables(Arrays.asList(new Table(JsqlUtils.transDbSchemesType(entity.getClass().getSimpleName()))));
        Object[] colsAndValuesForValues = JsqlUtils.getNamedColumnAndValueFromEntity(entity, valueCols, valueUpdatePolicy);
        update.setColumns((List<Column>) colsAndValuesForValues[0]);
        update.setExpressions((List<Expression>) colsAndValuesForValues[1]);

        AndExpressionList andExpressionList = new AndExpressionList();
        conditionCols
                .forEach(e -> andExpressionList.append(JsqlUtils.equalTo(e, JsqlUtils.getColumnValueFromEntity(entity, e.getColumnName()))));

        update.setWhere(andExpressionList.get());

        return this.update("BaseDao.updateEntities", new HashMap<String, String>() {{
            put("baseSql", update.toString());
        }});
    }
    // endregion

    // region select 方法块

    /**
     * 根据sql方法名称取回查询结果列表
     *
     * @param sqlName xml中的sql id
     * @return 返回实体列表
     */
    public List selectList(String sqlName) {
        return getSqlSession().selectList(sqlName);
    }

    /**
     * 根据sql方法名称和条件，取回查询结果列象
     *
     * @param sqlName xml中的sql id
     * @param entity  待操作实体
     * @return 返回实体列表
     */
    public List selectList(String sqlName, Object entity) {
        return getSqlSession().selectList(sqlName, entity);
    }


    /**
     * 简单加载实体对象
     *
     * @param entity 待操作实体
     * @return 返回实体列表
     */
    public List selectEntities(Object entity) {
        return selectEntities(entity, null);
    }

    /**
     * 简单加载实体对象
     *
     * @param entity  待操作实体
     * @param orderBy 排序字段
     * @return 返回实体列表
     */
    public List selectEntities(Object entity, String orderBy) {
        return selectEntities(entity, null, orderBy);
    }


    /**
     * 添加外部条件的简单加载实体对象
     *
     * @param entity           待操作实体
     * @param conditionWrapper 外部封装条件
     * @param orderBy          排序字段
     * @return 返回实体列表
     */
    public List selectEntities(Object entity, ConditionWrapper conditionWrapper, String orderBy) {
        PlainSelect plainSelect = new PlainSelect();
        plainSelect.setSelectItems(Arrays.asList(new AllColumns()));
        plainSelect.setFromItem(new Table(JsqlUtils.transDbSchemesType(entity.getClass().getSimpleName())));
        AndExpressionList andExpressionList = new AndExpressionList();

        // 添加外部条件
        // PS：有添加外部条件的字段，会被清除实体值，防止后续再加入条件
        andExpressionList.append(conditionWrapper != null ? conditionWrapper.get() : null);

        Object[] colsAndValues = JsqlUtils.getNamedColumnAndValueFromEntity(entity, null, ValueUpdatePolicy.NOT_EMPTY_NOT_NULL);

        for (int i = 0; i < ((List) colsAndValues[0]).size(); i++) {
            andExpressionList.append(JsqlUtils.equalTo((Column) ((List) colsAndValues[0]).get(i), (Expression) ((List) colsAndValues[1]).get(i)));
        }

        plainSelect.setWhere(andExpressionList.get());
        plainSelect.setOrderByElements(JsqlUtils.getOrderByElementFromString(orderBy));

        // 构建Sql并执行
        List lastResult = this.selectList("BaseDao.selectEntities", new HashMap<String, String>() {{
            put("baseSql", plainSelect.toString());
        }});

        // 生成对应对象列表，并且赋值
        return fillEntities(entity, lastResult);
    }

    /**
     * 利用结果集填充对应实体
     *
     * @param entity     参数实体，用于新建结果实体类
     * @param lastResult sql查询的结果集
     * @return 返回填充后的实体列表
     */
    private List fillEntities(Object entity, List lastResult) {
        List<?> result;
        if (lastResult instanceof Page) {
            result = ((Page) lastResult).getResult();
        } else {
            result = lastResult;
        }
        List<Object> tempList = new ArrayList<>();
        for (Object sr : result) {
            try {
                Object singleResult = (Object) Class.forName(entity.getClass().getName()).newInstance();

                for (String key : ((Map<String, Object>) sr).keySet()) {

                    try {
                        ClassUtil.setValueByField(singleResult, JsqlUtils.transSnakeToCamel(key), ((Map<String, Object>) sr).get(key));
                    } catch (Exception e) {
                        logger.error("processing entity setter value error, key : [" + key + "] entity : [" + entity.getClass().getName() + "] ", e);
                    }
                }

                tempList.add(singleResult);
            } catch (Exception e) {
                logger.error("processing entity setter value error, entity : [" + entity.getClass().getName() + "] ", e);
            }
        }
        if (lastResult instanceof Page) {
            ((Page) lastResult).getResult().clear();
            ((Page) lastResult).getResult().addAll(tempList);
        } else {
            lastResult = tempList;
        }
        return lastResult;
    }

    // endregion
}
