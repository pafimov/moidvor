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

public class NewsList extends AppCompatActivity {
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private List<String> listData;
    private List<NewsBD> listTemp;
    private DatabaseReference mDataBase;
    private static String NEWS_KEY = "News";
    private ValueEventListener valueEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_list);
        //инициализируем необходимые переменные
        init();
        //получаем данные о новостях из базы данных
        getDataFromDB();
        //задаём OnItemClickListener для listView
        OnClickSet();
    }
    private void init(){
        listView = findViewById(R.id.listViewer);
        listData = new ArrayList<>();
        listTemp = new ArrayList<>();
        adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, listData);
        listView.setAdapter(adapter);
        mDataBase = FirebaseDatabase.getInstance().getReference(NEWS_KEY);
    }
    private void getDataFromDB(){
         valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(listData.size()>0) {
                    listData.clear();
                }
                if(listTemp.size()>0) {
                    listTemp.clear();
                }
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    NewsBD newsBD = ds.getValue(NewsBD.class);
                    assert newsBD != null;
                    newsBD.key = ds.getKey();
                    listData.add(newsBD.name);
                    listTemp.add(newsBD);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        mDataBase.addValueEventListener(valueEventListener);
    }
    private void OnClickSet(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getApplicationContext(), NewsCheck.class);
                i.putExtra("name", listTemp.get(position).name);
                i.putExtra("text", listTemp.get(position).text);
                i.putExtra("key", listTemp.get(position).key);
                startActivity(i);
            }
        });
    }
    //делаем, чтобы при выходе из активности удалялся valueEventListener
    @Override
    public void onBackPressed() {
        mDataBase.removeEventListener(valueEventListener);
        super.onBackPressed();
    }
}