import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import vn.hus.nlp.tokenizer.VietTokenizer;
import jvntagger.MaxentTagger;
import jvntagger.POSTagger;

import com.google.gson.Gson;

/**
 * @author myname2
 * 
 */
public class TextProcessor {
	private static final String CSV_FILENAME = "baomoi.tsv";
	private String contentFileName;
	private String outputFileName;
	public Map<String, Integer> verbTable = new HashMap<String, Integer>();

	public TextProcessor() {
		contentFileName = getContentFileName();
		outputFileName = getOutputFileName();
	}

	// Tạo tên file lưu các content
	private String getContentFileName() {
		String fileName;
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		Date date = new Date(); // get system time
		String stringDate = dateFormat.format(date);
		fileName = "baomoi.content." + stringDate + ".txt";
		return fileName;
	}

	// Tạo tên file output
	private String getOutputFileName() {
		String fileName;
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		Date date = new Date(); // get system time
		String stringDate = dateFormat.format(date);
		fileName = "HaiNT_" + stringDate + "_VERB.tsv";
		return fileName;
	}

	// Tách các content lưu vào file content
	public void contentParser() throws IOException {
		PrintWriter output = new PrintWriter(contentFileName);
		BufferedReader br = new BufferedReader(new FileReader(CSV_FILENAME));
		String currentLine;
		String[] tokens = null;
		String content;
		while ((currentLine = br.readLine()) != null) {
			if (!currentLine.equals("\n")) {
				tokens = currentLine.split("\t");
				String json = tokens[3];
				Article art = new Article();
				Gson gson = new Gson();
				art = gson.fromJson(json, Article.class);
				content = art.getContent();
				content = content.replace("\n", "");
				content = content.replace("<strong>", "");
				content = content.replace("</strong>", "");
				content = content.replace("\t", "");
				output.println(content);
			}
		}
		br.close();
		output.close();
	}

	// Lưu các động từ vào Hashmap + đếm số lần xuất hiện
	public void creatVerbTable() throws IOException {
		VietTokenizer tokenizer = new VietTokenizer();
		String modelDir = "model/maxent";
		POSTagger tagger = null;
		tagger = new MaxentTagger(modelDir);
		String currentLine;
		String sentence;
		String taggedSentence;
		String[] taggedWords;
		BufferedReader br = new BufferedReader(new FileReader(contentFileName));

		while ((currentLine = br.readLine()) != null) {
			if (!currentLine.equals("\n")) {
				sentence = tokenizer.segment(currentLine); // Tách content
				taggedSentence = tagger.tagging(sentence.toLowerCase()); // Đánh
																			// tag

				taggedWords = taggedSentence.split(" ");

				for (int i = 0; i < taggedWords.length; i++) {
					String word = taggedWords[i];
					if (word.endsWith("/V")) {
						String verb = word.substring(0, word.length() - 2);
						verb = verb.replace("_", " ");
						if (verbTable.containsKey(verb)) {
							verbTable.put(verb, verbTable.get(verb) + 1);
						} else {
							verbTable.put(verb, 1);
						}
					}
				}
			}
		}
		br.close();
	}

	// In 30 động từ xuất hiện nhiều nhất vào file output
	public void printVerbArray() throws IOException {
		PrintWriter pw = new PrintWriter(outputFileName);
		Map<String, Integer> sortedVerbTable = sortByValues(verbTable);
		int count = 0;
		for (Map.Entry entry : sortedVerbTable.entrySet()) {
			count++;
			pw.println(entry.getKey() + "\t"
					+ processWord((String) entry.getKey()) + "\t"
					+ entry.getValue());
			if (count == 30)
				break;
		}
		pw.close();
	}

	// Chuyển các từ theo định dạng được yêu cầu (viết hoa chữ cái đầu mỗi
	// tiếng)
	private String processWord(String originalWord) {
		StringBuilder changedWord = new StringBuilder(originalWord);
		char ch = changedWord.charAt(0);
		changedWord.setCharAt(0, Character.toUpperCase(ch));
		for (int i = 1; i < changedWord.length() - 1; i++) {
			if (changedWord.charAt(i) == ' ')
				changedWord.setCharAt(i + 1,
						Character.toUpperCase(changedWord.charAt(i + 1)));
		}
		return changedWord.toString();
	}

	// Hàm sắp xếp Hashmap
	/*
	 * Java method to sort Map in Java by value e.g. HashMap or Hashtable throw
	 * NullPointerException if Map contains null values It also sort values even
	 * if they are duplicates
	 */
	public static <K extends Comparable, V extends Comparable> Map<K, V> sortByValues(
			Map<K, V> map) {
		List<Map.Entry<K, V>> entries = new LinkedList<Map.Entry<K, V>>(
				map.entrySet());

		Collections.sort(entries, new Comparator<Map.Entry<K, V>>() {

			@Override
			public int compare(Entry<K, V> o1, Entry<K, V> o2) {
				return o2.getValue().compareTo(o1.getValue());
			}
		});

		// LinkedHashMap will keep the keys in the order they are inserted
		// which is currently sorted on natural ordering
		Map<K, V> sortedMap = new LinkedHashMap<K, V>();

		for (Map.Entry<K, V> entry : entries) {
			sortedMap.put(entry.getKey(), entry.getValue());
		}

		return sortedMap;
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) throws Exception {
		TextProcessor tp = new TextProcessor();

		tp.contentParser();
		tp.creatVerbTable();
		tp.printVerbArray();
	}
}
