package com.example.demo.service;

import com.example.demo.entity.MyInfo;
import com.example.demo.mapper.HelloMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

@Service
@CacheConfig(cacheNames = "person") //将cacheNames抽取出来,下面不用key了
public class HelloService {
    @Autowired
    HelloMapper helloMapper;

    /*
     * 执行流程：先执行@Cacheable注解中的getCache(String name)方法,根据key的参数开始判断是否启用缓存
     * 根据name判断ConcurrentMap中是否有此缓存，如果没有缓存那么创建缓存并保存数据，
     * 另外service层的方法也会执行。如果有缓存不再创建缓存，另外service层的方法也不会执行。

     * */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public MyInfo getMyInfo() {
        MyInfo myInfo = null;
        try {
            myInfo = helloMapper.getMyInfo();
        } catch (Exception e) {
            //这是许多新手都会犯的一个错误，在业务层手工捕捉并处理了异常，
            // 你都把异常“吃”掉了，Spring自然不知道这里有错，更不会主动去回滚数据。例如：下面这段代码直接导致增加余额的事务回滚没有生效。
            //手动回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return myInfo;
    }

    /*1. @Cacheable的几个属性详解：
     *       cacheNames/value：指定缓存组件的名字 value也可以，只是个名字而已
     *       key：缓存数据使用的key,可以用它来指定。默认使用方法参数的值，一般不需要指定
     *       keyGenerator：作用和key一样，二选一
     *       cacheManager和cacheResolver作用相同：指定缓存管理器，二选一
     *       condition：指定符合条件才缓存，比如：condition="#id>3"
     *                   也就是说传入的参数id>3才缓存数据
     *      unless：否定缓存，当unless为true时不缓存，可以获取方法结果进行判断
     *      sync：是否使用异步模式*/
    //@Cacheable(cacheNames= "person")
    //@Cacheable(cacheNames= "person",key="#id",condition="#id>3")
    @Cacheable(cacheNames = "person", key = "#id")
    public String queryPersonById(Integer id) {
        return "";
    }

    /**
     * @CachePut:即调用方法，又更新缓存数据 修改了数据库中的数据，同时又更新了缓存
     * <p>
     * 运行时机：
     * 1.先调用目标方法
     * 2.将目标方法返回的结果缓存起来
     * <p>
     * 测试步骤：
     * 1.查询1号的个人信息
     * 2.以后查询还是之前的结果
     * 3.更新1号的个人信息
     * 4.查询一号员工返回的结果是什么？
     * 应该是更新后的员工
     * 但只更新了数据库，但没有更新缓存是什么原因？
     * 5.如何解决缓存和数据库同步更新？
     * 这样写：@CachePut(cacheNames = "person",key = "#person.id")
     * @CachePut(cacheNames = "person",key = "#result.id")
     * <p>
     * 5.@CacheEvict也是结合@Cacheable一起使用才有意义
     * @CacheEvict的作用：清除缓存中的指定数据或清除缓存中所有数据
     * @CacheEvict的属性
     */
    @CachePut(cacheNames = "person", key = "#result.id")
    public String updatePerson(MyInfo person) {
        return "person";

    }

    /**
     * @CacheEvict:清除缓存 1.key:指定要清除缓存中的某条数据
     * 2.allEntries=true:删除缓存中的所有数据
     * beforeInvocation=false:默认是在方法之后执行清除缓存
     * 3.beforeInvocation=true:现在是在方法执行之前执行清除缓存，
     * 作用是：只清除缓存、不删除数据库数据
     */
    //@CacheEvict(cacheNames = "person",key = "#id")
    @CacheEvict(cacheNames = "person", allEntries = true)
    public void deletePerson(Integer id) {
        System.out.println("删除" + id + "号个人信息");
        //删除数据库数据的同时删除缓存数据
        //personDao.delete(id);

        /**
         * beforeInvocation=true
         * 使用在方法之前执行的好处:
         * 1.如果方法出现异常，缓存依旧会被删除
         */
        //int a=1/0;
    }

    /**
     * @Caching是 @Cacheable、@CachePut、@CacheEvict注解的组合
     * 以下注解的含义：
     * 1.当使用指定名字查询数据库后，数据保存到缓存
     * 2.现在使用id、age就会直接查询缓存，而不是查询数据库
     */
    @Caching(
            cacheable = {@Cacheable(value = "person", key = "#name")},
            put = {@CachePut(value = "person", key = "#result.id"),
                    @CachePut(value = "person", key = "#result.age")
            }
    )
    public String queryPersonByName(String name) {
        System.out.println("查询的姓名：" + name);
        return "personDao.queryByName(name)";
    }


}
