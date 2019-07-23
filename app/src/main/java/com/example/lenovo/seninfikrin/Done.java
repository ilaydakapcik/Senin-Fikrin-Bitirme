package com.example.lenovo.seninfikrin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.lenovo.seninfikrin.Common.Common;
import com.example.lenovo.seninfikrin.Model.QuestionScore;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Done extends AppCompatActivity {

    Button btnbitis;
    TextView txtResultScore,getTxtResultQuestion;
    ProgressBar progressBar;

    FirebaseDatabase database;
    DatabaseReference question_score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_done);

        database=FirebaseDatabase.getInstance();
        question_score=database.getReference("Question_Score");
        txtResultScore=findViewById(R.id.txtTotalScore);
        getTxtResultQuestion=findViewById(R.id.txtTotalQuestion);
        progressBar=findViewById(R.id.doneProgressBar);
        btnbitis=findViewById(R.id.btnbitis);

        btnbitis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Done.this,Home.class);
                startActivity(intent);
                finish();


            }
        });

        Bundle extra=getIntent().getExtras();
        if(extra!=null)
        {
            int score=extra.getInt("SCORE");
            int totalQuestion=extra.getInt("TOTAL");


            txtResultScore.setText(String.format("SCORE : %d",score));
            progressBar.setMax(totalQuestion);

            question_score.child(String.format("%s_%s", Common.currentUser.getUserName(),
                                                        Common.kategori))
                    .setValue(new QuestionScore(String.format("%s_%s", Common.currentUser.getUserName(),
                            Common.kategori),
                            Common.currentUser.getUserName(),
                            String.valueOf(score)));

        }


    }
}
