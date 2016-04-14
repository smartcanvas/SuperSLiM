package com.tonicartos.superslimexample;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Simple view holder for a single text view.
 */
class CountryViewHolder extends RecyclerView.ViewHolder {

    private TextView mTextView;

    CountryViewHolder(View view) {
        super(view);

        mTextView = (TextView) view.findViewById(R.id.text);
    }

    public void bindItem(final String text) {
        mTextView.setText(text);

        mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mTextView.getContext(), text, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public String toString() {
        return mTextView.getText().toString();
    }
}
