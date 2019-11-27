package  com.example.propman;


import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;


public class Messaging extends AppCompatActivity {
    LinearLayout linear;
    RelativeLayout relative;
    ImageView send;
    EditText messageArea;
    ScrollView scrollView;
    Firebase reference1, reference2;
    final private String FCM_API = "https://fcm.googleapis.com/fcm/send";
    final private String serverKey = "key=" + "AAAAERSaSd4:APA91bFoMzFZ8RnrhePzG8a36Cujw9T-ZvH16vzsyGuzHurYIKOOwU8dV6kGaF0ghlFbJ5zO5ljar6lwI5NbdWmk06ek8p3jEKrcZJ8lKdh8t6buk_UcNJew5Zj6R2FYpzp7zFeWda6g";
    final private String contentType = "application/json";
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    final FirebaseUser user = mAuth.getCurrentUser();
    final String uid2 = user.getUid();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);

        linear = findViewById(R.id.layout1);
        relative = findViewById(R.id.layout2);
        send = findViewById(R.id.sendButton);
        messageArea = findViewById(R.id.messageArea);
        scrollView = findViewById(R.id.scrollView);
        if(ViewProperty.solver) {
            Intent i = getIntent();
            System.out.println("CHATWITH1 " + UserDetails.chatWith);
            System.out.println("CHATWITH2 " + UserDetails.chatWith);
            String uid4sp = i.getExtras().getString("uidx");
            UserDetails.username = uid2;
            UserDetails.chatWith = uid4sp;
            System.out.println("UID 5 APPERIANCE " + uid4sp);
            System.out.println("atat" + UserDetails.chatWith);
            ViewProperty.solver = false;
        }


        Firebase.setAndroidContext(this);
        reference1 = new Firebase("https://propman-de374.firebaseio.com/messages/" + UserDetails.username + "_" + UserDetails.chatWith);
        reference2 = new Firebase("https://propman-de374.firebaseio.com/messages/" + UserDetails.chatWith + "_" + UserDetails.username);
        System.out.println("REFRECEN 1:"+reference1);
        System.out.println("REFRECEN 1:"+reference2);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = messageArea.getText().toString();

                if(!messageText.equals("")){
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("message", messageText);
                    map.put("user", UserDetails.username);
                    reference1.push().setValue(map);
                    reference2.push().setValue(map);
                    messageArea.setText("");
                    notification(UserDetails.chatWith,"Message Received", messageText);
                }
                System.out.println("BURASI 1");
            }
        });


        System.out.println("BURASI 2");
        reference1.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map map = dataSnapshot.getValue(Map.class);
                String message = map.get("message").toString();
                String userName = map.get("user").toString();
                System.out.println(UserDetails.chatWith + " " + userName + " " + message);

                System.out.println("BURASI 3");

                if(userName.equals(UserDetails.username)){
                    addMessageBox(message, 1);
                }
                else{
                    addMessageBox(message, 2);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public void addMessageBox(String message, int type){
        TextView textView = new TextView(Messaging.this);
        textView.setTextSize(25);
        textView.setText(message);

        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp2.weight = 7.0f;

        if(type == 1) {
            lp2.gravity = Gravity.LEFT;
            textView.setBackgroundResource(R.drawable.bubble_in);
        }
        else{
            lp2.gravity = Gravity.RIGHT;
            textView.setBackgroundResource(R.drawable.bubble_out);
        }
        textView.setLayoutParams(lp2);
        linear.addView(textView);
        scrollView.fullScroll(View.FOCUS_DOWN);

    }

    public void notification(String uid,String NOTIFICATION_TITLE, String NOTIFICATION_MESSAGE){


        String TOPIC = "/topics/" + uid ; //topic has to match what the receiver subscribed to


        JSONObject notification = new JSONObject();
        JSONObject notifcationBody = new JSONObject();

        try {

            notifcationBody.put("type", "MESSAGE");
            notifcationBody.put("title", NOTIFICATION_TITLE);
            notifcationBody.put("message", NOTIFICATION_MESSAGE);
            notifcationBody.put("id", uid2);

            notification.put("to", TOPIC);
            notification.put("data", notifcationBody);

        } catch (JSONException e) {

        }

        sendNotification(notification);
    }

    private void sendNotification(JSONObject notification) {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(FCM_API, notification,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("onResponse: " + response.toString());

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(Messaging.this, "Request error", Toast.LENGTH_LONG).show();

                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("Authorization", serverKey);
                params.put("Content-Type", contentType);
                return params;
            }
        };
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }
}