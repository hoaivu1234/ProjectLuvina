package com.luvina.la.common;

/**
 * Copyright(C) 2025  Luvina Software Company
 * SortConstants.java, 5/11/2025 hoaivd
 */

public final class SortConstants {

    // Các hướng sắp xếp
    public static final String ASC = "ASC";
    public static final String DESC = "DESC";

    // Các trường sắp xếp (field names)
    public static final String EMPLOYEE_NAME_FIELD = "employeeName";
    public static final String CERTIFICATION_NAME_FIELD = "c.certificationName";
    public static final String END_DATE_FIELD = "ec.endDate";

    // Các giá trị ưu tiên sắp xếp (sortPriority)
    public static final String SORT_PRIORITY_EMPLOYEE_NAME = "ord_employee_name";
    public static final String SORT_PRIORITY_CERTIFICATION_NAME = "ord_certification_name";
    public static final String SORT_PRIORITY_END_DATE = "ord_end_date";

    // Ngăn chặn khởi tạo class
    private SortConstants() {
        throw new AssertionError("Cannot instantiate constants class");
    }
}
