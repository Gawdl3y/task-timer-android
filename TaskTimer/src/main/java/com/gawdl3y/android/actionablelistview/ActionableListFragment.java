package com.gawdl3y.android.actionablelistview;

import android.support.v4.app.ListFragment;
import android.widget.*;

public class ActionableListFragment extends ListFragment {
    @Override
    public void setListAdapter(ListAdapter adapter) {
        if(!(adapter instanceof ActionableAdapter)) throw new IllegalArgumentException("Adapter must be instance of ActionableAdapter");
        super.setListAdapter(adapter);
    }
}
