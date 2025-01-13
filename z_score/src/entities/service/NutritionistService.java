package entities.service;

import entities.Nutritionist;
import entities.repository.DaoFactory;
import entities.repository.NutritionistDao;

public class NutritionistService {
	
	private NutritionistDao nutritionistDaoImpl = DaoFactory.createNutritionistDao();
	
	public Long save(Nutritionist nutritionist) {
		return nutritionistDaoImpl.insert(nutritionist);
	}
}
