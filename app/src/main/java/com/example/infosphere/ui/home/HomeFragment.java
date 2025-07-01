package com.example.infosphere.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.Volley;
import com.example.infosphere.AdapterClass;
import com.example.infosphere.R;
import com.example.infosphere.databinding.FragmentHomeBinding;
import com.kwabenaberko.newsapilib.NewsApiClient;
import com.kwabenaberko.newsapilib.models.request.EverythingRequest;
import com.kwabenaberko.newsapilib.models.response.ArticleResponse;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private AdapterClass adapterClass;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Set formatted date
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            LocalDateTime current = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM dd, EEEE, hh:mm a");
            binding.date.setText(current.format(formatter));
        }

        // RecyclerView setup
        adapterClass = new AdapterClass();
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(adapterClass);

        // SwipeRefreshLayout setup
        binding.swipeRefreshLayout.setColorSchemeResources(R.color.teal_700, R.color.purple_500, R.color.black);
        binding.swipeRefreshLayout.setOnRefreshListener(this::fetchNews);

        // Initial fetch
        fetchNews();

        return root;
    }

    private void fetchNews() {
        binding.swipeRefreshLayout.setRefreshing(true);

        NewsApiClient newsApiClient = new NewsApiClient("e29c05db23dd48b9899159cf4192ff3c");
        newsApiClient.getEverything(
                new EverythingRequest.Builder()
                        .q("world news OR technology OR sports OR entertainment OR business OR health OR science")
                        .language("en")
                        .build(),
                new NewsApiClient.ArticlesResponseCallback() {
                    @Override
                    public void onSuccess(ArticleResponse response) {
                        if (getActivity() == null) return;
                        requireActivity().runOnUiThread(() -> {
                            adapterClass.updateData(response.getArticles());
                            binding.swipeRefreshLayout.setRefreshing(false);
                        });
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        if (getActivity() == null) return;
                        requireActivity().runOnUiThread(() ->
                                binding.swipeRefreshLayout.setRefreshing(false));
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        fetchNews(); // Refresh when returning to fragment
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
