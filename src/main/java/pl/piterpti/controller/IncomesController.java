package pl.piterpti.controller;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import pl.piterpti.constants.Constants;
import pl.piterpti.model.Category;
import pl.piterpti.model.DateFromTo;
import pl.piterpti.model.Income;
import pl.piterpti.model.Operation;
import pl.piterpti.model.OperationsPerDay;
import pl.piterpti.model.User;
import pl.piterpti.service.CategoryService;
import pl.piterpti.service.IncomeService;
import pl.piterpti.service.UserService;
import pl.piterpti.toolkit.OperationToolkit;
import pl.piterpti.toolkit.Toolkit;

@Controller
public class IncomesController {
	
	public static final String VIEW_USER_INCOMES = "incomes/userIncomes";
	public static final String VIEW_DATE_INCOMES = "incomes/incomesDateReport";
	public static final String VIEW_ADD_INCOME = "incomes/addIncome";
	public static final String VIEW_EDIT_INCOME = "incomes/editIncome";
	
	private static final String ACTIVE_INCOMES = "activeIncomes";
	
	/**
	 * Maximum operation number to display in user incomes page
	 */
	private static final int MAX_INCOMES_TO_DISPLAY = 25;
	
	private Logger logger = Logger.getLogger(IncomesController.class);
	
	@Autowired
	private IncomeService incomeService;
	
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private UserService userService;
		
	@RequestMapping(value = "/incomes/userIncomes", method = RequestMethod.GET)
	public ModelAndView userIncomes(@Param("page") Integer page) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject(ACTIVE_INCOMES, "active");
		modelAndView.setViewName(VIEW_USER_INCOMES);
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String userName = auth.getName();

		if (page == null) {
			page = new Integer(1);
		}
		
		long count = incomeService.userIncomesCount(userName);
		
		Pageable topResults = new PageRequest(page.intValue() - 1, MAX_INCOMES_TO_DISPLAY);

		List<Income> incomes = incomeService.findUserIncomesWithLimit(userName, topResults);
		
		if (incomes.isEmpty()) {
			// show msg that there is no outcomes
			modelAndView.addObject("message", "There is not any incomes for user " + userName);

		} else {

			// ok
			modelAndView.addObject("incomes", incomes);

			int pagesCount = (int) (Math.ceil((double) count / MAX_INCOMES_TO_DISPLAY));

			List<Integer> pages = new ArrayList<>();
			if (pagesCount < 6) {
				for (int i = 1; i <= pagesCount; i++) {
					pages.add(i);
				}

				modelAndView.addObject("pages", pages);
			} else {
				modelAndView.addObject("lastPage", pagesCount);
				int startIdx = page.intValue() - 1;
				if (startIdx < 2) {
					startIdx = 2;
				}
				if (startIdx >= pagesCount - 3) {
					startIdx = (int) (pagesCount - 3);
				}

				for (int i = startIdx; i < startIdx + 3; i++) {
					pages.add(i);
					if (i + 1 >= pagesCount) {
						break;
					}
				}
				modelAndView.addObject("selectedPages", pages);

				if (pages.get(0) > 2) {
					modelAndView.addObject("prefix", "...");
				}

				if (pages.size() == 3 && pages.get(pages.size() - 1) + 1 < pagesCount) {
					modelAndView.addObject("suffix", "...");
				}

			}
		}

		modelAndView.addObject("currentPage", page.intValue());

