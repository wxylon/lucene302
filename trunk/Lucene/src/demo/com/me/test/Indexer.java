package com.me.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

/**
 * @author ht
 * 
 */
public class Indexer {
	private static String INDEX_DIR = "D:\\test\\index";
	private static String DATA_DIR = "D:\\test\\small\\";

	public static void main(String[] args) throws Exception {
		long start = new Date().getTime();
		int numIndexed = index(new File(INDEX_DIR), new File(DATA_DIR));
		long end = new Date().getTime();

		System.out.println("Indexing " + numIndexed + " files took "
				+ (end - start) + " milliseconds");
	}

	/**
	 * 索引dataDir下.txt文件，并储存在indexDir下，返回索引的文件数量
	 * 
	 * @param indexDir
	 * @param dataDir
	 * @return
	 * @throws IOException
	 */
	public static int index(File indexDir, File dataDir) throws IOException {
		if (!dataDir.exists() || !dataDir.isDirectory()) {
			throw new IOException(dataDir
					+ " does not exist or is not a directory");
		}
		
		IndexWriter writer = new IndexWriter(FSDirectory.open(indexDir),
				new StandardAnalyzer(Version.LUCENE_CURRENT), true,
				IndexWriter.MaxFieldLength.LIMITED);
		indexDirectory(writer, dataDir);
		int numIndexed = writer.numDocs();
		writer.optimize();
		writer.close();
		return numIndexed;
	}

	/**
	 * 循环遍历dir下的所有.txt文件并进行索引
	 * 
	 * @param writer
	 * @param dir
	 * @throws IOException
	 */
	private static void indexDirectory(IndexWriter writer, File dir)
			throws IOException {

		File[] files = dir.listFiles();

		for (int i = 0; i < files.length; i++) {
			File f = files[i];
			if (f.isDirectory()) {
				indexDirectory(writer, f); // recurse
			} else if (f.getName().endsWith(".txt")) {
				indexFile(writer, f);
			}
		}
	}

	/**
	 * 对单个txt文件进行索引
	 * 
	 * @param writer
	 * @param f
	 * @throws IOException
	 */
	private static void indexFile(IndexWriter writer, File f)
			throws IOException {

		if (f.isHidden() || !f.exists() || !f.canRead()) {
			return;
		}

		System.out.println("Indexing " + f.getCanonicalPath());
		// String text = loadFileToString(f);
		Document doc = new Document();
		// doc.add(new Field("contents",text,Field.Store.YES,
		// Field.Index.ANALYZED));
		doc.add(new Field("contents", new FileReader(f)));
		doc.add(new Field("filename", f.getCanonicalPath(), Field.Store.YES, Field.Index.ANALYZED));
		writer.addDocument(doc);
	}

	/**
	 * 文本转化为String
	 * 
	 * @param file
	 * @return String
	 */
	private static String loadFileToString(File file) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			StringBuffer sb = new StringBuffer();
			String line = br.readLine();
			while (null != line) {
				sb.append(line);
				line = br.readLine();
			}
			br.close();
			return sb.toString();
		} catch (FileNotFoundException e) {
			System.out.println("file not found !");
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
