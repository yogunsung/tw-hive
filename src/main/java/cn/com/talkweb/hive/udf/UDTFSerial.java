/** 
 * Project Name:tw-hive 
 * File Name:UDTFSerial.java 
 * Package Name:cn.com.talkweb.hive.udf 
 * Date:2015年2月9日 上午10:38:58 
 * Copyright (c) 2015, TalkWeb All Rights Reserved. 
 * 
 */  
package cn.com.talkweb.hive.udf;

import java.util.ArrayList;

import org.apache.hadoop.hive.ql.exec.UDFArgumentException;
import org.apache.hadoop.hive.ql.exec.UDFArgumentLengthException;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDTF;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspectorFactory;
import org.apache.hadoop.hive.serde2.objectinspector.StructObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorFactory;

/** UDTF（用户定义表生成函数）：操作作用于单个数据行，并产生多个数据行----一个表作为输出
 * <ul>
 * 	<li>继承org.apache.hadoop.hive.ql.udf.generic.GenericUDTF</li>
 * 	<li>实现initialize, process, close三个方法</li>
 * 	<li>UDTF首先会调用initialize方法，此方法返回UDTF的返回行的信息（返回个数，类型）
 * 	初始化完成后，会调用process方法，对传入的参数进行处理，可以通过forword()方法把结果返回。最后close()方法调用，对需要清理的方法进行清理。 
 *  </li>
 * </ul>
 * @author songyg 
 * @version  
 * @since JDK 1.6 
 */
public class UDTFSerial extends GenericUDTF {
	
	Object[] result = new Object[1];

	@Override
	public StructObjectInspector initialize(ObjectInspector[] args)
			throws UDFArgumentException {
		if (args.length != 1) {
			throw new UDFArgumentLengthException(
					"UDTFSerial takes only one argument");
		}
		if (!args[0].getTypeName().equals("int")) {
			throw new UDFArgumentException(
					"UDTFSerial only takes an integer as a parameter");
		}
		ArrayList<String> fieldNames = new ArrayList<String>();
		ArrayList<ObjectInspector> fieldOIs = new ArrayList<ObjectInspector>();
		fieldNames.add("col1");
		fieldOIs.add(PrimitiveObjectInspectorFactory.javaIntObjectInspector);

		return ObjectInspectorFactory.getStandardStructObjectInspector(
				fieldNames, fieldOIs);
	}

	@Override
	public void process(Object[] args) throws HiveException {
		try {
			int n = Integer.parseInt(args[0].toString());
			for (int i = 0; i < n; i++) {
				result[0] = i + 1;
				forward(result);
			}
		} catch (Exception e) {
			throw new HiveException("UDTFSerial has an exception");
		}
	}

	@Override
	public void close() throws HiveException {
		
	}

}
