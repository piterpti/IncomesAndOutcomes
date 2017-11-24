package pl.piterpti.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;
/**
 * Task model
 * 
 * @author piter
 *
 */
@Entity
@Table(name = "task")
public class Task {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "task_id")
	private long task_id;

	@Column(name = "title", length = 100)
	private String title;
	
	@Column(name = "description", length = 255)
	private String description;
	
	@Column(name = "date")
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date date;
	
	// estimated time to do task in minutes
	@Column(name = "estimatedTime")
	private int estimatedTime;
	
	@Column(name = "priority")
	private int priority;
	
	@Column(name = "completed")
	private boolean completed;
	
	public Task() {}
	
	public Task(String title, String description, Date date) {
		this.title = title;
		this.description = description;
		this.date = date;
	}
	
	public Task(String title, String description, Date date, int estimatedTime) {
		this.title = title;
		this.description = description;
		this.date = date;
		this.estimatedTime = estimatedTime;
	}

	public long getTask_id() {
		return task_id;
	}

	public void setTask_id(long task_id) {
		this.task_id = task_id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public int getEstimatedTime() {
		return estimatedTime;
	}

	public void setEstimatedTime(int estimatedTime) {
		this.estimatedTime = estimatedTime;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public boolean isCompleted() {
		return completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
	}	
}
