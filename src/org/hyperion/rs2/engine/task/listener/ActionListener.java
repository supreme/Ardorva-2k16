package org.hyperion.rs2.engine.task.listener;

import org.hyperion.rs2.engine.task.Task;

/**
 * A listener that executes code after some sort of event occurs.
 * 
 * @author lare96
 */
public abstract class ActionListener extends Task {

    /** If the listener should be shutdown once the code has been executed. */
    private boolean shutdown;

    /**
     * Create a new {@link ActionListener}.
     * 
     * @param shutdown
     *            if the listener should be shutdown once the code has been
     *            executed.
     * @param rate
     *            the rate in which the code will be executed.
     */
    public ActionListener(boolean shutdown, int rate) {
        super(rate, true);
        this.shutdown = shutdown;
    }

    /**
     * Create a new {@link ActionListener}.
     * 
     * @param shutdown
     *            if the listener should be shutdown once the code has been
     *            executed.
     */
    public ActionListener(boolean shutdown) {
        this(shutdown, 1);
    }

    /**
     * Create a new {@link ActionListener}.
     * 
     * @param rate
     *            the rate in which the code will be executed.
     */
    public ActionListener(int rate) {
        this(true, rate);
    }

    /**
     * Create a new {@link ActionListener} that will be listen in <tt>600</tt>ms
     * intervals and will shutdown once the code is executed.
     */
    public ActionListener() {
        this(true, 1);
    }

    /**
     * The code within {@link #run} will not be executed until this is
     * unflagged.
     * 
     * @return <code>true</code> if this listener should keep listening,
     *         <code>false</code> if the event has occurred and the listener
     *         should execute the code.
     */
    public abstract boolean listenWhile();

    /** The code that will be executed once {@link #listenFor} is unflagged. */
    public abstract void run();

    @Override
    public void execute() {

        // Don't proceed unless unflagged.
        if (listenWhile()) {
            return;
        }

        // We're unflagged, fire the logic.
        try {
            this.run();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            // Shutdown if needed, otherwise keep the checking and
            // firing.
            if (shutdown) {
                this.cancel();
            }
        }
    }
}