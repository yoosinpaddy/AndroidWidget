package com.trichain.androidwidget;

import android.text.SpannableString;

public class EventModel {
    SpannableString startD;
    SpannableString endD;
    SpannableString name;
    SpannableString date;
    SpannableString location;

    public EventModel(SpannableString startD, SpannableString endD, SpannableString name) {
        this.startD = startD;
        this.endD = endD;
        this.name = name;
    }

    public EventModel(SpannableString startD, SpannableString endD, SpannableString name, SpannableString date, SpannableString location) {
        this.startD = startD;
        this.endD = endD;
        this.name = name;
        this.date = date;
        this.location = location;
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

    public SpannableString getDate() {
        return date;
    }

    public SpannableString getLocation() {
        return location;
    }
}
