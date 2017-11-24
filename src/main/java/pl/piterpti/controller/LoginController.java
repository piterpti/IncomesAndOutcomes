package pl.piterpti.controller;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LoginController {
	
	private static final String ACTIVE_LOGIN = "activeLogin";

	@RequestMapping(value = "/login",  method = RequestMethod.GET)
	public ModelAndView login(String successMessage) {
		
		ModelAndView modelAndView = new ModelAndView();
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
		if (auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)) {
			
			// user logged in
			modelAndView.setViewName("redirect:/home");
			
		} else {
			
			// user not logged in
			modelAndView.setViewName("login");
			modelAndView.addObject(ACTIVE_LOGIN, "active");			
			if (successMessage != null && "true".equals(successMessage)) {
				modelAndView.addObject("successMessage", "User has been registered succesfully");
			}
		}
		
		return modelAndView;
	}
	
}
