package com.kugot;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.Objects;

public class ListLiveData<T> extends LiveData<com.kugot.ListHolder<T>> {
    public ListLiveData(List<T> value) {
        super(new com.kugot.ListHolder<T>(value));
    }

    public void populate(List<T> list) {
        getValue().populate(list);
        setValue(getValue());
    }

    public void removeItem(int position) {
        getValue().removeItem(position);
        setValue(getValue());
    }

    public void removeItem(T item) {
        getValue().removeItem(item);
        setValue(getValue());
    }

    public T getItem(int anInt) {
        try {
            return Objects.requireNonNull(getValue()).getList().get(anInt);
        } catch (Exception e) {
            return null;
        }
    }

    public void addItem(T item) {
        getValue().addItem(item, 0);
        setValue(getValue());
    }
    public void addItem(T item, int position) {
        getValue().addItem(item, position);
        setValue(getValue());
    }

    public void setItem(T item, int position) {
        getValue().setItem(item, position);
        setValue(getValue());
    }

    public void setOrAddItem(T item) {
        getValue().setOrAddItem(item);
        setValue(getValue());
    }
}
