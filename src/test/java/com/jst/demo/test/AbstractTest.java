/*
* Copyright (c) 2015-2018 SHENZHEN GUIYI SCIENCE AND TECHNOLOGY DEVELOP CO., LTD. All rights reserved.
*
* 注意：本内容仅限于深圳市捷顺金科研发有限公司内部传阅，禁止外泄以及用于其他的商业目的 
*/
package com.jst.demo.test;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AbstractTest {
	
    public MockMvc mvc;
    
    public Map<String, Object> baseMap ;
    
    @Autowired
    WebApplicationContext wac ;
    
	@Before
    public void setup(){
       /*
        * MockMvcBuilders使用构建MockMvc对象.
        */
		baseMap = new HashMap<>() ;
		baseMap.put("appVersion",1);
		baseMap.put("deviceType",1);
		baseMap.put("clientId","123123213213213213213");
         mvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }
}
