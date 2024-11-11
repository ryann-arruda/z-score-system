package repository;

import java.util.List;

import entities.Child;

public interface ChildDao {
	boolean insert(Child obj);
	boolean update(Child obj);
	boolean deleteById(Integer id);
	Child findById(Integer id);
	List<Child> findAll();
}
