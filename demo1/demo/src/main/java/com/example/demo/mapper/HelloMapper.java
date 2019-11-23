package com.example.demo.mapper;

import com.example.demo.entity.MyInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface HelloMapper {
    MyInfo getMyInfo();
}
