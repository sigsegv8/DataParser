package com.taydavid.dto;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.google.common.collect.Table;

public class MergedCollection {

	private Set<String> mHeader = ConcurrentHashMap.newKeySet();
	private Map<String, Table<String, String, String>> mTableMap = new ConcurrentHashMap<>();

	public Set<String> getHeader() {
		return mHeader;
	}

	public Map<String, Table<String, String, String>> getTableMap() {
		return mTableMap;
	}

}
