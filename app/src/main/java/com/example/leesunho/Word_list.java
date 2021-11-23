package com.example.leesunho;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Word_list extends AppCompatActivity {

    private ArrayList<Word> arrayList;
    private WordAdapter wordAdapter;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private ImageButton btn_add_word;
    private Button btn_edit_word;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_list);

        recyclerView = findViewById(R.id.recyclerview_word);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        arrayList = new ArrayList<>(); //데이터베이스에서 불러온 객체를 담을 리스트

        Intent intent = getIntent();
        String selected = intent.getStringExtra("title");

        database = FirebaseDatabase.getInstance("https://leesunho-fed08-default-rtdb.firebaseio.com/"); //파이어베이스 데이터베이스 연동
        databaseReference = database.getReference(); //DB 테이블 연결
        databaseReference.child("단어장").child("내 단어장").child(selected).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //파이어베이스 데이터베이스의 데이터를 받아오는 곳
                arrayList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){ //반복문으로 데이터 List를 추출
                    Word word = snapshot.getValue(Word.class); // 만들어둔 객체에 데이터를 담는다.
                    arrayList.add(word);
                }
                wordAdapter.notifyDataSetChanged(); //리스트 저장 및 새로고침
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //에러 발생 시
                Log.e("Word_list", String.valueOf(databaseError.toException())); //에러문 출력

            }
        });

        wordAdapter = new WordAdapter(this, arrayList);
        recyclerView.setAdapter(wordAdapter);

        //리사이클러뷰 아이템 간격 조절
        RecyclerDecoration_Height decoration_height = new RecyclerDecoration_Height(10);
        recyclerView.addItemDecoration(decoration_height);

        // 단어 추가 버튼
        btn_add_word = (ImageButton) findViewById(R.id.btn_add_word);
        btn_add_word.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Add_Word.class);
                intent.putExtra("selected",selected);
                intent.putExtra("num",Integer.toString(arrayList.size()));
                startActivity(intent);
            }
        });

    }
}