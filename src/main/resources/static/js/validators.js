// date pattern for forms
var datePattern =/^([0-9]{2})-([0-9]{2})-([0-9]{4})$/;

function addOperationValidate(operation) {
	
	var value = document.getElementById("Value").value;
	var date = document.getElementById("Date").value;
	var category = document.getElementById("Category").value;;
	var desc = document.getElementById("ShortDescription").value;
	
	var text;
	if (isEmpty(value)) {
		text = operation + " value can not be empty!";
	} else if (isNaN(value)) {
		text = operation + " value must be number!";
	} else if (isEmpty(date)) {
		text = operation + " date can not be empty!";
	} else if (!datePattern.test(date)) {
		text = operation + " date should be in format DD-MM-YYYY!";
	} else if (isEmpty(Category)) {
		text = operation + " category can not be empty!";
	} else if (isEmpty(desc)) {
		text = operation + " description can not be empty!";
	}
	
	if (! isEmpty(text)) {
		document.getElementById("jsValidator").innerHTML = text;
		return false;
	} else {
		return true
	}
}

function validateReportDates() {
	
	var fromDate = document.getElementById("fromDate").value;
	var toDate = document.getElementById("toDate").value;
	
	
	var text;
	if (isEmpty(fromDate)) {
		text = "Date 'from' can not be empty!";
	} else if (isEmpty(toDate)) {
		text = "Date 'to' can not be empty!";
	} else if (!datePattern.test(fromDate)) {
		text = "Date 'from' should be in format DD-MM-YYYY!";
	} else if (!datePattern.test(toDate)) {
		text = "Date 'to' should be in format DD-MM-YYYY!";
	}
	
	if (! isEmpty(text)) {
		document.getElementById("jsValidator").innerHTML = text;
		return false;
	} else {
		return true
	}
	
}

function validateCategory() {
	var category = document.getElementById("Category").value;
	
	var text;
	if (isEmpty(category)) {
		text = "Category can not be empty!";
	} else if (category.length < 2) {
		text = "Category must be at least 2 characters length!";
	}
	
	if (! isEmpty(text)) {
		document.getElementById("jsValidator").innerHTML = text;
		return false;
	} else {
		return true
	}
}

function validateRegistration() {
	var username = document.getElementById("Username").value;
	var login = document.getElementById("Login").value;
	var password = document.getElementById("Password").value;
	
	var text;
	if (isEmpty(username)) {
		text = "Username can not be empty!";
	} else if (isEmpty(login)) {
		text = "Login can not be empty!";
	} else if (isEmpty(password)) {
		text = "Password can not be empty!";
	} else if (password.length < 6) {
		text = "Password must be at least 6 characters length!";
	} else if (!validatePassword(password)) {
		text = "Password must contain at least 1 digit!";
	}
	
	if (! isEmpty(text)) {
		document.getElementById("jsValidator").innerHTML = text;
		return false;
	} else {
		return true
	}	
}

function validatePassword(password) {
	var matches = password.match(/\d+/g);
	if (matches != null) {
	    return true;
	} else {
		return false;
	}
}

function validatePasswordChange() {
	var oldPassword = document.getElementById("oldPassword").value;
	var newPassword = document.getElementById("newPassword").value;
	var newPasswordRepeat = document.getElementById("newPasswordRepeat").value;
	
	var text;
	if (isEmpty(oldPassword)) {
		text = "Old password can no be empty!";
	} else if (isEmpty(newPassword)) {
		text = "Next password can not be empty!";
	} else if (isEmpty(newPasswordRepeat)) {
		text = "New password repeat can not be empty";
	} else if (newPassword.length < 6) {
		text = "Password must be at least 6 characters length!";
	} else if (!validatePassword(newPassword)) {
		text = "Password must contain at least 1 digit!";
	} else if (newPassword != newPasswordRepeat) {
		text = "Entered password are not equals";
	}
	
	if (!isEmpty(text)) {
		document.getElementById("jsValidator").innerHTML = text;
		return false;
	} else {
		return true
	}
}

function validateTask() {
	
	var title = document.getElementById("Title").value;
	var date = document.getElementById("Date").value;
	var priority = document.getElementById("Priority").value;
	
	var text;
	
	if (isEmpty(title)) {
		text = "Task title can not be empty";
	} else if (isEmpty(date)) {
		text = "Task date can not be empty";
	} else if (!datePattern.test(date)) {
		text = "Date should be in format DD-MM-YYYY!";
	} else if (isEmpty(priority)) {
		text = "Task priority can not be empty";
	}
	
	if (!isEmpty(text)) {
		document.getElementById("jsValidator").innerHTML = text;
		return false;
	} else {
		return true
	}
}

function isEmpty(str) {
    return (!str || 0 === str.length);
}