package repository;

import java.util.List;

import entities.Nutritionist;

public interface NutritionistDao {
	boolean insert(Nutritionist obj);
	boolean update(Nutritionist obj);
	boolean deleteById(Integer id);
	Nutritionist findById(Integer id);
	List<Nutritionist> findAll();
}
