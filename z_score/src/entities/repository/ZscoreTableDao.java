package entities.repository;

import java.util.List;

import entities.ZscoreTable;

public interface ZscoreTableDao {
	List<ZscoreTable> findAll();
}