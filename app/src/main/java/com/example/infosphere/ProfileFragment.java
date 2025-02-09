package com.example.infosphere;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;


public class ProfileFragment extends Fragment {
    private ImageView profUserImage;
    private TextView profUserName;
    private TextView profUserEmail;

    private FirebaseAuth mAuth;
    private DatabaseReference userRef;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        profUserImage = view.findViewById(R.id.prof_userImage);
        profUserName = view.findViewById(R.id.prof_userName);
        profUserEmail = view.findViewById(R.id.prof_userEmail);
        mAuth=FirebaseAuth.getInstance();
        FirebaseUser user=mAuth.getCurrentUser();
        if(user!=null){
            userRef= FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid());
            userRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String profilePictureUrl=snapshot.child("profilePictureUrl").getValue(String.class);
                    String userName=snapshot.child("name").getValue(String.class);
                    String userEmail=snapshot.child("email").getValue(String.class);
                    if(profilePictureUrl!=null)
                    {
                        Picasso.get().load(profilePictureUrl).into(profUserImage);
                    }
                    if(userName!=null)
                    {
                        profUserName.setText(userName);
                    }
                    if(userEmail!=null)
                    {
                        profUserEmail.setText(userEmail);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

       userRef.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
               String profilePictureUrl=snapshot.child("profilePictureUrl").getValue(String.class);
               profUserImage.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       Intent intent=new Intent(getActivity(),ImageActivity.class);
                       intent.putExtra("profilePictureUrl",profilePictureUrl);
                       startActivity(intent);
                   }
               });
           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {

           }
       });

        return view;
    }
}