package pl.piterpti.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
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

	private Logger logger = Logger.getLogger(TaskController.class);
	
	private static final String VIEW_TASKS = "/tasks/tasks";
	private static final String VIEW_ADD_TASKS = "/tasks/addTask";
	private static final String VIEW_EDIT_TASK = "/tasks/editTask";
	private static final String VIEW_TASKS_HISTORY = "/tasks/tasksHistory";
	
	private static final String ACTIVE_TASKS = "activeTasks";
	
	private static final int TASKS_PER_PAGE = 30;
	
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
			mav.addObject("tasks", userTodayTasks);
		}

		mav.setViewName(VIEW_TASKS);
		mav.addObject(ACTIVE_TASKS, "active");
		return mav;
	}

	@RequestMapping(value = "tasks/addTask", method = RequestMethod.GET)
	public ModelAndView getAddTaskPage() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName(VIEW_ADD_TASKS);
		mav.addObject(ACTIVE_TASKS, "active");

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
				mav.addObject(ACTIVE_TASKS, "active");
				mav.addObject("errorMessage", taskValidationResult);
				
			}
			
		}
		
		return mav;
	}
	
	@RequestMapping(value = "tasks/changeCompleted", method = RequestMethod.GET)
	public ModelAndView setCompleted(HttpServletRequest request, long id) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("redirect:/tasks");
		
		final String referer = request.getHeader("referer");
		
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
		
		if (referer.endsWith("tasks")) {
			mav.setViewName("redirect:/tasks");
		} else if (referer.contains("tasksHistory")) {
			int page = 1;
			if (referer.indexOf("page") != -1) {
				String pageStr = referer.split("page=")[1];
				page = Integer.valueOf(pageStr);
			}
			mav.setViewName("redirect:/tasks/tasksHistory?page=" + page);
		}
		
		return mav;
	}
	
	@RequestMapping(value = "tasks/tasksHistory", method = RequestMethod.GET)
	public ModelAndView getTaskHistory(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName(VIEW_TASKS_HISTORY);
		mav.addObject(ACTIVE_TASKS, "active");
		
		Object pageObj = request.getParameter("page");
		Integer page = new Integer(1);
		if (pageObj instanceof String) {
			try {
				page = Integer.valueOf((String) pageObj);
			} catch (Exception e) {
				// ignore
			}
		}
		
		if (page == null || page.intValue() < 1) {
			page = new Integer(1);
		}
		
		String userName = Toolkit.getLoggerUserName();
		
		long count = taskService.countUserTasks(userName);
		
		int pagesCount = (int) (Math.ceil((double) count / TASKS_PER_PAGE));
		
		if (page.intValue() > pagesCount) {
			page = new Integer(1);
		}
		
		Pageable topResults = new PageRequest(page.intValue() - 1, TASKS_PER_PAGE);
		
		List<Task> userTasks = taskService.findUserTasks(userName, topResults);
		
		if (userTasks.isEmpty()) {
			mav.addObject("message", "There is not any task added already!");
		} else {
			
			mav.addObject("tasks", userTasks);
			getPages(page, mav, count);
		}
		
		return mav;
	}
	
	@RequestMapping(value = "tasks/edit", method = RequestMethod.GET)
	public ModelAndView getTaskEditPage(HttpServletRequest request) {
		ModelAndView mav = getDefaultMav();
		mav.setViewName(VIEW_EDIT_TASK);
		
		Object idObj = request.getParameter("id");
		long taskId = -1;
		if (idObj instanceof String) {
			try {
				taskId = Long.valueOf((String)idObj);
			} catch (Exception e) {
				logger.error("Wrong task id to edit :" + taskId);
			}
		}
		
		if (taskId < 0) {
			return ErrorController.getErrorPage(ErrorController.ERROR_BAD_REQUEST);
		}
		
		List<Task> task = taskService.getUserTaskById(Toolkit.getLoggerUserName(), taskId);
		
		if (task.isEmpty()) {
			return ErrorController.getErrorMav("Task not found or permission denied");
		}
		
		mav.addObject("task", task.get(0));
		mav.addObject("priorities", getPriorities());
		return mav;
	}
	
	@RequestMapping(value = "tasks/edit", method = RequestMethod.POST)
	public ModelAndView updateTask(Task task) {
		ModelAndView mav = getDefaultMav();
		mav.setViewName("redirect:/tasks");
		
		if (task == null) {
			return ErrorController.getErrorPage(ErrorController.ERROR_BAD_REQUEST);
		}
		
		long taskId = task.getTask_id();
		
		if (taskId < 1) {
			return ErrorController.getErrorPage(ErrorController.ERROR_BAD_REQUEST);
		}
		
		String userName = Toolkit.getLoggerUserName();
		List<Task> tasks = taskService.getUserTaskById(userName, taskId);
		if (tasks.isEmpty()) {
			return ErrorController.getErrorMav("Task not found or permission denied");
		}
		
		taskService.updateTask(task);
		return mav;
	}

	private void getPages(Integer page, ModelAndView mav, long count) {
		int pagesCount = (int)(Math.ceil((double)count / TASKS_PER_PAGE));
		
		List<Integer> pages = new ArrayList<>();
		if (pagesCount < 6) {
			for (int i = 1; i <= pagesCount; i++) {
				pages.add(i);
			}

			mav.addObject("pages", pages);
		} else {
			mav.addObject("lastPage", pagesCount);
			int startIdx = page.intValue() - 1;
			if (startIdx < 2) {
				startIdx = 2;
			}
			if (startIdx >= pagesCount - 3) {
				startIdx = (int) (pagesCount - 3);
			}

			for (int i = startIdx; i < startIdx + 3; i++) {
				pages.add(i);
				if (i + 1 >= pagesCount) {
					break;
				}
			}
			mav.addObject("selectedPages", pages);

			if (pages.get(0) > 2) {
				mav.addObject("prefix", "...");
			}

			if (pages.size() == 3 && pages.get(pages.size() - 1) + 1 < pagesCount) {
				mav.addObject("suffix", "...");
			}
		}
		
		mav.addObject("currentPage", page.intValue());
	}

	private static List<PairNameValue> getPriorities() {
		List<PairNameValue> priorities = new ArrayList<>();

		int idx = 1;
		for (String s : TaskService.TASK_PRIORITIES) {
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
					return -(new Integer(o1.getPriority()).compareTo(new Integer(o2.getPriority())));
				}
				
				if (o1.isCompleted()) {
					return 1;
				}
				
				if (o2.isCompleted()) {
					return -1;
				} 
				
				return -(new Integer(o1.getPriority()).compareTo(new Integer(o2.getPriority())));
				
			}
		});
		
	}
	
	private ModelAndView getDefaultMav() {
		ModelAndView mav = new ModelAndView();
		mav.addObject(ACTIVE_TASKS, "active");
		return mav;
	}

}
