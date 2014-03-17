/*
    Copyright (C) 2009, Cam-Tu Nguyen, Xuan-Hieu Phan
    
    Email:	ncamtu@gmail.com; pxhieu@gmail.com
    URL:	http://www.hori.ecei.tohoku.ac.jp/~hieuxuan
    
    Graduate School of Information Sciences,
    Tohoku University
*/

package jvntagger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.OutputStreamWriter;

public class POSTagging {

	public static void main(String [] args){
		displayCopyright();
        
        if (!checkArgs(args)) {
            displayHelp();
            return;
        }
        
        //get model dir
        String modelDir = args[3];        
        
        //initialize tagger
        POSTagger tagger = null;
        
        if (args[1].equalsIgnoreCase("crfs"))
        	tagger = new CRFTagger(modelDir);
        else if (args[1].equalsIgnoreCase("maxent"))
        	tagger = new MaxentTagger(modelDir);
        
        //tagging
        try {
	        if (args[4].equalsIgnoreCase("-inputfile")){
	        	File inputFile = new File(args[5]);
	        	BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
	        			new FileOutputStream(inputFile.getPath() + ".pos"), "UTF-8"));
	        	
	        	String result = tagger.tagging(inputFile);
	        	
	        	writer.write(result);
	        	writer.close();
	        }
	        else{ //input dir
	        	String inputDir = args[5];
	        	 if (inputDir.endsWith(File.separator)) {
		                inputDir = inputDir.substring(0, inputDir.length() - 1);
		            }
		            
		            File dir = new File(inputDir);
		            String[] children = dir.list(new FilenameFilter() {
		                public boolean accept(File dir, String name) {
		                    return name.endsWith(".seg");
		                }
		            });    
		            
		            for (int i = 0; i < children.length; i++) {
		            	System.out.println("Tagging " + children[i]);
		            	String filename = inputDir + File.separator + children[i];
			                if ((new File(filename)).isDirectory()) {
			                    continue;
			            }
			                
			            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
				        			new FileOutputStream(filename + ".pos"), "UTF-8"));
			             
			            writer.write(tagger.tagging(new File(filename)));
			             
			            writer.close();
		            }
	        }
        }
        catch (Exception e){
        	System.out.println("Error while tagging");
        	System.out.println(e.getMessage());
        }
        
	}
	
	public static boolean checkArgs(String[] args) {
        // case 1: CRFChunker -modeldir <model directory> -inputfile <input data file>
        // case 2: CRFChunker -modeldir <model directory> -inputdir <input data directory>
        
        if (args.length < 5) {
            return false;
        }
        
        if (args[0].compareTo("-tagger") != 0){
        	return false;
        }
        
        if (args[1].compareToIgnoreCase("crfs") != 0 
        		&& args[1].compareToIgnoreCase("maxent") != 0)
        	return false;
        	
        if (args[2].compareToIgnoreCase("-modeldir") != 0) {
            return false;
        }
        
        if (!(args[4].compareToIgnoreCase("-inputfile") == 0 ||
                args[4].compareToIgnoreCase("-inputdir") == 0)) {
            return false;
        }
        
        return true;
    }
	
	public static void displayCopyright() {
        System.out.println("Vietnamese Part-Of-Speech Tagging:");
        System.out.println("\tusing Conditional Random Fields or Maximum Entropy");
        System.out.println("\ttesting on more than 10000 sentences of Viet Treebank with the highest F1-measure of 93.27%");
        System.out.println("Copyright (C) by Cam-Tu Nguyen {1,2} and Xuan-Hieu Phan {2}");
        System.out.println("{1}: College of Technology, Hanoi National University");
        System.out.println("{2}: Graduate School of Information Sciences, Tohoku University");
        System.out.println("Email: {ncamtu@gmail.com ; pxhieu@gmail.com}");
        System.out.println();
    }
    
    public static void displayHelp() {
        System.out.println("Usage:");
        System.out.println("\tCase 1: POSTagging -tagger <crfs/maxent> -modeldir <model directory> -inputfile <input data file>");
        System.out.println("\tCase 2: POSTagging -tagger <crfs/maxent> -modeldir <model directory> -inputdir <input data directory>");
        System.out.println("Where:");
        System.out.println("\t<crfs/maxent> is the tagger used for pos tagging which is either maximum entropy (maxent) or conditional random fields (crfs)");
        System.out.println("\t<model directory> is the directory contain the model and option files");
        System.out.println("\t<input data file> is the file containing input sentences that need to");
        System.out.println("\tbe tagged (each sentence on a line)");
        System.out.println("\t<input data directory> is the directory containing multiple input data files");
        System.out.println();
    }
}
