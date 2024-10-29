package com.example.infosphere;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kwabenaberko.newsapilib.models.Article;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AdapterClass extends RecyclerView.Adapter<AdapterClass.ViewHolder> {

    @NonNull
    public ArrayList<Pojo> newsList=new ArrayList<>();
    Context context;
    public AdapterClass(ArrayList<Pojo> newsList, Context context) {
        this.newsList = newsList;
        this.context = context;
    }

    public AdapterClass() {

    }


    public AdapterClass.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterClass.ViewHolder holder, int position) {
        holder.title.setText(newsList.get(position).getTitle());
        holder.desc.setText(newsList.get(position).getDescription());
        Picasso.get().load(newsList.get(position)
                        .getUrlToImage())
                .error(R.drawable.newspic)

                .into(holder.imageToUrl);
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }
    public void updateData(List<com.kwabenaberko.newsapilib.models.Article> newArticles) {
        if (newArticles != null) {
            this.newsList.clear();
            // Convert News API library Article objects to your Pojo objects
            for (com.kwabenaberko.newsapilib.models.Article article : newArticles) {
                this.newsList.add(new Pojo(article.getTitle(), article.getDescription(), article.getUrlToImage()));
            }
            notifyDataSetChanged();
        }
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageToUrl;
        TextView title;
        TextView desc;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageToUrl=itemView.findViewById(R.id.imageToUrl);
            title=itemView.findViewById(R.id.title_news);
            desc=itemView.findViewById(R.id.description_news);
        }
    }
}
