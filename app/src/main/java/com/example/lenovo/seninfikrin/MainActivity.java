package com.example.lenovo.seninfikrin;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.lenovo.seninfikrin.Common.Common;
import com.example.lenovo.seninfikrin.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

public class MainActivity extends AppCompatActivity {

    MaterialEditText edtNewUser,edtNewPassword,edtNewEmail;
    MaterialEditText edtUser,edtPassword;

    Button btnSignUp,btnSignIn;

    FirebaseDatabase database;
    DatabaseReference users;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Firebase
        database=FirebaseDatabase.getInstance();
        users=database.getReference("Users");


        edtUser=findViewById(R.id.edtUser);
        edtPassword=findViewById(R.id.edtPassword);
        btnSignIn=findViewById(R.id.btn_sign_in);
        btnSignUp=findViewById(R.id.btn_sign_up);



        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSignUpDialog();
            }
        });


        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn(edtUser.getText().toString(),edtPassword.getText().toString());
            }
        });

    }

    private void signIn(final String user, final String pwd) {
        users.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(user).exists())
                {
                    if(!user.isEmpty())
                    {
                        User login=dataSnapshot.child(user).getValue(User.class);
                        if(login.getPassword().equals(pwd))
                        {
                            Toast.makeText(MainActivity.this,edtUser.getText().toString(),Toast.LENGTH_SHORT).show();
                           Intent homeActivity = new Intent(MainActivity.this,Home.class);
                           Common.currentUser=login;
                           startActivity(homeActivity);
                           finish();
                        }
                        else
                            Toast.makeText(MainActivity.this,"Yanlış Şifre",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(MainActivity.this,"Lütfen Kullanıcı Adını Giriniz ",Toast.LENGTH_SHORT).show();
                    }
                }
                else
                    Toast.makeText(MainActivity.this,"Kullanıcı Bulunamadı.",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void showSignUpDialog() {
        AlertDialog.Builder alertDialog=new AlertDialog.Builder(MainActivity.this);
        alertDialog.setTitle("Kayıt Ol");
        alertDialog.setMessage("Lütfen Doldurunuz");

        LayoutInflater inflater=this.getLayoutInflater();
        View signup_layout=inflater.inflate(R.layout.sign_up_layout,null);

        edtNewUser=signup_layout.findViewById(R.id.edtNewUserName);
        edtNewEmail=signup_layout.findViewById(R.id.edtNewEmail);
        edtNewPassword=signup_layout.findViewById(R.id.edtNewPassword);

        alertDialog.setView(signup_layout);

        alertDialog.setIcon(R.drawable.ic_account_circle_black_24dp);

        alertDialog.setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();



            }
        });

        alertDialog.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                final User user = new User(edtNewUser.getText().toString(),
                        edtNewPassword.getText().toString(),
                        edtNewEmail.getText().toString());


                users.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child(user.getUserName()).exists())
                            Toast.makeText(MainActivity.this, "Kullanıcı adı mevcut!!", Toast.LENGTH_SHORT).show();

                        else {
                            users.child(user.getUserName()).setValue(user);
                            Toast.makeText(MainActivity.this, "Kullanıcı Kaydı Başarılı!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {


                    }
                });
                dialogInterface.dismiss();
            }


        });

        alertDialog.show();


    }
}
