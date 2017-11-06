package pl.piterpti.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import pl.piterpti.model.Task;
import pl.piterpti.repository.TaskRepository;

public class TaskServiceImpl implements TaskService {

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
	public List<Task> getUserTodaysTasks(String login) {
		return taskRepository.getUserTasksInDate(login, new Date());
	}

	@Override
	public Task saveTask(Task task) {
		return taskRepository.save(task);
	}

	@Override
	public Task updateTask(Task task) {
		return taskRepository.save(task);
	}

	@Override
	public Task findTaskById(long id) {
		return taskRepository.findOne(id);
	}

	@Override
	public List<Task> getUserTasksInDatePeriod(String login, Date fromDate, Date toDate) {
		// TODO Auto-generated method stub
		return null;
	}

}
