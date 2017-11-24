package pl.piterpti.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import pl.piterpti.model.Task;
import pl.piterpti.repository.TaskRepository;

@Service
public class TaskServiceImpl implements TaskService {

	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	@Autowired
	private TaskRepository taskRepository;
	
	@Override
	public List<Task> findAll() {
		return taskRepository.findAll();
	}

	@Override
	public List<Task> getUserTasksWithLimit(String login, int limit) {
		Pageable pageable = new PageRequest(1, limit);
		return taskRepository.getUserTasksWithLimit(login, pageable);
	}

	@Override
	public List<Task> getUserTodayTasks(String login) {
		return taskRepository.getUserTasksInDate(login, getTaskDate(new Date()));
	}

	@Override
	public Task saveTask(Task task) {
		task.setDate(getTaskDate(task.getDate()));
		return taskRepository.save(task);
	}

	@Override
	public Task updateTask(Task task) {
		task.setDate(getTaskDate(task.getDate()));
		return taskRepository.save(task);
	}

	@Override
	public Task findTaskById(long id) {
		return taskRepository.findOne(id);
	}

	@Override
	public List<Task> getUserTasksInDatePeriod(String login, Date fromDate, Date toDate) {
		return null;
	}
	
	private Date getTaskDate(Date date) {
		try {
			return sdf.parse(sdf.format(date));
		} catch (ParseException e) {
			return null;
		}
	}

	@Override
	public List<Task> getUserTaskById(String login, long id) {
		return taskRepository.getUserTaskById(login, id);
	}
}
