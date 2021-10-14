package com.training.spring_travelagency.entity.enums;

import org.springframework.security.core.GrantedAuthority;


public enum Roles implements GrantedAuthority {
    USER, MANAGER, ADMIN;

    @Override
    public String getAuthority() {
        return name();
    }
}
