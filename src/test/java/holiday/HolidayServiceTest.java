/*
 * Copyright (c) 2020 xiamang , Inc. All Rights Reserved.
 */

package holiday;

import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.List;

import org.junit.Test;

import xyz.xiamang.holiday.dto.DayDetail;
import xyz.xiamang.holiday.dto.DayType;
import xyz.xiamang.holiday.exception.UnSupportException;
import xyz.xiamang.holiday.service.HolidayService;

/**
 *
 * @author ysq
 */
public class HolidayServiceTest {
    HolidayService holidayService = new HolidayService();

    @Test
    public void testHoliday() {
        //周六 中秋节补班 false
        assertEquals(holidayService.isHoliday("20210918"), false);
        //周五 false
        assertEquals(holidayService.isHoliday("20210917"), false);
        //中秋节 true
        assertEquals(holidayService.isHoliday("20210919"), true);
        //周六 true
        assertEquals(holidayService.isHoliday("20210912"), true);
        //周六 true
        assertEquals(holidayService.isHoliday("2021-09-12"), true);
        assertEquals(holidayService.isHoliday(new Date(2021 - 1900, 8, 12)), true);//20210912
    }

    @Test(expected = UnSupportException.class)
    public void testException() {
        holidayService.isHoliday("1992-01-01");
    }

    @Test
    public void testQueryDetail() {
        DayDetail detail = holidayService.queryDayDetail("20210918");
        assertEquals(false, detail.isHoliday());
        assertEquals(Integer.valueOf(1), detail.getWage());
        assertEquals("中秋节前调休", detail.getName());
    }

    @Test
    public void testQueryByType() {
        List<DayDetail> list = holidayService.queryDayByType(DayType.ALL_WORKDAY, "20210912", "20210922");
        for (DayDetail dayDetail : list) {
            System.out.println(dayDetail);
        }
        System.out.println(list.size());
        assertEquals(7, list.size());
        System.out.println("########");
        List<DayDetail> list2 = holidayService.queryDayByType(DayType.ALL_NO_WORKDAY, "20210912", "20210922");
        for (DayDetail dayDetail : list2) {
            System.out.println(dayDetail);
        }
        System.out.println(list2.size());
        assertEquals(4, list2.size());
    }

    @Test
    public void testNext() {
        DayDetail next = holidayService.nextDayByType(DayType.ALL_WORKDAY, "2021-09-18");
        System.out.println(next);
        assertEquals("20210922", next.getDayAlias());
    }

    @Test
    public void testBatch1000() {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            holidayService.isHoliday("2021-12-31");
        }
        long end = System.currentTimeMillis();
        System.out.println("千次查询" + (end - start) + "ms");
        //assertTrue((end - start) <= 10);
    }
}
