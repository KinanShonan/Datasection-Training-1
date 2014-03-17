/*
    Copyright (C) 2009, Cam-Tu Nguyen, Xuan-Hieu Phan
    
    Email:	ncamtu@gmail.com; pxhieu@gmail.com
    URL:	http://www.hori.ecei.tohoku.ac.jp/~hieuxuan
    
    Graduate School of Information Sciences,
    Tohoku University
*/

package jvntagger;

import java.io.File;

import jflexcrf.Labeling;
import jvntagger.data.DataReader;
import jvntagger.data.DataWriter;
import jvntagger.data.TaggingData;

public class CRFTagger implements POSTagger {
	DataReader reader = new POSDataReader();
	DataWriter writer = new POSDataWriter();
	
	TaggingData dataTagger = new TaggingData();
	
	Labeling labeling = null;
	
	public CRFTagger(String modelDir){
		init(modelDir);
	}
	
	public void init(String modelDir) {
		// TODO Auto-generated method stub
		dataTagger.addContextGenerator(new POSContextGenerator(modelDir + File.separator + "featuretemplate.xml"));
		labeling = new Labeling(modelDir, dataTagger, reader, writer);
	}

	public String tagging(String instr) {
		// TODO Auto-generated method stub
		return labeling.strLabeling(instr);
	}

	public String tagging(File file) {
		// TODO Auto-generated method stub
		return labeling.strLabeling(file);
	}
	
	public void setDataReader(DataReader reader){
		this.reader = reader;
	}
	
	public void setDataWriter(DataWriter writer){
		this.writer = writer;
	}

}
