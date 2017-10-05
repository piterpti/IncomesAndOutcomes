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

@Controller
public class RegistrationController {

	@Autowired
	private UserService userService;

	@RequestMapping(value = "/registration", method = RequestMethod.GET)
	public ModelAndView registration() {
		ModelAndView modelAndView = new ModelAndView();

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		if (auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)) {

			modelAndView.setViewName("index");

		} else {

			modelAndView.setViewName("registration");

			User user = new User();

			modelAndView.addObject("user", user);
		}

		return modelAndView;
	}

	@RequestMapping(value = "/registration", method = RequestMethod.POST)
	public ModelAndView createNewUser(@Valid User user, BindingResult bindingResult) {
		ModelAndView modelAndView = new ModelAndView();
		User userExist = userService.findByLogin(user.getLogin());
		if (userExist != null) {

			bindingResult.rejectValue("login", "error.user",
					"There is already user registered with the login provided");
		}

		if (bindingResult.hasErrors()) {

			modelAndView.setViewName("registration");

		} else {

			userService.saveUser(user);
			modelAndView.addObject("successMessage", "User has been registered succesfully");
			modelAndView.setViewName("registration");

		}

		return modelAndView;
	}

}
