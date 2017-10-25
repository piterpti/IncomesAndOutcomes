package pl.piterpti.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import pl.piterpti.model.DateFromTo;
import pl.piterpti.toolkit.Toolkit;

@Controller
public class HelloController {

	@RequestMapping(value = "/hello", method = RequestMethod.GET)
	public ModelAndView hello() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("hello");
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
		String name = auth.getName();
		
		modelAndView.addObject("username", name);
		
		return modelAndView;
	}
	
	
}
