package com.example.demo.entity;

import com.alibaba.fastjson.annotation.JSONField;
import java.util.Date;

public class MyInfo {
    private String name;
    private Integer age;
    @JSONField(format="yyyy-MM-dd HH:mm")
    private Date birthDay;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Date getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(Date birthDay) {
        this.birthDay = birthDay;
    }

    @Override
    public String toString() {
        return "MyInfo{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", birthDay=" + birthDay +
                '}';
    }
}
