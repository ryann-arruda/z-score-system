package entities.repository;

import java.util.Map;

import entities.ZscoreTable;

public interface ZscoreTableDao {
	Map<String, ZscoreTable> findAll();
}