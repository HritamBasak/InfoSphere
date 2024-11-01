package com.example.infosphere;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewHistory extends RecyclerView.Adapter<RecyclerViewHistory.ViewHolder> {

    @NonNull
    public ArrayList<Pojo_History> list=new ArrayList<>();
    Context context;
    public RecyclerViewHistory(ArrayList<Pojo_History> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public RecyclerViewHistory() {

    }

//    public RecyclerViewHistory(List<Pojo_History> pojoHistoryList) {
//    }


    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.history_recyclerview,parent,false);
       return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Pojo_History pojo_history=list.get(position);
        holder.question.setText(pojo_history.getQuestion());
//        holder.date.setText(list.get(position).getDate());
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(context)
                        .setTitle("Delete")
                        .setMessage("Are You Sure You Want To Delete?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                int item=holder.getAdapterPosition();
                                if(item!=RecyclerView.NO_POSITION)
                                {
                                    Pojo_History pojo_history=list.get(item);
                                    String key= pojo_history.getKey();
                                    FirebaseDatabase.getInstance().getReference().child("question_history").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(key).removeValue();
                                    list.remove(item);
                                    notifyItemRemoved(item);
                                }
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void updateList(List<Pojo_History> updatedList) {
        list.clear();
        list.addAll(updatedList);
        notifyDataSetChanged();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView question;
        TextView delete;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            question=itemView.findViewById(R.id.history_question);
//            date=itemView.findViewById(R.id.date_history);
            delete=itemView.findViewById(R.id.delete);
        }
    }
}
