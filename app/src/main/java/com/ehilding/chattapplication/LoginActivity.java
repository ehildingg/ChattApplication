package com.ehilding.chattapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    EditText userETLogin, passETLogin;
    Button loginBtn, registerBtn;

    // Firebase:
    FirebaseAuth auth;
    FirebaseUser  firebaseUser;


    // Bättre att ha denna koden i onStart av någon anledning, vet inte exakt varför.
    // Garanterat att man stannar inloggad hela tiden.
    @Override
    protected void onStart() {
        super.onStart();
        // Tar in nuvarande användare i firebaseUser
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        // Checking for user existance. Om firebaseUser finns sparad och inte är null, gå direkt till MainActivity...
        if (firebaseUser != null) {
            Intent i = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userETLogin = findViewById(R.id.edit_login_username);
        passETLogin = findViewById(R.id.edit_login_password);
        loginBtn    = findViewById(R.id.button_login);
        registerBtn = findViewById(R.id.button_not_member_register);

        // Firebase Auth:
        auth = FirebaseAuth.getInstance();

        // SIGN-UP KNAPP: om man trycker på den kommer man till RegisterActivity.
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);
            }
        });

        /*LOGIN-KNAPP: - Hämtar text från Edittexts, skickar in i signInWithEmailAndPassword,
        och om task.isSuccessful så skickas man till Main.*/
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email_text = userETLogin.getText().toString();
                String pass_text  = passETLogin.getText().toString();


                // Kollar om fält är tomma
                if (TextUtils.isEmpty(email_text) || TextUtils.isEmpty(pass_text)) {
                    Toast.makeText(LoginActivity.this, "Please  fill the Fields", Toast.LENGTH_SHORT).show();
                } else {
                    auth.signInWithEmailAndPassword(email_text, pass_text)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Intent i = new Intent(LoginActivity.this, MainActivity.class);
                                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(i);
                                        finish();
                                    } else {
                                        Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });



    }
}