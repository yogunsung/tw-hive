/** 
 * Project Name:tw-hive 
 * File Name:TestOutputFormat.java 
 * Package Name:cn.com.talkweb.hive.serde 
 * Date:2015年2月10日 下午4:11:19 
 * Copyright (c) 2015, TalkWeb All Rights Reserved. 
 * 
 */  
package cn.com.talkweb.hive.serde;

import java.io.IOException;
import java.util.Properties;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hive.ql.exec.FileSinkOperator.RecordWriter;
import org.apache.hadoop.hive.ql.io.HiveIgnoreKeyTextOutputFormat;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.util.Progressable;

/** 
 * @author songyg 
 * @version  
 * @since JDK 1.6 
 */
@SuppressWarnings("rawtypes")
public class MyOutputFormat<K extends WritableComparable, V extends Writable>
		extends HiveIgnoreKeyTextOutputFormat<K, V> {

	public RecordWriter getHiveRecordWriter(JobConf jc, Path outPath,
			Class<? extends Writable> valueClass, boolean isCompressed,
			Properties tableProperties, Progressable progress)
			throws IOException {
		
		RecordWriter writer = super
		        .getHiveRecordWriter(jc, outPath, BytesWritable.class,
		                isCompressed, tableProperties, progress);
		return new MyRecordWriter(writer);
	}

}
