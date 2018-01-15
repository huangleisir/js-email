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

package com.jst.email.dao.base;

import org.springframework.stereotype.Repository;

import com.jst.email.bean.Email;
import com.jst.prodution.util.ILogger;

/** 
 * 
 * @Package: com.jst.invoice.dao.base  
 * @ClassName: InvoiceDao 
 * @Description: 发票dao
 *
 * @author: wen 
 * @date: 2017年10月31日 上午10:15:57 
 * @version V1.0 
 */
@Repository
@SuppressWarnings("unchecked")
public class EmailDao extends AbstractDao {

	 private static ILogger log = new ILogger("invoice", EmailDao.class);
	   
	    private final String MAPPER_NAMESPACE = "Email." ;

	   
	    public int save(Email email){
	    	return baseDao.insert(MAPPER_NAMESPACE.concat("insert"), email) ;
	    }
	    
}
