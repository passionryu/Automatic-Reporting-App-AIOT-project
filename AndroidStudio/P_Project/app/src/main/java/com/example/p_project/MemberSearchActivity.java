package com.example.p_project;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MemberSearchActivity extends Activity {
    Button returnBtn;
    ImageButton searchbutton;
    TextView search_selector;
    View selectDialog;
    EditText searchEdt_member;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<UserAccount> arrayList;

    private FirebaseDatabase database;
    private DatabaseReference dataRef;

    int selected_search=1;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchmember);

        returnBtn=(Button) findViewById(R.id.retrunBtn);
        search_selector=(TextView) findViewById(R.id.search_selector);
        searchEdt_member=(EditText) findViewById(R.id.searchEdt_member);
        searchbutton=(ImageButton) findViewById(R.id.search_imgbtn);

        recyclerView= findViewById(R.id.searchList);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        arrayList=new ArrayList<>();

        database=FirebaseDatabase.getInstance();
        dataRef=database.getReference("firebasetest").child("UserAccount");

        dataRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayList.clear();
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    UserAccount user=childSnapshot.getValue(UserAccount.class);
                    arrayList.add(user);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("MemberSearchActivity",String.valueOf(error.toException()));
            }
        });

        adapter=new CustomAdapter_memberlist(arrayList,this);
        recyclerView.setAdapter(adapter);

        search_selector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectDialog=(View) View.inflate(MemberSearchActivity.this,R.layout.dialog_searchmember,null);
                AlertDialog.Builder sdlg=new AlertDialog.Builder(MemberSearchActivity.this);
                sdlg.setTitle("검색 기준");
                sdlg.setView(selectDialog);
                RadioGroup radio_searchmember=selectDialog.findViewById(R.id.radio_searchmember_dialog);
                sdlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int checkedButtonID= radio_searchmember.getCheckedRadioButtonId();
                        View checkedButton=radio_searchmember.findViewById(checkedButtonID);

                        if(checkedButton==null){
                            return;
                        }

                        selected_search=radio_searchmember.indexOfChild(checkedButton);

                        Log.d("MemberSearch","selected_search : "+selected_search);
                        switch (selected_search){
                            case CustomAdapter_memberlist.SEARCH.PHONE:
                                search_selector.setText("전화번호");
                                break;

                            case CustomAdapter_memberlist.SEARCH.NAME:
                                search_selector.setText("이름");
                                break;

                            case CustomAdapter_memberlist.SEARCH.EMAIL:
                                search_selector.setText("email");
                                break;
                        }
                    }
                });
                //sdlg.setNegativeButton("취소",null);
                sdlg.show();
            }
        });

        searchbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String value=searchEdt_member.getText().toString();
                CustomAdapter_memberlist customAdapter=(CustomAdapter_memberlist) adapter;

                if(value.equals("")){
                    customAdapter.setArrayList(arrayList);
                    adapter.notifyDataSetChanged();
                }
                else{
                    customAdapter.searchList(value,selected_search);
                }
            }
        });


        returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Member","count : "+adapter.getItemCount());
                MemberSearchActivity.this.finish();
            }
        });
    }
}