package com.example.leesunho;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Revise_word extends AppCompatActivity {

    private Button btn_cancel;
    private Button btn_okay;
    private EditText et_spelling;
    private EditText et_meaning;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revise_word);

        btn_cancel = findViewById(R.id.btn_cancel);
        btn_okay = findViewById(R.id.btn_okay);
        et_spelling = findViewById(R.id.et_spelling);
        et_meaning = findViewById(R.id.et_meaning);

        Intent intent = getIntent();
        String spelling = intent.getStringExtra("Spelling");
        String meaning = intent.getStringExtra("Meaning");
        String id = intent.getStringExtra("id");
        int position = intent.getIntExtra("position",0);
        String selected = intent.getStringExtra("selected");

        et_spelling.setText(spelling);
        et_meaning.setText(meaning);

        database = FirebaseDatabase.getInstance("https://leesunho-fed08-default-rtdb.firebaseio.com/"); //파이어베이스 데이터베이스 연동
        databaseReference = database.getReference(); //DB 테이블 연결

        // 수정 버튼 이벤트
        btn_okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getSpelling = et_spelling.getText().toString();
                String getMeaning = et_meaning.getText().toString();

                if (getSpelling.replace(" ", "").equals("") || getMeaning.replace(" ", "").equals("")) {
                    Toast.makeText(Revise_word.this, "입력을 완료해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    Word newWord = new Word();
                    newWord.setSpelling(getSpelling);
                    newWord.setMeaning(getMeaning);

                    databaseReference.child("단어장").child("내 단어장").child(selected).child(id).setValue(newWord)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(Revise_word.this, "단어가 수정되었습니다.", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), Word_list.class);
                                    intent.putExtra("Spelling",newWord.getSpelling());
                                    intent.putExtra("Meaning",newWord.getMeaning());
                                    intent.putExtra("position",position);
                                    setResult(2,intent);
                                    finish();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Revise_word.this, "단어 수정에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        // 취소 버튼 이벤트
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
    public boolean onTouchEvent(MotionEvent event) {
        //배경 클릭시 꺼지는거 막기
        if( event.getAction() == MotionEvent.ACTION_OUTSIDE ) {
            return false;
        }
        return true;
    }
}