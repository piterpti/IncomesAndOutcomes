package pl.piterpti.model;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name = "user")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "user_id")
	private long id;
	
	@Column(name = "login")
	@NotEmpty
	private String login;
	
	@Column(name = "password")
	@NotEmpty
	private char[] password;
	
	@Column(name = "user_name")
	private String userName;
	
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles;
	
	@Column(name = "enabled")
	private boolean enabled;
	
	@OneToMany
	@JoinColumn(name = "user_id")
	private List<Outcome> outcomes;
	
	@OneToMany
	@JoinColumn(name = "user_id")
	private List<Income> incomes;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public char[] getPassword() {
		return password;
	}

	public void setPassword(char[] password) {
		this.password = password;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}
	
	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	public List<Outcome> getOutcomes() {
		return outcomes;
	}

	public void setOutcomes(List<Outcome> outcomes) {
		this.outcomes = outcomes;
	}
	
	public List<Income> getIncomes() {
		return incomes;
	}

	public void setIncomes(List<Income> incomes) {
		this.incomes = incomes;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", login=" + login + ", password=" + Arrays.toString(password) + ", userName="
				+ userName + "]";
	}
	
	
	public boolean hasOperation(long operationId) {
		Operation o = new Outcome();
		o.setId(operationId);
		return hasOperation(o);
	}
	
	/**
	 * Verify that passed incomes/outcomes are available for user 
	 * @param operation
	 * @return
	 */
	public boolean hasOperation(Operation operation) {
		if (incomes != null) {
			for (Operation o : incomes) {
				if (o.getId() == operation.getId()) {
					return true;
				}
			}
		}
		
		if (outcomes != null) {
			for (Operation o : outcomes) {
				if (o.getId() == operation.getId()) {
					return true;
				}
			}
		}
		
		return false;
	}
}
