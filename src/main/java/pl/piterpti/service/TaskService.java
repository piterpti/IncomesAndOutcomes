package pl.piterpti.service;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;

import pl.piterpti.model.Task;

public interface TaskService {
	
	// task priorities human readable
	public static final String[] TASK_PRIORITIES = new String[] { "LOW", "NORMAL", "HIGH" };
	
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
	
	/**
	 * Find all task for passed username and task id
	 * @param login user login
	 * @param id task id
	 * @return list of tasks
	 */
	public List<Task> getUserTaskById(String login, long id);
	
	/**
	 * Count user tasks
	 * @param login user login
	 * @return user task count
	 */
	public long countUserTasks(String login);
	
	
	/**
	 * Gets all user tasks
	 * @param login user login
	 * @param pageable result set
	 * @return
	 */
	public List<Task> findUserTasks(String login, Pageable pageable);
}
