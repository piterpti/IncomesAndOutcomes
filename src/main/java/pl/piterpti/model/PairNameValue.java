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
	
	

}
