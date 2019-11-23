package com.example.demo;

import com.example.demo.controller.HelloWorldController;
import com.example.demo.entity.MyInfo;
import com.example.demo.entity.Salary;
import com.example.demo.entity.config.Config;
import com.example.demo.mapper.SalaryMapper;
import com.example.demo.service.HelloService;
import com.example.demo.service.RedisService;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Optional;

@RunWith(SpringRunner.class)
@EnableConfigurationProperties(Config.class)
@SpringBootTest
class DemoApplicationTests {
    @Autowired
    HelloService helloService;
    @Autowired
    Config config;
    @Autowired
    SalaryMapper salaryMapper;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private RedisService redisService;
    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mvc;
    @Autowired
    private MongoTemplate mongoTemplate;

    //初始化执行
    //取代before
    @BeforeEach
    public void setUp() throws Exception {
//        mvc = MockMvcBuilders.standaloneSetup(new HelloWorldController()).build();
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        System.out.println("运行了吗");
    }

    @Test
    void contextLoads() {
        MyInfo myInfo = helloService.getMyInfo();
        System.out.println(myInfo.toString());
    }

    @Test
    void testCongig() {
        //jpa分页
        int page = 1, size = 1;
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Salary> salary = salaryMapper.findAll(pageable);
        System.out.println(salary.getSize());
    }

    @Test
    void testRedis() {
        redisService.set("cc", "22");
        System.out.println(redisService.get("cc"));
        //简单验证结果集是否正确
//        Assert.assertEquals(3, userMapper.getAll().size());
//
//        //验证结果集，提示
//        Assert.assertTrue("错误，正确的返回值为200", status == 200);
//        Assert.assertFalse("错误，正确的返回值为200", status != 200);
    }

