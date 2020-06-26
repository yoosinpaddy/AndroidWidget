package com.trichain.androidwidget;

import android.text.SpannableString;

public class EventModel {
    SpannableString startD;
    SpannableString endD;
    SpannableString name;

    public EventModel(SpannableString startD, SpannableString endD, SpannableString name) {
        this.startD = startD;
        this.endD = endD;
        this.name = name;
    }

    public SpannableString getStartD() {
        return startD;
    }

    public SpannableString getEndD() {
        return endD;
    }

    public SpannableString getName() {
        return name;
    }
}
