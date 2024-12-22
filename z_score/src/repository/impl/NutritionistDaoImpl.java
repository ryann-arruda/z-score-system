package repository.impl;

import java.sql.Connection;

import entities.Nutritionist;
import repository.NutritionistDao;

public class NutritionistDaoImpl implements NutritionistDao{
	private Connection conn;
	
	public NutritionistDaoImpl(Connection conn) {
		this.conn = conn;
	}

	@Override
	public Long insert(Nutritionist obj) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean update(Nutritionist obj) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteById(Long id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Nutritionist findById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}
}
