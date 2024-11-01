package com.example.infosphere;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.ai.client.generativeai.GenerativeModel;
import com.google.ai.client.generativeai.java.GenerativeModelFutures;
import com.google.ai.client.generativeai.type.Content;
import com.google.ai.client.generativeai.type.GenerateContentResponse;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class GeminiActivity extends AppCompatActivity {
    DatabaseReference db;
    FirebaseUser mUser;
    FirebaseAuth mAuth;
    TextView shown_image;
    EditText editText;
    TextView help;
    TextView question_show;
    EditText ask_gemini;
    TextView ans,ques;
    ImageView back;
    ImageView send;
    RecyclerViewHistory adapter;
    List<Pojo_History> pojoHistoryList;
    @SuppressLint("MissingInflatedId")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_gemini);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        mAuth=FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();
        pojoHistoryList=new ArrayList<>();
        adapter=new RecyclerViewHistory((ArrayList<Pojo_History>) pojoHistoryList,this);
        shown_image=findViewById(R.id.shown_image);
        editText=findViewById(R.id.ask_gemini);
        help=findViewById(R.id.help);
        question_show=findViewById(R.id.question_show);
        ask_gemini=findViewById(R.id.ask_gemini);
        ans=findViewById(R.id.textView9);
        ques=findViewById(R.id.textView8);
        back=findViewById(R.id.backk);
        send=findViewById(R.id.send);

        db= FirebaseDatabase.getInstance().getReference().child("question_history").child(mUser.getUid());
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modelCall();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(GeminiActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        adapter=new RecyclerViewHistory((ArrayList<Pojo_History>) pojoHistoryList,this);
    }
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(GeminiActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
    public void modelCall()
    {
//        Drawable[] drawables=editText.getCompoundDrawables();
//        Drawable rightDrawable= drawables[2];
        // Specify a Gemini model appropriate for your use case
        String question=editText.getText().toString();
        if(question.isEmpty())
        {
            Toast.makeText(GeminiActivity.this,"Please enter your question!",Toast.LENGTH_SHORT).show();
        }
        else {
            String key=db.push().getKey();

            Map<String,Object> map=new HashMap<>();
            map.put("question",question);
            db.child(key).setValue(map);

            GenerativeModel gm =
                    new GenerativeModel(
                            /* modelName */ "gemini-1.5-flash",
                            // Access your API key as a Build Configuration variable (see "Set up your API key"
                            // above)
                            /* apiKey */ "AIzaSyDWczA_lswfrSdE85VnlC1WBRQjA-Xcc7E");
            GenerativeModelFutures model = GenerativeModelFutures.from(gm);

            Content content =
                    new Content.Builder().addText(editText.getText().toString()).build();

// For illustrative purposes only. You should use an executor that fits your needs.
            //Executor executor = Executors.newSingleThreadExecutor();

            ListenableFuture<GenerateContentResponse> response = model.generateContent(content);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                Futures.addCallback(
                        response,
                        new FutureCallback<GenerateContentResponse>() {
                            @Override
                            public void onSuccess(GenerateContentResponse result) {
                                String resultText = result.getText();
                                ans.setText("Answer:");
                                shown_image.setText(resultText);
                                ques.setText("Question:");
                                question_show.setText(ask_gemini.getText().toString());
                                help.setText("");
                                ask_gemini.setText("");
                                pojoHistoryList.add(new Pojo_History(question,key));
                                adapter.notifyItemInserted(pojoHistoryList.size() - 1);
                            }

                            @Override
                            public void onFailure(Throwable t) {
                                t.printStackTrace();
                            }
                        }, this.getMainExecutor());
            }
        }
    }
}