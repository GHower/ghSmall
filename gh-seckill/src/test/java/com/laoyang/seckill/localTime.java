package com.laoyang.seckill;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * @author ghower
 * @Date 2020-07-22 20:15
 * @Email 1520567597@qq.com
 */
public class localTime {
    public static void main(String[] args) {
         LocalDate now = LocalDate.now();
         LocalDate plus = now.plusDays(2);
         LocalDateTime now1 = LocalDateTime.now();
         LocalTime now2 = LocalTime.now();

         LocalTime max = LocalTime.MAX;
         LocalTime min = LocalTime.MIN;

         LocalDateTime start = LocalDateTime.of(now, min);
         LocalDateTime end = LocalDateTime.of(plus, max);

        String startStr = start.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String endStr = end.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

         System.out.println(now);
         System.out.println(now1);
         System.out.println(now2);
         System.out.println(plus);

         System.out.println(start);
         System.out.println(end);
        System.out.println(startStr);
        System.out.println(endStr);
    }
}
