/** 
 * Project Name:tw-hive 
 * File Name:TestRecordWriter.java 
 * Package Name:cn.com.talkweb.hive.serde 
 * Date:2015年2月10日 下午4:14:08 
 * Copyright (c) 2015, TalkWeb All Rights Reserved. 
 * 
 */  
package cn.com.talkweb.hive.serde;

import java.io.IOException;

import org.apache.hadoop.hive.ql.exec.FileSinkOperator.RecordWriter;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;

/** 
 * @author songyg 
 * @version  
 * @since JDK 1.6 
 */
public class MyRecordWriter implements RecordWriter {
	
	 RecordWriter writer;
	 BytesWritable bytesWritable;

	public MyRecordWriter(RecordWriter writer) {
		this.writer = writer;
		this.bytesWritable = new BytesWritable();
	}

	public void write(Writable w) throws IOException {
		String strReplace = ((Text) w).toString().replace("\001", "@$_$@");
		Text txtReplace = new Text();
		txtReplace.set(strReplace);
		byte[] output = txtReplace.getBytes();
		bytesWritable.set(output, 0, output.length);
		writer.write(bytesWritable);
	}

	public void close(boolean abort) throws IOException {
		writer.close(abort);
	}

	
}
