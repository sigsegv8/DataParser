package com.taydavid.dto;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.google.common.collect.Table;

/**
 * Merged Collection is the data transfer object to hold final headers and
 * tables from each read document
 * 
 * @author tayda
 *
 */
public class MergedCollection {

	private Set<String> mHeader = ConcurrentHashMap.newKeySet();
	private Map<String, Table<String, String, String>> mTableMap = new ConcurrentHashMap<>();

	/**
	 * Get headers to use in combined CSV
	 * 
	 * @return mHeader
	 */
	public Set<String> getHeader() {
		return mHeader;
	}

	/**
	 * Get all data tables from read documents
	 * 
	 * @return mTableMap
	 */
	public Map<String, Table<String, String, String>> getTableMap() {
		return mTableMap;
	}

}
