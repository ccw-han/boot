package com.example.demo.entity;

import javax.persistence.*;

@Entity
//@Document mongodb使用注解
@Table(name = "salary")
public class Salary {
    @Id
    // 字段自动生成
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private int quantity;
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Salary{" +
                "id=" + id +
                ", quantity=" + quantity +
                ", name='" + name + '\'' +
                '}';
    }
}
