/*
 * Copyright (c) 2021 xiamang.xyz, Inc. All Rights Reserved.
 */

package xyz.xiamang.holiday.service;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xyz.xiamang.holiday.config.JsonConfigLoader;
import xyz.xiamang.holiday.dto.DayDetail;
import xyz.xiamang.holiday.dto.DayType;
import xyz.xiamang.holiday.exception.UnSupportException;

/**
 * 节假日判断API
 * @author ysq
 */
public class HolidayService {

    /**
     * 所有日期详情 key:yyyyMMdd value:{@link DayDetail}
     */
    private static final Map<String, DayDetail> DAY_DETAIL_MAP = new LinkedHashMap<>();
    /**
     * 节假日配置项缓存 key:yyyyMMdd value:{@link DayDetail}
     */
    private static final Map<String, DayDetail> HOLIDAY_CONFIG = new LinkedHashMap<>();

    private static final String[] WEEK_NAMES = { "周日", "周一", "周二", "周三", "周四", "周五", "周六" };

    private static final Integer[] WEEK_INDEX = { 7, 1, 2, 3, 4, 5, 6 };

    private static String MAX_YEAR = "2021";
    private static String MAX_DAY = "2021-12-31";

    private static final Logger logger = LoggerFactory.getLogger(HolidayService.class);

    public HolidayService() {
        super();
        init();
    }

    private void init() {
        loadFromJsonConfig();
    }

    private void loadFromJsonConfig() {
        long start = System.currentTimeMillis();
        java.net.URL baseDir = this.getClass().getResource("/holiday");
        File dir = new File(baseDir.getFile());
        File[] fs = dir.listFiles();
        Arrays.sort(fs, new Comparator<File>() {
            @Override
            public int compare(File f1, File f2) {
                return f1.getName().compareTo(f2.getName());
            }
        });
        List<String> fileNames = new ArrayList<>();
        for (File f : fs) {
            logger.info("{}", f.getName());
            if (!f.getName().contains("json")) {
                logger.warn("{} 不包含节假日配置忽略", f.getName());
                continue;
            }
            fileNames.add(f.getName());
            MAX_YEAR = f.getName().substring(0, 4);
            @SuppressWarnings("rawtypes")
            Map map = JsonConfigLoader.load(f, Map.class);
            logger.debug("{}", map);
            parseConfig(map);
        }
        initAllDay();
        long end = System.currentTimeMillis();
        logger.info("#### 节假日配置初始化完成耗时 {}ms 共{}个日期 配置文件{}", end - start, DAY_DETAIL_MAP.size(), fileNames);
        //        logger.info("config cost {}ms {} {}", end - start, DAY_DETAIL_MAP.size(),
        //                DAY_DETAIL_MAP.get("20210911"));
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void parseConfig(Map map) {
        Map<String, Map> m2 = (Map) map.get("holiday");
        m2.values().forEach(m -> {
            DayDetail day = new DayDetail();
            String d = (String) m.get("date");
            day.setDay(d);
            day.setDayAlias(d.replace("-", ""));
            day.setName((String) m.get("name"));
            day.setWage((Integer) m.get("wage"));
            Boolean f = (Boolean) m.get("holiday");
            day.setHoliday(f);
            day.setDayType(f ? DayType.HOLIDAY : DayType.TRADEDAY);
            day.setHolidayName((String) m.get("target"));
            HOLIDAY_CONFIG.put(day.getDayAlias(), day);
        });
    }

    private void initAllDay() {
        Calendar calendar = Calendar.getInstance();
        int year = Integer.valueOf(MAX_YEAR); //calendar.get(Calendar.YEAR) + 1;//明年
        calendar.set(2013, 0, 1);//from 2013年1月1日
        int y = 0;
        while (y <= year) {
            queryDayDetailAndPutCache(calendar);
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            y = calendar.get(Calendar.YEAR);
        }
    }

    /**
     * 是否是节假日，默认为当天
     * @return boolean
     */
    public boolean isHoliday() {
        return isHoliday(new Date());
    }

    /**
     * 是否是节假日 注：放假日期都算，周六日及春节等法定假日、但由于节假日的调休补班不算
     * @param day 日期
     * @return 是否节假日
     */
    public boolean isHoliday(Date day) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String key = sdf.format(day);
        return isHoliday(key);
    }

    /**
     * 是否是节假日, 注 放假日期都算，周六日及春节等法定假日、但由于节假日的调休补班不算
     * @param day 日期 格式 yyyy-mm-dd或yyyymmdd 示例2021-09-18或20210918
     * @return 是否节假日
     */
    public boolean isHoliday(String day) {
        DayDetail detail = queryDayDetail(day);
        if (detail == null) {
            String msg = "仅支持 2013-01-01至" + MAX_DAY + " 期间节假日查询，请更新至最新版本，或在src/main/resources/holiday/文件夹中进行配置";
            throw new UnSupportException(msg);
        }
        return detail.isHoliday();
    }

