/*
* Copyright (c) 2015-2018 SHENZHEN JST SCIENCE AND TECHNOLOGY DEVELOP CO., LTD. All rights reserved.
*
* 注意：本内容仅限于深圳市捷顺金科研发有限公司内部传阅，禁止外泄以及用于其他的商业目的 
*/

package com.jst.email.service;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Vector;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.util.StringUtil;
import com.jst.email.bean.Email;
import com.jst.email.common.constant.SysConstant;
import com.jst.email.common.util.DateUtil;
import com.jst.email.common.util.SendMail;
import com.jst.email.common.util.SnowflakeIdUtil;
import com.jst.email.dao.base.EmailDao;
import com.jst.prodution.base.bean.BaseBean;
import com.jst.prodution.base.service.AbstractBaseService;
import com.jst.prodution.email.serviceBean.MailBean;
import com.jst.prodution.utils.exception.JstException;

/**
 * 
 * @Package: com.jst.email.service
 * @ClassName: SendEmailService 发邮件服务类
 *
 * @author: Administrator
 * @date: 2017年11月6日 上午10:58:25
 * @version V1.0
 */
@Service
public class SendEmailService extends AbstractBaseService {
	
	private final Logger logger = LoggerFactory.getLogger(SendEmailService.class);
    
	@Autowired
	private EmailDao  emailDao;
	@Override
	public void checkParams(BaseBean input) {
		MailBean bean = (MailBean) input;
	
		if (StringUtil.isEmpty(bean.getTo())) {
			throw new JstException(SysConstant.EMAIL_FROM, BaseBean.RES_TYPE_APP_ERR, "收件人不能为空");
		}
		;
		if (StringUtil.isEmpty(bean.getFlowNo())) {
			throw new JstException(SysConstant.INVOICE_FLOWNO, BaseBean.RES_TYPE_APP_ERR, "发票流水号不能为空");
		}
		;
		if (StringUtil.isEmpty(bean.getPdfUrl())) {
			throw new JstException(SysConstant.PDF_URL, BaseBean.RES_TYPE_APP_ERR, "pdfUrl地址不能为空");
		}
		;
	}

	@Override
	public BaseBean process(BaseBean input)  {
		MailBean bean = (MailBean) input;
		logger.info("发邮件入参："+JSON.toJSONString(bean));
		try{
			   Thread.sleep(3000);   // 航信异常生成pdf,这里等待3秒去获取pdf文件
			   HttpClient httpClient = new DefaultHttpClient();
			   HttpGet method = new HttpGet(bean.getPdfUrl());
		       method.setHeader("accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		       HttpResponse response = httpClient.execute(method);
		       int statusCode = response.getStatusLine().getStatusCode();
		       logger.info("url:"+bean.getPdfUrl()+"----状态码:" + statusCode);
		       if (statusCode != HttpStatus.SC_OK) {
		       logger.error("请求失败返回代码" +statusCode);
		           throw new JstException("EM0001","读取文件失败;返回代码 ："+statusCode);
		       }
		logger.info("发送邮件开始....");
		final File pdffile = File.createTempFile("elfp", ".pdf");// 创建临时文件
		logger.info("临时文件所在的本地路径：" + pdffile.getCanonicalPath());
		// 读文件流
		//DataInputStream in = new DataInputStream(urlCon.getInputStream());
		DataInputStream in = new DataInputStream(response.getEntity().getContent());
		DataOutputStream out = new DataOutputStream(new FileOutputStream(pdffile));
		byte[] buffer = new byte[2048];
		int count = 0;
		while ((count = in.read(buffer)) > 0) {
			out.write(buffer, 0, count);
		}
		// 关闭输出流
		out.close();
		in.close();
		Vector<String> file = new Vector<String>();
		file.add(pdffile.getCanonicalPath());
		bean.setFile(file);
		boolean flag =SendMail.sendMail(bean);
		pdffile.delete(); // 删掉文件
		
		//保存到数据库
		Email email  = new Email();
		email.setId(String.valueOf(SnowflakeIdUtil.generate()));
		email.setCreateTime(DateUtil.getSysDateTime());
		email.setInvoiceId(bean.getFlowNo());
		email.setUrl(bean.getPdfUrl());
		if(flag){
			email.setStatus(SysConstant.EMAIL_STATUS_S);
		}else{
			email.setStatus(SysConstant.EMAIL_STATUS_F);
		}
		email.setReceiveEmail(bean.getTo());
	    log.info("保存邮件信息----："+JSON.toJSONString(email));
		int num = emailDao.save(email);
		}catch(Exception e){
			log.info("发邮件失败"+e.getMessage());
		}
		return bean;
	}


	
	@Override
	protected String getSysResCode() {
		return null;
	}

}
