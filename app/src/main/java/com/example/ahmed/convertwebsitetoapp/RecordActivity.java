package com.example.ahmed.convertwebsitetoapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ahmed.convertwebsitetoapp.chatting.User;
import com.example.ahmed.convertwebsitetoapp.model.MessageItem;
import com.example.ahmed.convertwebsitetoapp.ui.ViewProxy;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class RecordActivity extends AppCompatActivity {
    private TextView recordTimeText;
    private ImageButton audioSendButton;
    private View recordPanel;
    private View slideText;
    private float startedDraggingX = -1;
    private float distCanMove = dp(80);
    private long startTime = 0L;
    long timeInMilliseconds = 0L;
    long timeSwapBuff = 0L;
    long updatedTime = 0L;
    private Timer timer;
    /////////////////////
    EditText etMessage = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record_vid);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Your toolbar is now an action bar and you can use it like you always do, for example:
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        init1();
        init2();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
               showDummyMessage();
            }
        }, 2000);

    }

    LinearLayout llChatContainer = null;
    ListView listViewChats = null;
    ListAdapter listAdapter = null;
    ArrayList<MessageItem> messageItems = new ArrayList<MessageItem>();

    public void init1() {

        //llChatContainer = (LinearLayout) findViewById(R.id.llChatContainer);
        listViewChats = (ListView) findViewById(R.id.lvChatItems);
        listAdapter = new ListAdapter(getApplicationContext(), messageItems);
        listViewChats.setAdapter(listAdapter);

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                showDummyMessage();
//            }
//        }, 500);
        sharedPreferences = getSharedPreferences(KEY_USER_CHAT, MODE_PRIVATE);
        editor = sharedPreferences.edit();
//        key = FirebaseDatabase.getInstance().getReference().push().getKey();
////        editor.putString(KEY_VAL, key);
////        editor.apply();
////        editor.commit();

        //////////////////////////////////////////
        etMessage = (EditText) findViewById(R.id.etMsmContainer);
        final ImageView imageView = (ImageView) findViewById(R.id.ivSendMessage);
        etMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s)) {
                    imageView.setEnabled(false);
                } else {
                    imageView.setEnabled(true);
                }
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(RecordActivity.this, "click", Toast.LENGTH_SHORT).show();
                sendMessage();
            }
        });
        ////////////////////////////////////////////////////
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
//
//
//
    }

    public class ListAdapter extends BaseAdapter {

        Context context = null;
        ArrayList<MessageItem> messageItems = null;
        int flag = 0;

        public ListAdapter(Context context, ArrayList<MessageItem> messageItems) {
            this.context = context;
            this.messageItems = messageItems;
            this.flag = flag;
        }


        @Override
        public int getCount() {
            return this.messageItems.size();
        }

        @Override
        public MessageItem getItem(int position) {
            return this.messageItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View view = null;

            MessageItem messageItem = getItem(position);
            String statusWho = messageItem.getRightOrLeft();

            if (statusWho == "right") {
                view = LayoutInflater.from(context).inflate(R.layout.user_right, null);
                TextView textView = (TextView) view.findViewById(R.id.tvUserRight);
                textView.setText(messageItem.getMessageContent());
//                view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.slide_from_right));
//                MediaPlayer.create(context, R.raw.alert).start();

            } else {
                view = LayoutInflater.from(context).inflate(R.layout.user_left, null);
                TextView textView = (TextView) view.findViewById(R.id.tvUserLeft);
                textView.setText(messageItem.getMessageContent());
//                    MediaPlayer.create(context, R.raw.alert2).start();
//                    view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.slide_from_left));

            }


            return view;
        }
    }

    public final static String KEY_USER_CHAT = "KEY_USER_CHAT";
    public final static int MODE = Context.MODE_PRIVATE;
    SharedPreferences sharedPreferences = null;
    SharedPreferences.Editor editor = null;
    public final static String KEY_VAL = "KEY_VAL";
    String key = "";

    public void sendMessage() {
        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);
