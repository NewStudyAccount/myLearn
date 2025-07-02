package com.example.controller;


import com.example.domain.Response;
import com.example.domain.TableDataInfo;
import com.example.domain.pojo.SysCategory;
import com.example.domain.req.SysCategoryQueryPageReq;
import com.example.domain.req.SysCategoryReq;
import com.example.service.SysCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "分类接口")
@RestController
@RequestMapping("/sysCategory")
public class SysCategoryController {


    @Autowired
    private SysCategoryService sysCategoryService;

    @Operation(summary = "分页查询分类接口")
    @PostMapping("/queryCategoryListPage")
    public TableDataInfo<?> queryCategoryListPage(@RequestBody SysCategoryQueryPageReq sysCategoryQueryPageReq){
        return sysCategoryService.queryCategoryListPage(sysCategoryQueryPageReq);
    }

    @Operation(summary = "查询分类接口")
    @PostMapping("/queryCategoryList")
    public Response<?> queryCategoryList() {
        return Response.success(sysCategoryService.queryCategoryList());
    }

    @Operation(summary = "添加分类接口")
    @PostMapping("/addCategory")
    public Response<?> addCategory(@RequestBody SysCategoryReq sysCategoryReq) {
        return Response.success(sysCategoryService.addCategory(sysCategoryReq));
    }



}
