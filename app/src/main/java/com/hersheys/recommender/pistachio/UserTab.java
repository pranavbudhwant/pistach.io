package com.hersheys.recommender.pistachio;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.app.ProgressDialog;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.firebase.storage.OnProgressListener;
import com.squareup.picasso.Picasso;

import java.text.Collator;
import java.text.DecimalFormat;
import java.util.UUID;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link UserTab.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link UserTab#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserTab extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    Button signOutButton;
    ImageView imgButton;
    TextView userName,userEmail,ratedMovies,avgRating;
    Uri profilePhoto;
    SwipeRefreshLayout mSwipeRefreshLayout;

    private static int RESULT_LOAD_IMG = 1;
    String imgDecodableString;


    public UserTab() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserTab.
     */
    // TODO: Rename and change types and number of parameters
    public static UserTab newInstance(String param1, String param2) {
        UserTab fragment = new UserTab();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    public void loadImagefromGallery(View view) {
        // Create intent to Open Image applications like Gallery, Google Photos
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start the Intent
        startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
        Toast.makeText(getContext(),"In Gallery Function",Toast.LENGTH_LONG).show();


    }

     public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if (requestCode == RESULT_LOAD_IMG && resultCode == Activity.RESULT_OK
                    && null != data) {
                // Get the Image from data
                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };
                // Get the cursor
                Cursor cursor = getContext().getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imgDecodableString = cursor.getString(columnIndex);
                cursor.close();
                // Set the Image in ImageView after decoding the String
                //imgButton.setImageBitmap(BitmapFactory.decodeFile(imgDecodableString));
                FirebaseUser user = mAuth.getInstance().getCurrentUser();

                if (selectedImage != null) {
                    final ProgressDialog progressDialog = new ProgressDialog(getContext());
                    progressDialog.setTitle("Uploading...");
                    progressDialog.show();

                    FirebaseStorage database = FirebaseStorage.getInstance();
                    StorageReference storeref = database.getReference().child("Users/");
                    StorageReference ref = storeref.child(user.getUid());
                    ref.putFile(selectedImage)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                             @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                progressDialog.dismiss();
                                Toast.makeText(getContext(), "Uploaded", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressDialog.dismiss();
                                    Toast.makeText(getContext(), "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                    double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                            .getTotalByteCount());
                                    progressDialog.setMessage("Uploaded "+(int)progress+"%");
                                }
                            });
                }

                imgButton.setImageURI(selectedImage);
            }
            else {
                Toast.makeText(this.getContext(), "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this.getContext(), "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_user_tab, container, false);
        signOutButton = (Button)view.findViewById(R.id.signOut);
        signOutButton.setOnClickListener((View.OnClickListener) this);

        final FirebaseUser user = mAuth.getInstance().getCurrentUser();

        FirebaseStorage database = FirebaseStorage.getInstance();
        StorageReference storeref = database.getReference().child("Users/"+user.getUid());
        storeref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                profilePhoto = uri;
                //Toast.makeText(getContext(), profilePhoto.toString(), Toast.LENGTH_LONG).show();


                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setPhotoUri(profilePhoto).build();


                user.updateProfile(profileUpdates)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    //Log.d(TAG, "User profile updated.");
                                    Toast.makeText(getContext(), "User Profile Image Updated", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                //https://firebasestorage.googleapis.com/v0/b/pistachio-8f641.appspot.com/o/Users%2FGxB8d51eMhhMm3wLFNu8FfXl45E3?alt=media&token=7bd359eb-f81d-4beb-962f-0561248db56b

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
        //Toast.makeText(getContext(),profilePhoto.toString(),Toast.LENGTH_LONG).show();

        imgButton =(ImageView) view.findViewById(R.id.profile_photo);
        Uri link = user.getPhotoUrl();
        Toast.makeText(getContext(),"Link - "+link.toString(),Toast.LENGTH_LONG).show();

        if(link!=null) {
            Picasso.with(getContext()).load(link).into(imgButton);
        }
        else{
        imgButton.setImageResource(R.mipmap.profile_photo);
        }

        imgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadImagefromGallery(v);
                //Toast.makeText(getContext(),"Profile Photo Added",Toast.LENGTH_LONG).show();
            }
        });


        userName = (TextView)view.findViewById(R.id.userName);
        userEmail = (TextView)view.findViewById(R.id.userEmail);
        ratedMovies = (TextView)view.findViewById(R.id.ratedMovies);
        avgRating = (TextView)view.findViewById(R.id.avgRating);

        userName.setText(user.getDisplayName());
        userEmail.setText(user.getEmail());

        mSwipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.refresh_profile);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                view.findViewById(R.id.profilePage).setVisibility(View.INVISIBLE);
                userName.setText(user.getDisplayName());
                userEmail.setText(user.getEmail());
                view.findViewById(R.id.profilePage).setVisibility(View.VISIBLE);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
        final String email = userEmail.getText().toString();
        view.findViewById(R.id.editPassword).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(email!=null) {
                    mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getContext(), "Password Reset Email Sent", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
                else{
                    Toast.makeText(getContext(), email, Toast.LENGTH_LONG).show();
                }
            }
        });

        view.findViewById(R.id.editProfile).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent editIntent = new Intent(getContext(), EditProfile.class);
                getContext().startActivity(editIntent);
            }
        });

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference ref = db.getReference("Users");
        DatabaseReference userRatingRef = ref.child(user.getUid()).child("Ratings");

        userRatingRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long count = 0;
                double avg = 0;
                for(DataSnapshot ratings: dataSnapshot.getChildren()){
                    avg += Float.parseFloat(ratings.getValue().toString());
                    count++;
                }
                avg = avg/count;
                DecimalFormat f = new DecimalFormat("##.00");
                ratedMovies.setText(Long.toString(count));
                avgRating.setText(f.format(avg));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(getActivity(), SignInActivity.class);
        startActivity(intent);
    }



    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}