package pl.piterpti.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import pl.piterpti.model.Category;
import pl.piterpti.model.Income;
import pl.piterpti.model.User;
import pl.piterpti.service.CategoryService;
import pl.piterpti.service.IncomeService;
import pl.piterpti.service.UserService;
import pl.piterpti.toolkit.Toolkit;

@Controller
public class IncomesController {
	
	public static final String VIEW_USER_INCOMES = "incomes/userIncomes";
	public static final String VIEW_DATE_INCOMES = "incomes/incomesDateReport";
	public static final String VIEW_ADD_INCOME = "incomes/addIncome";
	
	private static final int MAX_INCOMES_TO_DISPLAY = 10;
	
	private Logger logger = Logger.getLogger(IncomesController.class);
	
	@Autowired
	private IncomeService incomeService;
	
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private UserService userService;
	
	
	@RequestMapping(value = "/incomes/userIncomes", method = RequestMethod.GET)
	public ModelAndView userIncomes() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName(VIEW_USER_INCOMES);
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String userName = auth.getName();

		Pageable topResults = new PageRequest(0, MAX_INCOMES_TO_DISPLAY);

		List<Income> incomes = incomeService.findUserIncomesWithLimit(userName, topResults);
		
		if (incomes.isEmpty()) {
			// show msg that there is no outcomes
			modelAndView.addObject("message", "There is not any outcomes for user " + userName);

		} else {

			// ok

			List<Income> tmpIncomes = incomes;
			if (incomes.size() > MAX_INCOMES_TO_DISPLAY) {
				tmpIncomes = new ArrayList<>();

				int i = 0;
				int idx = incomes.size() - 1;
				while (i++ < MAX_INCOMES_TO_DISPLAY) {
					tmpIncomes.add(incomes.get(idx--));
				}
			}

			modelAndView.addObject("incomes", tmpIncomes);

		}

		return modelAndView;
	}
	
	@RequestMapping(value = "incomes/incomesDateReport", method = RequestMethod.GET)
	public ModelAndView prepareIncomesDateReport() {
		ModelAndView mav = new ModelAndView();
		
		mav.setViewName(VIEW_USER_INCOMES);
		mav.addObject("datePeriod", Toolkit.getDatePeriodToForm());
		
		return mav;
	}
	
	@RequestMapping(value = "incomes/incomesDateReport", method = RequestMethod.POST)
	public ModelAndView showIncomeDateReport() {
		ModelAndView mav = new ModelAndView();
		
		return mav;
	}
	
	@RequestMapping(value = "incomes/addIncome", method = RequestMethod.GET)
	public ModelAndView addIncomeView() {
		ModelAndView mav = new ModelAndView();
		
		Income income = new Income();
		income.setDate(new Date());
		
		mav.addObject("income", income);
		
		List<Category> categories = categoryService.findAll();
		mav.addObject("categories", categories);
		
		
		mav.setViewName(VIEW_ADD_INCOME);
		return mav;
	}
	
	@RequestMapping(value = "incomes/addIncome", method = RequestMethod.POST)
	public ModelAndView addIncome(@Valid Income income, BindingResult bindingResult) {
		ModelAndView mav = new ModelAndView();
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String userName = auth.getName();

		User user = userService.findByLogin(userName);
		if (user == null) {

			mav.setViewName(VIEW_ADD_INCOME);

		} else {
			
			if (income != null && income.getCategory() != null) {
				
				Category category = categoryService.findByName(income.getCategory().getName());
				if (category != null) {
					income.setCategory(category);
				}
				
			}
			
			List<Income> incomes = user.getIncomes() != null ? user.getIncomes() : new ArrayList<>();
			incomes.add(income);
			
			user.setIncomes(incomes);
			
			incomeService.addIncome(income);
			
			userService.updateUser(user);
			
			mav.addObject("incomes", incomes);
			mav.setViewName("redirect:/" + VIEW_USER_INCOMES);
			
		}
		
		return mav;
	}
	
	

}
