package com.example.skilldvor;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddUser extends AppCompatActivity {
    private DatabaseReference mDataBase;
    private EditText streetView, houseView, flatView,passwordView;
    private Button done;
    private String USER_KEY = "User";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);
        //инициализируем необходимые переменные
        init();
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //собираем данные с полей ввода
                String street = streetView.getText().toString();
                String house = houseView.getText().toString();
                String flat = flatView.getText().toString();
                String password = passwordView.getText().toString();
                //проверяем, не пустые ли поля ввода
                if(!TextUtils.isEmpty(street) && !TextUtils.isEmpty(house) && !TextUtils.isEmpty(flat) && !TextUtils.isEmpty(password)){
                    //добавляем пользователя в базу данных
                    User user = new User(street, house, flat, password);
                    mDataBase.push().setValue(user);
                    Toast.makeText(getApplicationContext(), "Житель добавлен", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Заполните все поля", Toast.LENGTH_LONG).show();
                }
                //выход из активности
                finish();
            }
        });
    }
    private void init(){
        streetView = findViewById(R.id.streetAdd);
        houseView = findViewById(R.id.houseAdd);
        flatView = findViewById(R.id.flatAdd);
        passwordView = findViewById(R.id.passwordAdd);
        done = findViewById(R.id.doneAdd);
        mDataBase = FirebaseDatabase.getInstance().getReference(USER_KEY);
    }
}