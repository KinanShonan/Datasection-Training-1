/*
    Copyright (C) 2009, Cam-Tu Nguyen, Xuan-Hieu Phan
    
    Email:	ncamtu@gmail.com; pxhieu@gmail.com
    URL:	http://www.hori.ecei.tohoku.ac.jp/~hieuxuan
    
    Graduate School of Information Sciences,
    Tohoku University
*/

package jvntagger.data;

import java.util.List;
import java.util.Map;
 
abstract public class DataWriter {
	
	public abstract void writeFile(List lblSeqs, String filename);
	
	public abstract String writeString(List lblSeqs);
}
