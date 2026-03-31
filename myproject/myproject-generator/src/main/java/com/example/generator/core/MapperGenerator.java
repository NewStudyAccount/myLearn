package com.example.generator.core;

import com.example.generator.config.GeneratorProperties;
import com.example.generator.domain.TableInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class MapperGenerator {

    private static final Logger log = LoggerFactory.getLogger(MapperGenerator.class);

    private final TemplateEngine templateEngine;
    private final GeneratorProperties properties;

    public MapperGenerator(TemplateEngine templateEngine, GeneratorProperties properties) {
        this.templateEngine = templateEngine;
        this.properties = properties;
    }

public String generateInterface(TableInfo tableInfo) {
        Map<String, Object> context = new HashMap<>();
        context.put("table", tableInfo);
        context.put("packageName", properties.getBasePackage());

        String mapperPackage = properties.getBasePackage() + ".mapper";
        String entityPackage = properties.getBasePackage() + ".domain";

        context.put("mapperPackage", mapperPackage);
        context.put("entityPackage", entityPackage);
        context.put("mapperName", tableInfo.getEntityName() + "Mapper");
        context.put("entityName", tableInfo.getEntityName());

        return templateEngine.render("templates/backend/mapper.vm", context);
    }

    public String generateXml(TableInfo tableInfo) {
        Map<String, Object> context = new HashMap<>();
        context.put("table", tableInfo);
        context.put("mapperPackage", properties.getBasePackage() + ".mapper");
        context.put("mapperName", tableInfo.getEntityName() + "Mapper");
        context.put("entityName", tableInfo.getEntityName());
        context.put("entityFullName", properties.getBasePackage() + ".domain." + tableInfo.getEntityName());

        return templateEngine.render("templates/backend/mapper-xml.vm", context);
    }

    public String generateXml(TableInfo tableInfo) {
        Map<String, Object> context = new HashMap<>();
        context.put("table", tableInfo);
        context.put("mapperPackage", properties.getBasePackage() + ".mapper");
        context.put("mapperName", tableInfo.getEntityName() + "Mapper");
        context.put("entityName", tableInfo.getEntityName());
        context.put("entityFullName", properties.getBasePackage() + ".domain." + tableInfo.getEntityName());

        String template = getMapperXmlTemplate();
        return templateEngine.renderString(template, context);
    }

    private String getMapperInterfaceTemplate() {
        return """
package ${mapperPackage};

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import ${entityPackage}.${entityName};
import org.apache.ibatis.annotations.Mapper;

/**
 * ${table.tableComment}
 */
@Mapper
public interface ${mapperName} extends BaseMapper<${entityName}> {

}
""";
    }

    private String getMapperXmlTemplate() {
        return """
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="${mapperPackage}.${mapperName}">

    <resultMap id="BaseResultMap" type="${entityFullName}">
#foreach($column in $table.columns)
#if($column.isPrimaryKey)
        <id column="${column.columnName}" property="${column.fieldName}" />
#else
        <result column="${column.columnName}" property="${column.fieldName}" />
#end
#end
    </resultMap>

    <sql id="Base_Column_List">
#foreach($column in $table.columns)
        ${column.columnName}#if($foreach.hasNext),#end

#end
    </sql>

    <select id="selectById" resultMap="BaseResultMap">
        SELECT
        <include refid="Base_Column_List" />
        FROM ${table.tableName}
        WHERE ${table.primaryKey.columnName} = #{${table.primaryKey.fieldName}}
    </select>

    <insert id="insert" useGeneratedKeys="true" keyProperty="${table.primaryKey.fieldName}">
        INSERT INTO ${table.tableName}
        <trim prefix="(" suffix=")" suffixOverrides=",">
#foreach($column in $table.columns)
#if(!$column.isPrimaryKey)
            <if test="${column.fieldName} != null">
                ${column.columnName},
            </if>
#end
#end
        </trim>
        <trim prefix="VALUES (" suffix=")" suffixOverrides=",">
#foreach($column in $table.columns)
#if(!$column.isPrimaryKey)
            <if test="${column.fieldName} != null">
                #{${column.fieldName}},
            </if>
#end
#end
        </trim>
    </insert>

    <update id="updateById">
        UPDATE ${table.tableName}
        <set>
#foreach($column in $table.columns)
#if(!$column.isPrimaryKey)
            <if test="${column.fieldName} != null">
                ${column.columnName} = #{${column.fieldName}},
            </if>
#end
#end
        </set>
        WHERE ${table.primaryKey.columnName} = #{${table.primaryKey.fieldName}}
    </update>

    <delete id="deleteById">
        DELETE FROM ${table.tableName}
        WHERE ${table.primaryKey.columnName} = #{${table.primaryKey.fieldName}}
    </delete>

</mapper>
""";
    }
}