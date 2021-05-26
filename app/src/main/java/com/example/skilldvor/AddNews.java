package com.example.skilldvor;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddNews extends AppCompatActivity {
    private EditText nameView, textView;
    private Button done;
    private DatabaseReference mDataBase;
    private static String NEWS_KEY = "News";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_news);
        //инициализируем необходимые переменные
        init();

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //собираем данные с полей ввода
                String name = nameView.getText().toString();
                String text = textView.getText().toString();
                //проверяем, не пустые ли поля ввода
                if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(text)) {
                    //добавляем новость в базу данных
                    NewsBD newsBD = new NewsBD(name, text);
                    mDataBase.push().setValue(newsBD);
                    Toast.makeText(getApplicationContext(), "Новость добалена", Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(getApplicationContext(), "Заполните все поля", Toast.LENGTH_LONG).show();
                }
                finish();
            }
        });
    }
    private void init(){
        done = findViewById(R.id.AddNews);
        nameView = findViewById(R.id.NewsName);
        textView = findViewById(R.id.NewsText);
        mDataBase = FirebaseDatabase.getInstance().getReference(NEWS_KEY);
    }
}