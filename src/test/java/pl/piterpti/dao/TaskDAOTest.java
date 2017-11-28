package pl.piterpti.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import pl.piterpti.model.Task;
import pl.piterpti.model.User;
import pl.piterpti.service.TaskService;
import pl.piterpti.service.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TaskDAOTest {

	@Autowired
	private TaskService taskService;
	
	@Autowired
	private UserService userService;
	
	private Random random = new Random();
	
	@SuppressWarnings("unused")
//	@Test
	private void testAddSomeTasks() {
		Task task = new Task("title", "description", new Date());
		Task task2 = new Task("title2", "description2", new Date());
		taskService.saveTask(task);
		taskService.saveTask(task2);
	}
	
	@SuppressWarnings("unused")
//	@Test
	private void testAddRandomTasks() {
		
		int taskCount = 100;
		
		User user = userService.findByLogin("piter");
		
		List<Task> tasks = new ArrayList<Task>();
		
		Task task;
		for (int i = 0; i < taskCount; i++) {
			task = new Task();
			task.setTitle(randomString(random.nextInt(10) + 1));
			task.setDescription(randomString(random.nextInt(20) + 1));
			task.setDate(new Date(new Date().getTime() + random.nextInt((int)TimeUnit.DAYS.toMillis(10))));
			task.setPriority(random.nextInt(3) + 1);
			taskService.saveTask(task);
			tasks.add(task);
		}
		
		user.setTasks(tasks);
		userService.updateUser(user);
		
	}
	
	private String randomString(int length) {
		char[] chars = "abcdefghijklmnopqrstuvwxyz".toCharArray();
		StringBuilder sb = new StringBuilder();
		Random random = new Random();
		for (int i = 0; i < length; i++) {
		    char c = chars[random.nextInt(chars.length)];
		    sb.append(c);
		}
		return sb.toString();
	}
}
