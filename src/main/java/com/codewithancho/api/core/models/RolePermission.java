package com.codewithancho.api.core.models;

import com.codewithancho.api.core.annotations.Column;
import com.codewithancho.api.core.annotations.PrimaryKey;
import com.codewithancho.api.core.annotations.Table;

@Table(name = "user_permissions")
public class RolePermission {

    @PrimaryKey
    @Column(name = "id")
    private String id;

    @Column(name = "permission")
    private String permission;
}