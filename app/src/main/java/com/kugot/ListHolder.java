package com.kugot;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

public class ListHolder<T> {

    private int index = -1;
    private UpdateType updateType = null;
    private final List<T> list;

    public ListHolder(@NonNull List<T> list){
        this.list = list;
    }

    public void applyChange(RecyclerView.Adapter<? extends RecyclerView.ViewHolder> adapter) {
        if (updateType == null) return;
        switch (updateType) {
            case CHANGE: {
                adapter.notifyItemChanged(index);
                break;
            }
            case INSERT: {
                adapter.notifyItemInserted(index);
                break;
            }
            case REMOVE: {
                adapter.notifyItemRemoved(index);
                break;
            }
            case DATA_SET_CHANGED: {
                adapter.notifyDataSetChanged();
                break;
            }
        }
    }

    public void populate(List<T> list) {
        this.list.addAll(list);
        updateType = UpdateType.DATA_SET_CHANGED;
    }

    public @NonNull List<T> getList() {
        return Collections.unmodifiableList(list);
    }

    public void removeItem(int position) {
        list.remove(position);
        index = position;
        updateType = UpdateType.REMOVE;
    }

    public void removeItem(T item) {
        index = list.indexOf(item);
        list.remove(index);
        updateType = UpdateType.REMOVE;
    }

    public void addItem(T item, int position) {
        list.add(position, item);
        index = position;
        updateType = UpdateType.INSERT;
    }

    public void setItem(T item, int position) {
        list.set(position, item);
        index = position;
        updateType = UpdateType.CHANGE;
    }

    public void setOrAddItem(T item) {
        index = list.indexOf(item);
        if (index == -1) {
            addItem(item, 0);
        } else {
            setItem(item, index);
        }
    }
}
