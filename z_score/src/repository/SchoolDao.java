package repository;

import java.util.Set;

import entities.School;

public interface SchoolDao {
	Long insert(School obj);
	boolean update(School obj);
	boolean deleteById(Long id);
	School findById(Long id);
	Set<School> findAll();
}
