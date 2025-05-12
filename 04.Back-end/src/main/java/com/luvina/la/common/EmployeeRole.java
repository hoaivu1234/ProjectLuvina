/**
 * Copyright(C) 2025  Luvina Software Company
 * EmployeeRole.java, 11/05/2025 hoaivd
 */

package com.luvina.la.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * EmployeeRole là enum định nghĩa vai trò của nhân viên trong hệ thống.
 * Bao gồm các vai trò như: USER và ADMIN, mỗi vai trò gắn với một giá trị số nguyên.
 *
 * @author hoaivd
 */
@AllArgsConstructor
@Getter
public enum EmployeeRole {

    /**
     * Vai trò người dùng thông thường.
     */
    USER(0),

    /**
     * Vai trò quản trị viên hệ thống.
     */
    ADMIN(1);

    /**
     * Giá trị số nguyên tương ứng với vai trò.
     */
    private final int value;
}
