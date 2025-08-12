package com.sky.service.impl;

import com.sky.dto.GoodsSalesDTO;
import com.sky.entity.OrderDetail;
import com.sky.entity.Orders;
import com.sky.mapper.OrderDetailMapper;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.ReportService;
import com.sky.service.WorkspaceService;
import com.sky.vo.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private OrderDetailMapper orderDetailMapper;
    @Autowired
    private WorkspaceService workspaceService;

    // 营业额统计
    @Override
    public TurnoverReportVO turnoverStatistics(LocalDate begin, LocalDate end) {

        List<LocalDate>dateList=new ArrayList<>();

        // 日期列表
        dateList.add(begin);
        while (!begin.equals(end)){
            begin=begin.plusDays(1);
            dateList.add(begin);
        }

        List<Double>turnoverList=new ArrayList<>();
        for (LocalDate date : dateList) {
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);
            Map<String,Object>map=new HashMap<>();
            map.put("begin",beginTime);
            map.put("end",endTime);
            map.put("status",Orders.COMPLETED);
            Double turnover = orderMapper.sumByMap(map);
            turnoverList.add(turnover == null ? 0.0 : turnover);

        }

        return TurnoverReportVO.builder()
                .dateList(StringUtils.join(dateList,","))
                .turnoverList(StringUtils.join(turnoverList,","))
                .build();
    }

    // 用户统计
    @Override
    public UserReportVO userStatistics(LocalDate begin, LocalDate end) {
        List<LocalDate>dateList=new ArrayList<>();
        // 日期列表
        dateList.add(begin);
        while (!begin.equals(end)){
            begin=begin.plusDays(1);
            dateList.add(begin);
        }

        List<Integer>userList=new ArrayList<>();
        List<Integer>newUserList=new ArrayList<>();
        for (LocalDate date : dateList) {
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);
            Map<String,Object>map=new HashMap<>();
            map.put("begin",null);
            map.put("end",null);
            Integer userNum = userMapper.countByMap(map);
            userList.add(userNum==null?0:userNum);

            map.put("begin",beginTime);
            map.put("end",endTime);
            Integer newUserNum =userMapper.countByMap(map);
            newUserList.add(newUserNum==null?0:newUserNum);


        }

        return UserReportVO.builder()
                .dateList(StringUtils.join(dateList,","))
                .totalUserList(StringUtils.join(userList,","))
                .newUserList(StringUtils.join(newUserList,","))
                .build();



    }

    // 订单统计
    @Override
    public OrderReportVO ordersStatistics(LocalDate begin, LocalDate end) {
        List<LocalDate>dateList=new ArrayList<>();
        // 日期列表
        dateList.add(begin);
        while (!begin.equals(end)){
            begin=begin.plusDays(1);
            dateList.add(begin);
        }

        //订单总数
        Integer totalOrderCount=0;
        //有效订单数
        Integer totalValidOrderCount=0;
        List<Integer>orderCountList=new ArrayList<>();
        List<Integer>validOrderCountList=new ArrayList<>();
        for (LocalDate date : dateList) {
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);
            Map<String,Object>map=new HashMap<>();
            map.put("begin",beginTime);
            map.put("status",null);
            map.put("end",endTime);
            Integer orderCount = orderMapper. countByMap(map);
            orderCountList.add(orderCount==null?0:orderCount);
            map.put("status",Orders.COMPLETED);
            Integer validOrderCount = orderMapper.countByMap(map);
            validOrderCountList.add(validOrderCount==null?0:validOrderCount);

            totalOrderCount+=orderCount;
            totalValidOrderCount+=validOrderCount;
        }

        double orderCompletionRate= (double) totalValidOrderCount /totalOrderCount ;

        return OrderReportVO.builder()
                .dateList(StringUtils.join(dateList,","))
                .orderCompletionRate(orderCompletionRate)
                .totalOrderCount(totalOrderCount)
                .orderCountList(StringUtils.join(orderCountList,","))
                .validOrderCount(totalValidOrderCount)
                .validOrderCountList(StringUtils.join(validOrderCountList,","))
                .build();
    }

    //销量排名前十
    @Override
    public SalesTop10ReportVO getSalesTop10(LocalDate begin, LocalDate end) {

        List<LocalDate>dateList=new ArrayList<>();
        // 日期列表
        dateList.add(begin);
        while (!begin.equals(end)){
            begin=begin.plusDays(1);
            dateList.add(begin);
        }

       List<GoodsSalesDTO>list= orderDetailMapper.getSalesTop10(begin,end,Orders.COMPLETED);

        List< String>nameList=new ArrayList<>();
        List<Integer>numberList=new ArrayList<>();
        for (GoodsSalesDTO goodsSalesDTO : list) {
           nameList.add(goodsSalesDTO.getName());
           numberList.add(goodsSalesDTO.getNumber());
        }

        return SalesTop10ReportVO.builder()
                .nameList(StringUtils.join(nameList,","))
                .numberList(StringUtils.join(numberList,","))
                .build();
    }

   @Override
   public void exportBusinessData(HttpServletResponse response) {
       //1. 查询数据库，获取营业数据---查询最近30天的运营数据
       LocalDate dateBegin = LocalDate.now().minusDays(30);
       LocalDate dateEnd = LocalDate.now().minusDays(1);

       //查询概览数据
       BusinessDataVO businessDataVO = workspaceService.getBusinessData(LocalDateTime.of(dateBegin, LocalTime.MIN), LocalDateTime.of(dateEnd, LocalTime.MAX));

       //2. 通过POI将数据写入到Excel文件中
       InputStream in = this.getClass().getClassLoader().getResourceAsStream("template/运营数据报表模板.xlsx");

       try {
           //基于模板文件创建一个新的Excel文件
           XSSFWorkbook excel = new XSSFWorkbook(in);

           //获取表格文件的Sheet页
           XSSFSheet sheet = excel.getSheet("Sheet1");

           //填充数据--时间
           sheet.getRow(1).getCell(1).setCellValue("时间：" + dateBegin + "至" + dateEnd);

           //获得第4行
           XSSFRow row = sheet.getRow(3);
           row.getCell(2).setCellValue(businessDataVO.getTurnover());
           row.getCell(4).setCellValue(businessDataVO.getOrderCompletionRate());
           row.getCell(6).setCellValue(businessDataVO.getNewUsers());

           //获得第5行
           row = sheet.getRow(4);
           row.getCell(2).setCellValue(businessDataVO.getValidOrderCount());
           row.getCell(4).setCellValue(businessDataVO.getUnitPrice());

           //填充明细数据
           for (int i = 0; i < 30; i++) {
               LocalDate date = dateBegin.plusDays(i);
               //查询某一天的营业数据
               BusinessDataVO businessData = workspaceService.getBusinessData(LocalDateTime.of(date, LocalTime.MIN), LocalDateTime.of(date, LocalTime.MAX));

               //获得某一行
               row = sheet.getRow(7 + i);
               row.getCell(1).setCellValue(date.toString());
               row.getCell(2).setCellValue(businessData.getTurnover());
               row.getCell(3).setCellValue(businessData.getValidOrderCount());
               row.getCell(4).setCellValue(businessData.getOrderCompletionRate());
               row.getCell(5).setCellValue(businessData.getUnitPrice());
               row.getCell(6).setCellValue(businessData.getNewUsers());
           }

           //3. 通过输出流将Excel文件下载到客户端浏览器
           ServletOutputStream out = response.getOutputStream();
           excel.write(out);

           //关闭资源
           out.close();
           excel.close();
       } catch (IOException e) {
           e.printStackTrace();
       }

   }




}
