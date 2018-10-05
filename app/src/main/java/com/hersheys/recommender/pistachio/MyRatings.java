package com.hersheys.recommender.pistachio;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MyRatings.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MyRatings#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyRatings extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public MyRatings() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyRatings.
     */
    // TODO: Rename and change types and number of parameters
    public static MyRatings newInstance(String param1, String param2) {
        MyRatings fragment = new MyRatings();
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
        View view = inflater.inflate(R.layout.fragment_my_ratings, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.card_list);
        final List<item> mList = new ArrayList<>();

        Random random = new Random();
        Set set = new HashSet<Integer>(10);
        while(set.size() < 10){
            set.add(random.nextInt(3883));
        }

        Iterator iter = set.iterator();
        while(iter.hasNext()){
            FirebaseStorage storage = FirebaseStorage.getInstance();
            String path = "movie_images/" + iter.next().toString() + ".jpg";
            storage.getReference().child(path).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                //Here Your Storage Path is path where you have uploaded your file/image.
                @Override
                public void onSuccess(Uri uri) {
                    // Got the download URI for '*File/image*'
                    mList.add(new item(R.drawable.daredevil, "Daredevil (2017)", "Adventure | Animation", "IMDB: 7.2", uri.toString()));
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    //handle error
                }
            });
        }

        //mList.add(new item(R.drawable.daredevil, "Daredevil (2017)", "Action | Thriller", "IMDB: 7.2"));
        //mList.add(new item(R.drawable.daredevil, "Daredevil (2017)", "Action | Thriller", "IMDB: 7.2"));

        Adapter adapter = new Adapter(getActivity(), mList);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

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
