package com.hersheys.recommender.pistachio;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Arrays;
import java.util.Random;
import java.util.Set;

import org.tensorflow.contrib.android.*;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Recommended.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Recommended#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Recommended extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    SwipeRefreshLayout mSwipeRefreshLayout;
    RecyclerView recyclerView;
    int index = 0;
    View view;

    List<item> mList;

    //Load the tensorflow inference library
    static {
        System.loadLibrary("tensorflow_inference");
    }

    private String MODEL_PATH = "model.pb";
    private String INPUT_NAME = "dense_33_input_3";
    private String OUTPUT_NAME = "output_node0";
    private TensorFlowInferenceInterface tf;
    private float []movie_ratings = new float[3883];

    //ARRAY TO HOLD THE PREDICTIONS
    float[] prediction = new float[3883];

    Set globalSet = new HashSet<Integer>(50);

    public Recommended() {
        // Required empty public constructor
        for(int i=0; i<3883; i++)
            movie_ratings[i] = 0.f;
    }

    public int[] getPredictions(float[] movie_ratings) {
        tf.feed(INPUT_NAME,movie_ratings,1,3883);
        tf.run(new String[]{OUTPUT_NAME});
        tf.fetch(OUTPUT_NAME,prediction);
        //Arrays.sort(prediction);
        float max = -1.f;
        int []arr = new int[3883];
        int loc = 0;
        for(int j=0; j<3883; j++){
            for(int i=0; i<3883; i++){
                if(max<prediction[i]){
                    max = prediction[i];
                    loc = i;
                }
            }
            max = -1.f;
            prediction[loc] = -1.f;
            arr[j] = loc;
        }
        return arr;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Recommended.
     */
    // TODO: Rename and change types and number of parameters
    public static Recommended newInstance(String param1, String param2) {
        Recommended fragment = new Recommended();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_recommended, container, false);
        recyclerView = view.findViewById(R.id.recommended_card_list);
        mSwipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swipe_refresh_layout);
        mList = new ArrayList<>();
        //final Random random = new Random();
        //final Set set = new HashSet<Integer>(5);
        tf = new TensorFlowInferenceInterface(getActivity().getAssets(),MODEL_PATH);

        index = 0;

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Users");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // User is signed in
            DatabaseReference userRatingRef = ref.child(user.getUid()).child("Ratings");
            userRatingRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot ratings: dataSnapshot.getChildren()){
                        int mid = Integer.parseInt(ratings.getKey());
                        float stars = Float.parseFloat(ratings.getValue().toString());
                        movie_ratings[mid] = stars;
                        globalSet.add(mid);
                        //System.out.println("keys is:"+mid+" and rating is:"+stars);

                        //mList.add(new item("https://firebasestorage.googleapis.com/v0/b/pistachio-8f641.appspot.com/o/images%2F"+Integer.toString(mid)+".jpg?alt=media&token=baff526a-ac90-4390-84ac-da4b9ee0f29a",mid,stars,"Recommended"));
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        } else {
            // No user is signed in
        }


        /*while(set.size() < 5){
            int newID = random.nextInt(3883);
            if(!globalSet.contains(newID))
                set.add(newID);
        }*/


        //Iterator iter = set.iterator();

        int value[] = getPredictions(movie_ratings);
        int curr_index = index;
        while(curr_index<index+5) {
            if(!globalSet.contains(value[curr_index])){
                curr_index++;
                mList.add(new item("https://firebasestorage.googleapis.com/v0/b/pistachio-8f641.appspot.com/o/images%2F"+Integer.toString(value[curr_index])+".jpg?alt=media&token=baff526a-ac90-4390-84ac-da4b9ee0f29a",value[curr_index],prediction[value[curr_index]],"Recommended"));
            }

        }
        if(mList.size()>0)
            view.findViewById(R.id.recommended_such_empty).setVisibility(View.INVISIBLE);
        else
            view.findViewById(R.id.recommended_such_empty).setVisibility(View.VISIBLE);

        index = curr_index;
        Adapter adapter = new Adapter(getActivity(), mList);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter.setOnBottomReachedListener(new OnBottomReachedListener() {
            @Override
            public void OnBottomReached(int position) {
                int value[] = getPredictions(movie_ratings);
                //Iterator iter = set.iterator();
                int curr_index = index;
                while(curr_index<index+5) {
                    //int newID = random.nextInt(3883);
                    if(!globalSet.contains(value[curr_index])){
                        curr_index++;
                        mList.add(new item("https://firebasestorage.googleapis.com/v0/b/pistachio-8f641.appspot.com/o/images%2F"+Integer.toString(value[curr_index])+".jpg?alt=media&token=baff526a-ac90-4390-84ac-da4b9ee0f29a",value[curr_index],prediction[value[curr_index]],"Recommended"));
                    }

                }
                index = curr_index;
            }
        });

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mList.clear();
                globalSet.clear();
                index = 0;
                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference ref = database.getReference("Users");
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    // User is signed in
                    DatabaseReference userRatingRef = ref.child(user.getUid()).child("Ratings");
                    userRatingRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(DataSnapshot ratings: dataSnapshot.getChildren()){
                                int mid = Integer.parseInt(ratings.getKey());
                                float stars = Float.parseFloat(ratings.getValue().toString());
                                //mList.add(new item("https://firebasestorage.googleapis.com/v0/b/pistachio-8f641.appspot.com/o/images%2F"+Integer.toString(mid)+".jpg?alt=media&token=baff526a-ac90-4390-84ac-da4b9ee0f29a",mid,stars,"Recommended"));
                                movie_ratings[mid] = stars;
                                globalSet.add(mid);
                            }
                            if(mList.size()>0)
                                view.findViewById(R.id.recommended_such_empty).setVisibility(View.INVISIBLE);
                            else
                                view.findViewById(R.id.recommended_such_empty).setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                } else {
                    // No user is signed in
                }

                /*while(set.size() < 5){
                    int newID = random.nextInt(3883);
                    if(!globalSet.contains(newID))
                        set.add(newID);
                }*/


                //Iterator iter = set.iterator();

                int value[] = getPredictions(movie_ratings);
                int curr_index = index;
                while(curr_index<index+5) {
                    //int newID = random.nextInt(3883);
                    if(!globalSet.contains(value[curr_index])){
                        curr_index++;
                        mList.add(new item("https://firebasestorage.googleapis.com/v0/b/pistachio-8f641.appspot.com/o/images%2F"+Integer.toString(value[curr_index])+".jpg?alt=media&token=baff526a-ac90-4390-84ac-da4b9ee0f29a",value[curr_index],prediction[value[curr_index]],"Recommended"));
                    }

                }
                index = curr_index;
                Adapter adapter = new Adapter(getActivity(), mList);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                mSwipeRefreshLayout.setRefreshing(false);

                adapter.setOnBottomReachedListener(new OnBottomReachedListener() {
                    @Override
                    public void OnBottomReached(int position) {
                        int value[] = getPredictions(movie_ratings);
                        //Iterator iter = set.iterator();
                        int curr_index = index;
                        while(curr_index<index+5) {
                            //int newID = random.nextInt(3883);
                            if(!globalSet.contains(value[curr_index])){
                                curr_index++;
                                mList.add(new item("https://firebasestorage.googleapis.com/v0/b/pistachio-8f641.appspot.com/o/images%2F"+Integer.toString(value[curr_index])+".jpg?alt=media&token=baff526a-ac90-4390-84ac-da4b9ee0f29a",value[curr_index],prediction[value[curr_index]],"Recommended"));
                            }

                        }
                        index = curr_index;
                    }
                });

            }
        });
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
