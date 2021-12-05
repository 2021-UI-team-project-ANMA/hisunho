package com.example.leesunho;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
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
    private Word_Edit_Adapter word_edit_adapter;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private ImageButton btn_add_word;
    private Button btn_edit_word;
    private Button btn_hide_spelling;
    private Button btn_hide_meaning;
    private ActivityResultLauncher<Intent> resultLauncher;
    private AlertDialog alertDialog; // 삭제 확인 다이얼로그


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
                    word.setId(snapshot.getKey());
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

        //액티비티 콜백 함수
        resultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if(result.getResultCode() == 1){
                            Intent intent = result.getData();
                            String spelling = intent.getStringExtra("Spelling");
                            String meaning = intent.getStringExtra("Meaning");

                            Word word = new Word();
                            word.setSpelling(spelling);
                            word.setMeaning(meaning);
                            arrayList.add(word);
                            wordAdapter.notifyItemInserted(wordAdapter.getItemCount());
                        }
                        else if(result.getResultCode() == 2){
                            System.out.println(result.getResultCode());
                            Intent intent = result.getData();
                            String spelling = intent.getStringExtra("Spelling");
                            String meaning = intent.getStringExtra("Meaning");
                            int position = intent.getIntExtra("position",0);

                            arrayList.get(position).setSpelling(spelling);
                            arrayList.get(position).setMeaning(meaning);
                            word_edit_adapter.notifyItemChanged(position);
                        }
                    }
                });

        // 단어 추가 버튼
        btn_add_word = (ImageButton) findViewById(R.id.btn_add_word);
        btn_add_word.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String last_id = arrayList.get(arrayList.size()-1).getId();
                Intent intent = new Intent(getApplicationContext(), Add_Word.class);
                intent.putExtra("selected",selected);
                intent.putExtra("last_id", last_id);
                resultLauncher.launch(intent);
            }
        });

        // 편집 버튼
        btn_edit_word = (Button)findViewById(R.id.btn_edit_word);
        btn_edit_word.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(recyclerView.getAdapter() == wordAdapter){
                    btn_edit_word.setText("완료");
                    btn_hide_spelling.setVisibility(View.INVISIBLE);
                    btn_hide_meaning.setVisibility(View.INVISIBLE);
                    recyclerView.setAdapter(word_edit_adapter);
                }
                else{
                    btn_edit_word.setText("편집");
                    btn_hide_spelling.setVisibility(View.VISIBLE);
                    btn_hide_meaning.setVisibility(View.VISIBLE);
                    recyclerView.setAdapter(wordAdapter);
                }

            }
        });

        //스펠링 가리기
        btn_hide_spelling = findViewById(R.id.btn_hide_spelling);
        btn_hide_spelling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(wordAdapter.getHide_spelling() == 0){
                    btn_hide_spelling.setText("단어 보이기");
                    wordAdapter.setHide_spelling(1);
                    wordAdapter.notifyDataSetChanged();
                }
                else{
                    btn_hide_spelling.setText("단어 가리기");
                    wordAdapter.setHide_spelling(0);
                    wordAdapter.notifyDataSetChanged();
                }
            }
        });

        //뜻 가리기
        btn_hide_meaning = findViewById(R.id.btn_hide_meaning);
        btn_hide_meaning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(wordAdapter.getHide_meaning() == 0){
                    btn_hide_meaning.setText("뜻 보이기");
                    wordAdapter.setHide_meaning(1);
                    wordAdapter.notifyDataSetChanged();
                }
                else{
                    btn_hide_meaning.setText("뜻 가리기");
                    wordAdapter.setHide_meaning(0);
                    wordAdapter.notifyDataSetChanged();
                }
            }
        });




        btn_hide_meaning = findViewById(R.id.btn_hide_meaning);

        word_edit_adapter = new Word_Edit_Adapter(this, arrayList);
        word_edit_adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onReviseClick(View v, int position) {
                Intent intent = new Intent(getApplicationContext(), Revise_word.class);
                intent.putExtra("Spelling", arrayList.get(position).getSpelling());
                intent.putExtra("Meaning", arrayList.get(position).getMeaning());
                intent.putExtra("id",arrayList.get(position).getId());
                intent.putExtra("selected",selected);
                intent.putExtra("position",position);
                resultLauncher.launch(intent);
            }

            @Override
            public void onDeleteClick(View v, int position) {

                DialogInterface.OnClickListener dialogListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(i == DialogInterface.BUTTON_POSITIVE){
                            delete(position);
                            arrayList.remove(position);
                            word_edit_adapter.notifyItemRemoved(position);
                            wordAdapter.notifyItemRemoved(position);
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(Word_list.this);
                builder.setIcon(R.drawable.ic_baseline_warning_24);
                builder.setTitle("삭제 확인");
                builder.setMessage("정말 삭제하시겠습니까?");

                builder.setPositiveButton("삭제", dialogListener);
                builder.setNegativeButton("취소", null);

                alertDialog = builder.create();
                alertDialog = builder.show();
            }
            private void delete(int position){
                String id = arrayList.get(position).getId();
                System.out.println(id);
                databaseReference.child("단어장").child("내 단어장").child(selected).child(id).setValue(null);
            }
        });
    }

}