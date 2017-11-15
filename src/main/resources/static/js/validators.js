function addOperationValidate(operation) {
	
	var value = document.getElementById("Value").value;
	var date = document.getElementById("Date").value;
	var category = document.getElementById("Category").value;;
	var desc = document.getElementById("ShortDescription").value;
	
	var datePattern =/^([0-9]{2})-([0-9]{2})-([0-9]{4})$/;

	var text;
	if (isEmpty(value)) {
		text = operation + " value can not be empty!";
	} else if (isNaN(value)) {
		text = operation + " value must be number";
	} else if (isEmpty(date)) {
		text = operation + " date can not be empty";
	} else if (!datePattern.test(date)) {
		text = operation + " date should be in format DD-MM-YYYY";
	} else if (isEmpty(Category)) {
		text = operation + " category can not be empty";
	} else if (isEmpty(desc)) {
		text = operation + " description can not be empty";
	}
	
	if (! isEmpty(text)) {
		document.getElementById("jsValidator").innerHTML = text;
		return false;
	} else {
		return true
	}
}

function addOutcomeValidate() {
	var value = document.getElementById("Value").value;
	var date = document.getElementById("Date").value;
	var category = document.getElementById("Category").value;;
	var desc = document.getElementById("ShortDescription").value;
	
	var datePattern =/^([0-9]{2})-([0-9]{2})-([0-9]{4})$/;
}

function isEmpty(str) {
    return (!str || 0 === str.length);
}