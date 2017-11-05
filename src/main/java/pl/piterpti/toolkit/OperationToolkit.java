package pl.piterpti.toolkit;

import pl.piterpti.model.Operation;

public class OperationToolkit {
	
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
}
