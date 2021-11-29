package com.example.leesunho;

import androidx.activity.result.ActivityResultLauncher;
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

import java.util.HashMap;

public class Add_Word extends AppCompatActivity {

    private Button btn_cancel;
    private Button btn_okay;
    private EditText et_spelling;
    private EditText et_meaning;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_word);

        btn_cancel = findViewById(R.id.btn_cancel);
        btn_okay = findViewById(R.id.btn_okay);
        et_spelling = findViewById(R.id.et_spelling);
        et_meaning = findViewById(R.id.et_meaning);

        Intent intent = getIntent();
        String selected = intent.getStringExtra("selected");
        String num = intent.getStringExtra("num");
        int temp = Integer.parseInt(num) + 1;
        String key = Integer.toString(temp);


        database = FirebaseDatabase.getInstance("https://leesunho-fed08-default-rtdb.firebaseio.com/"); //파이어베이스 데이터베이스 연동
        databaseReference = database.getReference(); //DB 테이블 연결

        btn_okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getSpelling = et_spelling.getText().toString();
                String getMeaning = et_meaning.getText().toString();

                if(getSpelling.replace(" ","").equals("") || getMeaning.replace(" ","").equals("")){
                    Toast.makeText(Add_Word.this, "입력을 완료해주세요.", Toast.LENGTH_SHORT).show();
                }
                else{
                    Word newWord = new Word();
                    newWord.setSpelling(getSpelling);
                    newWord.setMeaning(getMeaning);

                    databaseReference.child("단어장").child("내 단어장").child(selected).child(key).setValue(newWord)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(getApplicationContext(), "단어가 추가되었습니다.", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), Word_list.class);
                                    intent.putExtra("Spelling",newWord.getSpelling());
                                    intent.putExtra("Meaning",newWord.getMeaning());
                                    setResult(RESULT_OK,intent);
                                    finish();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getApplicationContext(), "단어 추가에 실패하였습니다.", Toast.LENGTH_SHORT).show();
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