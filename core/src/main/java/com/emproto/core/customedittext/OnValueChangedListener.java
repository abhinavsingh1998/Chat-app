package com.emproto.core.customedittext;

public interface OnValueChangedListener {
    public void onValueChanged(String value, String dropDownValue);

    public void afterValueChanges(String value1);
}
