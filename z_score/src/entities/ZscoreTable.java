package entities;

import java.util.HashMap;

public class ZscoreTable {
	private HashMap<Long, ZscoreTableRow> rows;
	
	public ZscoreTable() {
		rows = new HashMap<>();
	}
	
	public void addRow(Long month, ZscoreTableRow zscoreTableRow) {
		rows.put(month, zscoreTableRow);
	}
	
	public ZscoreTableRow getRow(Long month) {
		return rows.get(month);
	}
	
	@Override
	public String toString() {
		return "ZscoreTable [" + rows.size() + " rows]";
	}
}
