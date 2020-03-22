package com.taydavid.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletionException;

import com.google.common.collect.Table;
import com.google.common.collect.TreeBasedTable;
import com.google.common.collect.Table.Cell;
import com.taydavid.model.DocumentCollection;

import au.com.bytecode.opencsv.CSVWriter;

public class Utils {

	private static final String FILENAME_COMBINED = "combined.csv";
	private static final String OUTPUT_FOLDER = "output/";

	private static Table<String, String, String> sFinalTable = TreeBasedTable.create();

	private Utils() {

	}

	/**
	 * Get the unique column ID from a List
	 * 
	 * @param columnKeys list of column Strings. Header representation
	 * @return idCol column position of ID
	 */
	public static int getUniqueIDCol(List<String> columnKeys) {
		int idCol = columnKeys.indexOf(Constants.ID_COLUMN);
		if (idCol == -1 || idCol != columnKeys.lastIndexOf(Constants.ID_COLUMN)) {
			throw new CompletionException("Multiple unique identifier columns detected in document.", new Throwable());
		}
		return idCol;
	}

	/**
	 * Write combined csv file with all consolidated document data tables
	 */
	public static void writeMergedCSV() {

		for (Map.Entry<String, Table<String, String, String>> mapElement : DocumentCollection.sTableMap.entrySet()) {
			sFinalTable.putAll(mapElement.getValue());
		}

		List<String> firstRowHeaderList = new ArrayList<>(DocumentCollection.sFirstRowHeaderSet);
		Collections.sort(firstRowHeaderList);
		firstRowHeaderList.remove(Constants.ID_COLUMN);
		firstRowHeaderList.add(0, Constants.ID_COLUMN);
		new File(OUTPUT_FOLDER).mkdirs();
		try (CSVWriter writer = new CSVWriter(new OutputStreamWriter(
				new FileOutputStream(OUTPUT_FOLDER + FILENAME_COMBINED), StandardCharsets.UTF_8))) {
			String[] header = firstRowHeaderList.toArray(new String[0]);
			writer.writeNext(header);
			List<String> tempHeader = new ArrayList<>(firstRowHeaderList);
			tempHeader.remove(Constants.ID_COLUMN);
			List<String> rowToWrite = new ArrayList<>();
			String cellRowKey = "";
			for (Cell<String, String, String> cell : sFinalTable.cellSet()) {
				if (!cellRowKey.equals(cell.getRowKey())) {
					writer.writeNext(rowToWrite.toArray(new String[0]));
					rowToWrite.clear();
					rowToWrite.add(cell.getRowKey());
					tempHeader = new ArrayList<>(firstRowHeaderList);
					tempHeader.remove(Constants.ID_COLUMN);
				}
				cellRowKey = cell.getRowKey();
				for (Iterator<String> i = tempHeader.iterator(); i.hasNext();) {
					String next = i.next();
					if (next.equals(cell.getColumnKey())) {
						rowToWrite.add(cell.getValue());
						i.remove();
						break;
					} else {
						rowToWrite.add("");
						i.remove();
					}
				}
			}
			writer.writeNext(rowToWrite.toArray(new String[0]));
			System.out.println("Merged CSV file output: " + OUTPUT_FOLDER + FILENAME_COMBINED);
		} catch (FileNotFoundException fe) {
			throw new CompletionException("FileNotFoundException occured during CSV file writing", fe);
		} catch (IOException e) {
			throw new CompletionException("IOException occured during CSV file writing", e);
		}
	}
}
