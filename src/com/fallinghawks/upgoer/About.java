package com.fallinghawks.upgoer;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

public class About extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);
        TextView textView = (TextView) findViewById(R.id.aboutbox);
        textView.setText(Html.fromHtml("<p>The idea for this writing-can-be-changed thing for the phone came from <a href=\"http://xkcd.com/1133\">a funny picture about the flying space car Up Goer Five</a>, and from another person who wrote about the same funny picture, and also wrote a <a href=\"http://splasho.com/upgoer5/\">writing-changer</a>, but for the world-brain-TV.</p><p>[The UpGoerFive Editor was inspired by <a href=\"http://xkcd.com/1133\">xkcd comic #1133</a>, which was a description of the Saturn V rocket using only the 1000 most commonly used words in contemporary fiction (according to Wiktionary), and <a href=\"http://splasho.com/upgoer5/\">Splasho's online text editor</a> also inspired by the comic.]<p>This editor has a selection dropdown for the thousand most commonly used words and their various forms (full list kindly provided by Theo Sanderson/Splasho), and highlights in red any words not in the list.</p><p>2013 Andrea Chen</p>"));
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
