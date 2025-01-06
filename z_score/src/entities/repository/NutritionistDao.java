package entities.repository;

import entities.Nutritionist;

public interface NutritionistDao {
	Long insert(Nutritionist obj);
	boolean update(Nutritionist obj);
	boolean deleteById(Long id);
	Nutritionist findById(Long id);
}
