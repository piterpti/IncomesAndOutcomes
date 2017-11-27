package pl.piterpti.model;

/**
 * Pair names & values
 * 
 * @author piter
 *
 */
public class PairNameValue {
	
	private String name;
	private String value;
	private String value2;
	
	public PairNameValue(String name, String value) {
		this.name = name;
		this.value = value;
	}
	
	public PairNameValue() {}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getValue2() {
		return value2;
	}

	public void setValue2(String value2) {
		this.value2 = value2;
	}
}
