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
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link WatchLater.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link WatchLater#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WatchLater extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    SwipeRefreshLayout mSwipeRefreshLayout;
    RecyclerView recyclerView;

    View view;

    ArrayList<item> mList;

    private OnFragmentInteractionListener mListener;

    public WatchLater() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WatchLater.
     */
    // TODO: Rename and change types and number of parameters
    public static WatchLater newInstance(String param1, String param2) {
        WatchLater fragment = new WatchLater();
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
        setRetainInstance(true);
        return inflater.inflate(R.layout.fragment_watch_later, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.watch_later_card_list);
        mSwipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swipe_refresh_layout);
        mList = new ArrayList<>();

        if(savedInstanceState!=null && savedInstanceState.getIntegerArrayList("movieIDs")!=null){
            for(Integer mid:savedInstanceState.getIntegerArrayList("movieIDs")){
                mList.add(new item("https://firebasestorage.googleapis.com/v0/b/pistachio-8f641.appspot.com/o/images%2F" + mid.toString() + ".jpg?alt=media&token=baff526a-ac90-4390-84ac-da4b9ee0f29a", mid.intValue(), 0, "watchLater"));
            }
            //mList = savedInstanceState.getParcelableArrayList("mList");
            if (mList.size() > 0)
                view.findViewById(R.id.watch_later_such_empty).setVisibility(View.INVISIBLE);
            else
                view.findViewById(R.id.watch_later_such_empty).setVisibility(View.VISIBLE);
        }
        else {
            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference ref = database.getReference("Users");
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                // User is signed in
                DatabaseReference userRatingRef = ref.child(user.getUid()).child("Bookmarks");
                userRatingRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        mList.clear();
                        for (DataSnapshot ratings : dataSnapshot.getChildren()) {
                            int mid = Integer.parseInt(ratings.getKey());
                            //float stars = Float.parseFloat(ratings.getValue().toString());
                            mList.add(new item("https://firebasestorage.googleapis.com/v0/b/pistachio-8f641.appspot.com/o/images%2F" + Integer.toString(mid) + ".jpg?alt=media&token=baff526a-ac90-4390-84ac-da4b9ee0f29a", mid, 0, "watchLater"));
                        }
                        if (mList.size() > 0)
                            view.findViewById(R.id.watch_later_such_empty).setVisibility(View.INVISIBLE);
                        else
                            view.findViewById(R.id.watch_later_such_empty).setVisibility(View.VISIBLE);

                    /*if(mList.size()>0)
                        view.findViewById(R.id.my_ratings_such_empty).setVisibility(View.INVISIBLE);*/
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            } else {
                // No user is signed in
            }
        }
        Adapter adapter = new Adapter(getActivity(), mList);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mList.clear();
                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference ref = database.getReference("Users");
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    // User is signed in
                    DatabaseReference userRatingRef = ref.child(user.getUid()).child("Bookmarks");
                    userRatingRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(DataSnapshot ratings: dataSnapshot.getChildren()){
                                int mid = Integer.parseInt(ratings.getKey());
                                //float stars = Float.parseFloat(ratings.getValue().toString());
                                mList.add(new item("https://firebasestorage.googleapis.com/v0/b/pistachio-8f641.appspot.com/o/images%2F"+Integer.toString(mid)+".jpg?alt=media&token=baff526a-ac90-4390-84ac-da4b9ee0f29a",mid,0,"watchLater"));
                            }
                            if(mList.size()>0)
                                view.findViewById(R.id.watch_later_such_empty).setVisibility(View.INVISIBLE);
                            else
                                view.findViewById(R.id.watch_later_such_empty).setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                } else {
                    // No user is signed in
                }

                Adapter adapter = new Adapter(getActivity(), mList);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });


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
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ArrayList<Integer> movieIDs = new ArrayList<>();
        for(item it:mList){
            movieIDs.add(it.getMovieId());
        }
        outState.putIntegerArrayList("movieIDs", movieIDs);

        //outState.putParcelableArrayList("mList", mList);
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
