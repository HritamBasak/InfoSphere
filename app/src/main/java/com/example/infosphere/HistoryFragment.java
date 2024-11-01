package com.example.infosphere;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HistoryFragment extends Fragment {
    DatabaseReference reference;
    FirebaseAuth mAuth;
    FirebaseUser user;
    RecyclerView recyclerView;
    RecyclerViewHistory adapter;
    List<Pojo_History> pojoHistoryList=new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history, container, false);
    }
    public void onViewCreated(View view,Bundle savedInstanceState)
    {
        recyclerView=view.findViewById(R.id.recyclerView_history);
        adapter=new RecyclerViewHistory((ArrayList<Pojo_History>) pojoHistoryList,getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAuth=FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference().child("question_history").child(user.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Pojo_History> updatedList = new ArrayList<>(); // Create a new list
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String question = dataSnapshot.child("question").getValue(String.class);
                    if (question != null) {
                        Pojo_History pojo_history = new Pojo_History(question, dataSnapshot.getKey());
                        updatedList.add(pojo_history);
                    }
                }
                adapter.updateList(updatedList); //
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}