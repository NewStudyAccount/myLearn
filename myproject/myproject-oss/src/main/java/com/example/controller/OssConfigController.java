package com.example.controller;


import com.example.domain.Response;
import com.example.domain.SysOssConfig;
import com.example.domain.req.OssConfigReq;
import com.example.domain.req.OssConfigUpdateReq;
import com.example.service.SysOssConfigService;
import com.example.utils.SnowflakeIdGenerator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/project/ossConfig")
public class OssConfigController {


    @Autowired
    private SysOssConfigService sysOssConfigService;

    @PostMapping("/insert")
    public Response<?> insertSysOssConfig(@RequestBody OssConfigReq ossConfigReq){

        SysOssConfig sysOssConfig = new SysOssConfig();

        BeanUtils.copyProperties(ossConfigReq,sysOssConfig);
        SnowflakeIdGenerator snowflakeIdGenerator = new SnowflakeIdGenerator(1);
        sysOssConfig.setId(snowflakeIdGenerator.nextId());
        int i = sysOssConfigService.insertSysOssConfig(sysOssConfig);
        return i > 0 ? Response.success("添加成功") : Response.fail("添加失败");
    }

    @PostMapping("/list")
    public Response<?> listSysOssConfig(){
        return Response.success(sysOssConfigService.listSysOssConfig());
    }

    @PostMapping("/update")
    public Response<?> updateSysOssConfig(@RequestBody @Validated OssConfigUpdateReq ossConfigUpdateReq){

        SysOssConfig sysOssConfig = new SysOssConfig();
        BeanUtils.copyProperties(ossConfigUpdateReq,sysOssConfig);
        int i = sysOssConfigService.updateSysOssConfig(sysOssConfig);
        return i > 0 ? Response.success("修改成功") : Response.fail("修改失败");
    }


}
