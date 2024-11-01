package com.example.infosphere;

import static androidx.core.app.ActivityCompat.startActivityForResult;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import com.example.infosphere.ui.home.HomeFragment;
import com.example.infosphere.ui.slideshow.SlideshowFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.core.view.WindowCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.infosphere.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    FirebaseUser currentUser;
    FirebaseAuth mAuth;
    DatabaseReference userRef;
    int PIC_IMAGE_REQUEST=1;
    ImageView profilePic;
    TextView userName;
    ImageButton gemini;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=MainActivity.this;
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar=findViewById(R.id.toolbar);
        gemini=findViewById(R.id.gemini);
        toolbar.setBackgroundColor(ContextCompat.getColor(this,R.color.black));
        setSupportActionBar(binding.appBarMain.toolbar);
//        binding.appBarMain.gemini.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null)
//                        .setAnchorView(R.id.gemini).show();
//            }
//        });
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,R.id.profileFragment,R.id.feedbackfragment,R.id.morefragment,R.id.historyFragment)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        // Get user's database reference
        if (currentUser != null) {
            userRef = FirebaseDatabase.getInstance().getReference("users").child(currentUser.getUid());
        }



        NavigationView navigationView1 = findViewById(R.id.nav_view);
        View headerView = navigationView1.getHeaderView(0);
        ImageView plus = headerView.findViewById(R.id.plus);
        profilePic = headerView.findViewById(R.id.profilePic);
        TextView profileName=headerView.findViewById(R.id.profileName);
        TextView profileMail=headerView.findViewById(R.id.profileMail);
        userName=findViewById(R.id.user_name);

//        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id=item.getItemId();
                if(id==R.id.historyFragment)
                {
                    navController.navigate(R.id.historyFragment);
                }
                if(id==R.id.shareFragment)
                {
                    navController.navigate(R.id.shareFragment);
                }
                if(id==R.id.nav_home)
                {
                    navController.navigate(R.id.nav_home);
                }
                else if(id==R.id.profileFragment)
                {
                    navController.navigate(R.id.profileFragment);
                }
                else if(id==R.id.nav_gallery)
                {
                    navController.navigate(R.id.nav_gallery);
                }
                else if(id==R.id.feedbackfragment)
                {
                    navController.navigate(R.id.feedbackfragment);
                }
                else if(id==R.id.morefragment)
                {
                    navController.navigate(R.id.morefragment);
                }
                else if(id==R.id.nav_slideshow) {
                    new AlertDialog.Builder(context)
                            .setTitle("SignOut")
                                    .setMessage("Are You Sure You Want To SignOut?")
                                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    mAuth.signOut();
                                                    Intent intent=new Intent(MainActivity.this,SignInActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent intent=new Intent(MainActivity.this, HomeFragment.class);
                                }
                            })
                            .show();
//                    navController.navigate(R.id.nav_slideshow);
                }
                DrawerLayout drawer = findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,PIC_IMAGE_REQUEST);
            }
        });


            if (userRef != null) {
                userRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String profilePictureUrl = snapshot.child("profilePicUrl").getValue(String.class);
                        String name = snapshot.child("name").getValue(String.class);
                        String email = snapshot.child("email").getValue(String.class);
                        if (profilePictureUrl != null) {
                            Picasso.get().load(profilePictureUrl).into(profilePic);
                        }
                        if (name != null) {
                            profileName.setText(name);
                            userName.setText(name);
                        }
                        if (email != null) {
                            profileMail.setText(email);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

        gemini.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,GeminiActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    protected void onResume()
    {
        super.onResume();
        if (userRef != null) {
            userRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String profilePictureUrl = snapshot.child("profilePictureUrl").getValue(String.class);

                    if (profilePictureUrl != null) {
                        Picasso.get().load(profilePictureUrl).into(profilePic);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle error
                }
            });
        }
    }
    public void onActivityResult(int requestCode,int resultCode,Intent data)
    {
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode==PIC_IMAGE_REQUEST && resultCode==RESULT_OK && data!=null && data.getData()!=null)
        {
            Uri selectedImage=data.getData();
            FirebaseStorage firebaseStorage=FirebaseStorage.getInstance();
            StorageReference storageReference=firebaseStorage.getReference();

            StorageReference imageRef=storageReference.child("images/"+currentUser.getUid()+".jpg");

            UploadTask uploadTask = imageRef.putFile(selectedImage);

            // Monitor the upload progress and handle success/failure
            uploadTask.addOnSuccessListener(taskSnapshot -> {
                // Image uploaded successfully
                // Get the download URL of the uploaded image
                imageRef.getDownloadUrl().addOnSuccessListener(downloadUrl -> {
                    // Update the profile picture URL in your database or user profile
                    updateProfilePictureUrl(downloadUrl.toString());
                });
            }).addOnFailureListener(e -> {
                // Handle upload failure
                Toast.makeText(this, "Failed to upload image", Toast.LENGTH_SHORT).show();
            });
        }
    }

    public void updateProfilePictureUrl(String url) {
        if (userRef != null) {
            userRef.child("profilePictureUrl").setValue(url)
                    .addOnSuccessListener(aVoid -> {
                        // Profile picture URL updated successfully
                        Toast.makeText(this, "Profile picture updated", Toast.LENGTH_SHORT).show();

                        // Load the updated image into the ImageView
                        Picasso.get().load(url).into(profilePic);
                    })
                    .addOnFailureListener(e -> {
                        // Handle database update failure
                        Toast.makeText(this, "Failed to update profile picture", Toast.LENGTH_SHORT).show();
                    });
        }
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.action_settings)
        {
            int currentNightMode=AppCompatDelegate.getDefaultNightMode();
            if(currentNightMode==AppCompatDelegate.MODE_NIGHT_NO)
            {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

            }
            else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}