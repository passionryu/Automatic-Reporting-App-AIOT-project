package com.example.p_project;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapter_loglist extends RecyclerView.Adapter<CustomAdapter_loglist.CustomViewHolder> {


    public CustomAdapter_loglist(ArrayList<AccidentLog> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    private ArrayList<AccidentLog> arrayList;
    private Context context;

    final static int[] rgbs=CustomAdapter_memberlist.rgbs;

    public class CustomViewHolder extends RecyclerView.ViewHolder{
        //ImageView imageViewAccident;
        LinearLayout background;
        TextView textViewDate, textViewLocation, textViewIDToken;
        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            //imageViewAccident=itemView.findViewById(R.id.imageViewAccident);
            textViewDate= itemView.findViewById(R.id.dateTxt_log_item);
            textViewLocation=itemView.findViewById(R.id.locTxt_log_item);
            textViewIDToken=itemView.findViewById(R.id.IDTokenTxt_log_item);

            background=itemView.findViewById(R.id.background_loglist);
        }
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_log,parent,false);
        CustomViewHolder holder=new CustomViewHolder(view);

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int position=holder.getAdapterPosition();
                //Log.d("logadapter","position : "+position);
                AccidentLog log=arrayList.get(position);
                //Log.d("logadapter","isnull : "+(log==null));

                View loginfodialog=View.inflate(context,R.layout.dialog_loginfo,null);
                AlertDialog.Builder ldg=new AlertDialog.Builder(context);
                ldg.setTitle("No. "+position);
                ldg.setView(loginfodialog);

                TextView textViewDate=loginfodialog.findViewById(R.id.dateTxt_loginfo_dialog);
                TextView textViewS1=loginfodialog.findViewById(R.id.s1Txt_loginfo_dialog);
                TextView textViewS2=loginfodialog.findViewById(R.id.s2Txt_loginfo_dialog);
                TextView textViewtilt=loginfodialog.findViewById(R.id.tiltTxt_loginfo_dialog);
                TextView textViewLat=loginfodialog.findViewById(R.id.latTxt_loginfo_dialog);
                TextView textViewLon=loginfodialog.findViewById(R.id.lonTxt_loginfo_dialog);

                //Log.d("logadpater","isnull"+(textViewDate==null));

                textViewtilt.setText(""+log.getTilt_z());
                textViewLat.setText(""+log.getLatitude());
                textViewS1.setText(""+log.getSpeed1());
                textViewS2.setText(""+log.getSpeed2());
                textViewLon.setText(""+log.getLongitude());
                textViewDate.setText(log.getDate());

                ldg.setPositiveButton("확인",null);

                ldg.show();
                return true;
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        AccidentLog log=arrayList.get(position);
//        Glide.with(holder.itemView)
//                .load(log.getPhoto())
//                .into(holder.imageViewAccident);
        holder.textViewIDToken.setText("idToken: "+log.getIdToken());
        holder.textViewLocation.setText("Location: "+log.getLatitude()+", "+log.getLongitude());
        holder.textViewDate.setText("Date: "+log.getDate());
        holder.background.setBackgroundColor(rgbs[position%2]);
    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public void setArrayList(ArrayList<AccidentLog> arrayList) {
        this.arrayList = arrayList;
    }

    public void searchList(int year, int month, int day){
        String dateString=""+year+"-"+(month+1)+"-"+day;
        ArrayList<AccidentLog> filteredList=new ArrayList<>();

        for(AccidentLog log:arrayList){
            if(log.getDate().contains(dateString))
                filteredList.add(log);
        }
        this.arrayList=filteredList;
        this.notifyDataSetChanged();
    }
}
