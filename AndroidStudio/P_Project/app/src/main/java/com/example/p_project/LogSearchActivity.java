package com.example.p_project;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LogSearchActivity extends AppCompatActivity {

    private RecyclerView recyclerViewLog;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<AccidentLog> arrayList;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private Button buttonReset, buttonDate;
    private ImageButton retturnButton;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchlog);

        recyclerViewLog=(RecyclerView) findViewById(R.id.logRcView_searchlog_activity);
        recyclerViewLog.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerViewLog.setLayoutManager(layoutManager);
        arrayList=new ArrayList<>();

        database=FirebaseDatabase.getInstance();

        databaseReference=database.getReference("firebasetest").child("accidentLogs");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayList.clear();
                for(DataSnapshot child:snapshot.getChildren()){
                    AccidentLog log=child.getValue(AccidentLog.class);
                    arrayList.add(log);
                }
                Log.d("LogSearch","line : 66");
                adapter.notifyDataSetChanged();
                Log.d("LogSearch","count : "+adapter.getItemCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Log.d("LogSearch","line : 75");
        adapter=new CustomAdapter_loglist(arrayList, LogSearchActivity.this);
        recyclerViewLog.setAdapter(adapter);

        buttonDate=findViewById(R.id.dateBtn_searchlog_activity);
        buttonReset=findViewById(R.id.resetBtn_searchlog_activity);
        retturnButton=findViewById(R.id.retrunImgBtn_searchlog_activity);

        CustomAdapter_loglist cadapter=(CustomAdapter_loglist) adapter;

        buttonDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View datePickerView=(View) View.inflate(LogSearchActivity.this, R.layout.dialog_datepicker,null);
                AlertDialog.Builder dplg=new AlertDialog.Builder(LogSearchActivity.this);
                dplg.setView(datePickerView);

                dplg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DatePicker datePicker_d=datePickerView.findViewById(R.id.datePicker_dialog);
                        int year=datePicker_d.getYear();
                        int month=datePicker_d.getMonth();
                        int day=datePicker_d.getDayOfMonth();

                        cadapter.searchList(year,month,day);
                    }
                });
                dplg.show();
            }
        });

        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cadapter.setArrayList(arrayList);
                adapter.notifyDataSetChanged();
            }
        });

        retturnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogSearchActivity.this.finish();
            }
        });
    }
}
