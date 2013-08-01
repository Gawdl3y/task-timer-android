package com.gawdl3y.android.actionablelistview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Checkable;
import android.widget.ListView;

/**
 * A {@code ListView} that:
 * <ul>
 *     <li>will keep check states in sync with a {@code CheckableAdapter} as long as any setItemChecked calls are made through the {@code CheckableListView}, and not the adapter</li>
 *     <li>contains a listener class for item check/uncheck events</li>
 * </ul>
 * @author Schuyler Cebulskie
 */
public class CheckableListView extends ListView {
    private OnListItemCheckStateChangeListener mOnItemCheckStateChangeListener;

    public CheckableListView(Context context) {
        super(context);
    }

    public CheckableListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CheckableListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean performItemClick(View view, int position, long id) {
        boolean sooper = super.performItemClick(view, position, id);

        // Fire event to listener
        if(getChoiceMode() == CHOICE_MODE_MULTIPLE) {
            if(isItemChecked(position)) {
                if(mOnItemCheckStateChangeListener != null) mOnItemCheckStateChangeListener.onListItemChecked(view, position, id);
            } else {
                if(mOnItemCheckStateChangeListener != null) mOnItemCheckStateChangeListener.onListItemUnchecked(view, position, id);
            }
        }

        return sooper;
    }

    @Override
    public void setChoiceMode(int choiceMode) {
        super.setChoiceMode(choiceMode);
        if(getChoiceMode() == CHOICE_MODE_NONE) {
            if(getAdapter() != null && getAdapter() instanceof CheckableAdapter) ((CheckableAdapter) getAdapter()).getCheckStates().clear();
            if(getCheckedItemPositions() != null) getCheckedItemPositions().clear();
        }
    }

    @Override
    public void setItemChecked(int position, boolean value) {
        if(getChoiceMode() == CHOICE_MODE_NONE) return;
        super.setItemChecked(position, value);

        // Update adapter
        if(getAdapter() != null && getAdapter() instanceof CheckableAdapter) {
            CheckableAdapter adapter = (CheckableAdapter) getAdapter();
            if(getChoiceMode() == CHOICE_MODE_MULTIPLE) {
                adapter.setItemChecked(position, value);
            } else {
                adapter.getCheckStates().clear();
                if(value) adapter.setItemChecked(position, true);
            }
        }
    }

    /**
     * Sets the listener for listening to item check/uncheck events
     * @param listener The listener
     */
    public void setOnItemCheckStateChangeListener(OnListItemCheckStateChangeListener listener) {
        mOnItemCheckStateChangeListener = listener;
    }


    /**
     * The interface for listening to item check/uncheck events
     * @author Schuyler Cebulskie
     */
    public interface OnListItemCheckStateChangeListener {
        public boolean onListItemChecked(View view, int position, long id);
        public boolean onListItemUnchecked(View view, int position, long id);
    }
}
