package com.taydavid.tests;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.google.common.collect.Lists;
import com.taydavid.document.CSVDocument;
import com.taydavid.document.HTMLDocument;
import com.taydavid.dto.MergedCollection;

public class DocumentCollectionTest {

	@Test
	public void testHTMLDocumentHeaders() {
		final MergedCollection mc = new MergedCollection();
		final List<String> expectedHeader = Arrays.asList("Address", "PhoneNum", "ID", "Name");
		HTMLDocument htmlDoc = new HTMLDocument();
		htmlDoc.read("first.html", mc);
		assertEquals(expectedHeader, Lists.newArrayList(mc.getHeader()));
	}

	@Test
	public void testCSVDocumentHeaders() {
		final MergedCollection mc = new MergedCollection();
		final List<String> expectedHeader = Arrays.asList("Occupation", "Gender", "ID", "Name");
		CSVDocument csvDoc = new CSVDocument();
		csvDoc.read("second.csv", mc);
		assertEquals(expectedHeader, Lists.newArrayList(mc.getHeader()));
	}

}