		return modelAndView;
	}
	
	@RequestMapping(value = "incomes/incomesDateReport", method = RequestMethod.GET)
	public ModelAndView prepareIncomesDateReport() {
		ModelAndView mav = new ModelAndView();
		
		mav.setViewName(VIEW_DATE_INCOMES);
		mav.addObject("datePeriod", Toolkit.getDatePeriodToForm());
		mav.addObject(ACTIVE_INCOMES, "active");
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String userName = auth.getName();
		
		long count = incomeService.userIncomesCount(userName);
		
		if (count < 1) {
			mav.addObject("errorMessage", "There is not any intcomes. Try add some");
		}
		
		return mav;
	}
	
	@RequestMapping(value = "incomes/incomesDateReport", method = RequestMethod.POST)
	public ModelAndView showIncomeDateReport(DateFromTo dft, BindingResult bindingResult) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName(VIEW_DATE_INCOMES);
		mav.addObject(ACTIVE_INCOMES, "active");

		if (bindingResult.hasErrors()) {
			logger.warn("Binding result has errors when generating outcomes report");
		}

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String userName = auth.getName();

		User user = userService.findByLogin(userName);
		
		Date toDate = new Date(dft.getToDate().getTime() + TimeUnit.DAYS.toMillis(1));
		List<Income> incomes = incomeService.findUserIncomesInDate(user.getId(), dft.getFromDate(), toDate);
		
		
		
		if (!incomes.isEmpty()) {
			
			List<OperationsPerDay> opds = new ArrayList<>();
			List<Operation> incomeList = new ArrayList<>();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			
			BigDecimal summary = new BigDecimal("0");
			
			for (Income income : incomes) {

				summary = summary.add(income.getValue());
				
				if (incomeList.isEmpty()) {

					incomeList.add(income);

				} else {

					Date firstDate = income.getDate();
					Date secondDate = incomeList.get(incomeList.size() - 1).getDate();
					
					if (sdf.format(firstDate).equals(sdf.format(secondDate))) {
						
						incomeList.add(income);
						
					} else {
						
						OperationsPerDay opd = new OperationsPerDay();
						opd.setDate(incomeList.get(0).getDate());
						opd.setOperations(incomeList);
						
						opds.add(opd);
						
						incomeList = new ArrayList<>();
						incomeList.add(income);
					}

				}

			}
			
			if (!incomeList.isEmpty()) {
				OperationsPerDay opd = new OperationsPerDay();
				opd.setDate(incomeList.get(0).getDate());
				opd.setOperations(incomeList);
				
				opds.add(opd);
				
			}

			mav.addObject("opds", opds);
			
			String summaryStr = summary.toString() + " " + Constants.CURRENCY;
			
			mav.addObject("summary", summaryStr);
			mav.addObject("operationsByCat", OperationToolkit.getOperationsSortedByCateogryValue(incomes));
		}

		
		mav.addObject("datePeriod", dft);
		return mav;

	}
	
	@RequestMapping(value = "incomes/addIncome", method = RequestMethod.GET)
	public ModelAndView addIncomeView() {
		ModelAndView mav = new ModelAndView();
		mav.addObject(ACTIVE_INCOMES, "active");
		
		Income income = new Income();
		income.setDate(new Date());
		
		mav.addObject("income", income);
		
		List<Category> categories = categoryService.findUserActiveCategories(Toolkit.getLoggerUserName());
		mav.addObject("categories", categories);
		
		
		mav.setViewName(VIEW_ADD_INCOME);
		return mav;
	}
	
	@RequestMapping(value = "incomes/addIncome", method = RequestMethod.POST)
	public ModelAndView addIncome(@Valid Income income, BindingResult bindingResult) {
		ModelAndView mav = new ModelAndView();
		mav.addObject(ACTIVE_INCOMES, "active");
		
		String errorMsg = OperationToolkit.validateOperation(income);
		
		if (errorMsg != null) {
			mav = addIncomeView();
			mav.addObject("errorMessage", errorMsg);
			if (income != null) {
				mav.addObject("income", income);
			}
			return mav;
		}
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String userName = auth.getName();

		User user = userService.findByLogin(userName);
		if (user == null) {

			mav.setViewName(VIEW_ADD_INCOME);

		} else {
			
			if (income != null && income.getCategory() != null) {
				
				Category category = categoryService.findUserCategoryByName(userName, income.getCategory().getName());
				if (category != null) {
					income.setCategory(category);
				}
				
			}
			
			List<Income> incomes = user.getIncomes() != null ? user.getIncomes() : new ArrayList<>();
			incomes.add(income);
			
			user.setIncomes(incomes);
			
			incomeService.save(income);
			
			userService.updateUser(user);
			
			mav.addObject("incomes", incomes);
			mav.setViewName("redirect:/" + VIEW_USER_INCOMES);
			
		}
		
		return mav;
	}
	
	@RequestMapping(value = "incomes/editIncome", method = RequestMethod.GET)
	public ModelAndView editIncome(@Param("id") long id) {
		ModelAndView mav = new ModelAndView();
		mav.addObject(ACTIVE_INCOMES, "active");
		
		mav.setViewName(VIEW_EDIT_INCOME);
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String userName = auth.getName();

		User user = userService.findByLogin(userName);
		
		if (id < 1) {
			return ErrorController.getErrorMav("Wrong income id");
		}
		
		Income income = incomeService.findById(id);
		
		if (income == null) {
			return ErrorController.getErrorMav("Income with id " + id + " not found");
		}
		
		boolean userOutcome = false;
		for (Income o : user.getIncomes()) {
			if (o.getId() == income.getId()) {
				userOutcome = true;
			}
		}
		
		if (!userOutcome) {
			logger.warn("Warning: wrong income id (" + id + ") for user " + user.getId() + " - " + user.getLogin());
			return ErrorController.getErrorMav("You do not have access to edit note with id " + id);
		}

		mav.addObject("income", income);
		
		List<Category> categories = categoryService.findUserActiveCategories(Toolkit.getLoggerUserName());	
		mav.addObject("categories", categories);
		
		return mav;
	}
	
	@RequestMapping(value = "incomes/editIncome", method = RequestMethod.POST)
	public ModelAndView editIncome(@Param("income") @Valid Income income) {
		ModelAndView mav = new ModelAndView();
		mav.addObject(ACTIVE_INCOMES, "active");
		
		// validate income
		String errorMsg = OperationToolkit.validateOperation(income);
		
		if (errorMsg != null) {
			mav = editIncome(income.getId());
			mav.addObject("errorMessage", errorMsg);
			mav.addObject("income", income);
			return mav;
		}
		
		if (income.getCategory() != null) {
			Category category = categoryService.findUserCategoryByName(Toolkit.getLoggerUserName() ,income.getCategory().getName());
			
			if (category != null) {
				income.setCategory(category);
			}
		}
		
		if (income != null) {
			incomeService.save(income);
		}
		
		mav.setViewName("redirect:/" + VIEW_USER_INCOMES);
		
		return mav;
	}
	
	@RequestMapping(value = "incomes/deleteIncome")
	public ModelAndView deleteIncome(@Param("id") long id, @Param("date") String date) {
		ModelAndView mav = new ModelAndView();
		mav.addObject(ACTIVE_INCOMES, "active");

		String userName = Toolkit.getLoggerUserName();
		
		User user = userService.findByLogin(userName);

		if (id < 1) {
			return ErrorController.getErrorMav("Wrong operation to delete");
		}
		
		if (!user.hasOperation(id)) {
			return ErrorController.getErrorMav("User " + userName + " cannot delete selected income.");
		}
		
		if (date != null) {
			mav.setViewName("redirect:/" + VIEW_DATE_INCOMES);
		} else {
			mav.setViewName("redirect:/" + VIEW_USER_INCOMES);
		}		
		
		incomeService.deleteById(id);
		
		return mav;
	}
}
