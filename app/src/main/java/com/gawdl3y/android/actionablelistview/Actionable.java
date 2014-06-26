package com.gawdl3y.android.actionablelistview;

/**
 * Interface for performing an action
 * @author Schuyler Cebulskie
 */
public interface Actionable {
    /**
     * Performs an action on something
     * @param actionType The type of action to perform
     * @param id         The identifier for the item to perform the action on
     *                   (could be a position, index, ID, etc.)
     * @return {@code true} if the associated {@code ActionMode} should be ended, {@code false} if not
     */
    public boolean performAction(int actionType, int id);
}
