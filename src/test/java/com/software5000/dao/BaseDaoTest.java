package com.software5000.dao;


import com.software5000.base.BaseDao;
import com.software5000.base.jsql.ConditionWrapper;
import com.software5000.util.JsqlUtils;
import net.sf.jsqlparser.expression.Expression;
import org.w3c.dom.Entity;

public class BaseDaoTest {

    private BaseDao baseDao;

    public void myTest(){
        Entity entity = null;
        ConditionWrapper conditionWrapper = new ConditionWrapper(entity);
        baseDao.selectEntities(entity,
                new ConditionWrapper(entity)
                .eq("entityName")
                .gt("entityNum", JsqlUtils.convertValueType(3))
                .le("entityIndex",JsqlUtils.convertValueType(9L))
                .in("entityType", JsqlUtils.convertValueTypeList(new Object[]{1,2,3})),
                "id desc"
                );


    }

}
