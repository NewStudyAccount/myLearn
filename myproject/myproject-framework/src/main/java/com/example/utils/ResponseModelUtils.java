package com.example.utils;

import com.alibaba.fastjson2.JSON;
import com.example.domain.Response;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

public class ResponseModelUtils {
    public static void write(HttpServletResponse response, Response responseModel) throws IOException {
        //响应头设置，json
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        //创建输出流对象
        PrintWriter writer =  response.getWriter();
        //构建输出数据

        String jsonString = JSON.toJSONString(responseModel);
//        String jsonString = .toJSONString(responseModel);
        //向前端相应数据
        writer.print(jsonString);
    }
}
