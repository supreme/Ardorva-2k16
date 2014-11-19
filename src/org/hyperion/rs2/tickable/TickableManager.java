package org.hyperion.rs2.tickable;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;

/**
 * A class that manages <code>Tickable</code>s for a specific
 * <code>GameEngine</code>.
 * 
 * @author Michael Bull
 * 
 */
public class TickableManager {

	/**
	 * A queue of new tasks that should be added.
	 */
	private Queue<Tickable> newTasks = new ArrayDeque<Tickable>();

	/**
	 * A list of currently active tasks.
	 */
	private List<Tickable> tasks = new ArrayList<Tickable>();
	
	/**
	 * Submits a new tickable to the <code>GameEngine</code>.
	 * 
	 * @param tickable
	 *            The tickable to submit.
	 */
	public void submit(final Tickable tickable) {
		if (tickable.isImmediate()) {
			tickable.execute();
		}
		newTasks.add(tickable);
	}
	
	/**
	 * Called every pulse: executes tasks that are still pending, adds new
	 * tasks and stops old tasks.
	 */
	public void pulse() {
		Tickable task;
		while ((task = newTasks.poll()) != null) {
			tasks.add(task);
		}

		for (Iterator<Tickable> it = tasks.iterator(); it.hasNext(); ) {
			task = it.next();
			task.cycle();
			if (!task.isRunning()) {
				it.remove();
			}
		}
	}

}
