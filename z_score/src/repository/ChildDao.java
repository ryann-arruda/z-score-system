package repository;

import java.util.List;

import entities.Child;

public interface ChildDao {
	Long insert(Child obj);
	boolean update(Child obj);
	boolean deleteById(Long id);
	Child findById(Long id);
	List<Child> findAll();
}