//        String key = FirebaseDatabase.getInstance().getReference().push().getKey();
        FirebaseDatabase.getInstance().getReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(RecordActivity.this.key) /*|| sharedPreferences.contains(KEY_VAL)*/) {
                    Toast.makeText(RecordActivity.this, "exist", Toast.LENGTH_SHORT).show();
                    String key = sharedPreferences.getString(KEY_VAL, "");
                    FirebaseDatabase.getInstance().getReference().child(key)
                            .push().setValue(new MessageItem("", etMessage.getText().toString(), "right"));


                }else{
                    Toast.makeText(RecordActivity.this, "not exist", Toast.LENGTH_SHORT).show();
                    String  txt = etMessage.getText().toString();
                    RecordActivity.this.key = FirebaseDatabase.getInstance().getReference().push().getKey();
                    FirebaseDatabase.getInstance().getReference().child(key).push().setValue(new MessageItem("", etMessage.getText().toString(), "right"));
                    editor.putString(KEY_VAL, RecordActivity.this.key);
                    editor.apply();
                    editor.commit();

                    //adding the user
                    FirebaseDatabase.getInstance().getReference().child("users").push().setValue(key);
                }
                etMessage.getText().clear();
                MediaPlayer.create(getApplicationContext(), R.raw.alert2).start();
                refresh();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    public void refresh(){
        FirebaseDatabase.getInstance().getReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(key)) {
                    Toast.makeText(RecordActivity.this, "here", Toast.LENGTH_SHORT).show();
                    FirebaseDatabase.getInstance().getReference().child(key).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            messageItems.clear();
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                MessageItem messageItem = dataSnapshot1.getValue(MessageItem.class);
                                //messageItems.add(new MessageItem("", messageItem.getRightOrLeft(), messageItem.getMessageContent()));
                                messageItems.add(messageItem);
                                //Log.e("res4152", messageItem.getMessageContent());
                                //Toast.makeText(RecordActivity.this, messageItem.getMessageContent(), Toast.LENGTH_SHORT).show();
                            }
                            //Toast.makeText(RecordActivity.this, messageItems.size()+"", Toast.LENGTH_SHORT).show();
                            listAdapter = new ListAdapter(getApplicationContext(), messageItems);
                            listViewChats.setAdapter(listAdapter);
                            listAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void showDummyMessage() {

//        View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.user_left, null);
//        llChatContainer.addView(view);
//        view.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_from_left));
        messageItems.add(new MessageItem("", "Can i help u in developing your desired website or mobile application", "left"));
        MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.alert);
        mediaPlayer.start();
        listViewChats.setAdapter(new ListAdapter(getApplicationContext(), messageItems));


    }

    public void init2() {


        recordPanel = findViewById(R.id.record_panel);
        recordTimeText = (TextView) findViewById(R.id.recording_time_text);
        slideText = findViewById(R.id.slideText);
        audioSendButton = (ImageButton) findViewById(R.id.chat_audio_send_button);
        TextView textView = (TextView) findViewById(R.id.slideToCancelTextView);
        textView.setText("SlideToCancel");
        audioSendButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) slideText
                            .getLayoutParams();
                    params.leftMargin = dp(30);
                    slideText.setLayoutParams(params);
                    ViewProxy.setAlpha(slideText, 1);
                    startedDraggingX = -1;
                    // startRecording();
                    startrecord();
                    audioSendButton.getParent()
                            .requestDisallowInterceptTouchEvent(true);
                    recordPanel.setVisibility(View.VISIBLE);
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP
                        || motionEvent.getAction() == MotionEvent.ACTION_CANCEL) {
                    startedDraggingX = -1;
                    stoprecord();
                    // stopRecording(true);
                } else if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                    float x = motionEvent.getX();
                    if (x < -distCanMove) {
                        stoprecord();
                        // stopRecording(false);
                    }
                    x = x + ViewProxy.getX(audioSendButton);
                    FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) slideText
                            .getLayoutParams();
                    if (startedDraggingX != -1) {
                        float dist = (x - startedDraggingX);
                        params.leftMargin = dp(30) + (int) dist;
                        slideText.setLayoutParams(params);
                        float alpha = 1.0f + dist / distCanMove;
                        if (alpha > 1) {
                            alpha = 1;
                        } else if (alpha < 0) {
                            alpha = 0;
                        }
                        ViewProxy.setAlpha(slideText, alpha);
                    }
                    if (x <= ViewProxy.getX(slideText) + slideText.getWidth()
                            + dp(30)) {
                        if (startedDraggingX == -1) {
                            startedDraggingX = x;
                            distCanMove = (recordPanel.getMeasuredWidth()
                                    - slideText.getMeasuredWidth() - dp(48)) / 2.0f;
                            if (distCanMove <= 0) {
                                distCanMove = dp(80);
                            } else if (distCanMove > dp(80)) {
                                distCanMove = dp(80);
                            }
                        }
                    }
                    if (params.leftMargin > dp(30)) {
                        params.leftMargin = dp(30);
                        slideText.setLayoutParams(params);
                        ViewProxy.setAlpha(slideText, 1);
                        startedDraggingX = -1;
                    }
                }
                view.onTouchEvent(motionEvent);
                return true;
            }
        });
    }

    private void startrecord() {
        // TODO Auto-generated method stub
        startTime = SystemClock.uptimeMillis();
        timer = new Timer();
        MyTimerTask myTimerTask = new MyTimerTask();
        timer.schedule(myTimerTask, 1000, 1000);
        vibrate();
    }

    private void stoprecord() {
        // TODO Auto-generated method stub
        if (timer != null) {
            timer.cancel();
        }
        if (recordTimeText.getText().toString().equals("00:00")) {
            return;
        }
        recordTimeText.setText("00:00");
        vibrate();
    }

    private void vibrate() {
        // TODO Auto-generated method stub
        try {
            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(200);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int dp(float value) {
        return (int) Math.ceil(1 * value);
    }

    class MyTimerTask extends TimerTask {

        @Override
        public void run() {
            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
            updatedTime = timeSwapBuff + timeInMilliseconds;
            final String hms = String.format(
                    "%02d:%02d",
                    TimeUnit.MILLISECONDS.toMinutes(updatedTime)
                            - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS
                            .toHours(updatedTime)),
                    TimeUnit.MILLISECONDS.toSeconds(updatedTime)
                            - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS
                            .toMinutes(updatedTime)));
            long lastsec = TimeUnit.MILLISECONDS.toSeconds(updatedTime)
                    - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS
                    .toMinutes(updatedTime));
            System.out.println(lastsec + " hms " + hms);
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    try {
                        if (recordTimeText != null)
                            recordTimeText.setText(hms);
                    } catch (Exception e) {
                        // TODO: handle exception
                    }

                }
            });
        }
    }

    //	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.main, menu);
//		return true;
//	}
//
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
//		int id = item.getItemId();
//		if (id == R.id.action_settings) {
//			return true;
//		}
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                overridePendingTransitionExit();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void finish() {
        super.finish();
        overridePendingTransitionExit();
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransitionEnter();
    }

    /**
     * Overrides the pending Activity transition by performing the "Enter" animation.
     */
    protected void overridePendingTransitionEnter() {
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }

    /**
     * Overrides the pending Activity transition by performing the "Exit" animation.
     */
    protected void overridePendingTransitionExit() {
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

}
