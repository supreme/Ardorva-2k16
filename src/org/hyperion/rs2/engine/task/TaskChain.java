package org.hyperion.rs2.engine.task;

/**
 * A basic task contained within a chain for a {@link TaskChainExecutor}.
 * 
 * @author lare96
 * @see TaskChainExecutor#append(TaskChain)
 * @see TaskChainExecutor#appendAll(TaskChain[])
 * @see TaskChainExecutor#appendAll(java.util.Collection)
 */
public interface TaskChain {

    /** The code executed when this task is fired. */
    public void execute();

    /**
     * The delay for this task that will come into effect once the code from the
     * previous task in the chain is executed.
     * 
     * @return the delay for this task, cannot be below <tt>0</tt>.
     */
    public int delay();
}

