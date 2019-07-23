package com.example.lenovo.seninfikrin;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.lenovo.seninfikrin.Common.Common;
import com.example.lenovo.seninfikrin.Model.Soru;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Collection;
import java.util.Collections;

import static com.example.lenovo.seninfikrin.Common.Common.kategori;

public class Start extends AppCompatActivity {


    Button btnPlay;

    FirebaseDatabase database;
    DatabaseReference anket;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        database = FirebaseDatabase.getInstance();
        anket=database.getReference("anket");

        loadQuestion(Common.kategori);

        btnPlay=findViewById(R.id.btnPlay);

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(Start.this,anket.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void loadQuestion(String kategori) {
        // ilk önce eski sorular varsa listeyi temizle

        if(Common.soruList.size()>0)
            Common.soruList.clear();

        anket.orderByChild("kategori").equalTo(kategori).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    Soru soru=postSnapshot.getValue(Soru.class);
                    Common.soruList.add(soru);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //Rastgele Liste
        Collections.shuffle(Common.soruList);
    }
}
