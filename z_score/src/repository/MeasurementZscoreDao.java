package repository;

import java.util.List;

import entities.MeasurementZscore;

public interface MeasurementZscoreDao {
	boolean insert(MeasurementZscore obj);
	boolean update(MeasurementZscore obj);
	boolean deleteById(Integer id);
	MeasurementZscore findById(Integer id);
	List<MeasurementZscore> findAll();
}
