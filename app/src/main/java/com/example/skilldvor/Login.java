package com.example.skilldvor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
//Данные для входа в кабинет управляющей компании: улица "1", дом "1", квартира "1", пароль "1".
//Данные для входа в кабинет жителя(одного из): улица "Kirova", дом "18", квартира "23", пароль "qwerty123".
public class Login extends AppCompatActivity {
    public static String userStreet, userHouse, userFlat;
    private DatabaseReference mDataBase;
    private EditText streetView, houseView, flatView,passwordView;
    private Button done;
    private String USER_KEY = "User";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //инициализируем необходимые переменные
        init();

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //собираем данные с полей ввода
                final String street = streetView.getText().toString();
                final String house = houseView.getText().toString();
                final String flat = flatView.getText().toString();
                final String password = passwordView.getText().toString();
                //проверяем для кабинета управляющей компании
                if(street.equals("1") && house.equals("1") && flat.equals("1") && password.equals("1")) {
                    userStreet = "root";
                    userHouse = "root";
                    userFlat = "root";
                    //переходим в кабинет управляющей компании
                    Intent i = new Intent(getApplicationContext(), RootMenu.class);
                    startActivity(i);
                }else{
                    //иначе ищем среди пользователей в базе данных
                    ValueEventListener valueEventListener = new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            boolean fl = false;
                            for(DataSnapshot ds: dataSnapshot.getChildren()){
                                User user = ds.getValue(User.class);
                                assert user != null;
                                //если данные для входа нашлись т.е. они верны
                                if(user.street.equals(street)&& user.house.equals(house) && user.flat.equals(flat) && user.password.equals(password)) {
                                    fl = true;
                                    userStreet = user.street;
                                    userHouse = user.house;
                                    userFlat = user.flat;
                                    //переходим в личный кабинет жителя
                                    Intent i = new Intent(getApplicationContext(), UserMenu.class);
                                    mDataBase.removeEventListener(this);
                                    startActivity(i);
                                }
                            }
                            //если не нашлись такие даныые, пишем, что введённые данные не верны
                            if(!fl){
                                Toast.makeText(getApplicationContext(), "Неверные данные для входа", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    };
                    mDataBase.addValueEventListener(valueEventListener);
                }
            }
        });
    }
    private void init(){
        streetView = findViewById(R.id.street);
        houseView = findViewById(R.id.house);
        flatView = findViewById(R.id.flat);
        passwordView = findViewById(R.id.password);
        done = findViewById(R.id.done);
        mDataBase = FirebaseDatabase.getInstance().getReference(USER_KEY);
    }
}