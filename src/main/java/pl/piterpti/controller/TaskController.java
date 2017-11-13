package pl.piterpti.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import pl.piterpti.model.Task;
import pl.piterpti.service.TaskService;
import pl.piterpti.toolkit.Toolkit;

@Controller
public class TaskController {

	private static final String VIEW_TASKS = "/tasks/tasks";
	
	@Autowired
	private TaskService taskService;
	
	
	@RequestMapping(value = "/tasks", method = RequestMethod.GET)
	public ModelAndView getTodaysUserTask() {
		ModelAndView mav = new ModelAndView();
		
		String userName = Toolkit.getLoggerUserName();
		
		List<Task> userTodayTasks = taskService.getUserTodayTasks(userName);
		mav.addObject("todayTasks", userTodayTasks);
		
		mav.setViewName("redirect:/" + VIEW_TASKS);
		return mav;
	}
	
	@RequestMapping(value = "tasks/addTask", method = RequestMethod.POST)
	public ModelAndView addTask() {
//		String userName = Toolkit.getLoggerUserName();
		
		// TODO add task management
		
		return getTodaysUserTask();
	}
	
	
}
