package entities.repository;

import java.util.List;

import entities.LevelEducation;

public interface LevelEducationDao {
	Long insert(LevelEducation obj);
	boolean update(LevelEducation obj);
	boolean deleteById(Long id);
	LevelEducation findById(Long id);
	List<LevelEducation> findAll();
}
