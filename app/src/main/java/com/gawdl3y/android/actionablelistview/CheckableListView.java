package com.gawdl3y.android.actionablelistview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Checkable;
import android.widget.ListView;

/**
 * A {@code ListView} that:
 * <ul>
 *     <li>will keep check states in sync with a {@link CheckableAdapter} as long as any {@code setItemChecked} calls are made through the {@link CheckableListView}, and not the adapter</li>
 *     <li>will check/uncheck its views that implement the {@link Checkable} interface</li>
 *     <li>fires {@link OnListItemCheckStateChangeListener} events</li>
 * </ul>
 * @author Schuyler Cebulskie
 */
public class CheckableListView extends ListView implements OnListItemCheckStateChangeListener {
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

        // Fire check state change event
        if(getChoiceMode() == CHOICE_MODE_MULTIPLE) {
            if(isItemChecked(position)) {
                onListItemChecked(view, position, id);
            } else {
                onListItemUnchecked(view, position, id);
            }
        }

        return sooper;
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

    @Override
    public void clearChoices() {
        super.clearChoices();
        if(getAdapter() != null && getAdapter() instanceof CheckableAdapter) ((CheckableAdapter) getAdapter()).getCheckStates().clear();
        invalidateViews();
    }

    @Override
    public void onListItemChecked(View view, int position, long id) {
        if(view instanceof Checkable) ((Checkable) view).setChecked(true);
        if(mOnItemCheckStateChangeListener != null) mOnItemCheckStateChangeListener.onListItemChecked(view, position, id);
    }

    @Override
    public void onListItemUnchecked(View view, int position, long id) {
        if(view instanceof Checkable) ((Checkable) view).setChecked(false);
        if(mOnItemCheckStateChangeListener != null) mOnItemCheckStateChangeListener.onListItemUnchecked(view, position, id);
    }

    /**
     * Sets the listener for listening to item check/uncheck events
     * @param listener The listener
     */
    public void setOnItemCheckStateChangeListener(OnListItemCheckStateChangeListener listener) {
        mOnItemCheckStateChangeListener = listener;
    }
}
