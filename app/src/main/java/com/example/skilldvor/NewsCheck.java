package com.example.skilldvor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NewsCheck extends AppCompatActivity {
    TextView nameView, textView;
    Button del;
    String street, house, flat;
    String name, text, key;
    DatabaseReference mDataBase;
    private static String NEWS_KEY = "News";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_check);
        //инициализируем необходимые переменные
        init();
        //отображаем содержимое новости
        display();
    }
    private void init(){
        nameView = findViewById(R.id.NewsNameDisplay);
        textView = findViewById(R.id.NewsTextDisplay);
        del = findViewById(R.id.delButton);
        street = Login.userStreet;
        house = Login.userHouse;
        flat = Login.userFlat;
        mDataBase = FirebaseDatabase.getInstance().getReference(NEWS_KEY);
        //проверяем, из какого кабинета зашли(является ли он кабинетом управляющей компании)
        if(street.equals("root")&& house.equals("root") && flat.equals("root")){
            //если это кабинет управляющей компании, то активируем кнопку удаления новости
            del.setVisibility(View.VISIBLE);
            del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //удаляем новость из базы данных
                    key = getIntent().getStringExtra("key");
                    mDataBase.child(key).removeValue();
                    //выходим из активности
                    finish();
                }
            });
        }
    }
    private void display(){
        Intent i = getIntent();
        name = i.getStringExtra("name");
        text = i.getStringExtra("text");
        nameView.setText(name);
        textView.setText(text);
    }
}