package project.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * The domain class to hold Person's record data. It implements Comparable to support ID based sorting.
 * @author Meraj
 *
 */
public class Record implements Comparable<Record> {

	private int id;

	//contains columns
	private List<String> heads;

	//contains data or rows
	private List<String> values;

	/**
	 * Returns a Record object with ID populated. ID must be a positive number.
	 * @param pId record ID
	 */
	public Record(int pId) {

		if (pId < 0) {
			throw new IllegalArgumentException("Record's ID must be a valid positive number.");
		}
		id = pId;
		heads = new ArrayList<String>();
		values = new ArrayList<String>();
	}

	public List<String> getValues() {
		return values;
	}

	public void setValues(String... pValues) {
		for (String field : pValues) {
			this.values.add(field);
		}
	}

	public void setValue(String value) {
		values.add(value);
	}

	public List<String> getHeads() {
		return heads;
	}

	public void setHeads(String... headValues) {
		for (String head : headValues) {
			heads.add(head);
		}
	}

	public void setHead(String head) {
		heads.add(head);
	}

	public int getId() {
		return id;
	}

	//to sort the records w.r.t. ID
	@Override
	public int compareTo(Record person) {
		return this.id - person.id;
	}

	@Override
	public String toString() {
		return "Person [id=" + id + ", heads=" + heads + ", fields=" + values + "]";
	}

}
