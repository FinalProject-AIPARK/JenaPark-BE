package com.aipark.jena.enums;

import lombok.Getter;

@Getter
public enum Authority {
    ROLE_USER("ROLE_USER"),
    ROLE_ADMIN("ROLE_ADMIN");

    private final String key;

    Authority(String key) {
        this.key = key;
    }
}
