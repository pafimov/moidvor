package com.example.skilldvor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class RootMenu extends AppCompatActivity {
    Button viewGo, viewAdd, viewAddNews, viewNews;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_root_menu);
        //инициализируем необходимые переменные
        init();
        //эта активность выполняет навигационную функцию. в зависимости от нажатой кнопки переходим на соответствующую активность
        viewGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //список расстановок жителей
                Intent i = new Intent(getApplicationContext(), OrderList.class);
                startActivity(i);
            }
        });

        viewAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //добавление нового жителя
                Intent i = new Intent(getApplicationContext(), AddUser.class);
                startActivity(i);
            }
        });
        viewAddNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //добавление новости
                Intent i = new Intent(getApplicationContext(), AddNews.class);
                startActivity(i);
            }
        });
        viewNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //список новостей
                Intent i = new Intent(getApplicationContext(), NewsList.class);
                startActivity(i);
            }
        });
    }
    private void init(){
        viewGo = findViewById(R.id.viewgo);
        viewAdd = findViewById(R.id.viewadd);
        viewAddNews = findViewById(R.id.viewaddnews);
        viewNews = findViewById(R.id.viewNewsRoot);
    }
}