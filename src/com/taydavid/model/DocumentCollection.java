package com.taydavid.model;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.google.common.collect.Table;

public interface DocumentCollection {

	static Set<String> sFirstRowHeaderSet = ConcurrentHashMap.newKeySet();

	static Map<String, Table<String, String, String>> sTableMap = new ConcurrentHashMap<>();

	void read(final String file);
}
