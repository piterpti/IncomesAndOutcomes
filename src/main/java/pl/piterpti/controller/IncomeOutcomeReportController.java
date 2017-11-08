package pl.piterpti.controller;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import pl.piterpti.constants.Constants;
import pl.piterpti.model.DateFromTo;
import pl.piterpti.model.Income;
import pl.piterpti.model.Operation;
import pl.piterpti.model.OperationsPerDay;
import pl.piterpti.model.Outcome;
import pl.piterpti.model.User;
import pl.piterpti.service.UserService;

@Controller
public class IncomeOutcomeReportController {
	
	private Logger logger = Logger.getLogger(IncomeOutcomeReportController.class);
	
	public static final String VIEW_OPERATIONS_REPORT = "outcomesIncomesReport";
	
	@Autowired
	private UserService userService;
	
	private SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
	
	@RequestMapping(value = "report", method = RequestMethod.GET)
	public ModelAndView goToIndex(String sortBy, String from, String to, String order) {
		ModelAndView mav = new ModelAndView();
		
		if (order != null && from != null && to != null) {
			// show report in selected order
			return showIncomeOutcomeReportOrderBy(sortBy, from, to, order);
		}
		
		
		mav.setViewName("redirect:/index");
		return mav;
	}
	
	private ModelAndView showIncomeOutcomeReportOrderBy(String sortBy, String from, String to, String order) {
		ModelAndView mav = new ModelAndView();
		
		Date fromDate = null;
		Date toDate = null;
		
		try {
			
			fromDate = sdf.parse(from);
			toDate = sdf.parse(to);
			
		} catch (ParseException e) {
			logger.warn("Wrong format date: ", e);
		}
		
		if (fromDate != null && toDate != null) {
			
			DateFromTo dft = new DateFromTo(fromDate, toDate);
			
			return showIncomeDateReport(dft, null, sortBy, order);
			
			
		} else {
			mav = goToIndex(null, null, null, null);
		}
		
		return mav;
	}
	
	@RequestMapping(value = "report", method = RequestMethod.POST)
	public ModelAndView showIncomeDateReport(DateFromTo dft, BindingResult bindingResult, String sortBy, String order) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName(VIEW_OPERATIONS_REPORT);
		
		if (order == null) {
			order = "asc";
		} else if (order.equals("asc")) {
			order = "desc";
		} else {
			order = "asc";
		}
		
		 mav.addObject("order", order);
		
		if (dft == null) {
			return ErrorController.getErrorMav("Date period is null");
		}
				
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String userName = auth.getName();

		User user = userService.findByLogin(userName);
		
		Date toDate = new Date(dft.getToDate().getTime() + TimeUnit.DAYS.toMillis(1));
		
		List<Operation> operations = new ArrayList<>(userService.findUserOutcomesInDate(user.getId(), dft.getFromDate(), toDate));
		operations.addAll(userService.findUserIncomesInDate(user.getId(), dft.getFromDate(), toDate));
		
		if (sortBy == null) {
			sortBy = "date";
		}
		
		final String tmpSortBy = sortBy;
		final String tmpOrder = order;
		
		Collections.sort(operations, new Comparator<Operation>() {

			@Override
			public int compare(Operation o1, Operation o2) {
				int o = tmpOrder.equals("asc") ? -1 : 1;
				
				if ("date".equals(tmpSortBy)) { 
					if (o1.getDate().before(o2.getDate())) {
						return 1 * o;
					} else {
						return -1 * o;
					}
				}
				
				if ("value".equals(tmpSortBy)) {
					return o1.getValue().compareTo(o2.getValue()) * o;
				}
				
				if ("category".equals(tmpSortBy)) {
					return o1.getCategory().getName().compareTo(o2.getCategory().getName()) * o;
				}
				
				return 0;
			}
		});;
		
		if (!operations.isEmpty()) {
			
			List<OperationsPerDay> opds = new ArrayList<>();
			List<Operation> operationList = new ArrayList<>();
			BigDecimal summary = new BigDecimal("0");
			
			for (Operation operation : operations) {
				
				if (operation instanceof Outcome) {
					
					summary = summary.subtract(operation.getValue());
					
				} else if (operation instanceof Income) {
					
					summary = summary.add(operation.getValue());
				}
				
				if (operationList.isEmpty()) {
					
					operationList.add(operation);
					
				} else {
					
					if (operation.getDate().getTime() == operationList.get(operationList.size() - 1).getDate()
							.getTime()) {
						
						operationList.add(operation);						
						
					} else {
						
						OperationsPerDay opd = new OperationsPerDay();
						opd.setDate(operationList.get(0).getDate());
						opd.setOperations(operationList);
						
						opds.add(opd);
						
						operationList = new ArrayList<>();
						operationList.add(operation);
					}
				}
			}
			
			if (!operationList.isEmpty()) {
				OperationsPerDay opd = new OperationsPerDay();
				opd.setDate(operationList.get(0).getDate());
				opd.setOperations(operationList);
				
				opds.add(opd);
			}
			
			String summaryStr = summary.toString() + " " + Constants.CURRENCY;
			
			mav.addObject("opds", opds);
			mav.addObject("summary", summaryStr);
		}
		
		mav.addObject("datePeriod", dft);
		return mav;
	}

}
