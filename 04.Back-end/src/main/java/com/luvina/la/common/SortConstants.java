/**
 * Copyright(C) 2025  Luvina Software Company
 * SortConstants.java, 5/11/2025 hoaivd
 */

package com.luvina.la.common;

/**
 * Lưu trữ các hằng số liên quan đến việc sắp xếp dữ liệu, bao gồm các hướng sắp xếp (ASC, DESC),
 * các trường sắp xếp (employeeName, certificationName, endDate), và các giá trị ưu tiên sắp xếp.
 * Các hằng số này được sử dụng trong các thao tác sắp xếp dữ liệu trong hệ thống.
 *
 * @author hoaivd
 */
public final class SortConstants {

    /**
     * Hướng sắp xếp tăng dần (Ascending).
     */
    public static final String ASC = "ASC";

    /**
     * Hướng sắp xếp giảm dần (Descending).
     */
    public static final String DESC = "DESC";

    /**
     * Trường sắp xếp theo tên nhân viên.
     */
    public static final String EMPLOYEE_NAME_FIELD = "e.employeeName";

    /**
     * Trường sắp xếp theo tên chứng chỉ.
     */
    public static final String CERTIFICATION_NAME_FIELD = "c.certificationName";

    /**
     * Trường sắp xếp theo ngày kết thúc.
     */
    public static final String END_DATE_FIELD = "ec.endDate";

    /**
     * Giá trị ưu tiên sắp xếp theo tên nhân viên.
     */
    public static final String SORT_PRIORITY_EMPLOYEE_NAME = "ord_employee_name";

    /**
     * Giá trị ưu tiên sắp xếp theo tên chứng chỉ.
     */
    public static final String SORT_PRIORITY_CERTIFICATION_NAME = "ord_certification_name";

    /**
     * Giá trị ưu tiên sắp xếp theo ngày kết thúc.
     */
    public static final String SORT_PRIORITY_END_DATE = "ord_end_date";

    /**
     * Constructor private để ngăn không cho khởi tạo class chứa hằng số.
     *
     * @throws AssertionError luôn luôn ném lỗi nếu bị gọi.
     */
    private SortConstants() {
        throw new AssertionError("Cannot instantiate constants class");
    }
}
