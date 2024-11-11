package repository;

import java.util.List;

import entities.LevelEducation;

public interface LevelEducationDao {
	boolean insert(LevelEducation obj);
	boolean update(LevelEducation obj);
	boolean deleteById(Integer id);
	LevelEducation findById(Integer id);
	List<LevelEducation> findAll();
}
