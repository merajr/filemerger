package project.reader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import com.google.common.base.Charsets;

import project.domain.Record;
import project.util.Constants;

public class HTMLReader implements IReader {

	/**
	 * Reads from the HTMl file. The HTML file must contain a table with
	 * ID=directory. The parsing uses UTF_8 encoding scheme to parse data in
	 * multiple languages.
	 */
	@Override
	public void read(String fileName, Map<Integer, Record> persons, List<String> columns) throws IOException {
		File htmlFile = new File(fileName);
		if (!htmlFile.exists()) {
			throw new IOException("The html file:" + fileName + " does not exist. Skipping..");
		}
		System.out.println("HTMLReader is now going to read and parse " + fileName);

		// parsing the whole HTML file with the charset UTF_8
		Document doc = Jsoup.parse(htmlFile, Charsets.UTF_8.name());

		Element table = doc.select(Constants.HTML_TABLE).first();
		String tid = table.id();
		int rowIndex = 1;
		int idIndex = 0;
		if (tid.equals(Constants.HTML_TABLE_ID)) {
			Elements elements = table.getElementsByTag(Constants.HTML_TABLE_ROW);
			List<String> newColumns = new ArrayList<String>();
			// we can safely assume that the th tag is always the first tag in an HTML
			// table, containing column names.
			Elements columnsNodes = elements.get(0).children();
			int i = 0;
			for (Element columnNode : columnsNodes) {
				String columnName = getText(columnNode);
				if (columnName.equals(Constants.ID)) {
					idIndex = i;
				} else {
					i++;
				}
				newColumns.add(columnName);
			}

			i = 0;
			while (rowIndex < elements.size()) {
				Elements row = elements.get(rowIndex++).children();
				Element idValue = row.get(idIndex);
				String idStr = getText(idValue);
				int id = Integer.parseInt(idStr);
				Record record;
				if (persons.containsKey(id)) {
					record = persons.get(id);
				} else {
					record = new Record(id);
					persons.put(id, record);
				}

				for (Element value : row) {
					String newColumn = newColumns.get(i++);
					// duplicate columns are avoided
					if (!record.getHeads().contains(newColumn)) {
						record.setHead(newColumn);
						record.setValue(getText(value));
					}
				}
				i = 0;
			}
			for (String column : newColumns) {
				if (!columns.contains(column)) {
					columns.add(column);
				}
			}
		}
	}

	// it is possible that a td tag contains data or other tags. The code goes
	// through all the inner tags and extracts the data.
	private String getText(Element value) {
		List<Node> temps = new ArrayList<Node>();
		temps.addAll(value.childNodes());
		StringBuilder textBuilder = new StringBuilder();
		while (!temps.isEmpty()) {
			Node node = temps.remove(0);
			if (node instanceof TextNode) {
				textBuilder.append(((TextNode) node).text());
			} else {
				temps.addAll(0, node.childNodes());
			}
		}
		return textBuilder.toString();
	}
}
