package com.gawdl3y.android.actionablelistview;

/**
 * Stores all information needed to create a {@code MenuItem} for the {@code ActionMode} started by an {@code ActionableListView}
 * @author Schuyler Cebulskie
 */
public class ActionItem {
    private int mActionType = -1;
    private int mPosition;
    private int mIconResource;
    private int mTitleResource;

    public ActionItem(int actionType, int position, int iconRes, int titleRes) {
        mActionType = actionType;
        mPosition = position;
        mIconResource = iconRes;
        mTitleResource = titleRes;
    }

    public int getActionType() {
        return mActionType;
    }

    public void setActionType(int actionType) {
        mActionType = actionType;
    }

    public int getPosition() {
        return mPosition;
    }

    public void setPosition(int position) {
        mPosition = position;
    }

    public int getIconResource() {
        return mIconResource;
    }

    public void setIconResource(int iconResource) {
        mIconResource = iconResource;
    }

    public int getTitleResource() {
        return mTitleResource;
    }

    public void setTitleResource(int titleResource) {
        mTitleResource = titleResource;
    }
}
