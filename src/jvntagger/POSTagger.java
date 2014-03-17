/*
    Copyright (C) 2009, Cam-Tu Nguyen, Xuan-Hieu Phan
    
    Email:	ncamtu@gmail.com; pxhieu@gmail.com
    URL:	http://www.hori.ecei.tohoku.ac.jp/~hieuxuan
    
    Graduate School of Information Sciences,
    Tohoku University
*/

package jvntagger;

import java.io.File;

import jvntagger.data.DataReader;
import jvntagger.data.DataWriter;

public interface POSTagger {
	
	//--------------------------------
	// initialization 
	//--------------------------------
	public void init(String modelfile);
	
	//-------------------------------
	//tagging methods
	//-------------------------------
	
	/**
	 * Annotate string with part-of-speech tags
	 * @param instr string has been done word segmentation
	 * @return string annotated with part-of-speech tags
	 */
	public String tagging(String instr);
	
	
	/**
	 * Annotate content of file with part-of-speech tags
	 * @param file of which content has been done word segmentation
	 * @return string annotated with part-of-speech tags
	 */
	public String tagging(File file);
	
	/**
	 * Set data writer and reader to this pos tagger
	 * this is used to be adaptable to different format of input/output data
	 */
	public void setDataReader(DataReader reader);
	
	public void setDataWriter(DataWriter writer);
}
