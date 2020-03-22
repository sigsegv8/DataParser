package com.taydavid.model;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.google.common.collect.Table;
import com.taydavid.dto.MergedCollection;

public interface DocumentCollection {

	void read(final String file, final MergedCollection mc);
}
