package com.example.usercenter.model.enums;

/**
 * 队伍状态枚举
 */
public enum TeamStatus {

    PUBLIC(0, "公开"),
    PRIVATE(1, "私密"),
    SECRETE(2, "加密");

    private int value;
    private String name;

    public static TeamStatus getStatusByValue(Integer value) {
        if (value == null) {
            return null;
        }
        TeamStatus[] values = TeamStatus.values();
        for (TeamStatus teamStatus : values) {
            if (teamStatus.getValue() == value) {
                return teamStatus;
            }
        }
        return null;
    }

    TeamStatus(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
