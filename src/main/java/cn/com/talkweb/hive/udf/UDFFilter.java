/** 
 * Project Name:tw-hive 
 * File Name:UDFFilter.java 
 * Package Name:cn.com.talkweb.hive.udf 
 * Date:2015年2月9日 上午10:29:05 
 * Copyright (c) 2015, TalkWeb All Rights Reserved. 
 * 
 */  
package cn.com.talkweb.hive.udf;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.io.Text;

/** UDF（用户自定义函数）：操作作用于单个数据行，产生一个数据行作为输出。（数学函数，字符串函数）
 * <ul>
 * 	<li>继承org.apache.hadoop.hive.ql.exec.UDF</li>
 * <li>至少实现一个evaluate方法，evaluate方法参数不限</li>
 * </ul>
 * @author songyg 
 * @version  
 * @since JDK 1.6 
 */
public class UDFFilter extends UDF {
	
	public Text evaluate(String cntyName){
		Text res = new Text();
		if(!StringUtils.isEmpty(cntyName)){
			res.set(cntyName.split("_")[1]);
		}else{
			res.set("");
		}
		return res;
	}
}
