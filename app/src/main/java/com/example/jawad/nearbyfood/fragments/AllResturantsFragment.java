package com.example.jawad.nearbyfood.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jawad.nearbyfood.R;
import com.example.jawad.nearbyfood.activities.ResturantDetailsActivity;
import com.example.jawad.nearbyfood.pojos.CuisineCategories;
import com.example.jawad.nearbyfood.pojos.Resturant;
import com.example.jawad.nearbyfood.utils.GridSpacingItemDecoration;
import com.example.jawad.nearbyfood.utils.Utils;
import com.example.jawad.nearbyfood.viewholders.CuisineCategoriesViewHolder;
import com.example.jawad.nearbyfood.viewholders.ResturantsViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class AllResturantsFragment extends Fragment {
    private DatabaseReference mDatabase;

    private RecyclerView mRecycler;
    private LinearLayoutManager mManager;
    private View rootview;

    private FirebaseRecyclerAdapter<Resturant, ResturantsViewHolder> mAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.fragment_quick_search, container, false);
        return rootview;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // [END create_database_reference]

        mRecycler = (RecyclerView) rootview.findViewById(R.id.recyclerView_quickSearchCategory);
        mRecycler.setHasFixedSize(true);

        setupRecyclerView();
    }


    private void setupRecyclerView() {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecycler.setLayoutManager(mLayoutManager);
        mRecycler.setItemAnimator(new DefaultItemAnimator());


        // Set up FirebaseRecyclerAdapter with the Query
        Query postsQuery = getQuery(mDatabase);

        mAdapter = new FirebaseRecyclerAdapter<Resturant, ResturantsViewHolder>(Resturant.class,R.layout.resturant_list_item,ResturantsViewHolder.class,postsQuery) {
            @Override
            protected void populateViewHolder(ResturantsViewHolder viewHolder, Resturant model, int position) {
                final DatabaseReference postRef = getRef(position);

                // Set click listener for the whole post view
                final String categoryKey = postRef.getKey();
                viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), ResturantDetailsActivity.class);
                        intent.putExtra(ResturantDetailsActivity.KEY_RESTURANT_KEY, categoryKey);

                        startActivity(intent);

                    }
                });


                // Bind Post to ViewHolder, setting OnClickListener for the star button
                viewHolder.bindToPost(model, new View.OnClickListener() {
                    @Override
                    public void onClick(View starView) {

                    }
                });
            }
        };

        mRecycler.setAdapter(mAdapter);
    }
    public Query getQuery(DatabaseReference databaseReference) {
        Query recentPostsQuery = databaseReference.child("resturants");
        return recentPostsQuery;
    }

}
