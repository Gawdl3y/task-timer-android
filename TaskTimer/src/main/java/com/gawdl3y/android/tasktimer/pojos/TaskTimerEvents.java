package com.gawdl3y.android.tasktimer.pojos;

import java.util.ArrayList;

/**
 * Events for listening to changes to tasks and groups
 * @author Schuyler Cebulskie
 */
public class TaskTimerEvents {
    public static final int EVENT_GROUP_ADD = 0;
    public static final int EVENT_GROUP_REMOVE = 1;
    public static final int EVENT_GROUP_UPDATE = 2;
    public static final int EVENT_TASK_ADD = 3;
    public static final int EVENT_TASK_REMOVE = 4;
    public static final int EVENT_TASK_UPDATE = 5;

    private static ArrayList<TaskTimerListener> sListeners = new ArrayList<TaskTimerListener>();

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
     * Registers a TaskTimerListener
     * @param listener The TaskTimerListener to register
     */
    public static void registerListener(TaskTimerListener listener) {
        sListeners.add(listener);
    }

    /**
     * Unregisters a TaskTimerListener
     * @param listener The TaskTimerListener to unregister
     */
    public static void unregisterListener(TaskTimerListener listener) {
        sListeners.remove(listener);
    }

    /**
     * Unregisters all TaskTimerListeners
     */
    public static void unregisterAllListeners() {
        sListeners.clear();
    }

    /**
     * Tests if a TaskTimerListener is registered or not
     * @param listener The TaskTimerListener to test for
     * @return {@code true} if the TaskTimerListener is registered, {@code false} otherwise
     */
    public static boolean isRegistered(TaskTimerListener listener) {
        return sListeners.contains(listener);
    }

    /**
     * Fires an event to all registered listeners
     * @param event The type of event to fire
     */
    public static void fireEvent(int event, Object... obj) {
        switch(event) {
            case EVENT_GROUP_ADD:
                for(TaskTimerListener listener : sListeners) {
                    if(listener instanceof GroupListener) ((GroupListener) listener).onGroupAdd((Group) obj[0]);
                }
                break;
            case EVENT_GROUP_REMOVE:
                for(TaskTimerListener listener : sListeners) {
                    if(listener instanceof GroupListener) ((GroupListener) listener).onGroupRemove((Group) obj[0]);
                }
                break;
            case EVENT_GROUP_UPDATE:
                for(TaskTimerListener listener : sListeners) {
                    if(listener instanceof GroupListener) ((GroupListener) listener).onGroupUpdate((Group) obj[0], (Group) obj[1]);
                }
                break;
            case EVENT_TASK_ADD:
                for(TaskTimerListener listener : sListeners) {
                    if(listener instanceof TaskListener) ((TaskListener) listener).onTaskAdd((Task) obj[0], (Group) obj[1]);
                }
                break;
            case EVENT_TASK_REMOVE:
                for(TaskTimerListener listener : sListeners) {
                    if(listener instanceof TaskListener) ((TaskListener) listener).onTaskRemove((Task) obj[0], (Group) obj[1]);
                }
                break;
            case EVENT_TASK_UPDATE:
                for(TaskTimerListener listener : sListeners) {
                    if(listener instanceof TaskListener) ((TaskListener) listener).onTaskUpdate((Task) obj[0], (Task) obj[1], (Group) obj[2]);
                }
                break;
        }
    }
}
