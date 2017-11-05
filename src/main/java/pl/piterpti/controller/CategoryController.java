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
import pl.piterpti.service.UserService;
import pl.piterpti.toolkit.Toolkit;

@Controller
public class CategoryController {

	private static final String VIEW_CATEGORIES = "/categories/categories";
	private static final String VIEW_ADD_CATEGORY = "/categories/addCategory";
	
	@Autowired
	private UserService userService;
	
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
		
		String userName = Toolkit.getLoggedUser().getLogin();
		
		List<Category> categories = userService.findUserCategories(userName);
		
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
		
		String userName = Toolkit.getLoggedUser().getLogin();
		
		List<Category> categories = userService.findUserCategories(userName);
		
		Category categoryExist = null;
		
		for (Category c : categories) {
			if (c.getName().equals(category.getName())) {
				categoryExist = c;
			}
		}
		
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
		
		if (id < 1) {
			return ErrorController.getErrorMav("Wrong category id (" + id + ")");
		}
		
		String userName = Toolkit.getLoggedUser().getLogin();
		List<Category> categories = userService.findUserCategories(userName);
		
		
		boolean exist = false;
		for (Category cat : categories) {
			if (cat.getId() == id) {
				exist = true;
				break;
			}
		}
		
		if (!exist) {
			return ErrorController.getErrorMav("You do not have permission to delete this category");
		}
		
		
		Category category = categoryService.findById(id);
		if (category == null) {
			return ErrorController.getErrorMav("Category not found");
		}
		
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
		
		// TODO category to user..
		
		categoryService.update(category);
		
		mav = getCategories();
		
		mav.setViewName("redirect:/categories");
		return mav;
	}
}
