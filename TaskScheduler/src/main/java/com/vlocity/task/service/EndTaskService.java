package com.vlocity.task.service;

import com.vlocity.task.dao.PutTaskDao;
import com.vlocity.task.dao.impl.PutTaskDaoImpl;
import com.vlocity.task.model.Task;

import com.vlocity.util.TaskSchedulerUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


/**
 * Task ender
 *
 * @version  1
 */
public class EndTaskService
{
	private static final String START = "S";

	private static final String END = "E";

	//~ Instance fields --------------------------
	/**  */
	private GetTaskService getTask = new GetTaskService();

	/**  */
	private PutTaskDao updateTask = new PutTaskDaoImpl();
	//~ Methods ----------------------------------
	/**
	 * @throws  Exception
	 * @see     com.vlocity.task.services.StartTask#startTask(java.lang.Integer)
	 */
	public void endTask()
	{
		List<Task> finalTaskList = new ArrayList<>();
		List<Task> startedList = new ArrayList<>();
		List<Task> taskList = getTask.getAllTasks();
		for (Task task : taskList)
		{
			if (!task.getFlag().contains(END) && task.getFlag().contains(START))
			{
				startedList.add(task);
			}
		}
		if (startedList.isEmpty())
		{
			System.out.println("No task started");
			System.exit(0);
		}

		TaskSchedulerUtil.getInstance().printTasks(startedList);
		System.out.println("Please choose task ID of task to end:");

		Scanner input = new Scanner(System.in);

		int taskId = input.nextInt();
		boolean isEnded = false;
		for (Task task : startedList)
		{
			if (task.getId() == taskId)
			{
				StringBuilder flag = new StringBuilder(task.getFlag());
				flag.append(END);
				task.setFlag(flag.toString());
				isEnded = true;
			}
			finalTaskList.add(task);
		} // end for

		if (!isEnded)
		{
			System.out.println("Input task not valid");
			System.exit(0);
		}
		updateTask.updateTask(finalTaskList);
	}
}