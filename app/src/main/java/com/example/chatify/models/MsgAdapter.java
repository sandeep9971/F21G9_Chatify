package com.example.chatify.models;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.chatify.R;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static  com.example.chatify.Constant.LoginUserMsgsend;
import static  com.example.chatify.Constant.MyPref;
import static  com.example.chatify.Constant.SecondUserMsgSend;


public class MsgAdapter extends RecyclerView.Adapter<MsgAdapter.MyVieholder> {
    SharedPreferences sharedPreferences;
    Context context;
    String loginUserUid, seconduserimage;
    List<ChatModel> chatModel;


    public MsgAdapter(Context context, List<ChatModel> chatModel, String seconduserimage) {
        this.context = context;
        this.chatModel = chatModel;
        this.seconduserimage = seconduserimage;
        sharedPreferences = context.getSharedPreferences(MyPref, MODE_PRIVATE);
        loginUserUid = sharedPreferences.getString("uid", "");

    }

    @NonNull
    @Override
    public MyVieholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == LoginUserMsgsend) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.loginusermsgsend, parent, false);
            MyVieholder myVieholder = new MyVieholder(v);
            return myVieholder;
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.secondusermsgsend, parent, false);
            MyVieholder myVieholder = new MyVieholder(v);
            return myVieholder;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MyVieholder holder, int position) {
       if (chatModel.get(position).getsenderUserId().equalsIgnoreCase(loginUserUid)) {
           if(chatModel.get(position).getMessage_type().equalsIgnoreCase("0")) {
               holder.tvLoginUserMsg.setVisibility(View.VISIBLE);
               holder.tvLoginUserMsg.setText(chatModel.get(position).getMsg());
               SimpleDateFormat sfd = new SimpleDateFormat("h:mm aa");
               holder.msgTime.setText(sfd.format(new Date(chatModel.get(position).getChattime())));
               holder.msgStatusTick.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_baseline_done_all_24));
           }else
           {
               holder.tvLoginUserMsg.setVisibility(View.GONE);
             SimpleDateFormat sfd = new SimpleDateFormat("h:mm aa");
               holder.msgTime.setText(sfd.format(new Date(chatModel.get(position).getChattime())));
               holder.msgStatusTick.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_baseline_done_all_24));
           }

        } else {

           if (chatModel.get(position).getMessage_type().equalsIgnoreCase("0")) {
               holder.tvSecondUserMsg.setVisibility(View.VISIBLE);
               holder.tvSecondUserMsg.setText(chatModel.get(position).getMsg());
               SimpleDateFormat sfd = new SimpleDateFormat("h:mm aa ");
               holder.receiveTime.setText((sfd.format(new Date(chatModel.get(position).getChattime()))));
           }
           else
           {

               holder.tvSecondUserMsg.setVisibility(View.GONE);
            SimpleDateFormat sfd = new SimpleDateFormat("h:mm aa ");
               holder.receiveTime.setText((sfd.format(new Date(chatModel.get(position).getChattime()))));
           }
       }
    }

    @Override
    public int getItemCount() {
        return chatModel.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (chatModel.get(position).getsenderUserId().equalsIgnoreCase(loginUserUid)) {
            return LoginUserMsgsend;
        } else {
            return SecondUserMsgSend;
        }
    }

    public class MyVieholder extends RecyclerView.ViewHolder {
        TextView tvLoginUserMsg, tvSecondUserMsg, msgTime, receiveTime;
        ImageView msgStatusTick;
        public MyVieholder(View itemView) {
            super(itemView);
            tvLoginUserMsg = itemView.findViewById(R.id.tv_loginuser_msg);
            tvSecondUserMsg = itemView.findViewById(R.id.tv_seconduser_msg);

            receiveTime = itemView.findViewById(R.id.receivetime);
            msgTime = itemView.findViewById(R.id.msgtime);
            msgStatusTick=itemView.findViewById(R.id.img_msg_send);
        }
    }
}
