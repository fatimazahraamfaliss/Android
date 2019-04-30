package com.example.like.miniprojetandroid;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.internal.WebDialog;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {

    private EditText edituser,editpassword,editconfirmpassword;
    Button register;
    Login login;
    private DatabaseReference mUsers;
    public ProgressDialog dialog ;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        edituser=(EditText)findViewById(R.id.etUsername);
        editpassword=(EditText)findViewById(R.id.etPassword);
        editconfirmpassword=(EditText)findViewById(R.id.etPassword2);
        mUsers= FirebaseDatabase.getInstance().getReference("Login");
        dialog=new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();

        register=(Button) findViewById(R.id.register);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               // registerUser();
                String name = edituser.getText().toString().trim();
                String password = editpassword.getText().toString().trim();


                login = new Login();
                login.setName(name);
                login.setPassword(password);

                String uploadId = mUsers.push().getKey();

                mUsers.child(uploadId).setValue(login);
                dialog.setMessage("Utilisateur est bien Enregistrer");
                dialog.show();
            }
        });



    }

  /*  private void registerUser(){

        //getting email and password from edit texts
        String email = edituser.getText().toString().trim();
        String password  = editpassword.getText().toString().trim();

        //checking if email and passwords are empty
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Please enter email",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Please enter password", Toast.LENGTH_LONG).show();
            return;
        }

        //if the email and password are not empty
        //displaying a progress dialog

        dialog.setMessage("Registering Please Wait...");
        dialog.show();

        //creating a new user
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //checking if success
                        if(task.isSuccessful()){
                            finish();
                            startActivity(new Intent(getApplicationContext(), Mymaps.class));
                        }else{
                            //display some message here
                            Toast.makeText(Register.this,"Registration Error",Toast.LENGTH_LONG).show();
                        }
                        dialog.dismiss();
                    }
                });*/


}
