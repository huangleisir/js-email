/*
* Copyright (c) 2015-2018 SHENZHEN TOMTOP SCIENCE AND TECHNOLOGY DEVELOP CO., LTD. All rights reserved.
*
* 注意：本内容仅限于深圳市通拓科技研发有限公司内部传阅，禁止外泄以及用于其他的商业目的 
*/
package com.jst.email.common.util;
 
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.SecretKeyFactory;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
 
/**
 * 
 * 
 * @Package: com.tomtop.framework.common.utils  
 * @ClassName: SaltHashUtil 
 * @Description: 加盐哈希算法实现（采用PBKDF2WithHmacSHA256算法实现，
 * 可以 防止数据库被攻破情况还在一段时间内可以保障用户的密码可以不被破解掉，
 * 性能上第一次预热生成较慢，第二次以后较快可以使用）
 *
 * @author: lixin 
 * @date: 2016年8月24日 下午9:42:29 
 * @version V1.0
 */
public class SaltHashUtil
{
	/**
	 * 使用PBKDF2WithHmacSHA256算法做加盐哈希，比PBKDF2WithHmacSHA1加密效果要好一些，性能也不差
	 */
    public static final String PBKDF2_ALGORITHM = "PBKDF2WithHmacSHA256";
 
    public static final int HASH_BYTE_SIZE = 24;
    public static final int PBKDF2_ITERATIONS = 1000;
 
    public static final int ITERATION_INDEX = 0;
    public static final int SALT_INDEX = 1;
    public static final int PBKDF2_INDEX = 2;
 
    /**
     * 返回加盐哈希值，基于PBKDF2算法
     * @param password  密码
     * @param salt      盐值
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public static String hash(String password,String salt)
        throws NoSuchAlgorithmException, InvalidKeySpecException
    {
        return hash(password.toCharArray(),salt);
    }

    /**
     * 返回加盐哈希值，基于PBKDF2算法 
     * @param password 密码
     * @param salt     盐值
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public static String hash(char[] password,String salt)
        throws NoSuchAlgorithmException, InvalidKeySpecException
    {
        /**  对密码和盐值做加盐哈希，返回字节数组  **/
        byte[] hash = pbkdf2(password, salt.getBytes(), PBKDF2_ITERATIONS, HASH_BYTE_SIZE);
        
        /** 将加盐哈西后字节数组返回为String字符串 **/
        return toHex(hash);
    }
    
   
    /**
     *  使用PBKDF2算法计算密码最后的哈希值
     *
     * @param   password    密码
     * @param   salt        随机盐
     * @param   iterations  迭代次数，默认为1000次
     * @param   bytes       输出盐值长度
     * @return              对密码加盐哈希之后的值
     */
    private static byte[] pbkdf2(char[] password, byte[] salt, int iterations, int bytes)
        throws NoSuchAlgorithmException, InvalidKeySpecException
    {
        PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, bytes * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance(PBKDF2_ALGORITHM);
        return skf.generateSecret(spec).getEncoded();
    }
 
   
    /**
     * 对字节数组转换为16进制字符串
     *
     * @param   array       要转换的字节数组
     * @return              a length*2 character string encoding the byte array
     */
    private static String toHex(byte[] array)
    {
        BigInteger bi = new BigInteger(1, array);
        String hex = bi.toString(16);
        int paddingLength = (array.length * 2) - hex.length();
        if(paddingLength > 0)
            return String.format("%0" + paddingLength + "d", 0) + hex;
        else
            return hex;
    }
    
   
 
    /**
     * Tests the basic functionality of the PasswordHash class
     *
     * @param   args        ignored
     */
    public static void main(String[] args)
    {
    	System.out.println();
    	
    	String s = "{\"appid\": \"89966566548\",\"appkey\": \"sizmdekc234km2kgh23ddh42do32fd\",\"txntime\": \"201701091513\",\"seqid\": \"2017010915130031234\",\"userid\": \"23432\",\"carno\": \"B12312\",\"messagetype\": \"302\",\"message\": \"您爱车XXXXX已进入停车场\",\"url\": \"baidu.com\",\"showtype\": \"01\",\"pushtime\": \"201701091513\",\"sign\": \"acljdaowejklrjwlkerjkjhkh\"}" ;
    	for(int i=0;i< 100;i++) {
    		
    		try {
    			
    			long begineTime = System.currentTimeMillis();
    			
				System.out.println(SaltHashUtil.hash(s, RandomKeyUtil.generate()));
				
				System.out.println(" 耗时：   "  + (System.currentTimeMillis() - begineTime));
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				 e.printStackTrace();
			} catch (InvalidKeySpecException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    		
//    		try {
//			     System.out.println(SaltHashUtil.validatePassword("21323244", "ba415ca1ddf9f09b1ebdeea500510fe0778a851697f61cba"));
//			} catch (NoSuchAlgorithmException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (InvalidKeySpecException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}		
    		
    		
    	
//    	System.out.println(" 耗时：   "  + (System.currentTimeMillis() - begineTime));
//    	
//    		
//    		String salt = RandomKeyUtil.generate();
//    	
//    	try {
//			System.out.println(SaltHashUtil.createHash("aaaa", salt));
//			
//			System.out.println(SaltHashUtil.createHash("aaaa", salt));
//			
//		} catch (NoSuchAlgorithmException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (InvalidKeySpecException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
    	
    }
 
}