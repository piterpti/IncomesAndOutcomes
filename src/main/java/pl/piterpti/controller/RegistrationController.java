package pl.piterpti.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import pl.piterpti.model.User;
import pl.piterpti.service.UserService;
import pl.piterpti.toolkit.Toolkit;

@Controller
public class RegistrationController {

	private static final String ACTIVE_REGISTRATION = "activeRegistration";
	
	@Autowired
	private UserService userService;

	@RequestMapping(value = "/registration", method = RequestMethod.GET)
	public ModelAndView registration() {
		ModelAndView modelAndView = new ModelAndView();

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		if (auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)) {

			modelAndView.setViewName("redirect:/index");
			modelAndView.addObject(HomeController.ACTIVE_HOME, "active");

		} else {

			modelAndView.setViewName("registration");

			User user = new User();

			modelAndView.addObject("user", user);
			modelAndView.addObject(ACTIVE_REGISTRATION, "active");
		}

		return modelAndView;
	}

	@RequestMapping(value = "/registration", method = RequestMethod.POST)
	public ModelAndView createNewUser(@Valid User user, BindingResult bindingResult) {
		ModelAndView modelAndView = new ModelAndView();
		User userExist = userService.findByLogin(user.getLogin());

		modelAndView.setViewName("registration");
		modelAndView.addObject("", "active");
		
		if (user.getLogin() == null || user.getLogin().isEmpty()) {
			modelAndView.addObject("errorMessage", "Field login can not be empty");
			return modelAndView;
		}
		
		if (user.getPassword() == null || user.getPassword().length < 1) {
			modelAndView.addObject("errorMessage", "Password can not be empty");
			return modelAndView;
		}
		
		if (user.getUserName() == null || user.getUserName().isEmpty()) {
			modelAndView.addObject("errorMessage", "Username can not be empty");
			return modelAndView;
		}
		
		if (userExist != null) {

			modelAndView.addObject("errorMessage", "There is user with login " + userExist.getLogin() + " already");
			return modelAndView;
		}

		if (!Toolkit.validatePassword(String.valueOf(user.getPassword()))) {
			
			modelAndView.addObject("errorMessage", "Password must be min 6 chars length and contains minimum one digit!");
			return modelAndView;
			
		}
		
		if (bindingResult.hasErrors()) {

			modelAndView.addObject("errorMessage", "Error when user adding");
			return modelAndView;

		} else {

			userService.saveUser(user);
			modelAndView.addObject("successMessage", "User has been registered succesfully");
			modelAndView.setViewName("redirect:/login");
		}

		return modelAndView;
	}

}
