package com.fallinghawks.upgoer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyActivity extends Activity {

    private final Handler handler = new Handler();
    private String[] dictionary;
    private MultiAutoCompleteTextView editBox;
    final String regex1 = "[/.,\\?!:;']";
    Pattern punctuation = Pattern.compile(regex1);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        try {
            dictionary = readFile();
        } catch (IOException e) {
            Log.e("upgoer", "can't read dictionary", e);
            throw new RuntimeException("can't start", e);
        }

        editBox = (MultiAutoCompleteTextView)findViewById(R.id.autocomplete);

        editBox.setAdapter(
                new ArrayAdapter<String>(this, R.layout.modified_dropdown, dictionary));
        editBox.setTokenizer(new MyTokenizer());

        editBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String lastTwo = s.subSequence(s.length()-2, s.length()).toString();
                Matcher matcher = punctuation.matcher(lastTwo);
                if (lastTwo.charAt(0) == ' ' &&  matcher.find()) {
                    s.delete(s.length()-2,s.length()-1);
                    s.append(" ");
                }

                // mark nonmatching words in red
                ForegroundColorSpan[] spans = s.getSpans(0, s.length(), ForegroundColorSpan.class);
                for (ForegroundColorSpan span : spans) {
                    s.removeSpan(span);
                }

                int startWord = -1;
                for (int i = 0; i < s.length(); i++) {
                    char c = s.charAt(i);
                    boolean isWordLetter = Character.isLetter(c) || c == '\'';
                    if (isWordLetter && startWord == -1) {
                        // found the start of word
                        startWord = i;
                    } else if (startWord != -1 && !isWordLetter) {
                        // found end of word
                        updateWord(s, startWord, i);
                        startWord = -1;
                    }
                }
                if (startWord != -1) {
                    // found the end of a word at the end of the text
                    updateWord(s, startWord, s.length());
                }
            }

            private void updateWord(Editable s, int startWord, int endWord) {
                String word = s.subSequence(startWord, endWord).toString();
                if (!inDictionary(word)) {
                    s.setSpan(new ForegroundColorSpan(Color.RED), startWord, endWord, 0);
                }
            }
        });


        ImageButton shareButton = (ImageButton) findViewById(R.id.sharebutton);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String shareText = saveText();
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("text/plain");
                share.putExtra(Intent.EXTRA_TEXT, shareText+" #upgoerfive");
                startActivity(Intent.createChooser(share, "Share Image"));
            }
        });

        // restore text from saved file if it exists
        try {
            File root = Environment.getExternalStorageDirectory();
            File filepath = new File (root.getAbsolutePath() + "/upgoer/tenhundred.txt");
            FileInputStream file = new FileInputStream(filepath);
            StringBuilder stringBuffer = new StringBuilder("");
            byte[] buffer = new byte[1024];
            while (true) {
                int length = file.read(buffer);
                if (length == -1) {
                    break;
                }
                stringBuffer.append(new String(buffer, 0, length)) ;
            }
            file.close();
            String readString = stringBuffer.toString();
            if (readString != null) {
                editBox.setText(readString);
                editBox.setSelection(editBox.getText().length());
            }
        } catch (FileNotFoundException e) {
            // this happens the first time the app is launched
        } catch (IOException e) {
            Log.w("pixel", e);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (editBox.requestFocus()) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.showSoftInput(editBox, 0);
                    }
                }
            }, 100);
        }
    }

    private boolean inDictionary(String word) {
        for (String aTENHUNDRED : dictionary) {
            if (word.equalsIgnoreCase(aTENHUNDRED)) {
                return true;
            }
        }
        return false ;
    }

    public String saveText() {
        TextView textView = (TextView) findViewById(R.id.autocomplete);
        String thText =  textView.getText().toString();
        File root = Environment.getExternalStorageDirectory();
        File filepath = new File (root.getAbsolutePath() + "/upgoer");
        if (!filepath.isDirectory() && !filepath.mkdirs()) {
            Log.e("Upgoer","can't save file");
            return thText ;
        }
        OutputStreamWriter writer;
        try{
            File filename = new File(filepath,"tenhundred.txt");
            FileOutputStream fileOutputStream = new FileOutputStream(filename);
            writer = new OutputStreamWriter(fileOutputStream);
            writer.write(thText);
            writer.close();
            fileOutputStream.close();
        } catch(Exception e){
            Log.e("upgoer", "can't save!", e);
        }
        return thText;
    }
    @Override

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);

        // Calling super after populating the menu is necessary here to ensure that the
        // action bar helpers have a chance to handle this event.
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.clear:
                editBox.getEditableText().clear();
                saveText();
                editBox.requestFocus();
                break;
            case R.id.about:
                Intent intent = new Intent(this, About.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        // save the current state
        saveText();
        super.onStop();
    }

    private String[] readFile() throws IOException {
        InputStream is = getResources().openRawResource(R.raw.words);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        List<String> lines = new ArrayList<String>();
        while (true) {
            String line = br.readLine() ;
            if (line == null) {
                break;
            }
            lines.add(line);
        }
        br.close();
        return lines.toArray(new String[lines.size()]);
    }
}











