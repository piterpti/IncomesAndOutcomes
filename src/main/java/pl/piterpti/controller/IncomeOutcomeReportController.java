package pl.piterpti.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
	
	public static final String VIEW_OPERATIONS_REPORT = "outcomesIncomesReport";
	
	@Autowired
	private UserService userService;
	
	@RequestMapping(value = "report", method = RequestMethod.POST)
	public ModelAndView showIncomeDateReport(DateFromTo dft, BindingResult bindingResult) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName(VIEW_OPERATIONS_REPORT);
		
		if (dft == null) {
			return ErrorController.getErrorMav("Date period is null");
		}
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String userName = auth.getName();

		User user = userService.findByLogin(userName);
		
		Date toDate = new Date(dft.getToDate().getTime() + TimeUnit.DAYS.toMillis(1));
		
		List<Operation> operations = new ArrayList<>(userService.findUserOutcomesInDate(user.getId(), dft.getFromDate(), toDate));
		operations.addAll(userService.findUserIncomesInDate(user.getId(), dft.getFromDate(), toDate));
		
		Collections.sort(operations, new Comparator<Operation>() {

			@Override
			public int compare(Operation o1, Operation o2) {
				if (o1.getDate().before(o2.getDate())) {
					return 1;
				} else {
					return -1;
				}
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
