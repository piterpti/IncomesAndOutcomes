package pl.piterpti.dao;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import pl.piterpti.model.Task;
import pl.piterpti.service.TaskService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TaskDAOTest {

	@Autowired
	private TaskService taskService;
	
	@Test
	public void testAddSomeTasks() {
		Task task = new Task("title", "description", new Date());
		Task task2 = new Task("title2", "description2", new Date());
		taskService.saveTask(task);
		taskService.saveTask(task2);
	}
	
	
}
