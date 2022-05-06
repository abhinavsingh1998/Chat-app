package com.emproto.core.customdropdown;

public interface OnValueChangedListener {
    public void onValueChanged(String value, String dropDownValue);

    public void afterValueChanges(String value1);
}
