package project.writer;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.output.FileWriterWithEncoding;

import com.google.common.base.Charsets;

import au.com.bytecode.opencsv.CSVWriter;
import project.domain.Record;
import project.util.Constants;

public class CSVCombinedWriter {

	private String destFile;

	private List<String> columns;

	public CSVCombinedWriter(String file, List<String> pColumns) {
		destFile = file;
		columns = pColumns;
	}

	/**
	 * Normalizes the data fetched from the input files. It checks each record for
	 * missing column values and puts white space for any missing column. the ID
	 * column should appear at the start.
	 * 
	 * @param persons
	 *            the records
	 */
	public void normalize(Map<Integer, Record> persons) {
		System.out.println("Now the fetched data is being normalized before being sent for output. No of records:"
				+ persons.size());

		normalizeIDColumn();

		Set<Integer> ids = persons.keySet();

		// now checking the records and putting white space for the missing column
		// values in each record
		for (int i = 0; i < columns.size(); i++) {
			String column = columns.get(i);
			for (int id : ids) {
				Record person = persons.get(id);
				List<String> headings = person.getHeads();
				List<String> values = person.getValues();
				if (headings.contains(column)) {
					int index = headings.indexOf(column);
					String value = values.get(index);
					if (index != i) {
						headings.remove(index);
						headings.add(i, column);
						values.remove(index);
						values.add(i, value);
					}
				} else {
					headings.add(i, column);
					values.add(i, Constants.WHITE_SPACE);
				}
			}
		}
	}

	private void normalizeIDColumn() {
		// checking columns to see if ID column is in first position or not.
		int idIndex = columns.indexOf(Constants.ID);
		if (idIndex != 0) {
			columns.remove(idIndex);
			columns.add(0, Constants.ID);
		}
	}

	public void write(List<Record> persons) throws IOException {
		CSVWriter csvW = null;
		FileWriterWithEncoding fw = null;

		String fileName = FilenameUtils.concat(Constants.FILE_DIR, destFile);
		System.out.println("Finally the normalized output is written to the output file:" + fileName);
		File csvFile = new File(fileName);
		if (csvFile.exists()) {
			csvFile.delete();
		}
		csvFile.createNewFile();
		
		try {
			fw = new FileWriterWithEncoding(csvFile, Charsets.UTF_8);
			csvW = new CSVWriter(fw);

			// writing the columns head first
			String[] heads = columns.toArray(new String[0]);
			csvW.writeNext(heads);

			// now writing rows
			String[] values;
			for (Record record : persons) {
				values = record.getValues().toArray(new String[0]);
				csvW.writeNext(values);
			}
			csvW.flush();
		} finally {

			if (fw != null) {
				fw.close();
			}
			if (csvW != null) {
				csvW.close();
			}
		}
	}
}
