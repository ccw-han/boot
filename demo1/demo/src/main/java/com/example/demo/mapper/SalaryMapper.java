package com.example.demo.mapper;

import com.example.demo.entity.MyInfo;
import com.example.demo.entity.Salary;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.jpa.repository.JpaRepository;

@Mapper
public interface SalaryMapper extends JpaRepository<Salary, Long> {
    //https://blog.csdn.net/weixin_44811417/article/details/90713512 JPA文档
}
