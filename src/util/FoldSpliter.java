package util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import jvntagger.POSDataReader;
import jvntagger.data.*;



public class FoldSpliter {
	public static void main(String args[]){
		if (args.length != 3){
			System.out.println("Usage: FoldSpliter [inputdir] [outputdir] [number of folds]");
			return;			
		}
		
		int nfold = Integer.parseInt(args[2]);
		try{
			File indir = new File(args[0]);
			if (!indir.isDirectory())
				return;
			
			BufferedWriter [] writers = new BufferedWriter[nfold];
			for (int i = 0; i < writers.length; ++i){			
				writers[i] = new BufferedWriter(new OutputStreamWriter(
						new FileOutputStream(args[1] + File.separator + "test" + i + ".txt"), "UTF-8"));
			}
			
			POSDataReader reader = new POSDataReader(true);			
			String [] children = indir.list(new FilenameFilter() {
                public boolean accept(File dir, String name) {
                    return name.endsWith(".pos");
                }
            });
			// First way for spliting data:
			// each data file is split into folds in successtion
			int ndata = 0;
			for (int i = 0; i < children.length; ++i){
				System.out.println("REad " + children[i]);
				List<Sentence> data = reader.readFile(indir.getPath() + File.separator + children[i]);
				System.out.println("reading " + data.size() + " completed");
				ndata += data.size();
				for (int j = 0; j < data.size(); ++j){
				//	if (j % nfold == 0 && j < nfold) writers[j % nfold].write("################" + children[i] + "\n");
					data.get(j).print(writers[j % nfold]);
				}
			}
			
			for (int i = 0; i < writers.length; ++i)
				writers[i].close();
			
			
			System.out.println("ndata:" + ndata);
			//TESTING ANOTHER WAY FOR SPLITING DATA
			// reading all data, and splited by random
			/*List<Sentence> corpus = new ArrayList<Sentence>();
			for (int i = 0; i < children.length; ++i){
				System.out.println("REad " + children[i]);
				List<Sentence> data = reader.readFile(indir.getPath() + File.separator + children[i]);
				corpus.addAll(data);			
			}
			
			ndata = corpus.size()/writers.length;
			System.out.println(corpus.size());
			for (int i = 0; i < writers.length; ++i){
				for (int j = 0; j < ndata; ++j){
					
					int idx = (int) Math.round(Math.random() * (corpus.size() - 1));
					
					corpus.get(idx).print(writers[i]);
					corpus.remove(idx);
				}
				writers[i].close();
			}*/
		}
		catch (Exception e){
			System.out.println("Error: " + e.getMessage());
			e.printStackTrace();
			return;
		}
	}
}
