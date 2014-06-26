package com.gawdl3y.android.actionablelistview;

import android.view.View;

public interface OnListItemCheckStateChangeListener {
    public void onListItemChecked(View view, int position, long id);
    public void onListItemUnchecked(View view, int position, long id);
}