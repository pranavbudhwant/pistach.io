package com.hersheys.recommender.pistachio;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {
    TextInputEditText emailField, nameField, passField, cPassField;
    Button signUpButton;
    String email, name, pass, cpass;
    TextView sign_in_text;
    private FirebaseAuth mAuth;
    private static final String TAG = "SignUpActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();

        emailField = (TextInputEditText)findViewById(R.id.emailField);
        nameField = (TextInputEditText)findViewById(R.id.nameField);
        passField = (TextInputEditText)findViewById(R.id.passField);
        cPassField = (TextInputEditText)findViewById(R.id.confirmPassField);

        signUpButton = (Button)findViewById(R.id.signUpButton);
        signUpButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                email = emailField.getText().toString();
                name = nameField.getText().toString();
                pass  = passField.getText().toString();
                cpass = cPassField.getText().toString();

<<<<<<< HEAD
                if(validateEmail(email) && validatePassword(pass) && validatePassword(cpass)) {
                    mAuth.createUserWithEmailAndPassword(email, pass)
                            .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d(TAG, "createUserWithEmail:success");
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        Toast.makeText(SignUpActivity.this, "Authentication successful.",
                                                Toast.LENGTH_LONG).show();
                                        User userObj = new User(name, email);
                                        FirebaseDatabase.getInstance().getReference("Users")
                                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                .setValue(userObj);
=======
                mAuth.createUserWithEmailAndPassword(email, pass)
                        .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "createUserWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    Toast.makeText(SignUpActivity.this, "Authentication successful.",
                                            Toast.LENGTH_LONG).show();

                                    /*
                                    User userObj = new User(name, email);
                                    FirebaseDatabase.getInstance().getReference("Users")
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .setValue(userObj);*/

>>>>>>> 474cf4742ed44bf0d2f29a4543c02b2cbd6d98b5

                                    FirebaseAuth auth = FirebaseAuth.getInstance();
                                    FirebaseUser current_user = auth.getCurrentUser();

                                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                            .setDisplayName(name)
                                            .build();

                                    current_user.updateProfile(profileUpdates)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Log.d(TAG, "User profile updated.");

                                                    }
                                                }
                                            });

                                    current_user.sendEmailVerification()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Log.d(TAG, "Email sent.");
                                                    }
                                                }
                                            });
<<<<<<< HEAD
                                    */
                                        Intent signinIntent = new Intent(SignUpActivity.this, UserHomeActivity.class);
                                        signinIntent.putExtra("email", email);
                                        signinIntent.putExtra("password", pass);
                                        SignUpActivity.this.startActivity(signinIntent);

                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                        Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
=======

                                    Intent signinIntent = new Intent(SignUpActivity.this, UserHomeActivity.class);
                                    signinIntent.putExtra("email", email);
                                    signinIntent.putExtra("password",pass);
                                    SignUpActivity.this.startActivity(signinIntent);


                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
>>>>>>> 474cf4742ed44bf0d2f29a4543c02b2cbd6d98b5

                                    }

                                    // ...
                                }
                            });
                }
                else if(!pass.equals(cpass)){
                    Toast.makeText(getApplicationContext(),"Passwords Don't Match", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(getApplicationContext(),"Enter Valid Email and Password", Toast.LENGTH_LONG).show();
                }
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
    public boolean validateEmail(String email){
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();

    }

    public boolean validatePassword(String password){
        if(password.length() < 6 )
            return false;
        else
            return true;
    }
}
