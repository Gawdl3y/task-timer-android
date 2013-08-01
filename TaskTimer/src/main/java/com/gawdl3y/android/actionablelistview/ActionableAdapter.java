package com.gawdl3y.android.actionablelistview;

/**
 * An adapter that contains a method to receive actions, for use with {@code ActionableListView}s
 * @author Schuyler Cebulskie
 */
public abstract class ActionableAdapter extends CheckableAdapter implements Actionable {
    /**
     * Performs an action on an item
     * @param actionType The type of action to perform
     * @param position   The position of the item to perform on
     * @return {@code true} if this affects the data order, {@code false} if it does not
     */
    public abstract boolean performActionOnItem(int actionType, int position);

    /**
     * Performs an action on multiple items
     * @param actionType The type of action to perform
     * @param positions  The positions of the items to perform on
     * @return {@code true} if this affects the data order, {@code false} if it does not
     */
    public boolean performActionOnItems(int actionType, int[] positions) {
        boolean dataOrderSanctityLost = false;
        for(int p : positions) {
            if(performAction(actionType, p)) dataOrderSanctityLost = true;
        }
        return dataOrderSanctityLost;
    }

    /**
     * Forwards the call to performActionOnItem
     */
    @Override
    public boolean performAction(int actionType, int id) {
        return performActionOnItem(actionType, id);
    }
}
