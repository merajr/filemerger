package project.reader;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import project.domain.Record;

public interface IReader {

	/**
	 * Reads the input file and populates person records and the header columns.
	 * 
	 * @param fileName
	 *            input file
	 * @param persons
	 *            Map of Record objects paired with ID
	 * @param columns
	 *            column names
	 * @throws IOException
	 *             if some problem occurs
	 */
	public void read(String fileName, Map<Integer, Record> persons, List<String> columns) throws IOException;

}
