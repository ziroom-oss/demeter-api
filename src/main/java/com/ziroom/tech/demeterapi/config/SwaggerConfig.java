package com.ziroom.tech.demeterapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * swagger配置
 *
 * @author huangqiaowei
 * @date 2019-09-17 19:06
 **/
@Configuration
@EnableSwagger2
public class SwaggerConfig {


    /**
     * 创建一个Docket对象
     * 调用select()方法，
     * 生成ApiSelectorBuilder对象实例，该对象负责定义外漏的API入口
     * 通过使用RequestHandlerSelectors和PathSelectors来提供Predicate，在此我们使用any()方法，将所有API都通过Swagger进行文档管理
     *
     * @return docket
     */
    @Bean
    public Docket createRestApi() {
        ParameterBuilder uid = new ParameterBuilder();
        List<Parameter> pars = new ArrayList<>();
        uid.name("uid").description("用户系统号").modelRef(new ModelRef("string")).parameterType("header").defaultValue(
                "60028724").required(true).build();
        pars.add(uid.build());
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                .globalOperationParameters(pars);
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                //标题
                .title("项目管理系统swagger文档")
                //简介
                .description("我们的目标是星辰大海")
                //服务条款
                .termsOfServiceUrl("")
                .version("1.0")
                .build();
    }
}
