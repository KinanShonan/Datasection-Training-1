/*
    Copyright (C) 2009 by
    
    Xuan-Hieu Phan, Cam-Tu Nguyen
	
	Email:	pxhieu@gmail.com; ncamtu@gmail.com
	
	Graduate School of Information Sciences,
	Tohoku University
*/

package jflexcrf;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import jvntagger.data.DataReader;
import jvntagger.data.DataWriter;
import jvntagger.data.Sentence;
import jvntagger.data.TaggingData;


public class Labeling {
	
	//-----------------------------------------------
	// Member Variables
	//-----------------------------------------------
	private String modelDir = "";	
	public Maps taggerMaps = null;
	public Dictionary taggerDict = null;
	private FeatureGen taggerFGen = null;
	private Viterbi taggerVtb = null;
	private Model taggerModel = null;
	
	private TaggingData dataTagger = null;
	private DataReader dataReader = null;
	private DataWriter dataWriter = null;
	
	//-----------------------------------------------
	// Initilization
	//-----------------------------------------------
	public Labeling (String modelDir, TaggingData dataTagger, 
			DataReader dataReader, DataWriter dataWriter){
		init(modelDir);
		this.dataTagger = dataTagger;
		this.dataWriter = dataWriter;
		this.dataReader = dataReader;		
	}
	
	public boolean init(String modelDir) {
		this.modelDir = modelDir;
		
		Option taggerOpt = new Option(this.modelDir);
		if (!taggerOpt.readOptions()) {
			return false;
		}

		taggerMaps = new Maps();
		taggerDict = new Dictionary();
		taggerFGen = new FeatureGen(taggerMaps, taggerDict);
		taggerVtb = new Viterbi();

		taggerModel = new Model(taggerOpt, taggerMaps, taggerDict, taggerFGen,
				taggerVtb);
		if (!taggerModel.init()) {
			System.out.println("Couldn't load the model");
			System.out.println("Check the <model directory> and the <model file> again");
			return false;
		}
		return true;
	}
	
	public void setDataReader (DataReader reader){
		dataReader = reader;
	}
	
	public void setDataTagger(TaggingData tagger){
		dataTagger = tagger;
	}
	
	public void setDataWriter(DataWriter writer){
		dataWriter = writer;
	}
	
	//---------------------------------------------------------
	// labeling methods
	//---------------------------------------------------------
	/**
	 * labeling observation sequences 
	 * @param data list of sequences with specified format which can be read by DataReader
	 * @return a list of sentences with tags annotated
	 */
	public List seqLabeling(String data){
		List<Sentence> obsvSeqs = dataReader.readString(data);
		return labeling(obsvSeqs);
	}	
	
	/**
	 * labeling observation sequences 
	 * @param data list of sequences with specified format which can be read by DataReader
	 * @return  a list of sentences with tags annotated
	 */
	public List seqLabeling(File file){
		List<Sentence> obsvSeqs = dataReader.readFile(file.getPath());
		return labeling(obsvSeqs);
	}
	
	/**
	 * labeling observation sequences 
	 * @param file contains a list of observation sequence, this file has a format wich can be read by DataReader
	 * @return string representing label sequences, the format is specified by writer
	 */
	public String strLabeling(String data){
		List lblSeqs = seqLabeling(data);
		String ret = dataWriter.writeString(lblSeqs);
		return ret;
	}
	
	/**
	 * labeling observation sequences 
	 * @param file contains a list of observation sequence, this file has a format wich can be read by DataReader
	 * @return string representing label sequences, the format is specified by writer
	 */
	public String strLabeling(File file){
		List<Sentence> obsvSeqs = dataReader.readFile(file.getPath());
		List lblSeqs = labeling(obsvSeqs);
		String ret = dataWriter.writeString(lblSeqs);
		return ret;
	}
	
	private List labeling(List<Sentence> obsvSeqs){
		List labelSeqs = new ArrayList();
		
		for (int i = 0; i < obsvSeqs.size(); ++i){//ith sentence
			List sequence = new ArrayList();
			Sentence sentence = obsvSeqs.get(i);
			
			for (int j = 0; j < sentence.size(); ++j){//jth observation
				Observation obsv = new Observation();
				obsv.originalData = sentence.getWordAt(j);
				
				String [] strCps = dataTagger.getContext(sentence, j);
				
				ArrayList<Integer> tempCpsInt = new ArrayList<Integer>();

				for (int k = 0; k < strCps.length; k++) {
					Integer cpInt = (Integer) taggerMaps.cpStr2Int.get(strCps[k]);
					if (cpInt == null) {
						continue;
					}
					tempCpsInt.add(cpInt);
				}
				
				obsv.cps = new int[tempCpsInt.size()];
				for (int k = 0; k < tempCpsInt.size(); ++k){
					obsv.cps[k] = tempCpsInt.get(k).intValue();
				}
				sequence.add(obsv);
			}
			
			labelSeqs.add(sequence);
		}
		
		taggerModel.inferenceAll(labelSeqs);	
		
		//assign labels to list of sentences
		for (int i = 0; i < obsvSeqs.size(); ++i){
			Sentence sent = obsvSeqs.get(i);
			List seq = (List) labelSeqs.get(i);
			
			for (int j = 0; j < sent.size(); ++j){
				Observation obsrv = (Observation) seq.get(j);			
				String label = (String) taggerMaps.lbInt2Str.get(obsrv.modelLabel);
				
				sent.getTWordAt(j).setTag(label);
			}
		}
		
		return obsvSeqs;
	}
	
}
