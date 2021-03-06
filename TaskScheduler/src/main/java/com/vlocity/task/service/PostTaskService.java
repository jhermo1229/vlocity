package com.vlocity.task.service;

import com.vlocity.task.dao.PostTaskDao;
import com.vlocity.task.dao.impl.PostTaskDaoImpl;
import com.vlocity.task.model.Task;

import com.vlocity.util.TaskSchedulerUtil;

import java.util.List;
import java.util.Scanner;


/**
 * Service for task creation
 *
 * @version  1
 */
public class PostTaskService
{
	//~ Static fields/initializers ---------------
	/**  */
	private static final String CREATE = "C";

	/**  */
	private static final String YES = "Y";
	//~ Instance fields --------------------------
	/**  */
	private PostTaskDao postTask = new PostTaskDaoImpl();
	//~ Methods ----------------------------------
	/** Task creation */
	@SuppressWarnings("resource")
	public void createTask()
	{
		Task task = new Task();
		Scanner s = new Scanner(System.in);
		System.out.println("Enter Task Name: ");
		task.setTaskName(s.nextLine());
		System.out.println("Enter start date (yyyyMMdd): ");
		task.setStartDate(s.nextInt());
		System.out.println("Enter end date (yyyyMMdd): ");
		task.setEndDate(s.nextInt());
		System.out.println("Any task dependency? (Y/N): ");
		String dependency = s.next();
		GetTaskService getTask = new GetTaskService();
		List<Task> taskList = getTask.getAllTasks();
		if (dependency.equalsIgnoreCase(YES))
		{
			TaskSchedulerUtil.getInstance().printTasks(taskList);
			System.out.println("");
			boolean isValid = false;
			while (!isValid)
			{
				System.out.println("Please choose task ID or IDs of dependent task:");
				Scanner input = new Scanner(System.in);
				String dependentId = input.next();

				isValid = validateInput(task, taskList, dependentId);
			}
		}
		else
		{
			task.setDependentTaskId("0");
		}
		task.setId(taskList.size() + 1);
		task.setFlag(CREATE);

		postTask.saveTask(task);
	}
	
	/** Validate if input is valid */
	public boolean validateInput(Task task, List<Task> taskList, String dependentId)
	{
		char[] idList = dependentId.toCharArray();
		boolean isValid = false;
		for (char c : idList)
		{
			// always return to false at multiple dependency
			isValid = false;
			for (Task existingTask : taskList)
			{
				if (existingTask.getId() == Character.getNumericValue(c))
				{
					task.setDependentTaskId(dependentId);

					isValid = true;

					break;
				}
			}
		}

		return isValid;
	}
}
