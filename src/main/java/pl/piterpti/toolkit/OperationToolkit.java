package pl.piterpti.toolkit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import pl.piterpti.model.Operation;
import pl.piterpti.model.User;
import pl.piterpti.service.UserService;

public class OperationToolkit {
	
	@Autowired
	private static UserService userService;
	
	/**
	 * Validating operation before add/edit to db
	 * @return
	 */
	public static String validateOperation(Operation operation) {
		
		if (operation.getValue() == null) {
			return "Value can not be empty";
		}
		
		if (operation.getCategory() == null) {
			return "Category can not be empty";
		}
		
		if (operation.getDate() == null) {
			return "Date can not be empty - should be in format DD-MM-YYYY";
		}
		
		if (operation.getShortDesc() == null || operation.getShortDesc().isEmpty()) {
			return "Description can not be empty";
		}
		
		return null;
	}
	
	public static User getLoggedUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String userName = auth.getName();

		User user = userService.findByLogin(userName);
		
		return user;
	}

}
