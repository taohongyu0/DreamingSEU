package com.example.testsystem.model.toback;

import com.example.testsystem.model.Role;

public class RoleIdAndToken {
    int roleId;
    String token;
    boolean banned;

    public RoleIdAndToken(){}

    public RoleIdAndToken(int roleId,String token,boolean banned){
        this.roleId = roleId;
        this.token = token;
        this.banned = banned;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isBanned() {
        return banned;
    }

    public void setBanned(boolean banned) {
        this.banned = banned;
    }
}
