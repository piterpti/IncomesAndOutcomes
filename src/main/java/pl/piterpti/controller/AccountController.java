package pl.piterpti.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import pl.piterpti.model.User;
import pl.piterpti.service.UserService;
import pl.piterpti.toolkit.Toolkit;

/**
 * Controller for account management 
 * 
 * @author piter 
 *
 */
@Controller
public class AccountController {

	@Autowired
	private UserService userService;
	
	private static final String VIEW_ACCOUNT = "/account";
	
	/**
	 * Get account managment page
	 * @return
	 */
	@RequestMapping(value = "/account", method = RequestMethod.GET)
	public ModelAndView getAccountPage(String oldPassword, String newPassword, String newPasswordRepeat) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName(VIEW_ACCOUNT);
		
		if (oldPassword == null) {
			oldPassword = "";
		}
		if (newPassword == null) {
			newPassword = "";
		}
		if (newPasswordRepeat == null) {
			newPasswordRepeat = "";
		}
		
		mav.addObject("oldPassword", oldPassword);
		mav.addObject("newPassword", newPassword);
		mav.addObject("newPasswordRepeat", newPasswordRepeat);
		
		return mav;
	}
	
	@RequestMapping(value = "/deleteAccount", method = RequestMethod.GET)
	public ModelAndView deleteAccount() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName(VIEW_ACCOUNT);
		
		String loggedUser = Toolkit.getLoggerUserName();
		
		User user = userService.findByLogin(loggedUser);
		
		if (user == null) {
			mav.addObject("errorMsg", "User not found");
		} else if (!user.isEnabled()) {
			mav.addObject("errorMsg", "User already deleted");
		}
		
		userService.deactivateUser(user.getId());
		mav.setViewName("redirect:/logout");
		
		return mav;
	}
	
	@RequestMapping(value = "/changePassword", method = RequestMethod.POST)
	public ModelAndView changePassword(String oldPassword, String newPassword, String newPasswordRepeat) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName(VIEW_ACCOUNT);
		
		if (oldPassword == null || oldPassword.isEmpty() || newPassword == null || newPassword.isEmpty()
				|| newPasswordRepeat == null || newPasswordRepeat.isEmpty()) {
			mav = getAccountPage(oldPassword, newPassword, newPasswordRepeat);
			mav.addObject("changePswdError", "You need to fill all fields");
		} else {
			// TODO validating password
			boolean validateSucces = true;
			
			if (validateSucces) {
				mav.addObject("changePswdMsg", "Password has changed");
			}
		}
		
		return mav;
	}
	
}
