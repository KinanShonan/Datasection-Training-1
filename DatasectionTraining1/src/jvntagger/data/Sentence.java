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
import java.util.ArrayList;
import java.util.List;

public class Sentence {
	List<TWord> sentence = null;
	
	public Sentence(){
		sentence = new ArrayList<TWord>();
	}
	
	public void addTWord(TWord tword){
		sentence.add(tword);
	}
	
	public void addTWord(String word, String tag){
		TWord tword = new TWord(word, tag);
		sentence.add(tword);
	}
	
	public void addTWord(String word){
		sentence.add(new TWord(word));
	}
	
	public String getWordAt(int pos){
		return sentence.get(pos).getWord();
	}
	
	public String getTagAt(int pos){
		return sentence.get(pos).getTag();
	}
	
	public TWord getTWordAt(int pos){
		return sentence.get(pos);
	}
	
	public void clear(){
		sentence.clear();
	}
	
	public int size(){
		return sentence.size();
	}
	
	//DEBUG
	public void print(){
		for (int i = 0; i < sentence.size(); ++i){
			sentence.get(i).print();
		}
		
		System.out.print("\n");
	}
	
	public void print(Writer out) throws IOException{
		for (int i = 0; i < sentence.size(); ++i){
			sentence.get(i).print(out);
		}
		out.write("\n");
	}
}
