/** 
 * Project Name:tw-hive 
 * File Name:UDFCount.java 
 * Package Name:cn.com.talkweb.hive.udf 
 * Date:2015年2月9日 上午10:29:41 
 * Copyright (c) 2015, TalkWeb All Rights Reserved. 
 * 
 */  
package cn.com.talkweb.hive.udf;

import org.apache.hadoop.hive.ql.exec.UDAF;
import org.apache.hadoop.hive.ql.exec.UDAFEvaluator;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;

/** UDAF（用户定义聚集函数）：接收多个输入数据行，并产生一个输出数据行。（count，max）
 * <ul>
 * 	<li>继承org.apache.hadoop.hive.ql.exec.UDAF</li>
 * 	<li>包含一个或多个嵌套的实现了org.apache.hadoop.hive.ql.exec.UDAFEvaluator的静态类</li>
 * 	<li>实现以下方法
 * 		<ul>
 * 			<li>init()：负责初始化计算函数并重设它的内部状态</li>
 * 			<li>iterate()：每次对一个新值进行聚集计算都会调用该方法。其中iterate()接收的参数跟hive的参数对应</li>
 * 			<li>terminatePartial()：部分聚集结果，返回一个封装了聚集计算当前状态的对象</li>
 * 			<li>merge()：合并一部分聚集值和另一部分聚集值，接收一个对象作为输入，对象类型与terminatePartial()返回类型一致</li>
 * 			<li>terminate ()：返回最终的聚集结果</li>
 * 		</ul>
 * 	</li>
 * </ul>
 * @author songyg 
 * @version  
 * @since JDK 1.6 
 */
public class UDAFAvg extends UDAF {
	
	public static class AvgEvaluate implements UDAFEvaluator {
		
		public static class PartialResult {
			
			public Integer count;
			public Integer total;
			
			public PartialResult() {
				count = 0;
				total = 0;
			}
			
		}
		
		private PartialResult partialResult;
		
		public void init() {
			partialResult = new PartialResult();
		}
		
		public boolean iterate(IntWritable value) {
			if(partialResult == null)
				partialResult = new PartialResult();
			
			if(value != null) {
				partialResult.total =partialResult.total + value.get();
				partialResult.count=partialResult.count + 1;
			}
			return true;
		}
		
		public PartialResult terminatePartial() {
			return partialResult;
		}
		
		public boolean merge(PartialResult other) {
			partialResult.total = partialResult.total + other.total;
			partialResult.count = partialResult.count + other.count;
			return true;
		}

		public DoubleWritable terminate() {
			return new DoubleWritable(partialResult.total / partialResult.count);
		}
		
	}
	
}
