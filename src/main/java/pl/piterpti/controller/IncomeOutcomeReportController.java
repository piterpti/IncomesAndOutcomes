package pl.piterpti.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import pl.piterpti.model.DateFromTo;
import pl.piterpti.model.Income;
import pl.piterpti.model.Operation;
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

		List<Operation> operations = new ArrayList<>(userService.findUserOutcomesInDate(user.getId(), dft.getFromDate(), dft.getToDate()));
		operations.addAll(userService.findUserIncomesInDate(user.getId(), dft.getFromDate(), dft.getToDate()));
		
		Collections.sort(operations, new Comparator<Operation>() {

			@Override
			public int compare(Operation o1, Operation o2) {
				if (o1.getDate().before(o2.getDate())) {
					return 1;
				} else {
					return -1;
				}
			}
		});
		
		if (!operations.isEmpty()) {
			// TODO 
			
		}
		
		return mav;
	}

}
