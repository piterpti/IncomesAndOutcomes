package pl.piterpti.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pl.piterpti.model.Category;

@Repository("categoryRepository")
public interface CategoryRepository extends JpaRepository<Category, Long> {

	public Category findByName(String name);
	
	public int deleteByName(String name);
	
}
