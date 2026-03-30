package com.example;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DateFormat {






    //使用ThreadLocal
    private static final ThreadLocal<SimpleDateFormat> DATE_FORMAT =
            ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyyMMdd"));

    private static final ThreadLocal<SimpleDateFormat> DATE_FORMAT2 =
            ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd"));

    public static void main(String[] args) throws ParseException {

//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
//        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
//
//
//
//        for (int i = 0; i < 10; i++) {
//            new Thread(() -> {
//                try {
////                    Date parse = simpleDateFormat.parse("20250904");
////                    String format = simpleDateFormat2.format( parse);
//
//                    //使用ThreadLocal
//                    Date parse = DATE_FORMAT.get().parse("20250904");
//                    String format = DATE_FORMAT2.get().format(parse);
//
//
//                    System.out.println(Thread.currentThread().getName()+format);
//                } catch (ParseException e) {
//                    throw new RuntimeException(e);
//                }
//            }).start();
//        }

//        String bisRfuDate = "2020/09/04";
//        String changeOverStartTime = "09:00";
//        String replaceDate = bisRfuDate.replace("/", "-");
//        replaceDate = replaceDate+"T"+ changeOverStartTime+":00";
//        System.out.println(replaceDate);

        String orgValue = "M0_0";

        String newOrgValue = orgValue
                .replaceAll("([A-Za-z])(\\d+(?:\\.\\d+)?)_(\\d+(?:\\.\\d+)?)", "$2$1/$3$1")
                .replaceAll("([A-Za-z])(\\d+(?:\\.\\d+)?)_([A-Za-z])(\\d+(?:\\.\\d+)?)", "$2$1/$4$3");

        System.out.println(newOrgValue);

    }



}
