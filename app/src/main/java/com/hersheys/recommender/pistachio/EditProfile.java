package com.hersheys.recommender.pistachio;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import javax.microedition.khronos.egl.EGLDisplay;

public class EditProfile extends AppCompatActivity {
    TextInputEditText  editemailField,editnameField;
    String email, name, pass, cpass;
    Button saveButton;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        editemailField = (TextInputEditText) findViewById(R.id.editEmail);
        editnameField = (TextInputEditText) findViewById(R.id.editName);
        saveButton = (Button) findViewById(R.id.saveButton);

        final FirebaseUser user = mAuth.getInstance().getCurrentUser();

        editemailField.setText(user.getEmail());
        editnameField.setText(user.getDisplayName());

        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                email = editemailField.getText().toString();
                name = editnameField.getText().toString();

                if (validateEmail(email)) {
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(name).build();

                    user.updateEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                //Log.d(TAG, "User profile updated.");
                                Toast.makeText(getApplicationContext(), "Email Updated", Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                    user.updateProfile(profileUpdates)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        //Log.d(TAG, "User profile updated.");
                                        Toast.makeText(getApplicationContext(), "Username Updated", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                    //Intent userIntent = new Intent(EditProfile.this, UserTab.class);
                    //EditProfile.this.startActivity(userIntent);
                    finish();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Enter Valid Email", Toast.LENGTH_LONG).show();
                }
            }
        });

        findViewById(R.id.edit_back_button).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    public boolean validateEmail(String email){
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();

    }
}
