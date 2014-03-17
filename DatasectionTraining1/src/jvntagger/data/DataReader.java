/*
    Copyright (C) 2009, Cam-Tu Nguyen, Xuan-Hieu Phan
    
    Email:	ncamtu@gmail.com; pxhieu@gmail.com
    URL:	http://www.hori.ecei.tohoku.ac.jp/~hieuxuan
    
    Graduate School of Information Sciences,
    Tohoku University
*/

package jvntagger.data;

import java.util.List;


//return list of sentences
abstract public class DataReader {
	public abstract List<Sentence> readFile(String datafile);
	
	public abstract List<Sentence> readString(String dataStr);
}
