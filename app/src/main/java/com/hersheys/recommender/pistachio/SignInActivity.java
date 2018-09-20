package com.hersheys.recommender.pistachio;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SignInActivity extends AppCompatActivity {
    Button signInButton;
    String username, password;
    EditText usernameField, passwordField;
    TextView sign_up_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        usernameField = (EditText) findViewById(R.id.unameField);
        passwordField = (EditText) findViewById(R.id.passField);

        signInButton = (Button) findViewById(R.id.signInButton);
        signInButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                username = usernameField.getText().toString();
                password = passwordField.getText().toString();
                Toast.makeText(getApplicationContext(), "U:" + username + "P:" + password, Toast.LENGTH_LONG).show();
                Intent signinIntent = new Intent(SignInActivity.this, UserHomeActivity.class);
                signinIntent.putExtra("username", username);
                signinIntent.putExtra("password",password);
                SignInActivity.this.startActivity(signinIntent);

            }
        });

        sign_up_text = (TextView) findViewById(R.id.signUpText);
        sign_up_text.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent signupIntent = new Intent(SignInActivity.this, SignUpActivity.class);
                SignInActivity.this.startActivity(signupIntent);
            }
        });

    }
}
