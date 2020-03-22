package com.taydavid.runner;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.taydavid.document.CSVDocument;
import com.taydavid.document.HTMLDocument;
import com.taydavid.model.DocumentCollection;
import com.taydavid.utils.Constants;
import com.taydavid.utils.Utils;

public class RecordMerger {

	/**
	 * Entry point of this test.
	 *
	 * @param args command line arguments: first.html and second.csv.
	 * @throws Exception bad things had happened.
	 */
	public static void main(final String[] args) throws Exception {
		if (args.length == 0) {
			System.err.println("Usage: java RecordMerger file1 [ file2 [...] ]");
			System.exit(1);
		}

		final Stream<String> fileStreams = Stream.of(args)
				.filter(files -> Pattern.matches(Constants.ACCEPTABLE_FILES_PATTERN_REGEX, files));

		final List<String> acceptableFileTypes = fileStreams.collect(Collectors.toList());
		final List<String> existingFileTypes = new ArrayList<>();

		acceptableFileTypes.forEach(file -> {
			if (Files.exists(Paths.get(Constants.BASE_DATA_FOLDER + file))) {
				existingFileTypes.add(file);
			}
		});

		existingFileTypes.parallelStream().forEach(file -> {
			if (Pattern.matches(Constants.CSV_FILE_PATTERN_REGEX, file)) {
				DocumentCollection csvDoc = new CSVDocument();
				csvDoc.read(file);
			} else if (Pattern.matches(Constants.HTML_FILE_PATTERN_REGEX, file)) {
				DocumentCollection htmlDoc = new HTMLDocument();
				htmlDoc.read(file);
			}
		});

		Utils.writeMergedCSV();
	}
}