    //验证controller是否正常响应并打印返回结果
    @Test
    public void getHello() throws Exception {
        String result = mvc.perform(MockMvcRequestBuilders.get("/login/hello").accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andReturn().toString();
        System.out.println("结果为：" + result);
        System.out.println("结果为：");
//        MvcResult authResult = null;
//        authResult = mockMvc.perform(get("/api/workitem/equipmenttypes")//使用get方式来调用接口。
//                .contentType(MediaType.APPLICATION_XHTML_XML)//请求参数的类型
//                .param("sessionid", "ZlbpLxXw")//请求的参数（可多个）
//        ).andExpect(status().isOk())
//                .andReturn();
//        //获取数据
//        JSONObject jsonObject =new  JSONObject(authResult.getResponse().getContentAsString());
//        JSONArray jsonArrayData = (JSONArray)jsonObject.get("data");
//
//        //获取第一个Array中的值,判断查询到的结果。
//        JSONObject jsonObject_data = null;
//        if(jsonArrayData.length()>0){
//            jsonObject_data = (JSONObject) jsonArrayData.get(0);
//        }
//        //加断言，判断属性值的问题。
//        Assert.assertNotNull(jsonObject.get("error_code"));
//        Assert.assertEquals(jsonObject.get("error_code"),0);
//        Assert.assertNotNull(jsonObject.get("error_msg"));
//        Assert.assertEquals(jsonObject.get("error_msg"),"操作成功");
//        Assert.assertNotNull(jsonObject.get("data"));
//        Assert.assertNotNull(jsonObject_data);
//        Assert.assertEquals(jsonObject_data.get("equipmentty"),1);
//        Assert.assertEquals(jsonObject_data.get("equipmenttypename"),"xxxxx");
    }

    @Test
    public void testMongo() {
        /*多数据源实现
        * mongodb.primary.uri=mongodb://192.168.0.75:20000
        mongodb.primary.database=primary
        mongodb.secondary.uri=mongodb://192.168.0.75:20000
        mongodb.secondary.database=secondary
        *2读取配置文件
        * @Data
        @ConfigurationProperties(prefix = "mongodb")
        public class MultipleMongoProperties {

            private MongoProperties primary = new MongoProperties();
            private MongoProperties secondary = new MongoProperties();
        }
        *3第一个封装
        * @Configuration
            @EnableMongoRepositories(basePackages = "com.neo.model.repository.primary",
                    mongoTemplateRef = PrimaryMongoConfig.MONGO_TEMPLATE)
            public class PrimaryMongoConfig {

                protected static final String MONGO_TEMPLATE = "primaryMongoTemplate";
            }
          第二个封装
          * @Configuration
            @EnableMongoRepositories(basePackages = "com.neo.model.repository.secondary",
                    mongoTemplateRef = SecondaryMongoConfig.MONGO_TEMPLATE)
            public class SecondaryMongoConfig {

                protected static final String MONGO_TEMPLATE = "secondaryMongoTemplate";
            }
        *4读取对应的配置信息并且构造对应的 MongoTemplate

@Configuration
public class MultipleMongoConfig {

    @Autowired
    private MultipleMongoProperties mongoProperties;

    @Primary
    @Bean(name = PrimaryMongoConfig.MONGO_TEMPLATE)
    public MongoTemplate primaryMongoTemplate() throws Exception {
        return new MongoTemplate(primaryFactory(this.mongoProperties.getPrimary()));
    }

    @Bean
    @Qualifier(SecondaryMongoConfig.MONGO_TEMPLATE)
    public MongoTemplate secondaryMongoTemplate() throws Exception {
        return new MongoTemplate(secondaryFactory(this.mongoProperties.getSecondary()));
    }

    @Bean
    @Primary
    public MongoDbFactory primaryFactory(MongoProperties mongo) throws Exception {
        return new SimpleMongoDbFactory(new MongoClient(mongo.getHost(), mongo.getPort()),
                mongo.getDatabase());
    }

    @Bean
    public MongoDbFactory secondaryFactory(MongoProperties mongo) throws Exception {
        return new SimpleMongoDbFactory(new MongoClient(mongo.getHost(), mongo.getPort()),
                mongo.getDatabase());
    }
}
* 54、创建两个库分别对应的对象和 Repository
对应可以共用

public class User implements Serializable {
        private static final long serialVersionUID = -3258839839160856613L;
        private String  id;
        private String userName;
        private String passWord;

        public User(String userName, String passWord) {
                this.userName = userName;
                this.passWord = passWord;
        }
}
对应的 Repository

public interface PrimaryRepository extends MongoRepository<PrimaryMongoObject, String> {
}
* 65、最后测试
@RunWith(SpringRunner.class)
@SpringBootTest
public class MuliDatabaseTest {

    @Autowired
    private PrimaryRepository primaryRepository;

    @Autowired
    private SecondaryRepository secondaryRepository;

    @Test
    public void TestSave() {

        System.out.println("************************************************************");
        System.out.println("测试开始");
        System.out.println("************************************************************");

        this.primaryRepository
                .save(new PrimaryMongoObject(null, "第一个库的对象"));

        this.secondaryRepository
                .save(new SecondaryMongoObject(null, "第二个库的对象"));

        List<PrimaryMongoObject> primaries = this.primaryRepository.findAll();
        for (PrimaryMongoObject primary : primaries) {
            System.out.println(primary.toString());
        }

        List<SecondaryMongoObject> secondaries = this.secondaryRepository.findAll();

        for (SecondaryMongoObject secondary : secondaries) {
            System.out.println(secondary.toString());
        }

        System.out.println("************************************************************");
        System.out.println("测试完成");
        System.out.println("************************************************************");
    }

}
        * */
        MyInfo myInfo = new MyInfo();
        myInfo.setName("ccc");
        myInfo.setAge(18);
        MongoCollection<Document> collection = mongoTemplate.createCollection("user");
//        collection.insertOne(myInfo);
//        Query query = new Query(Criteria.where("name").is("ccc"));
//        MyInfo user = mongoTemplate.findOne(query, MyInfo.class);
//        System.out.println(user.toString());

    }

    //    @Test
    //    public void testSetGet()  {
    //        MemcachedClient memcachedClient = memcachedRunner.getClient();
    //        memcachedClient.set("testkey",1000,"666666");
    //        System.out.println("***********  "+memcachedClient.get("testkey").toString());
    //    }



}
