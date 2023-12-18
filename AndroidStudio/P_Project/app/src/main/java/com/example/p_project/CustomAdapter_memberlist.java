package com.example.p_project;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class CustomAdapter_memberlist extends RecyclerView.Adapter<CustomAdapter_memberlist.CustomViewHolder> {

    private ArrayList<UserAccount> arrayList;
    private Context context;

    final static int[] rgbs=new int[]{Color.rgb(241,240,209), Color.rgb(228,206, 168)};

    final static class SEARCH{
        final static int PHONE=0, NAME=1, EMAIL=2;
    }
    public void searchList(String value, int valuetype){
        ArrayList<UserAccount> filteredList = new ArrayList<>();
        switch (valuetype){
            case SEARCH.PHONE:
                for(UserAccount user:arrayList){
                    if(user.getPhonenumber().contains(value)){
                        filteredList.add(user);
                    }
                }
                break;

            case SEARCH.NAME:
                for(UserAccount user:arrayList){
                    if(user.getUsername().contains(value)){
                        filteredList.add(user);
                    }
                }
                break;

            case SEARCH.EMAIL:
                for(UserAccount user:arrayList){
                    if(user.getPhonenumber().contains(value)){
                        filteredList.add(user);
                    }
                }
                break;

        }
        arrayList=filteredList;
        this.notifyDataSetChanged();
        Log.d("MemberList","Size : "+arrayList.size());
    }

    public void setArrayList(ArrayList<UserAccount> arrayList) {
        this.arrayList = arrayList;
    }

    public CustomAdapter_memberlist(ArrayList<UserAccount> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_member,parent,false);
        CustomViewHolder holder=new CustomViewHolder(view);

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int position=holder.getAdapterPosition();
                View editMemberDialog=(View) View.inflate(context,R.layout.dialog_editmember,null);

                AlertDialog.Builder emDLG=new AlertDialog.Builder(context);
                emDLG.setTitle("회원정보 수정");
                emDLG.setView(editMemberDialog);

                UserAccount user=arrayList.get(position);

                EditText username_editmember=editMemberDialog.findViewById(R.id.usernameEdt_editmember_dialog);
                EditText emailID_editmember=editMemberDialog.findViewById(R.id.emailID_editmember_dialog);
                EditText phonenumber_editmember=editMemberDialog.findViewById(R.id.phonenumber_editmember_dialog);

                username_editmember.setText(user.getUsername());
                emailID_editmember.setText(user.getEmailID());
                phonenumber_editmember.setText(user.getPhonenumber());

                emDLG.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        user.setUsername(username_editmember.getText().toString());
                        user.setEmailID(emailID_editmember.getText().toString());
                        user.setPhonenumber(phonenumber_editmember.getText().toString());

                        CustomAdapter_memberlist.this.notifyDataSetChanged();

                        DatabaseReference dataRef= FirebaseDatabase.getInstance().getReference("firebasetest").child("UserAccount");
                        dataRef.child(user.getIdToken()).setValue(user);
                    }
                });
                emDLG.setNegativeButton("취소",null);

                emDLG.show();

                return true;
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        UserAccount user=arrayList.get(position);
        holder.phonenumber_memberlist.setText(user.getPhonenumber());
        holder.main_memberlist.setText(user.getEmailID());
        holder.username_memberlist.setText(user.getUsername());
        holder.emailID_memberlist.setText(user.getIdToken());

        holder.background.setBackgroundColor(rgbs[position%2]);
    }

    @Override
    public int getItemCount() {
        return (arrayList!=null? arrayList.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView main_memberlist;
        TextView username_memberlist;
        TextView emailID_memberlist;
        TextView phonenumber_memberlist;
        LinearLayout background;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.emailID_memberlist=itemView.findViewById(R.id.emailIDTxt_member_item);
            this.main_memberlist=itemView.findViewById(R.id.mainTxt_member_item);
            this.phonenumber_memberlist=itemView.findViewById((R.id.phonenumberTxt_member_item));
            this.username_memberlist=itemView.findViewById(R.id.usernameTxt_member_item);
            this.background=itemView.findViewById(R.id.background_memberlist);
        }
    }
}
