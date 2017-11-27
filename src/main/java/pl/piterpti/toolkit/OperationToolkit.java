package pl.piterpti.toolkit;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.piterpti.model.Operation;
import pl.piterpti.model.PairNameValue;

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
	
	/**
	 * 
	 * @param outcomes
	 * @return Operation by category with summ value
	 */
	public static List<PairNameValue> getOperationsSortedByCateogryValue(List<? extends Operation> outcomes) {
		Map<String, BigDecimal> operationsByCat = new HashMap<>();
		BigDecimal catValue;
		BigDecimal summ = new BigDecimal("0");
		for (Operation o : outcomes) {
			catValue = operationsByCat.get(o.getCategory().getName());
			if (catValue == null) {
				operationsByCat.put(o.getCategory().getName(), o.getValue());
			} else {
				operationsByCat.put(o.getCategory().getName(), catValue.add(o.getValue()));
			}
			summ = summ.add(o.getValue());
		}
		
		List<PairNameValue> result = new ArrayList<>();
		
		BigDecimal percent;
		for (Map.Entry<String, BigDecimal> entry : operationsByCat.entrySet()) {
			PairNameValue pnv = new PairNameValue(entry.getKey(), entry.getValue() + "");
			percent = entry.getValue().divide(summ, MathContext.DECIMAL128).multiply(BigDecimal.valueOf(100));
			pnv.setValue2(percent.setScale(2, RoundingMode.CEILING) + " %");
			result.add(pnv);
		}
		
		Collections.sort(result, new Comparator<PairNameValue>() {

			@Override
			public int compare(PairNameValue pnv1, PairNameValue pnv2) {
				BigDecimal bd1 = new BigDecimal(pnv1.getValue());
				BigDecimal bd2 = new BigDecimal(pnv2.getValue());
				
				return bd2.compareTo(bd1);
			}
			
		});
		
		return result;
	}
}
