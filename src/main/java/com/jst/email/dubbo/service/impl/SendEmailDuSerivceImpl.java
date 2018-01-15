/*
* Copyright (c) 2015-2018 SHENZHEN JST SCIENCE AND TECHNOLOGY DEVELOP CO., LTD. All rights reserved.
*
* 注意：本内容仅限于深圳市捷顺金科研发有限公司内部传阅，禁止外泄以及用于其他的商业目的 
*/
/*
* Copyright (c) 2016-2020 SHENZHEN JST SCIENCE AND TECHNOLOGY DEVELOP CO., LTD. All rights reserved.
*
* 注意：本内容仅限于深圳市捷顺金科研发有限公司内部传阅，禁止外泄以及用于其他的商业目的 
*/

package com.jst.email.dubbo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.jst.email.service.SendEmailService;
import com.jst.prodution.base.bean.BaseBean;
import com.jst.prodution.email.dubbo.service.SendEmailDuService;

/** 
 * 
 * @Package: com.jst.email.dubbo.service  
 * @ClassName: SendEmailSerivceImpl 
 * @Description: 邮件发送dubbo
 *
 * @author: Administrator 
 * @date: 2017年11月6日 上午10:53:27 
 * @version V1.0 
 */
@Service
public class SendEmailDuSerivceImpl implements SendEmailDuService{

	@Autowired
	private SendEmailService sendEmailService;

	public BaseBean action(BaseBean input) {
		return sendEmailService.action(input);
	}
}

