package com.example.skilldvor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class RootLayoutCheck extends AppCompatActivity {
    ImageView imageDrop;
    TextView countView;
    LayoutInflater layoutInflater;
    ObjectDB[] objectDBS;
    RelativeLayout rl;
    int kustCount, skameykaCount;
    ListView objectsCoords;
    List<String> listData;
    ArrayAdapter<String> arrayAdapter;
    int topY, leftX, rightX, bottomY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_root_layout_check);
        //инициализируем необходимые переменные
        init();
        //получаем данные о расположении объектов
        getDataFromIntent();
    }

    private void init() {
        imageDrop = (ImageView) findViewById(R.id.ImageDrop);
        countView = findViewById(R.id.countObjects);
        layoutInflater = LayoutInflater.from(getApplicationContext());
        rl = findViewById(R.id.dropLayout);
        listData = new ArrayList<>();
        objectsCoords = findViewById(R.id.coordsList);
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listData);
        objectsCoords.setAdapter(arrayAdapter);
        imageDrop.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //отображаем объекты
                display();
                imageDrop.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }
    private void getDataFromIntent() {
        Intent i = getIntent();
        objectDBS = (ObjectDB[]) i.getSerializableExtra("objects");
    }
    private void display() {
        //ОТОБРАЖЕНИЕ РЕАЛИЗОВАНО ТАК, ЧТО, НЕ СМОТРЯ НА ЗАПОМИНАНИЕ РАСПОЛОЖЕНИЯ ПО КООРДИНАТАМ, ОНО АДАПТИВНО
        for( ObjectDB od: objectDBS){
            String type = od.type;
            View inf = null;
            String coordsString = "";
            if(type.equals("kust")) {
                kustCount++;
                coordsString = "Куст ";
                inf = layoutInflater.inflate(R.layout.template1, null, false);
                System.out.println(type);
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);
                inf.setLayoutParams(params);
            }else if(type.equals("skameyka")) {
                skameykaCount++;
                coordsString = "Скамейка ";
                inf = layoutInflater.inflate(R.layout.template2, null, false);
                System.out.println(type);
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(150, 100);
                inf.setLayoutParams(params);
            }
            topY = imageDrop.getTop();
            leftX = imageDrop.getLeft();
            rightX = imageDrop.getRight();
            bottomY = imageDrop.getBottom();
            float xr = ((float) (rightX - leftX)/ 100) * od.x;
            float yr = ((float) ( bottomY - topY)/ 100) * od.y;
            inf.setX(xr);
            inf.setY(yr);
            inf.setRotation(od.rotation);
            inf.bringToFront();
            rl.addView(inf);
            coordsString += "( X: " + (int)xr + "; Y: "+ (int)yr + " )";
            listData.add(coordsString);
            arrayAdapter.notifyDataSetChanged();
        }
        String countText = "Кусты: " + kustCount + " шт.\nСкамейки: " + skameykaCount + " шт.";
        countView.setText(countText);
    }
}