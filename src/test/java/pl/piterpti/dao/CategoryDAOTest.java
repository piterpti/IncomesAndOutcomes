package pl.piterpti.dao;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import pl.piterpti.model.Category;
import pl.piterpti.service.CategoryService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CategoryDAOTest {

	@Autowired
	private CategoryService categoryService;
	
	@Test
	@Transactional
	public void testSaveCategory() {
		
		List<Category> categories = categoryService.findAll();
		long startSize = categories.size();
		
		Category category = new Category();
		category.setName("Test category");
		categoryService.save(category);
		
		category = new Category();
		category.setName("Test category2");
		categoryService.save(category);
		
		category = new Category();
		category.setName("Test category3");
		categoryService.save(category);
		
		categories = categoryService.findAll();
		
		assertEquals(startSize + 3, categories.size());
		
		category = categoryService.findByName("Test category2");
		
		assertEquals("Test category2", category.getName());
		
		categoryService.deleteByName("Test category3");
		
		categories = categoryService.findAll();
		assertEquals(startSize + 2, categories.size());
		
	}
}
