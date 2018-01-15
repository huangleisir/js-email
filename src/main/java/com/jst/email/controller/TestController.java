package com.jst.email.controller;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.jst.email.bo.BaseBo;
import com.jst.email.common.bean.Result;

import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/test")
public class TestController {

	 	@RequestMapping(value = "/testdemo", method = RequestMethod.POST)
	    @ResponseBody
	    public Result test(@ApiParam(name="BaseBo",value="测试baseBo",required=true)@RequestBody BaseBo baseBo) {
	        Result result = new Result();
	        return result;
	    }
}
