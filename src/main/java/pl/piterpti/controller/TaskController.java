package pl.piterpti.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import pl.piterpti.model.PairNameValue;
import pl.piterpti.model.Task;
import pl.piterpti.model.User;
import pl.piterpti.service.TaskService;
import pl.piterpti.service.UserService;
import pl.piterpti.toolkit.Toolkit;

@Controller
public class TaskController {

	private static final String VIEW_TASKS = "/tasks/tasks";
	private static final String VIEW_ADD_TASKS = "/tasks/addTask";
	
	private static final String[] TASK_PRIORITIES = new String[] { "LOW", "NORMAL", "HIGH" };

	@Autowired
	private TaskService taskService;
	
	@Autowired
	private UserService userService;

	@RequestMapping(value = "/tasks", method = RequestMethod.GET)
	public ModelAndView getTodaysUserTask() {
		ModelAndView mav = new ModelAndView();

		String userName = Toolkit.getLoggerUserName();

		List<Task> userTodayTasks = taskService.getUserTodayTasks(userName);

		if (userTodayTasks.isEmpty()) {
			mav.addObject("message", "There is not any task for today! Enjoy your day!");
		} else {
			sortTaskByCompletedAndPriority(userTodayTasks);
			mav.addObject("todayTasks", userTodayTasks);
		}

		mav.setViewName(VIEW_TASKS);
		return mav;
	}

	@RequestMapping(value = "tasks/addTask", method = RequestMethod.GET)
	public ModelAndView getAddTaskPage() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName(VIEW_ADD_TASKS);

		Task task = new Task();
		task.setDate(new Date());
		mav.addObject("task", task);
		mav.addObject("priorities", getPriorities());

		return mav;
	}
	
	
	@RequestMapping(value = "task/addTask", method = RequestMethod.POST)
	public ModelAndView addTask(Task task) {
		
		ModelAndView mav = new ModelAndView();
		if (task == null) {
			
			mav = ErrorController.getErrorMav("Error while adding task");
			
		} else {
			
			String taskValidationResult = validateTask(task);
			
			if (taskValidationResult == null) {
				
				User user = userService.findByLogin(Toolkit.getLoggerUserName());
				user.getTasks().add(task);
				
				taskService.saveTask(task);
				userService.updateUser(user);
				mav = getTodaysUserTask();
				mav.addObject("message", "Task added!");
				
			} else {
				
				mav.setViewName(VIEW_ADD_TASKS);
				mav.addObject("errorMessage", taskValidationResult);
				
			}
			
		}
		
		return mav;
	}
	
	@RequestMapping(value = "tasks/changeCompleted", method = RequestMethod.GET)
	public ModelAndView setCompleted(long id) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("redirect:/tasks");
		
		if (id < 1) {
			return ErrorController.getErrorPage(ErrorController.ERROR_BAD_REQUEST);
		}
		
		String userName = Toolkit.getLoggerUserName();
		List<Task> userTasks = taskService.getUserTaskById(userName, id);
		
		if (userTasks == null || userTasks.isEmpty()) {
			return ErrorController.getErrorMav("You do not have permission to delete selected task");
		}
		
		for (Task task : userTasks) {
			task.setCompleted(!task.isCompleted());
			taskService.updateTask(task);
		}
		
		return mav;
	}

	private static List<PairNameValue> getPriorities() {
		List<PairNameValue> priorities = new ArrayList<>();

		int idx = 1;
		for (String s : TASK_PRIORITIES) {
			priorities.add(new PairNameValue(s, idx++ + ""));
		}

		return priorities;
	}
	
	
	/**
	 * Validating task, if null return then its ok
	 * @param task
	 * @return
	 */
	private String validateTask(Task task) {
		if (task == null) {
			return "Not enough data to add task.";
		}
		if (task.getTitle() == null || task.getTitle().isEmpty()) {
			return "Task title can not be empty";
		}
		if (task.getDate() == null) {
			return "Task date can not be empty";
		}
		if (task.getPriority() < 1) {
			return "Task priority can not be empty";
		}
		
		return null;
	}
	
	private void sortTaskByCompletedAndPriority(List<Task> tasks) {
		
		Collections.sort(tasks, new Comparator<Task>() {

			@Override
			public int compare(Task o1, Task o2) {
				if (o1.isCompleted() && o2.isCompleted()) {
					
					return - o1.getPriority() - o2.getPriority();
				}
				if (o1.isCompleted()) {
					return 1;
				}
				if (o2.isCompleted()) {
					return -1;
				}
				
				return - o1.getPriority() - o2.getPriority();
			}
		});
		
	}

}
