package com.credit;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

public class UserGenerator {
    public static void main(String[] args) {
        FastAutoGenerator.create("jdbc:mysql://localhost:3306/bank_application_credit", "root", "youpassword")
                .globalConfig(builder -> {
                    builder.author("weiyanhu") // 设置作者
                            .enableSwagger()// 开启 swagger 模式
                            .fileOverride() // 覆盖已生成文件
                            .outputDir("/Users/austin/Desktop/projects/Bank_Application/Backend/MyBatisGenerator/src/java"); // 指定输出目录
                }).strategyConfig(builder -> {
                    builder.mapperBuilder().enableBaseResultMap().enableMapperAnnotation().enableBaseColumnList();// 设置过滤表前
                }).templateConfig(templateConfig -> {
                    templateConfig.controller("").service("").serviceImpl("");
                }).packageConfig(builder -> {
                    builder.parent("com.credit").entity("pojo");
                })
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();
    }

}


