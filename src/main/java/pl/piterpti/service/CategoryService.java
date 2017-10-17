package pl.piterpti.service;

import java.util.List;

import pl.piterpti.model.Category;

/**
 * Service for Outcomes categories
 * 
 * @author piter
 *
 */
public interface CategoryService {

	public List<Category> findAll();
	
	public void save(Category category);
	
	public Category findByName(String name);
	
	public void deleteAll();
	
	public int deleteByName(String name);
	
}
