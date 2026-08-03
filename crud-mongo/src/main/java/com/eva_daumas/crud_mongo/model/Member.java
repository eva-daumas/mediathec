package com.eva_daumas.crud_mongo.model;


import java.time.LocalDateTime;


public class Member {


    private Long id;


    private String username;


    private String email;

    private String password;

    private String role = "USER";


    private LocalDateTime createdAt;


    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

}
