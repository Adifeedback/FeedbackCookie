package com.feedbacks.feedbackcookie;

import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder> {

    ArrayList<Meetings> mlist;
    String end_show_hide ;

    public QuestionAdapter(ArrayList<Meetings> list, String end_show_hide){
        this.mlist = list;
        this.end_show_hide = end_show_hide;
    }

    @Override
    public QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        return new QuestionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionAdapter.QuestionViewHolder holder, int position) {

        if (this.end_show_hide.equals("no")){
            holder.end.setVisibility(View.GONE);
        }
        if (this.end_show_hide.equals("yes")){
            holder.end.setVisibility(View.VISIBLE);
        }

        holder.question.setText(mlist.get(position).getTitle());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(holder.itemView.getContext(), FeedbacksActivity.class);
                it.putExtra("id", mlist.get(position).getId());
                holder.itemView.getContext().startActivity(it);
            }
        });
        holder.end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(holder.itemView.getContext())
                        .setTitle("End Meeting")
                        .setMessage("Are you sure you want to end this Meeting?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Meetings").child(mlist.get(position).getId());
                                dbRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            Toast.makeText(holder.itemView.getContext(), "Meeting Ended.", Toast.LENGTH_SHORT).show();
                                            notifyItemRemoved(position);
                                        }
                                    }
                                });
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }

    public class QuestionViewHolder extends RecyclerView.ViewHolder{
        TextView question;
        Button end;
        public QuestionViewHolder(@NonNull View itemView) {
            super(itemView);
            question = itemView.findViewById(R.id.meetingsText);
            end = itemView.findViewById(R.id.endBtn);
        }
    }
}
