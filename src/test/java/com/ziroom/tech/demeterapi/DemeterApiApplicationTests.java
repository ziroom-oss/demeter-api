package com.ziroom.tech.demeterapi;

import com.ziroom.tech.demeterapi.dao.entity.GraphAreaSkill;
import com.ziroom.tech.demeterapi.dao.mapper.GraphAreaSkillDao;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
class DemeterApiApplicationTests {


	@Resource
	GraphAreaSkillDao graphAreaSkillDao;
	@Test
	public void contextLoads() {
		GraphAreaSkill graphAreaSkill = new GraphAreaSkill();
		graphAreaSkill.setGraphId(111);
		graphAreaSkill.setSkillName("sdfs");
		graphAreaSkill.setSkillAreaName("sdfsdf");
		System.out.println(graphAreaSkillDao.insertSelective(graphAreaSkill));
	    System.out.println(graphAreaSkill.getId());
	}


}
