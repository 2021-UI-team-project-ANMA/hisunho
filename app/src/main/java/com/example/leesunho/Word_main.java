package com.example.leesunho;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Word_main extends AppCompatActivity {

    private ArrayList<RecommendList> arrayList;
    private ArrayList<MyWordList> arrayList2;
    private RecommendListAdapter recommendListAdapter;
    private MyWordListAdapter myWordListAdapter;
    private RecyclerView recyclerView;
    private RecyclerView recyclerView2;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.LayoutManager layoutManager2;
    private FirebaseDatabase database;
    private FirebaseDatabase database2;
    private DatabaseReference databaseReference;
    private DatabaseReference databaseReference2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_main);

        /* 추천단어장, 내 단어장 나눠주는 탭 부분 */
        TabHost tabHost = findViewById(R.id.host);
        tabHost.setup();

        TabHost.TabSpec spec = tabHost.newTabSpec("tab1");
        spec.setIndicator("추천 단어장");
        spec.setContent(R.id.tab_content1);
        tabHost.addTab(spec);

        spec = tabHost.newTabSpec("tab2");
        spec.setIndicator("내 단어장");
        spec.setContent(R.id.tab_content2);
        tabHost.addTab(spec);

        /* 탭부분 글자색 흰색으로 변경 */
        for(int i = 0; i < tabHost.getTabWidget().getChildCount(); i++){
            TextView tv = (TextView)tabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
            tv.setTextColor(Color.parseColor("#FFFFFF"));
        }

        /* 언어를 선택하는 스피너 부분 */
        Spinner spinner = findViewById(R.id.spinner);
        String[] languages = getResources().getStringArray(R.array.languages);
        ArrayAdapter<String> adapter_spinner = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, languages);
        adapter_spinner.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(adapter_spinner);

        /* 추천 단어장 리사이클러뷰 */
        recyclerView = findViewById(R.id.recyclerview_recommend_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        arrayList = new ArrayList<>(); //데이터베이스에서 불러온 객체를 담을 리스트

        database = FirebaseDatabase.getInstance("https://leesunho-fed08-default-rtdb.firebaseio.com/"); //파이어베이스 데이터베이스 연동
        databaseReference = database.getReference("RecommendList"); //DB 테이블 연결
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //파이어베이스 데이터베이스의 데이터를 받아오는 곳
                arrayList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){ //반복문으로 데이터 List를 추출
                    RecommendList recommendList = snapshot.getValue(RecommendList.class); // 만들어둔 객체에 데이터를 담는다.
                    arrayList.add(recommendList);
                }
                recommendListAdapter.notifyDataSetChanged(); //리스트 저장 및 새로고침
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //에러 발생 시
                Log.e("word1-database1", String.valueOf(databaseError.toException())); //에러문 출력

            }
        });

        recommendListAdapter = new RecommendListAdapter(this, arrayList);
        recyclerView.setAdapter(recommendListAdapter);

        //리사이클러뷰 아이템 간격 조절
        RecyclerDecoration_Height decoration_height = new RecyclerDecoration_Height(50);
        recyclerView.addItemDecoration(decoration_height);


        /* 내 단어장 리사이클러뷰 */
        recyclerView2 = findViewById(R.id.recyclerview_my_word);
        recyclerView2.setHasFixedSize(true);
        layoutManager2 = new LinearLayoutManager(this);
        recyclerView2.setLayoutManager(layoutManager2);
        arrayList2 = new ArrayList<>(); //데이터베이스에서 불러온 객체를 담을 리스트

        database2 = FirebaseDatabase.getInstance("https://leesunho-fed08-default-rtdb.firebaseio.com/"); //파이어베이스 데이터베이스 연동
        databaseReference2 = database2.getReference("MyWordList"); //DB 테이블 연결

        databaseReference2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //파이어베이스 데이터베이스의 데이터를 받아오는 곳
                arrayList2.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){ //반복문으로 데이터 List를 추출
                    MyWordList myWordList = snapshot.getValue(MyWordList.class); // 만들어둔 객체에 데이터를 담는다.
                    arrayList2.add(myWordList);
                }
                myWordListAdapter.notifyDataSetChanged(); //리스트 저장 및 새로고침
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //에러 발생 시
                Log.e("word1-database2", String.valueOf(databaseError.toException())); //에러문 출력

            }
        });

        myWordListAdapter = new MyWordListAdapter(this, arrayList2) {
        };
        recyclerView2.setAdapter(myWordListAdapter);

        //리사이클러뷰 아이템 간격 조절
        recyclerView2.addItemDecoration(decoration_height);

    }
}