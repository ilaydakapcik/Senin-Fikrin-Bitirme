package com.example.lenovo.seninfikrin;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.lenovo.seninfikrin.Common.Common;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class anket extends AppCompatActivity implements View.OnClickListener{


    final static long INTERVAL=100000;  //1 sec
    final static long TIMEOUT=100000;  //7 sec

    int progressValue=0;
    CountDownTimer mCountDown;

    int index=0,score=0,thisQuestion=0,totalQuestion=0;



    ProgressBar progressBar;
    Button btn1,btn2,btn3,btn4;
    TextView txtScore,txtQuestionNum,question_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anket);



        //Views

        txtScore=findViewById(R.id.txtScore);
        txtQuestionNum=findViewById(R.id.txtTotalQuestion);
        question_text=findViewById(R.id.soru_text);

        progressBar = findViewById(R.id.progressBar);


        btn1=findViewById(R.id.btnCevap1);
        btn2=findViewById(R.id.btnCevap2);
        btn3=findViewById(R.id.btnCevap3);
        btn4=findViewById(R.id.btnCevap4);

        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        mCountDown.cancel();
        if(index < totalQuestion)  //still have question in list
        {
            Button clickedButton = (Button)v;
            score+=10;
            showQuestion(++index); //next question
            //txtScore.setText(String.format("%d" ,score));

        }

    }

    private void showQuestion(int index) {

        if(index < totalQuestion)
        {

            thisQuestion++;
            txtQuestionNum.setText(String.format("%d / %d",thisQuestion,totalQuestion));

            progressBar.setProgress(0);
            progressValue=0;

            question_text.setText(Common.soruList.get(index).getSoru());
            btn1.setText(Common.soruList.get(index).getCevap1());
            btn2.setText(Common.soruList.get(index).getCevap2());
            btn3.setText(Common.soruList.get(index).getCevap3());
            btn4.setText(Common.soruList.get(index).getCevap4());

            mCountDown.start(); //Start Timer
        }

        else
        {
            //eğer son soruysa
            Intent intent = new Intent(this,Done.class);
            Bundle dataSend = new Bundle();
            dataSend.putInt("SCORE",score);
            dataSend.putInt("TOTAL",totalQuestion);
            intent.putExtras(dataSend);
            startActivity(intent);
            finish();


        }





    }

    @Override
    protected void onResume() {
        super.onResume();

        totalQuestion=Common.soruList.size();

        mCountDown = new CountDownTimer(TIMEOUT,INTERVAL){


            @Override
            public void onTick(long minisec) {
                progressBar.setProgress(progressValue);
                progressValue++;
            }

            @Override
            public void onFinish() {
                mCountDown.cancel();
                showQuestion(++index);


            }
        };

        showQuestion(index);

    }
}