    /**
     * 查询日期详情 是否节假日、补班、周几等
     * @param day 日期 格式 yyyy-mm-dd或yyyymmdd 示例2021-09-18或20210918
     * @return 日期详情
     */
    public DayDetail queryDayDetail(String day) {
        String key = dayKey(day);
        return DAY_DETAIL_MAP.get(key);
    }

    /**
     * 根据日期类型批量查询
     * @param type 日期类型
     * @param startDay 开始日期（包含）格式yyyyMMdd或yyyy-MM-dd
     * @param endDay 结束日期（包含）格式yyyyMMdd或yyyy-MM-dd
     * @return 日期详情
     */
    public List<DayDetail> queryDayByType(DayType type, String startDay, String endDay) {
        String startKey = dayKey(startDay);
        String endKey = dayKey(endDay);
        List<DayDetail> result = DAY_DETAIL_MAP.entrySet()
                .stream().filter(e -> {
                    Boolean f = false;
                    String key = e.getKey();
                    DayDetail value = e.getValue();
                    if (type.equals(DayType.ALL_WORKDAY)) {
                        f = DayType.WORKDAY.equals(value.getDayType())
                                || DayType.TRADEDAY.equals(value.getDayType());
                    } else if (type.equals(DayType.ALL_NO_WORKDAY)) {
                        f = DayType.HOLIDAY.equals(value.getDayType()) ||
                                DayType.WEEKEND.equals(value.getDayType());
                    } else {
                        f = type.equals(value.getDayType());
                    }
                    return f && key.compareTo(startKey) >= 0 && key.compareTo(endKey) <= 0;
                }).map(Entry::getValue)
                .collect(Collectors.toList());
        return result;
    }

    /**
     * 按类型查询最近一个日期
     * @param type 日期类型
     * @param startDay 开始日期（包含） 格式yyyyMMdd 或 yyyy-MM-dd
     * @param endDay 结束日期（包含） 格式yyyyMMdd 或 yyyy-MM-dd
     * @return 日期详情
     */
    public DayDetail nextDayByType(DayType type, String startDay) {
        String startKey = dayKey(startDay);
        for (Entry<String, DayDetail> entry : DAY_DETAIL_MAP.entrySet()) {
            String key = entry.getKey();
            if (key.compareTo(startKey) > 0) {
                Boolean f = false;
                DayDetail value = entry.getValue();
                if (type.equals(DayType.ALL_WORKDAY)) {
                    f = DayType.WORKDAY.equals(value.getDayType())
                            || DayType.TRADEDAY.equals(value.getDayType());
                } else if (type.equals(DayType.ALL_NO_WORKDAY)) {
                    f = DayType.HOLIDAY.equals(value.getDayType()) ||
                            DayType.WEEKEND.equals(value.getDayType());
                } else {
                    f = type.equals(value.getDayType());
                }
                if (f) {
                    return value;
                }
            }
        }
        return null;
    }

    public DayDetail queryDayDetailAndPutCache(Calendar calendar) {
        String key = dayKey(calendar);
        int w = calendar.get(Calendar.DAY_OF_WEEK);
        DayDetail detail = HOLIDAY_CONFIG.get(key);
        if (detail == null) {
            detail = new DayDetail();
            detail.setWage(1);
            detail.setDay(key.substring(0, 4) + "-" + key.substring(4, 6) + "-" + key.substring(6));
            detail.setDayAlias(key);
            //英文中一周从周日开始 周日=1 周六=7
            if (1 == w || 7 == w) {
                detail.setDayType(DayType.WEEKEND);
                detail.setHoliday(true);
            } else {
                detail.setDayType(DayType.WORKDAY);
                detail.setHoliday(false);
            }
            detail.setName(key);
            detail.setName(WEEK_NAMES[w - 1]);
        }
        detail.setDayOfWeek(WEEK_INDEX[w - 1]);
        DAY_DETAIL_MAP.put(key, detail);
        MAX_DAY = detail.getDayAlias();
        return detail;
    }

    private String dayKey(String day) {
        return day.replace("-", "");
    }

    private String dayKey(Calendar calendar) {
        int y = calendar.get(Calendar.YEAR);
        int m = calendar.get(Calendar.MONTH) + 1;
        int d = calendar.get(Calendar.DAY_OF_MONTH);
        return y + (m < 10 ? "0" + m : "" + m)
                + (d < 10 ? "0" + d : "" + d);
    }

    public static void main(String[] args) {
        HolidayService holidayService = new HolidayService();
        holidayService.init();
    }

}
