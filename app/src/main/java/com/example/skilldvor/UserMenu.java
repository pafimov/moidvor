package com.example.skilldvor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class UserMenu extends AppCompatActivity {
    Button addVariant, newsView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_menu);
        //инициализируем необходимые переменные
        init();
        //эта активность выполняет навигационную функцию. в зависимости от нажатой кнопки переходим на соответствующую активность
        addVariant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //создание новой расстановки
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            }
        });
        newsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //список новостей
                Intent i = new Intent(getApplicationContext(), NewsList.class);
                startActivity(i);
            }
        });
    }
    private void init(){
        addVariant = findViewById(R.id.newvariant);
        newsView = findViewById(R.id.news);
    }
}