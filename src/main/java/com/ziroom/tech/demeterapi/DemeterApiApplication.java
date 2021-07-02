package com.ziroom.tech.demeterapi;

import com.ziroom.tech.boot.RetrofitServiceScan;
import javax.annotation.Resource;
import javax.sql.DataSource;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author daijiankun
 */
@SpringBootApplication
@RetrofitServiceScan("com.ziroom.tech.demeterapi")
@MapperScan("com.ziroom.tech.demeterapi.dao.mapper")
public class DemeterApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemeterApiApplication.class, args);
	}

}
