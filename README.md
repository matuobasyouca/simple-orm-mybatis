这个项目期望是在Mybatis之上做一层简化封装，能够通过后台直接生成sql语句执行针对单表的增删改查语句并且执行。

[查看详细说明文档](https://github.com/matuobasyouca/simple-orm-mybatis/wiki)

# 使用的基础工作

1. 首先引入依赖
```
<dependency>
    <groupId>com.software5000</groupId>
    <artifactId>simple-orm-mybatis</artifactId>
    <version>1.0.0</version>
</dependency>
```

2. 接着需要将 ``` BaseDaoMapper.xml ``` 文件放在你的 mapper 的扫描文件夹内。

3. 最后需要在你的代码中添加一个 **BaseDao** 实现类，用于提供数据库操作服务（注意：需要在spring的扫描包内，因为需要注入某些属性），整个复制即可，类名可以改为你自己需要的名字
```
import com.software5000.base.BaseDao;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 整个类以 <code>org.mybatis:mybatis-spring:2.0.0</code> 的 <code>org.mybatis.spring.supportSqlSessionDaoSupport</code>
 * 为参考编写而成
 */
@Component
public class MyBaseDao extends BaseDao {
    private SqlSessionTemplate sqlSessionTemplate;

    @Resource
    public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
        if (this.sqlSessionTemplate == null || sqlSessionFactory != this.sqlSessionTemplate.getSqlSessionFactory()) {
            this.sqlSessionTemplate = this.createSqlSessionTemplate(sqlSessionFactory);
        }
    }

    protected SqlSessionTemplate createSqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    @Override
    public SqlSession getSqlSession() {
        return this.sqlSessionTemplate;
    }
}

```

# 实际使用
这里给了一个单元测试的例子，实际一般是在service层直接使用，无需添加任何代码。
示例中的 ``` DailyRecord ```是一个普通实体类，没有任何继承。
```
// 获取启动类，加载配置，确定装载 Spring 程序的装载方法，它回去寻找 主配置启动类（被 @SpringBootApplication 注解的）
@SpringBootTest
// 让 JUnit 运行 Spring 的测试环境， 获得 Spring 环境的上下文的支持
@RunWith(SpringRunner.class)
public class BaseDaoTest {

    @Autowired
    private MyBaseDao myBaseDao;

    @Test
    public void testBaseDao(){
        DailyRecord dailyRecord = new DailyRecord();
        List<DailyRecord> dailyRecords  = myBaseDao.selectEntity(dailyRecord);
        System.out.println(dailyRecords.size());
    }
}


```