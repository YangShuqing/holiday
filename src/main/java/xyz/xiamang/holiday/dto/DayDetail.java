/*
 * Copyright (c) 2021 , Inc. All Rights Reserved.
 */

package xyz.xiamang.holiday.dto;

/**
 *
 * @author ysq
 */
public class DayDetail {

    /**
     * 格式：yyyy-MM-dd 如2021-09-21
     */
    private String day;
    /**
     * 格式：yyyyMMdd 如20210921
     */
    private String dayAlias;
    /**
     * 是否为节假日
     */
    private boolean holiday;
    /**
     * 日期类型 节假日类型，分别表示 0工作日、1周末、2节日、3调休
     */
    private DayType dayType;
    /**
     * 工资倍数
     */
    private Integer wage;

    /**
     * 周几/节日名称/调休详情
     */
    private String name;
    /**
     * 节日名称
     */
    private String holidayName;

    /**
     * 一周中的第几天,周一 1 周日7
     */
    private Integer dayOfWeek;

    /**
     * 农历相关TODO:
     */
    private Object lunary;

    /**
     * @return the day
     */
    public String getDay() {
        return day;
    }

    /**
     * @param day the day to set
     */
    public void setDay(String day) {
        this.day = day;
    }

    /**
     * @return the dayAlias
     */
    public String getDayAlias() {
        return dayAlias;
    }

    /**
     * @param dayAlias the dayAlias to set
     */
    public void setDayAlias(String dayAlias) {
        this.dayAlias = dayAlias;
    }

    /**
     * @return the holiday
     */
    public boolean isHoliday() {
        return holiday;
    }

    /**
     * @param holiday the holiday to set
     */
    public void setHoliday(boolean holiday) {
        this.holiday = holiday;
    }

    /**
     * @return the dayType
     */
    public DayType getDayType() {
        return dayType;
    }

    /**
     * @param dayType the dayType to set
     */
    public void setDayType(DayType dayType) {
        this.dayType = dayType;
    }

    /**
     * @return the wage
     */
    public Integer getWage() {
        return wage;
    }

    /**
     * @param wage the wage to set
     */
    public void setWage(Integer wage) {
        this.wage = wage;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the holidayName
     */
    public String getHolidayName() {
        return holidayName;
    }

    /**
     * @param holidayName the holidayName to set
     */
    public void setHolidayName(String holidayName) {
        this.holidayName = holidayName;
    }

    /**
     * @return the dayOfWeek
     */
    public Integer getDayOfWeek() {
        return dayOfWeek;
    }

    /**
     * @param dayOfWeek the dayOfWeek to set
     */
    public void setDayOfWeek(Integer dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    @Override
    public String toString() {
        return "DayDetail [day=" + day + ", dayAlias=" + dayAlias + ", holiday=" + holiday + ", dayType=" + dayType + ", wage=" + wage + ", name="
                + name + ", holidayName=" + holidayName + ", dayOfWeek=" + dayOfWeek + ", lunary=" + lunary + "]";
    }

}
