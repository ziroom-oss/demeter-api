package com.ziroom.tech.demeterapi;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.*;

@SpringBootTest
class DemeterApiApplicationTests {


	@Test
	public void testMothReport() {
		Date date = new Date();
		//String format = DateUtil.format(date, "YYYY-MM-DD HH:mm:ss");
		DateTime dateTime = DateUtil.offsetMonth(date, -12);
		Date date1 = dateTime.toJdkDate();
		System.out.println("测试date:"+date);
		System.out.println("测试dateTime:"+dateTime);
		System.out.println("测试date1:"+date1);
	}

	@Test
	public void testMapForeach() {
			Date today = new Date();
			System.out.println("测试1： " + DateUtil.format(today, "yyyy-MM"));
			DateTime dateTime = DateUtil.offsetMonth(today, -12);
			String format = DateUtil.format(dateTime, "yyyy-MM");
			System.out.println("测试dateTime： " + format);
	}

}
