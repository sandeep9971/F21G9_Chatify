package com.example.chatify.models;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import  com.example.chatify.R;
import  com.example.chatify.UserChat;
import com.squareup.picasso.Picasso;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.example.chatify.Constant.MyPref;
import static   com.example.chatify.Constant.isConnectedToNetwork;



public class UserlistAdapter extends RecyclerView.Adapter<UserlistAdapter.Viewholder> {
    Context context;
    List<SignUpModel> list;
    String loginUserUid;
    public SharedPreferences sharedPreferences;
    public UserlistAdapter(Context context, List<SignUpModel> list) {
        this.context = context;
        this.list = list;
        sharedPreferences = context.getSharedPreferences(MyPref, MODE_PRIVATE);
        loginUserUid = sharedPreferences.getString("uid", "");
    }
    @Override
    public Viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.customview, parent, false);
        Viewholder myViewHolder = new Viewholder(v);
        return myViewHolder;
    }
    @Override
    public void onBindViewHolder(final Viewholder holder, final int position) {
if(list.get(position).uid.equalsIgnoreCase(loginUserUid)) {
    holder.textView.setText("Logged in user:- " + list.get(position).getName());
    holder.tvStatus.setVisibility(View.GONE);
holder.relativeLayout.setEnabled(false);
}
else {
    holder.textView.setText(list.get(position).getName());
    holder.relativeLayout.setEnabled(true);
    if (list.get(position).getStatus() != null) {
        holder.tvStatus.setText("(" + list.get(position).getStatus() + ")");
    } else {
        holder.tvStatus.setText("(No status)");
    }

}
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isConnectedToNetwork(context))
                {
                    Toast.makeText(context, "Please Check Internent Connection", Toast.LENGTH_LONG).show();
                }
                else
                {
                    Intent intent = new Intent(context, UserChat.class);
                    intent.putExtra("recylerAdpter_image", list.get(position).getImgUrl());
                    intent.putExtra("recylerAdpter_name", list.get(position).getName());
                    intent.putExtra("recylerAdpter_uidmsg", list.get(position).getUid());
                    intent.putExtra("recylerAdpter_phoneNo", list.get(position).getPhone());
                    intent.putExtra("recylerAdpter_token", list.get(position).getToken());

                    context.startActivity(intent);
                }
            }
        });
    }
    @Override
    public int getItemCount() {
        return list.size();

    }


    public class Viewholder extends RecyclerView.ViewHolder {
        TextView textView,tvStatus;
        ImageView img;
        RelativeLayout relativeLayout;
        public Viewholder(View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.textv);
            relativeLayout = itemView.findViewById(R.id.rl_contact);
            img=itemView.findViewById(R.id.index_img);
            tvStatus=itemView.findViewById(R.id.tv_status);
        }
    }
}
