import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import project.domain.Record;
import project.reader.IReader;
import project.reader.ReaderFactory;
import project.util.Constants;
import project.writer.CSVCombinedWriter;

public class MergerController {

	/**
	 * A facade based method that takes the names of the input files and the output
	 * file and takes care of the rest of the process. It calls the subsequent logic
	 * to merge the files.
	 * 
	 * @param inputFiles
	 *            the input files
	 * @param destFile
	 *            the output file
	 * @throws Exception
	 *             if there is a problem
	 */
	public static void merge(String inputFiles[], String destFile) throws Exception {

		List<String> columns = new ArrayList<String>();
		Map<Integer, Record> persons = new HashMap<Integer, Record>();

		// For each input file, get the corresponding reader and read the file contents.
		for (String file : inputFiles) {
			IReader reader = ReaderFactory.getReader(file);
			if (reader != null) {
				try {
					reader.read(file, persons, columns);
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				System.out.println("The file " + file + " is not currently supported. Skipping..");
			}
		}

		if (columns.size() == 0) {
			System.out.println("No data is fetched. Exiting..");
			System.exit(1);
		} else {

			// CSVCombinedWriter normalizes the data and writes it to the output file.
			CSVCombinedWriter cvcWriter = new CSVCombinedWriter(destFile, columns);
			cvcWriter.normalize(persons);

			List<Record> records = new ArrayList<Record>();
			records.addAll(persons.values());
			System.out.println("Now sorting the normalized data in ascending order by:" + Constants.ID);
			Collections.sort(records);

			cvcWriter.write(records);

			System.out.println("The files merging is completed.");

		}
	}
}
