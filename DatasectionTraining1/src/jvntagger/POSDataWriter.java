/*
    Copyright (C) 2009, Cam-Tu Nguyen, Xuan-Hieu Phan
    
    Email:	ncamtu@gmail.com; pxhieu@gmail.com
    URL:	http://www.hori.ecei.tohoku.ac.jp/~hieuxuan
    
    Graduate School of Information Sciences,
    Tohoku University
*/

package jvntagger;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

import jvntagger.data.DataWriter;
import jvntagger.data.Sentence;

public class POSDataWriter extends DataWriter {
	
	public void writeFile(List lblSeqs, String filename){		
		try {
			String ret = writeString(lblSeqs);
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(filename), "UTF-8"));
			out.write(ret);
			out.close();
		}
		catch (Exception e){
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
	
	public String writeString(List lblSeqs){
		String ret = "";
		for (int i = 0; i < lblSeqs.size(); ++i){
			Sentence sent = (Sentence) lblSeqs.get(i);
			
			for (int j = 0; j < sent.size(); ++j){
				ret += sent.getWordAt(j) + "/" + sent.getTagAt(j) + " ";
			}
			ret = ret.trim() + "\n";
		}		
		
		return ret.trim();
	}
	
}
