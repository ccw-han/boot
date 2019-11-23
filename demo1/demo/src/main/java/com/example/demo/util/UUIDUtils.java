package com.example.demo.util;

import java.util.UUID;

public class UUIDUtils {
    public static String genUUID(){
        return UUID.randomUUID().toString().replace("-", "");
    }
}
