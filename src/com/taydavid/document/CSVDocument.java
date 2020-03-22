package com.taydavid.document;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletionException;

import com.google.common.collect.Iterables;
import com.google.common.collect.Table;
import com.google.common.collect.TreeBasedTable;
import com.taydavid.model.DocumentCollection;
import com.taydavid.utils.Constants;
import com.taydavid.utils.Utils;

import au.com.bytecode.opencsv.CSVReader;

/**
 * Class to represent CSV Document and operations
 * 
 * @author tayda
 *
 */
public class CSVDocument implements DocumentCollection {
	private Table<String, String, String> mTable;

	public void addTable(final String fileName, Table<String, String, String> table) {
		sTableMap.put(fileName, table);
	}

	/**
	 * Read a CSV file and create a Guava table representation of items within the
	 * CSV file
	 * 
	 * @param file CSV file name
	 * @throws CompletionException
	 */
	@Override
	public void read(final String file) {
		List<String> columnKeys = null;
		mTable = TreeBasedTable.create();
		try (CSVReader reader = new CSVReader(
				new InputStreamReader(new FileInputStream(Constants.BASE_DATA_FOLDER + file), StandardCharsets.UTF_8),
				',', '"', 0)) {
			List<String[]> allRows = reader.readAll();
			columnKeys = Arrays.asList(allRows.get(0));
			int idCol = Utils.getUniqueIDCol(columnKeys);
			for (String[] row : Iterables.skip(allRows, 1)) {
				for (String colKey : columnKeys) {
					if (colKey.equals(Constants.ID_COLUMN)) {
						continue;
					}
					mTable.put(row[idCol], colKey, row[columnKeys.indexOf(colKey)]);
				}
			}
		} catch (IOException e) {
			throw new CompletionException("IOException occurred while working on CSV file.", e);
		}

		sFirstRowHeaderSet.addAll(columnKeys);
		sTableMap.put(file, mTable);

	}
}
