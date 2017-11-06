package pl.piterpti.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import pl.piterpti.model.Category;

@Repository("categoryRepository")
public interface CategoryRepository extends JpaRepository<Category, Long> {

	public Category findByName(String name);
		
	/**
	 * Search category in user categories by name
	 * @param login
	 * @param name
	 * @return
	 */
	@Query("SELECT c FROM User u JOIN u.categories c WHERE u.login = :login AND c.name = :category AND c.active = 1")
	public Category findUserCategoryByName(@Param("login") String login, @Param("category") String cateogry);
	

	/**
	 * Find user categories by user
	 * @param login
	 * @return user category list
	 */
	@Query("SELECT c FROM User u JOIN u.categories c WHERE u.login = :login")
	public List<Category> findUserCategories(@Param("login") String login);
	
	
	/**
	 * Find user active categories
	 * @return list of user active categories
	 */
	@Query("SELECT c FROM User u JOIN u.categories c WHERE u.login = :login AND c.active = true")
	public List<Category> findUserActiveCategories(@Param("login") String login);
	
}
