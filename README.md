# 节假日判定
   本项目支持中国节假日和工作日的判断。节假日判断是一个极具中国特色的问题，首先传统节日春节、端午和中秋无固定的公历日期，其次假期一般伴随着调休和补班，因而只能以国务院在每年末公布下一年放假通知为准。   

  为解决这个问题，常用解决方案一般为两种：  
a. 第一种是使用一些厂商提供的API接口，但一般不是免费，且稳定性和访问速度不能很好的保证。  
b. 第二种是自行开发代码在excel或数据库中进行维护，每年都要手动维护，容易忘记更新。  
    本项目结合以上两种方法的优点，使用[提莫的神秘商店](http://timor.tech/api/holiday/) 提供的在线数据，存储在JSON中维护，封装了常用查询场景方法，兼顾效率和高级自定义功能，可满足各类使用场景，如有未覆盖的需求也可在ISSUE中提出，会第一时间进行适配。  
## 特性
- 支持2013年1月1日至今所有节假日和工作日判断，并且会在每年的新通知发布后，第一时间更新
- 支持工作日节假日判断、按日期类型批量查询、下个工作日节假日查询
- 离线使用：只依赖数据，不依赖在线接口，查询效率高，千次查询5毫秒以下
- 异常提醒：查询未配置的日期时，会抛出异常，Fast-Fail 防止错误数据影响业务
- 支持自定义配置，满足公司年假等自定义需求

## 使用

### 1.pom 引用
```
<dependency>
  <groupId>xyz.xiamang</groupId>
  <artifactId>holiday</artifactId>
  <version>0.0.1-2021</version>
</dependency>
```

- 版本说明：末尾4位数字为支持的最新年份，如2021表示最新的为2021年，数据来源于国家法定节假日公告，一般为每年年底公布下一年的节假日，公布后版本会第一时间更新

### 2.java代码

```
//只需初始化一次 
HolidayService holidayService = new HolidayService();

//是否工作日 支持 date和string 格式：yyyyMMdd和yyyy-MM-dd
boolean f = holidayService.isHoliday("20210921");

//日期详情
DayDetail detail = holidayService.isHoliday("20210921");

//按类型批量查询
List<DayDetail> list = holidayService.queryDayByType(DayType.ALL_WORKDAY, "20210912", "20210922");     

//按类型查询下个日期
DayDetail next = holidayService.nextDayByType(DayType.ALL_WORKDAY, "2021-09-18");
```

### 自定义配置
- 在src/main/resources 目录下新建 holiday文件夹，并把本项目同目录的json文件一并复制过去，对json文件进行修改
- JSON数据来源：http://timor.tech/api/holiday/
- 配置文件说明：

```
{
  "code": 0,               // 0服务正常。-1服务出错
  "holiday": {
    "10-01": {
      "holiday": true,     // 该字段一定为true
      "name": "国庆节",     // 节假日的中文名。
      "wage": 3,           // 薪资倍数，3表示是3倍工资
      "date": "2018-10-01" // 节假日的日期
    },
    "10-02": {
      "holiday": true,     // 该字段一定为true
      "name": "国庆节",     // 节假日的中文名。
      "wage": 3,           // 薪资倍数，3表示是3倍工资
      "date": "2018-10-01" // 节假日的日期
    }
  }
}
```

## 计划
- 支持农历