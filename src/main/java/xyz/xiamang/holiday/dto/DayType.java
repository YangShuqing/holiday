/*
 * Copyright (c) 2021 Inc. All Rights Reserved.
 */

package xyz.xiamang.holiday.dto;

/**
 * 日期类型 节假日类型，分别表示 0工作日、1周末、2节日、3调休（上班）
 *
 * @author ysq
 */
public enum DayType {

    /**
     * 正常工作日
     */
    WORKDAY(0, "工作日"),
    /**
     * 正常 周六和周日
     */
    WEEKEND(1, "周末"),
    /**
     * 特指 春节、端午、中秋、国庆 四个节日的假期
     * 当假期与周六日重叠时，类型为节日 如春节假期中的周六和周日
     */
    HOLIDAY(2, "节日"),
    /**
     * 调休补班，即周六和周日，需要上班
     */
    TRADEDAY(3, "调休补班"),

    /**
     * 仅用于查询, 所有日期
     */
    ALL_DAY(10, "所有日期"),
    /**
     * 仅用于查询, 所有上班 即0和3
     */
    ALL_WORKDAY(11, "所有上班"),
    /**
     * 仅用于查询，所有非上班 即1和2
     */
    ALL_NO_WORKDAY(12, "所有非上班");

    private Integer code;
    private String name;

    /**
     * @param i
     * @param string
     */
    DayType(Integer code, String name) {
        this.code = code;
        this.name = name;
    }
}
