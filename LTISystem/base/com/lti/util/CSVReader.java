package com.lti.util;

import java.io.*;
import java.util.*;

public class CSVReader extends BufferedReader {

	protected String delimiter = ",";

	protected char escape = '\"';

	protected String nowLine = null;

	public CSVReader(Reader in) {
		super(in);
	}

	public CSVReader(Reader in, String d) {
		this(in);
		delimiter = d;
	}

	public CSVReader(Reader in, char d) {
		this(in, String.valueOf(d));
	}

	public CSVReader(Reader in, int sz) {
		super(in, sz);
	}

	public CSVReader(Reader in, int sz, String d) {
		this(in, sz);
		delimiter = d;
	}

	public CSVReader(Reader in, int sz, char d) {
		this(in, sz, String.valueOf(d));
	}

	public String getDelimiter() {
		return delimiter;
	}

	public String readLine() throws IOException {
		nowLine = super.readLine();
		return nowLine;
	}

	public String[] readLineAsArray() throws IOException {
		List<String> v = readLineAsList();
		if (v == null)
			return null;

		String items[] = new String[v.size()];
		for (int i = 0; i < v.size(); i++) {
			items[i] =  v.get(i);
		}

		return items;
	}

	public List<String> readLineAsList() throws IOException {
		String line = readLine();
		if (line == null)
			return null;

		return getCsvItems(line);
	}

	public String getNowLine() {
		return nowLine;
	}

	protected List<String> getCsvItems(String line) {
		List<String> v = new ArrayList<String>();

		int startIdx = 0;
		int searchIdx = -1;

		StringBuffer sbLine = new StringBuffer(line);

		while ((searchIdx = sbLine.toString().indexOf(delimiter, startIdx)) != -1) {
			String buf = null;

			if (sbLine.charAt(startIdx) != escape) {
				buf = sbLine.substring(startIdx, searchIdx);
				startIdx = searchIdx + 1;
			} else {

				int escapeIdx = -1;
				searchIdx = startIdx;
				boolean findDelimiter = false;

				while ((escapeIdx = sbLine.toString().indexOf(escape, searchIdx + 1)) != -1 && sbLine.length() > escapeIdx + 1) {
					char nextChar = sbLine.charAt(escapeIdx + 1);
					if (delimiter.indexOf(nextChar) != -1) {

						buf = sbLine.substring(startIdx + 1, escapeIdx);
						startIdx = escapeIdx + 2;

						findDelimiter = true;
						break;
					}
					if (nextChar == escape) {

						sbLine.deleteCharAt(escapeIdx);
						escapeIdx--;
					}
					searchIdx = escapeIdx + 1;
				}

				if (!findDelimiter) {
					break;
				}
			}

			v.add(buf);
		}

		if (startIdx < sbLine.length()) {
			int lastIdx = sbLine.length() - 1;
			if (sbLine.charAt(startIdx) == escape && sbLine.charAt(lastIdx) == escape) {
				sbLine.deleteCharAt(lastIdx);
				if (startIdx < sbLine.length())sbLine.deleteCharAt(startIdx);
			}
			v.add(sbLine.substring(startIdx, sbLine.length()));
		} else if (startIdx == sbLine.length()) {
			v.add("");
		}

		return v;
	}
}