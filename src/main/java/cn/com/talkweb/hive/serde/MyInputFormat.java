/** 
 * Project Name:tw-hive 
 * File Name:TestFileInputFormat.java 
 * Package Name:cn.com.talkweb.hive.serde 
 * Date:2015年2月10日 下午4:10:22 
 * Copyright (c) 2015, TalkWeb All Rights Reserved. 
 * 
 */  
package cn.com.talkweb.hive.serde;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.FileSplit;
import org.apache.hadoop.mapred.InputSplit;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.LineRecordReader;
import org.apache.hadoop.mapred.RecordReader;
import org.apache.hadoop.mapred.Reporter;
import org.apache.hadoop.mapred.TextInputFormat;

/** 
 * @author songyg 
 * @version  
 * @since JDK 1.6 
 */
public class MyInputFormat extends TextInputFormat {
	
	@Override
	public RecordReader<LongWritable, Text> getRecordReader(InputSplit split,
			JobConf job, Reporter reporter) throws IOException {
		reporter.setStatus(split.toString());
		return new MyRecordReader(new LineRecordReader(job, (FileSplit) split));
	}
}
