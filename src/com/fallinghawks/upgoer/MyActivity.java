package com.fallinghawks.upgoer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;

import java.io.*;

public class MyActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        MultiAutoCompleteTextView autoCompleteTextView = (MultiAutoCompleteTextView)findViewById(R.id.autocomplete);

        autoCompleteTextView.setAdapter(
                new ArrayAdapter<String>(this,R.layout.modified_dropdown, TENHUNDRED));
        autoCompleteTextView.setTokenizer(new MyTokenizer());

        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                int startWord = -1;
                for (int i = 0; i < s.length(); i++ ) {
                    char c = s.charAt(i);
                    if (Character.isLetter(c)) {
                        if (startWord == -1) {
                            startWord = i;
                        }
                    } else {
                        if (startWord != -1) {
                            String word = s.subSequence(startWord,i).toString();
                            if (!inDictionary(word)) {
                                // wrap in a span of red color
                                if (s.getSpans(startWord,i,ForegroundColorSpan.class).length == 0) {
                                    s.setSpan(new ForegroundColorSpan(Color.RED),startWord,i,0);
                                }
                            } else {
                                ForegroundColorSpan[] currSpan = s.getSpans(startWord, i, ForegroundColorSpan.class);
                                for (ForegroundColorSpan aCurrSpan : currSpan) {
                                    s.removeSpan(aCurrSpan);
                                }
                            }
                            startWord = -1;
                        }
                    }
                }
            }
        });


        Button shareButton = (Button) findViewById(R.id.sharebutton);
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
                TextView mainText = (TextView) findViewById(R.id.autocomplete);
                mainText.setText(readString);
            }
        } catch (FileNotFoundException e) {
            // this happens the first time the app is launched
        } catch (IOException e) {
            Log.w("pixel", e);
        }

    }

    private boolean inDictionary(String word) {
        for (String aTENHUNDRED : TENHUNDRED) {
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
        if (!filepath.mkdirs()) {
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
            e.printStackTrace(System.err);
        }
        return thText;
    }

    @Override
    protected void onDestroy() {
        // save the current state
        saveText();
        super.onDestroy();
    }

    private static final String[] TENHUNDRED = new String[] {
            "the", "I", "to", "and", "a", "of", "was", "he", "you", "it", "in", "her", "she", "that", "my", "his", "me",
            "on", "with", "at", "as", "had", "for", "but", "him", "said", "be", "up", "out", "look", "so", "have",
            "what", "not", "just", "like", "go", "they", "is", "this", "from", "all", "we", "were", "back", "do", "one",
            "about", "know", "if", "when", "get", "then", "into", "would", "no", "there", "I'm", "could", "don't",
            "ask", "down", "time", "didn't", "want", "eye", "them", "over", "your", "are", "or", "been", "now", "an",
            "by", "think", "see", "hand", "it's", "say", "how", "around", "head", "did", "well", "before", "off", "who",
            "more", "even", "turn", "come", "smile", "way", "really", "can", "face", "other", "some", "right", "their",
            "only", "walk", "make", "got", "try", "something", "room", "again", "thing", "after", "still", "thought",
            "door", "here", "too", "little", "because", "why", "away", "let", "take", "two", "start", "good", "where",
            "never", "through", "day", "much", "tell", "wasn't", "girl", "feel", "oh", "you're", "call", "talk", "will",
            "long", "than", "us", "made", "friend", "knew", "open", "need", "first", "which", "people", "that's",
            "went", "sure", "seem", "stop", "voice", "very", "felt", "took", "our", "pull", "laugh", "man", "okay",
            "close", "any", "came", "told", "love", "watch", "arm", "anything", "I'll", "though", "put", "left", "work",
            "guy", "hair", "next", "couldn't", "yeah", "while", "mean", "home", "few", "saw", "place", "school", "help",
            "wait", "late", "year", "can't", "house", "happen", "last", "always", "move", "old", "night", "nod", "life",
            "give", "sit", "stare", "sat", "should", "moment", "another", "behind", "side", "sound", "once", "find",
            "toward", "boy", "ever", "nothing", "front", "mother", "name", "am", "since", "reply", "he's", "myself",
            "leave", "bed", "new", "car", "use", "mind", "maybe", "has", "heard", "answer", "minute", "yes", "until",
            "both", "found", "end", "small", "I'd", "word", "someone", "same", "enough", "began", "run", "bit", "sigh",
            "each", "those", "almost", "against", "everything", "most", "thank", "wouldn't", "mom", "better", "play",
            "I've", "own", "every", "hard", "remember", "three", "stood", "live", "stand", "second", "sorry", "keep",
            "finally", "point", "gave", "already", "actually", "probably", "himself", "big", "everyone", "guess", "lot",
            "step", "hey", "hear", "light", "quickly", "dad", "kiss", "black", "pick", "else", "soon", "shoulder",
            "table", "best", "without", "notice", "stay", "care", "phone", "reach", "realize", "follow", "decide",
            "kind", "she's", "grab", "he'd", "show", "inside", "suddenly", "father", "rest", "herself", "grin", "hour",
            "hope", "also", "body", "might", "hadn't", "floor", "its", "continue", "ran", "across", "hold", "cry",
            "half", "pretty", "great", "course", "mouth", "class", "kid", "miss", "wonder", "morning", "least", "nice",
            "dark", "slowly", "done", "change", "doesn't", "together", "yet", "question", "anyway", "bad", "blue",
            "believe", "week", "God", "lip", "Mr.", "sleep", "fine", "what's", "family", "many", "worry", "roll",
            "parents", "under", "surprise", "water", "onto", "we're", "glance", "wall", "between", "seen", "read",
            "window", "idea", "white", "push", "feet", "must", "such", "seat", "set", "please", "red", "brother",
            "these", "whole", "lean", "part", "person", "slightly", "pass", "shook", "fact", "wrong", "gone", "far",
            "hit", "finger", "quite", "hate", "meet", "finish", "heart", "book", "past", "kill", "reason", "anyone",
            "figure", "top", "along", "world", "she'd", "high", "shirt", "does", "today", "young", "held", "outside",
            "listen", "whisper", "won't", "happy", "ground", "deep", "drop", "shrug", "dress", "fell", "there's",
            "yell", "breath", "air", "tear", "sister", "chair", "kitchen", "matter", "hurt", "fall", "wear", "isn't",
            "woman", "eat", "lie", "hell", "suppose", "cover", "couple", "large", "either", "five", "leg", "jump",
            "die", "return", "able", "bag", "alone", "shut", "stuff", "short", "ready", "understand", "kept", "plan",
            "raise", "street", "different", "problem", "break", "line", "early", "cut", "cold", "paper", "scream",
            "instead", "stupid", "silence", "tree", "caught", "ear", "food", "full", "four", "cause", "fuck", "explain",
            "expect", "fight", "exactly", "sort", "completely", "men", "dance", "met", "story", "whatever", "build",
            "speak", "glass", "pain", "check", "glare", "corner", "Mrs.", "chest", "hot", "rather", "month", "real",
            "touch", "park", "bring", "they're", "drink", "ago", "force", "you've", "fast", "lost", "attention", "wish",
            "mark", "wave", "shout", "fill", "begin", "baby", "interest", "money", "fun", "green", "however", "cheek",
            "mine", "clear", "brown", "forward", "near", "picture", "may", "cool", "drive", "hug", "shake", "sense",
            "alright", "you'll", "dream", "hang", "clothes", "act", "become", "manage", "meant", "game", "ignore",
            "stair", "taken", "party", "add", "sometimes", "job", "ten", "shot", "date", "quiet", "gaze", "group",
            "loud", "straight", "dead", "neck", "beside", "pause", "number", "conversation", "chance", "we'll", "rose",
            "quietly", "town", "blood", "color", "desk", "dinner", "hall", "horse", "music", "brought", "piece",
            "anymore", "beautiful", "order", "fire", "office", "true", "although", "warm", "easy", "enter", "perfect",
            "mutter", "softly", "cross", "shock", "smirk", "damn", "soft", "stomach", "snap", "spoke", "tire", "box",
            "catch", "skin", "teacher", "middle", "note", "yourself", "haven't", "lunch", "tomorrow", "breathe",
            "clean", "except", "appear", "lock", "knock", "bathroom", "movie", "agree", "offer", "kick", "form",
            "confuse", "lay", "less", "calm", "slip", "weren't", "sign", "dog", "lift", "immediately", "arrive", "deal",
            "tonight", "usually", "case", "frown", "shop", "scare", "promise", "mum", "couch", "pay", "state", "shit",
            "wrap", "pocket", "hello", "free", "huge", "ride", "bother", "land", "known", "especially", "expression",
            "carry", "ring", "spot", "allow", "several", "aren't", "during", "empty", "lady", "eyebrow", "strange",
            "coffee", "road", "threw", "wide", "bus", "forget", "gotten", "smell", "fear", "press", "boyfriend",
            "blonde", "throw", "round", "sun", "tall", "glad", "age", "write", "upon", "hide", "became", "crowd",
            "rain", "save", "trouble", "annoy", "nose", "weird", "death", "beat", "tone", "trip", "six", "control",
            "nearly", "consider", "gonna", "join", "learn", "above", "hi", "obviously", "entire", "direction", "foot",
            "angry", "power", "strong", "quick", "doctor", "edge", "song", "asleep", "twenty", "barely", "remain",
            "child", "enjoy", "gun", "slow", "city", "broke", "key", "lead", "throat", "normal", "you'd", "somewhere",
            "wake", "pair", "sky", "funny", "business", "student", "giggle", "bright", "admit", "jeans", "given",
            "children", "store", "sweet", "low", "climb", "rub", "apartment", "knee", "shoe", "attack", "bedroom",
            "joke", "spent", "situation", "stuck", "gently", "possible", "cell", "mention", "silent", "definitely",
            "rush", "hung", "brush", "perhaps", "groan", "ass", "rock", "card", "lose", "blush", "besides", "crazy",
            "type", "bore", "afraid", "chase", "respond", "marry", "remind", "pack", "daughter", "serious",
            "girlfriend", "mad", "somehow", "buy", "sent", "tight", "simply", "trust", "imagine", "wind", "chuckle",
            "bar", "pink", "shove", "ball", "sight", "drag", "they'd", "human", "truth", "share", "area", "concern",
            "shouldn't", "team", "escape", "mumble", "often", "search", "apparently", "attempt", "son", "within",
            "band", "cute", "led", "memory", "anger", "fly", "paint", "bottle", "busy", "comment", "exclaim", "avoid",
            "grow", "grey", "mirror", "gasp", "hallway", "dear", "star", "sick", "cat", "counter", "interrupt", "none",
            "blink", "spend", "usual", "worse", "locker", "important", "favorite", "grip", "TV", "ice", "pretend",
            "settle", "amaze", "pop", "disappear", "carefully", "train", "stick", "guard", "teeth", "flash", "uncle",
            "send", "doubt", "visit", "nervous", "excite", "approach", "excuse", "fit", "noise", "study", "letter",
            "police", "eventually", "burn", "field", "hospital", "tie", "summer", "huh", "shift", "self", "hurry",
            "greet", "wife", "position", "we've", "wipe", "heavy", "slam", "broken", "complete", "space", "brain",
            "tiny", "pants", "ah", "punch", "shower", "tongue", "afternoon", "seriously", "cup", "further", "race",
            "recognize", "computer", "rang", "safe", "jacket", "bottom", "hundred", "relax", "sudden", "wow", "college",
            "flip", "mood", "track", "crack", "block", "handle", "themselves", "drove", "seven", "struggle", "whether",
            "ahead", "sad", "dry", "women", "focus", "repeat", "thick", "relationship", "jerk", "present", "suck",
            "bell", "surround", "evening", "bite", "single", "fault", "shadow", "wood", "easily", "woke", "smoke",
            "draw", "suggest", "wet", "accept", "third", "totally", "wore", "breakfast", "trail", "animal", "warn",
            "aunt", "sir", "piss", "burst", "match", "fix", "practically"
    };


}











