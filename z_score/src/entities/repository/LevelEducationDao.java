package entities.repository;

import java.util.Set;

import entities.LevelEducation;

public interface LevelEducationDao {
	Long insert(LevelEducation obj);
	boolean update(LevelEducation obj);
	boolean deleteById(Long id);
	LevelEducation findById(Long id);
	Set<LevelEducation> findAll();
}
