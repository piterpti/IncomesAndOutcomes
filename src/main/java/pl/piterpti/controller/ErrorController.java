package pl.piterpti.controller;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import pl.piterpti.constants.Constants;

@Controller
public class ErrorController {
	
	// bad request
	public static final int ERROR_BAD_REQUEST = 404;

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
	
	/**
	 * Get error view with message to display on error page
	 * @param msgContent message to display
	 * @return mav
	 */
	public static ModelAndView getErrorMav(String msgContent) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName(Constants.VIEW_ERROR);
		if (msgContent != null) {
			mav.addObject(Constants.PARAM_ERROR_MSG, msgContent);
		}
		return mav;
	}
	
	public static ModelAndView getErrorPage(int code) {
		
		if (code == ERROR_BAD_REQUEST) {
			return getErrorMav("Bad request(404)");
		} else {
			return getErrorMav("Internal server error (500)");
		}
		
		
	}
	
	
}
