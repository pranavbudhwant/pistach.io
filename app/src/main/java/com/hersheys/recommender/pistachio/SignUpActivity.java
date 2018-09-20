package com.hersheys.recommender.pistachio;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SignUpActivity extends AppCompatActivity {
    TextInputEditText emailField, unameField, passField, cPassField;
    Button signUpButton;
    String email, username, pass, cpass;
    TextView sign_in_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        emailField = (TextInputEditText)findViewById(R.id.emailField);
        unameField = (TextInputEditText)findViewById(R.id.unameField);
        passField = (TextInputEditText)findViewById(R.id.passField);
        cPassField = (TextInputEditText)findViewById(R.id.confirmPassField);

        signUpButton = (Button)findViewById(R.id.signUpButton);
        signUpButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                email = emailField.getText().toString();
                username = unameField.getText().toString();
                pass  = passField.getText().toString();
                cpass = cPassField.getText().toString();
            }
        });

        sign_in_text = (TextView)findViewById(R.id.signInText);
        sign_in_text.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent sign_in_intent = new Intent(SignUpActivity.this, SignInActivity.class);
                SignUpActivity.this.startActivity(sign_in_intent);
            }
        });


    }
}
