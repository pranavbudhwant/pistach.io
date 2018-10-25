package com.hersheys.recommender.pistachio;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import android.text.TextUtils;

public class SignInActivity extends AppCompatActivity {
    Button signInButton;
    String email, password;
    EditText emailField, passwordField;
    TextView sign_up_text;
    private FirebaseAuth mAuth;
    String TAG = "SignInActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        mAuth = FirebaseAuth.getInstance();
        emailField = (EditText) findViewById(R.id.emailField);
        passwordField = (EditText) findViewById(R.id.passField);

        /*Intent intent = new Intent(SignInActivity.this, UserHomeActivity.class);
        SignInActivity.this.startActivity(intent);
		*/

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // User is signed in
            Intent signinIntent = new Intent(SignInActivity.this, UserHomeActivity.class);
            SignInActivity.this.startActivity(signinIntent);
        } else {
            // No user is signed in
        }
        signInButton = (Button) findViewById(R.id.signInButton);
        signInButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                email = emailField.getText().toString();
                password = passwordField.getText().toString();

                if(validateEmail(email) && validatePassword(password)) {
                    //Toast.makeText(getApplicationContext(), "E:" + email + "P:" + password, Toast.LENGTH_LONG).show();
                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(SignInActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d(TAG, "signInWithEmail:success");
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        //updateUI(user);
                                        Intent signinIntent = new Intent(SignInActivity.this, UserHomeActivity.class);
                                        signinIntent.putExtra("email", email);
                                        signinIntent.putExtra("password", password);
                                        SignInActivity.this.startActivity(signinIntent);
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                                        Toast.makeText(SignInActivity.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                        //updateUI(null);
                                    }
                                    // ...
                                }
                            });
                }
                else if (validatePassword(password)) {
                    Toast.makeText(getApplicationContext(), "Password has to be atleast 6 characters", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Enter Valid Email and Password", Toast.LENGTH_LONG).show();
                }
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

    public boolean validateEmail(String email){
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();

    }

    public boolean validatePassword(String password){
        if(password.length() < 6 )
            return false;
        else
            return true;
    }
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

}