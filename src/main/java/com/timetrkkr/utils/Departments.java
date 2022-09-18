package com.timetrkkr.utils;

import java.util.Objects;

public enum Departments implements DepartmentInterface{

    DEP_DEVELOPMENT("Development", 1),
    DEP_MARKETING("Marketing", 2),
    DEP_HR("Human Resource", 3),
    DEP_FINANCE("Finance", 4);

    public final String departmentName;
    public final int level;
    Departments(String departmentName, int level) {
        this.departmentName = departmentName;
        this.level = level;
    }

    @Override
    public int getDepartment(String departmentName) {
        return ( Objects.equals(departmentName, this.departmentName) ) ? this.level : 0;
    }
}
