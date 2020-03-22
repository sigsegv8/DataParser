package com.taydavid.document;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletionException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.google.common.collect.Table;
import com.google.common.collect.TreeBasedTable;
import com.taydavid.model.DocumentCollection;
import com.taydavid.utils.Constants;
import com.taydavid.utils.Utils;

/**
 * Class to represent HTML document and operations
 * 
 * @author tayda
 *
 */
public class HTMLDocument implements DocumentCollection {
	private Table<String, String, String> mTable;
	private static final String HTML_TAG_TABLE = "table";
	private static final String HTML_TAG_TABLE_ROW = "tr";
	private static final String HTML_TAG_TABLE_CELL = "td";

	/**
	 * Read a HTML table and create a Guava table representation of items within the
	 * HTML table
	 * 
	 * @param file HTML file name
	 * @throws CompletionException
	 */
	@Override
	public void read(final String file) {
		Document doc;
		File input = new File(Constants.BASE_DATA_FOLDER + file);
		List<String> columnKeys = new ArrayList<>();
		mTable = TreeBasedTable.create();
		try {
			doc = Jsoup.parse(input, null);

			Elements table = doc.getElementsByTag(HTML_TAG_TABLE);
			table.forEach(tbody -> {
				Elements rowElements = tbody.getElementsByTag(HTML_TAG_TABLE_ROW);
				rowElements.get(0).getAllElements().stream().skip(1).forEach(e -> columnKeys.add(e.text()));

				int idCol = Utils.getUniqueIDCol(columnKeys);

				rowElements.stream().skip(1).forEach(row -> {
					Elements tdElements = row.getElementsByTag(HTML_TAG_TABLE_CELL);
					for (String colKey : columnKeys) {
						if (colKey.equals(Constants.ID_COLUMN)) {
							continue;
						}
						mTable.put(tdElements.get(idCol).text(), colKey,
								tdElements.get(columnKeys.indexOf(colKey)).text());
					}
				});
			});
		} catch (IOException e) {
			throw new CompletionException("Exception occured while parsing input file with Jsoup.", e);
		}

		sFirstRowHeaderSet.addAll(columnKeys);
		sTableMap.put(file, mTable);
	}

}
