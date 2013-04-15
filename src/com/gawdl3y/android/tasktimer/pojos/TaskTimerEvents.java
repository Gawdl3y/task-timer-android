package com.gawdl3y.android.tasktimer.pojos;

/**
 * Events for listening to changes to tasks and groups
 * @author Schuyler Cebulskie
 */
public class TaskTimerEvents {
    private static GroupListener sGroupListener;
    private static TaskListener sTaskListener;

    /**
     * The base listener class
     * @author Schuyler Cebulskie
     */
    public static abstract interface TaskTimerListener {}

    /**
     * Class for listening to Group events
     * @author Schuyler Cebulskie
     */
    public static interface GroupListener extends TaskTimerListener {
        /**
         * For calling when a Group is added to the set
         * @param group The Group that was added
         */
        public void onGroupAdd(Group group);

        /**
         * For calling when a Group is removed from the set
         * @param group The Group that was removed
         */
        public void onGroupRemove(Group group);

        /**
         * For calling when a Group is updated
         * @param group    The Group that was updated
         * @param oldGroup A copy of the Group before it was updated
         */
        public void onGroupUpdate(Group group, Group oldGroup);
    }

    /**
     * Class for listening to Task events
     * @author Schuyler Cebulskie
     */
    public static interface TaskListener extends TaskTimerListener {
        /**
         * For calling when a Task is added to the set
         * @param task  The Task that was added
         * @param group The Group that the Task was added to
         */
        public void onTaskAdd(Task task, Group group);

        /**
         * For calling when a Task is removed from the set
         * @param task  The Task that was removed
         * @param group The Group that the Task was in
         */
        public void onTaskRemove(Task task, Group group);

        /**
         * For calling when a Task is updated
         * @param task    The Task that was updated
         * @param oldTask A copy of the Task before it was updated
         * @param group   The Group that the Task is in
         */
        public void onTaskUpdate(Task task, Task oldTask, Group group);
    }

    /**
     * Sets the GroupListener to use
     * @param listener The GroupListener to use
     */
    public static void setGroupListener(GroupListener listener) {
        sGroupListener = listener;
    }

    /**
     * Gets the GroupListener to use
     * @return The GroupListener to use
     */
    public static GroupListener getGroupListener() {
        return sGroupListener;
    }

    /**
     * Sets the TaskListener to use
     * @param listener The TaskListener to use
     */
    public static void setTaskListener(TaskListener listener) {
        sTaskListener = listener;
    }

    /**
     * Gets the TaskListener to use
     * @return The TaskListener to use
     */
    public static TaskListener getTaskListener() {
        return sTaskListener;
    }

    /**
     * Sets both the GroupListener and TaskListener to use from the same object
     * <p>The object passed <strong>must</strong> implement both GroupListener and TaskListener, or an IllegalArgumentException will be thrown</p>
     * @param listener The Group/TaskListener to use
     */
    public static void setListener(TaskTimerListener listener) {
        try {
            sGroupListener = (GroupListener) listener;
            sTaskListener = (TaskListener) listener;
        } catch(ClassCastException e) {
            throw new IllegalArgumentException("Listener does not implement both GroupListener and TaskListener");
        }
    }
}
