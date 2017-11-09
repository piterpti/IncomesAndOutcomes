package pl.piterpti.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AccountController {

	private static final String VIEW_ACCOUNT = "/account";
	
	@RequestMapping(value = "/account", method = RequestMethod.GET)
	public ModelAndView getAccountPage() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName(VIEW_ACCOUNT);
		
		return mav;
	}
	
}
