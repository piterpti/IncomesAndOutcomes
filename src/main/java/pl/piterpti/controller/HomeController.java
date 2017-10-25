package pl.piterpti.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import pl.piterpti.toolkit.Toolkit;

@Controller
public class HomeController {

	public static final String VIEW_HOME = "index";
	
	@RequestMapping(value = {"/home", "/", "/index"}, method = RequestMethod.GET)
	public ModelAndView home() {
		ModelAndView mav = new ModelAndView();
		
		mav.setViewName(VIEW_HOME);
		mav.addObject("datePeriod", Toolkit.getDatePeriodToForm());
		
		return mav;
	}
	
	
}
