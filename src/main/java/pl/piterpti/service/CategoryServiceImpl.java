package pl.piterpti.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.piterpti.model.Category;
import pl.piterpti.repository.CategoryRepository;

@Service
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	private CategoryRepository categoryRepository;
	
	@Override
	public List<Category> findAll() {
		return categoryRepository.findAll();
	}

	@Override
	public void save(Category category) {
		category.setActive(true);
		categoryRepository.save(category);
	}

	@Override
	public Category findByName(String name) {
		return categoryRepository.findByName(name);
	}

	@Override
	public void deleteAll() {
		categoryRepository.deleteAll();
	}

	@Override
	public int deleteByName(String name) {
		return categoryRepository.deleteByName(name);
	}

	@Override
	public Category findById(long id) {
		return categoryRepository.findOne(id);
	}

	@Override
	public void update(Category category) {
		categoryRepository.save(category);		
	}

	@Override
	public List<Category> findActive() {
		return categoryRepository.findByActiveTrue();
	}
	
	
}
