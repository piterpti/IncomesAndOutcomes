package pl.piterpti.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import pl.piterpti.model.Category;
import pl.piterpti.service.CategoryService;

@Controller
public class CategoryController {

	private static final String VIEW_CATEGORIES = "/categories/categories";
	private static final String VIEW_ADD_CATEGORY = "/categories/addCategory";
	
	@Autowired
	private CategoryService categoryService;
	
	/**
	 * Shows all categories
	 * @return mav
	 */
	@RequestMapping(value = "/categories", method = RequestMethod.GET)
	public ModelAndView getCategories() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName(VIEW_CATEGORIES);
		
		List<Category> categories = categoryService.findAll();
		
		mav.addObject("categories", categories);
		
		return mav;
	}
	
	
	@RequestMapping(value = "/categories/addCategory", method = RequestMethod.GET)
	public ModelAndView addCategory() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName(VIEW_ADD_CATEGORY);
		
		Category category = new Category();
		
		mav.addObject("category", category);
		
		return mav;
	}
	
	@RequestMapping(value = "/categories/addCategory", method = RequestMethod.POST)
	public ModelAndView addCategory(@Valid Category category) {
		ModelAndView mav = new ModelAndView();
		
		if (category.getName() == null || category.getName().isEmpty()) {
			mav = addCategory();
			mav.addObject("errorMessage", "Category name can not be empty");
			return mav;
		}
		
		Category categoryExist = categoryService.findByName(category.getName());
		
		if (categoryExist != null) {
			mav = addCategory();
			mav.addObject("errorMessage", "Category \"" + category.getName() + "\" already exist");
			return mav;
		}
		
		categoryService.save(category);
		mav = getCategories();
		
		return mav;
	}
	
	@RequestMapping(value = "/categories/deleteCategory", method = RequestMethod.GET)
	public ModelAndView deactivateCategory(@Param("id") long id) {
		ModelAndView mav = new ModelAndView();
		
		// TODO categories should be assigned to users..
//		User user = OperationToolkit.getLoggedUser();
		
		if (id < 1) {
			return ErrorController.getErrorMav("Wrong category id (" + id + ")");
		}
		
		Category category = categoryService.findById(id);
		
		if (category == null) {
			return ErrorController.getErrorMav("Category not found");
		}
		
		category.setActive(false);
		
		categoryService.update(category);
		
		mav = getCategories();
		
		mav.setViewName("redirect:/categories");
		return mav;
	}
	
	@RequestMapping(value = "/categories/activateCategory", method = RequestMethod.GET)
	public ModelAndView activateCategory(@Param("id") long id) {
		ModelAndView mav = new ModelAndView();
		
		// TODO categories should be assigned to users..
//		User user = OperationToolkit.getLoggedUser();
		
		if (id < 1) {
			return ErrorController.getErrorMav("Wrong category id (" + id + ")");
		}
		
		Category category = categoryService.findById(id);
		
		if (category == null) {
			return ErrorController.getErrorMav("Category not found");
		}
		
		category.setActive(true);
		
		categoryService.update(category);
		
		mav = getCategories();
		
		mav.setViewName("redirect:/categories");
		return mav;
	}
}
