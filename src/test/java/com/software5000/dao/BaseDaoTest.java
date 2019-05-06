package com.software5000.dao;


import com.google.common.collect.Lists;
import com.software5000.base.BaseDao;
import com.software5000.biz.entity.Department;
import com.software5000.biz.entity.Employee;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-core.xml", "classpath:spring-db.xml"})
//@Transactional
public class BaseDaoTest {

    private Logger logger = LoggerFactory.getLogger(BaseDaoTest.class);

    @Autowired
    BaseDao baseDao;


    @Test
    public void testSelectRecChannel() {

        try {
            List<Department> departments = Lists.newArrayList();

            for (int i = 0; i < 3; i++) {
                Department department = new Department();
                department.setDeptNo("DEV_" + i);
                department.setDeptName("研发部门" + i);
                departments.add(department);
            }

            List<Employee> employees = Lists.newArrayList();

            for (int i = 0; i < 3; i++) {
                Employee employee = new Employee();
                employee.setId(i+7);
                employee.setDeptId(i+1);
                employee.setEmpName("员工C" + i);
                employee.setEmpNo("DEV_" + i + "_C" + i);
                employees.add(employee);
            }

//            baseDao.updateEntity(employees);
//            baseDao.insertEntity(departments);
//            baseDao.insertEntity(employees);
        } catch (Exception e) {
            logger.error("query error!", e);
        }
    }
}
