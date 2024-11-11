package repository;

import java.util.Set;

import entities.School;

public interface SchoolDao {
	boolean insert(School obj);
	boolean update(School obj);
	boolean deleteById(Integer id);
	School findById(Integer id);
	Set<School> findAll();
}
