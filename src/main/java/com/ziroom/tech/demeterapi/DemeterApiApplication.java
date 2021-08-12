package com.ziroom.tech.demeterapi;

import com.ziroom.gelflog.spring.EnableSpringGelfLog;
import com.ziroom.tech.boot.RetrofitServiceScan;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author daijiankun
 */
@SpringBootApplication
@RetrofitServiceScan("com.ziroom.tech.demeterapi")
@MapperScan("com.ziroom.tech.demeterapi.dao.mapper")
@EnableSpringGelfLog
public class DemeterApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemeterApiApplication.class, args);
	}

}