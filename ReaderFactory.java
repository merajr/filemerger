package project.reader;

import project.util.Constants;

public class ReaderFactory {
	/**
	 * Returns the corresponding IReader implementation based on the type of input
	 * file.
	 * 
	 * @param inputFile
	 *            the input file
	 * @return IReader implementation
	 */
	public static IReader getReader(String inputFile) {
		IReader reader = null;
		if (inputFile.endsWith(Constants.HTML_EXTENSION)) {
			reader = new HTMLReader();
		} else if (inputFile.endsWith(Constants.CSV_EXTENSION)) {
			reader = new CSVFileReader();
		}

		return reader;
	}
}
