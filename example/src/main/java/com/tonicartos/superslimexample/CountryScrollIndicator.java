package com.tonicartos.superslimexample;

import android.content.Context;
import android.util.AttributeSet;

import xyz.danoz.recyclerviewfastscroller.sectionindicator.title.SectionTitleIndicator;

/**
 * Created by Rafael Baboni Dominiquini on 02/05/16.
 */
public class CountryScrollIndicator extends SectionTitleIndicator<CountryNamesAdapter.LineItem> {

    public CountryScrollIndicator(Context context) {
        super(context);
    }

    public CountryScrollIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CountryScrollIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setSection(CountryNamesAdapter.LineItem object) {
        setTitleText(String.valueOf(object.text.charAt(0)));
    }
}