package project.reader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.common.base.Charsets;

import au.com.bytecode.opencsv.CSVReader;
import project.domain.Record;
import project.util.Constants;

public class CSVFileReader implements IReader {

	/**
	 * Reads from the csv file. The csv file must contain first row as column names
	 * and must include an ID column, anywhere. The no of csvs in each row should be
	 * equal.The parsing uses UTF_8 encoding scheme to parse data in multiple
	 * languages.
	 */
	@Override
	public void read(String fileName, Map<Integer, Record> persons, List<String> columns) throws IOException {
		File csvFile = new File(fileName);
		if (!csvFile.exists()) {
			throw new IOException("The csv file:" + fileName + " does not exist. Skipping..");
		}
		System.out.println("CSVFileReader is now going to read and parse " + fileName);

		InputStreamReader in = null;
		CSVReader csvR = null;
		try {
			// parsing the whole csv file with the charset UTF_8
			in = new InputStreamReader(new FileInputStream(fileName), Charsets.UTF_8.name());

			csvR = new CSVReader(in);

			List<String> newColumns = new ArrayList<String>();
			String[] line = null;
			int lineIndex = 0;
			int idIndex = 0;
			do {
				line = csvR.readNext();
				if (line != null) {
					if (lineIndex == 0) {
						int i = 0;
						for (String csv : line) {
							if (csv.equals(Constants.ID)) {
								idIndex = i;
							} else {
								i++;
							}
							newColumns.add(csv);
						}
					} else {
						String idStr = line[idIndex];
						int id = Integer.parseInt(idStr);
						Record record;
						if (persons.containsKey(id)) {
							record = persons.get(id);
						} else {
							record = new Record(id);
							persons.put(id, record);
						}
						int i = 0;
						for (String value : line) {
							String newColumn = newColumns.get(i++);
							// duplicate columns are avoided
							if (!record.getHeads().contains(newColumn)) {
								record.setHead(newColumn);
								record.setValue(value);
							}
						}
						i = 0;

					}
					lineIndex++;
				}
			} while (line != null);

			for (String column : newColumns) {
				if (!columns.contains(column)) {
					columns.add(column);
				}
			}

		} finally {
			if (in != null) {
				in.close();
			}
			if (csvR != null) {
				csvR.close();
			}
		}
	}

}
