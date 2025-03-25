package entities;

import java.util.HashMap;

public class ZscoreTable {
	private HashMap<Integer, ZscoreTableRow> rows;
	
	public ZscoreTable() {
		rows = new HashMap<>();
	}
	
	public void addRow(Integer month, ZscoreTableRow zscoreTableRow) {
		rows.put(month, zscoreTableRow);
	}
	
	public ZscoreTableRow getRow(Integer month) {
		return rows.get(month);
	}
	
	@Override
	public String toString() {
		return "ZscoreTable [" + rows.size() + " rows]";
	}
}
