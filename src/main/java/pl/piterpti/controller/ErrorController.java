package pl.piterpti.controller;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ErrorController {

	public static final String VIEW_ERROR = "error"; 
	
	@RequestMapping(value = "/error", method = RequestMethod.GET)
	public ModelAndView showError() {
		ModelAndView modelAndView = new ModelAndView();
		
		modelAndView.addObject("errorMsg", "Wrong request");

		modelAndView.setViewName(VIEW_ERROR);
		return modelAndView;
	}
	
	/**
	 * Mapping for errors
	 * @param errorMsg Error msg may be be null
	 * @return view
	 */
	@RequestMapping(value = "/error", method = RequestMethod.POST)
	public ModelAndView showError(@Param("errorMsg") String errorMsg) {
		ModelAndView modelAndView = new ModelAndView();
		
		modelAndView.setViewName(VIEW_ERROR);
		
		if (errorMsg == null) {
			
			errorMsg = "Unknown error";
			
		}
		
		modelAndView.addObject("errorMsg", errorMsg);
		
		return modelAndView;
	}
	
	
}
