package com.example.leesunho;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

public class Add_Word extends AppCompatActivity {

    private Button btn_cancel;
    private Button btn_okay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_word);

        btn_cancel = findViewById(R.id.btn_cancel);
        btn_okay = findViewById(R.id.btn_okay);

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