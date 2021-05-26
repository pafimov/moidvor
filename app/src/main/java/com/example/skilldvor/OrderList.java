package com.example.skilldvor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class OrderList extends AppCompatActivity {
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private List<String> listData;
    private List<ObjectDB[]> listTemp;
    private DatabaseReference mDataBase;
    private String VARIANTS_KEY = "Variants";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);
        //инициализируем необходимые переменные
        init();
        //получаем данные об объектах из базы данных
        getDataFromDB();
        //задаём OnItemClickListener для listView
        setOnClickItem();
    }
    private void getDataFromDB(){
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(listData.size()>0) {
                    listData.clear();
                }
                if(listTemp.size()>0) {
                    listTemp.clear();
                }
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    listData.add(ds.child("info").getValue().toString());
                    ObjectDB[] objectDBS = new ObjectDB[Integer.parseInt(Long.toString(ds.child("Objects").getChildrenCount()))];
                    int sch = 0;
                    for(DataSnapshot ds2 : ds.child("Objects").getChildren()) {
                        ObjectDB objectDB= ds2.getValue(ObjectDB.class);
                        assert objectDB != null;
                        objectDBS[sch] = objectDB;
                        sch++;
                    }
                    listTemp.add(objectDBS);
                }
                adapter.notifyDataSetChanged();
                mDataBase.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        mDataBase.addValueEventListener(valueEventListener);
    }
    private void setOnClickItem(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ObjectDB[] objectDBs = listTemp.get(position);
                Intent i = new Intent(getApplicationContext(), RootLayoutCheck.class);
                i.putExtra("objects", objectDBs);
                startActivity(i);
            }
        });
    }
    private void init(){
        listView = findViewById(R.id.listView);
        listData = new ArrayList<>();
        listTemp = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listData);
        listView.setAdapter(adapter);
        mDataBase = FirebaseDatabase.getInstance().getReference(VARIANTS_KEY);
    }
}