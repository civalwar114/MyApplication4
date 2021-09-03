package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;


import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DataSnapshot;


import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private EditText EDT;
    private ListView Chat;
    private Button Send;


    private ArrayAdapter<String> arrayAdapter;

    private String str_name;
    private String str_EDT;
    private String user;
    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("message");





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Chat = (ListView) findViewById(R.id.Chat);
        Send = (Button) findViewById(R.id.Send);
        EDT = (EditText) findViewById(R.id.EDT);

        arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1);


        Chat.setAdapter(arrayAdapter);
        //<--자동 스크롤?-->
        Chat.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);

        str_name ="USER"+new Random().nextInt(1000);

        Send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Map<String,Object> map = new HashMap<String, Object>();
                // Toast.makeText(MainActivity.this, "asdf", Toast.LENGTH_SHORT).show(); //테스트용 토스트

                String key = reference.push().getKey(); //키로 데이터베이스 오픈
                reference.updateChildren(map);

                DatabaseReference dbref = reference.child(key);

                Map<String,Object> objectMap = new HashMap<String,Object>();

                objectMap.put("str_name",str_name);
                objectMap.put("text",EDT.getText().toString());

                dbref.updateChildren(objectMap);
                EDT.setText("");

            }
        });
        reference.addChildEventListener(new ChildEventListener() {
            @Override

            public void onChildAdded(DataSnapshot dataSnapshot,String s) {
                chatListener(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot,String s) {
                chatListener(dataSnapshot);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot,String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
    private  void chatListener(DataSnapshot dataSnapshot){

        //datasnapshot 밸류갑 가져오기
        Iterator i = dataSnapshot.getChildren().iterator();

        while (i.hasNext()){

            user=(String)((DataSnapshot)i.next()).getValue();
            str_EDT=(String)((DataSnapshot)i.next()).getValue();

            arrayAdapter.add(user+" : "+str_EDT);

        }

        arrayAdapter.notifyDataSetChanged();

    }


}