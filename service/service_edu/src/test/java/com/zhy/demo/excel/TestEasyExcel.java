package com.zhy.demo.excel;

import com.alibaba.excel.EasyExcel;

import java.util.ArrayList;
import java.util.List;

public class TestEasyExcel {
    public static void main(String[] args) {
    //实现excel写的操作
        String filename = "/Users/zhy/code/Java_code/write.xlsx";
        /**
         * 调用easyexcel里面的方法实现写操作，
         * write方法的两个参数：
         * 1、文件路径名称，
         * 2、二个参数实体类class
         * dowrite要求一个List表
         */
//        EasyExcel.write(filename, DemoData.class).sheet("学生列表").doWrite(getData());

        //读操作
        EasyExcel.read(filename, DemoData.class,new ExcelListener()).sheet().doRead();
    }

    private static List<DemoData> getData() {
        ArrayList<DemoData> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            DemoData demoData = new DemoData();
            demoData.setSno(i);
            demoData.setSname("lucy" + i);
            list.add(demoData);
        }
        return list;
    }
}
