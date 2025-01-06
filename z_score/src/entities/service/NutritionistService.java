package entities.service;

import entities.Nutritionist;
import entities.repository.DaoFactory;
import entities.repository.NutritionistDao;

public class NutritionistService {
	
	private NutritionistDao nutritionistDaoImpl = DaoFactory.createNutritionistDao();
	
	public void save(Nutritionist nutritionist) {
		nutritionistDaoImpl.insert(nutritionist);
	}
}
