package com.example.infosphere.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.infosphere.AdapterClass;
import com.example.infosphere.Pojo;
import com.example.infosphere.R;
import com.example.infosphere.databinding.FragmentHomeBinding;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.kwabenaberko.newsapilib.NewsApiClient;
import com.kwabenaberko.newsapilib.models.request.EverythingRequest;
import com.kwabenaberko.newsapilib.models.request.TopHeadlinesRequest;
import com.kwabenaberko.newsapilib.models.response.ArticleResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.DayOfWeek;
// ... other imports ...
public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    RecyclerView recyclerView;
    AdapterClass adapterClass;
    RequestQueue requestQueue;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();



        LocalDateTime current = null;
        String formatted=null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            current = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM dd, EEEE, HH:mm:ss"); // Format pattern
            formatted = current.format(formatter);
        }

        binding.date.setText(formatted);
        recyclerView=binding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapterClass=new AdapterClass();
        recyclerView.setAdapter(adapterClass);
        requestQueue= Volley.newRequestQueue(getContext());
        setUpRecyclerView();
        fetchNews();

//        final TextView textView = binding.textHome;
//        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    public void setUpRecyclerView()
    {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapterClass=new AdapterClass();
        recyclerView.setAdapter(adapterClass);
    }
    public void fetchNews()
    {
        NewsApiClient newsApiClient=new NewsApiClient("e29c05db23dd48b9899159cf4192ff3c");
        newsApiClient.getEverything(
                new EverythingRequest.Builder().
                        q("world news OR technology OR sports OR entertainment OR business OR health OR science")
                        .language("en")
                        .build(),
                new NewsApiClient.ArticlesResponseCallback() {
                    @Override
                    public void onSuccess(ArticleResponse response) {
                        requireActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                adapterClass.updateData(response.getArticles());
                            }
                        });
                    }
                    @Override
                    public void onFailure(Throwable throwable) {

                    }
                });

    }
    public void onResume() {
        super.onResume();
        fetchNews(); // Fetch news again when the fragment resumes
    }
}