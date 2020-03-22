package com.taydavid.model;

import com.taydavid.dto.MergedCollection;

public interface DocumentCollection {

	void read(final String file, final MergedCollection mc);
}
