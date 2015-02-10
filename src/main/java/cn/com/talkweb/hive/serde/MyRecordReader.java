/** 
 * Project Name:tw-hive 
 * File Name:TestRecordReader.java 
 * Package Name:cn.com.talkweb.hive.serde 
 * Date:2015年2月10日 下午4:13:38 
 * Copyright (c) 2015, TalkWeb All Rights Reserved. 
 * 
 */  
package cn.com.talkweb.hive.serde;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.LineRecordReader;
import org.apache.hadoop.mapred.RecordReader;

/** 
 * @author songyg 
 * @version  
 * @since JDK 1.6 
 */
public class MyRecordReader implements RecordReader<LongWritable, Text> {
	
	LineRecordReader reader;
	Text text;

	public MyRecordReader(LineRecordReader reader) {
		this.reader = reader;
		this.text = reader.createValue();
	}

	public boolean next(LongWritable key, Text value) throws IOException {
		while (reader.next(key, text)) {
			String strReplace = text.toString().toLowerCase().replace("@$_$@", "\001");
			Text txtReplace = new Text();
			txtReplace.set(strReplace);
			value.set(txtReplace.getBytes(), 0, txtReplace.getLength());
			return true;
		}
		return false;
	}

	public LongWritable createKey() {
		return reader.createKey();
	}

	public Text createValue() {
		return new Text();
	}

	public long getPos() throws IOException {
		return reader.getPos();
	}

	public void close() throws IOException {
		reader.close();
	}

	public float getProgress() throws IOException {
		return reader.getProgress();
	}

}
