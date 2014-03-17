/*
    Copyright (C) 2009, Cam-Tu Nguyen, Xuan-Hieu Phan
    
    Email:	ncamtu@gmail.com; pxhieu@gmail.com
    URL:	http://www.hori.ecei.tohoku.ac.jp/~hieuxuan
    
    Graduate School of Information Sciences,
    Tohoku University
*/

package jvntagger.data;

import java.io.IOException;
import java.io.Writer;

public class TWord {
	private String word;
	private String tag = null; //null if this word is not tagged	
	
	//constructors
	public TWord(String _word, String _tag){		
		word = _word.replaceAll(" ","_");;
		tag = _tag;
	}
	
	public TWord(String _word){
		word = _word.replaceAll(" ","_");				
	}
	
	//get methods
	public String getWord(){
		return word;
	}
	
	public String getTag(){
		return tag;
	}
	
	public void setTag(String t){
		tag = t;
	}
	
	//DEBUG
	public void print(){
		System.out.println(word + "\t" + tag);
	}
	
	public void print(Writer out) throws IOException{
		if (tag == null)
			System.out.println(tag);			
		out.write(word + "\t" + tag + "\n");		
	}
}
