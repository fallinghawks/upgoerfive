package com.fallinghawks.upgoer;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

public class About extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);
        TextView textView = (TextView) findViewById(R.id.aboutbox);
        textView.setText(Html.fromHtml(getString(R.string.aboutText)));
    }
}
