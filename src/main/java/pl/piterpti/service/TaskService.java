package pl.piterpti.service;

import java.util.Date;
import java.util.List;

import pl.piterpti.model.Task;

public interface TaskService {
	
	/**
	 * Finds all tasks
	 * @return list of all tasks
	 */
	public List<Task> findAll();
	
	/**
	 * Find user tasks with limit
	 * @param login user login
	 * @return list of user tasks
	 */
	public List<Task> getUserTasksWithLimit(String login, int limit);
	
	/**
	 * Find user todays tasks
	 * @param login user login
	 * @return list of user tasks
	 */
	public List<Task> getUserTodayTasks(String login);
	
	/**
	 * Add task to DB
	 * @param task task to add
	 * @return operation result
	 */
	public Task saveTask(Task task);
	
	/**
	 * Update task in db
	 * @param task task to update
	 * @return operation result
	 */
	public Task updateTask(Task task);
	
	/**
	 * Find taks by id
	 * @param id task id
	 * @return found task or null
	 */
	public Task findTaskById(long id);
	
	/**
	 * Get user tasks in passed date period
	 * @param login user login
	 * @param fromDate from date
	 * @param toDate to date
	 * @return list of user task in date period
	 */
	public List<Task> getUserTasksInDatePeriod(String login, Date fromDate, Date toDate);
}
