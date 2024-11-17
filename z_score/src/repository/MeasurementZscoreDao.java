package repository;

import java.util.List;

import entities.MeasurementZscore;

public interface MeasurementZscoreDao {
	Long insert(MeasurementZscore obj);
	boolean update(MeasurementZscore obj);
	boolean deleteById(Long id);
	MeasurementZscore findById(Long id);
	List<MeasurementZscore> findAll();
}
