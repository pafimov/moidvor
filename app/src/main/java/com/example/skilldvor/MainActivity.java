package com.example.skilldvor;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements OnTouchListener{
    //свойства выбранного элемента
    private View selected_item = null;
    private String selected_type;
    private int selected_margin_left;
    private int selected_template;
    private int selected_size_x = 100;
    private int selected_size_y = 100;

    //массив со всеми объектами на карте
    private ArrayList<ObjDvor> objects = new ArrayList<>();

    //переменные для работы кода
    private int offsetX = 0;
    private int offsetY = 0;
    boolean touchFlag = false;
    boolean dropFlag = false;
    boolean binFlag = false;
    LayoutParams imageParams;
    ImageView imageDrop, imageBin, imageDone;
    Button rotate;
    int eX, eY;
    int topY, leftX, rightX, bottomY, binTop, binLeft, binRight, binBottom;
    LayoutInflater layoutInflater;
    RelativeLayout rl;
    private float percentX, percentY;

    //база данных
    DatabaseReference mDataBase;
    private String VARIANTS_KEY = "Variants";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //инициализируем необходимые переменные
        init();

        rotate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selected_item != null) {
                    float rotation = selected_item.getRotation() + 90;
                    selected_item.animate().rotation(rotation);
                    for(ObjDvor od : objects){
                        if(od.getView() == selected_item){
                            od.rotation = rotation;
                            break;
                        }
                    }
                }
            }
        });
        imageDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //если в зоне расстановки есть объекты
                if(objects.size() > 0){
                    //добавляем в базу данных
                    DatabaseReference DBRef = mDataBase.push();
                    //String key = DBRef.getKey(); - ключ может понадобиться для доработок
                    DBRef.child("info").setValue("Ул. " + Login.userStreet+", д. " + Login.userHouse + ", кв. " + Login.userFlat);
                    for (ObjDvor od: objects){
                        DBRef.child("Objects").push().setValue(new ObjectDB(od.type, od.x, od.y, od.rotation));
                    }
                    Toast.makeText(getApplicationContext(), "Отправлено", Toast.LENGTH_LONG).show();
                    //выходим из активности
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(), "Нет объектов", Toast.LENGTH_LONG).show();
                }
            }
        });

        //создаём первый куст
        View img = layoutInflater.inflate(R.layout.template1, null, false);
        final RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);
        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        params.leftMargin= 300;
        params.bottomMargin = 30;
        img.setOnTouchListener(MainActivity.this);
        rl.addView(img, params);

        //создаём первую скамейку
        View img2 = layoutInflater.inflate(R.layout.template2, null, false);
        RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(150, 100);
        params2.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        params2.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        params2.leftMargin= 600;
        params2.bottomMargin = 30;
        img2.setOnTouchListener(MainActivity.this);
        rl.addView(img2, params2);

        //задаём OnTouchListener для RootView
        View root = findViewById(android.R.id.content).getRootView();
        root.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (touchFlag) {
                    switch (event.getActionMasked()) {
                        case MotionEvent.ACTION_DOWN:
                            //получаем координаты области расстановки и корзины
                            topY = imageDrop.getTop();
                            leftX = imageDrop.getLeft();
                            rightX = imageDrop.getRight();
                            bottomY = imageDrop.getBottom();
                            binTop = imageBin.getTop();
                            binLeft = imageBin.getLeft();
                            binRight = imageBin.getRight();
                            binBottom = imageBin.getBottom();
                            //высчитываем 1% области двора по оси X и Y
                            percentX = (float) (rightX - leftX)/100;
                            percentY = (float) ( bottomY - topY)/100;
                            break;
                        case MotionEvent.ACTION_MOVE:
                            //получаем координаты координаты перемещаемого объекта
                            eX = (int) event.getX();
                            eY = (int) event.getY();
                            int x = (int) event.getX() - offsetX;
                            int y = (int) event.getY() - offsetY;
                            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(new ViewGroup.MarginLayoutParams(selected_size_x, selected_size_y));
                            lp.setMargins(x, y, 0, 0);
                            //смотрим, попадает ли в область расстановки
                            if (selected_item.getLeft() > leftX && selected_item.getRight() < rightX && selected_item.getTop() > topY && selected_item.getBottom() < bottomY) {
                                imageDrop.setBackgroundColor(0xFFD1D1D1);
                                selected_item.bringToFront();
                                dropFlag = true;
                            } else {
                                imageDrop.setBackgroundColor(0xFFE1E1E1);
                                dropFlag = false;
                            }
                            //смотрим, попадает ли в область корзины
                            if (eX > binLeft && eX < binRight && eY > binTop && eY < binBottom) {
                                imageBin.setImageResource(R.drawable.openedbin);
                                selected_item.bringToFront();
                                binFlag = true;
                            } else {
                                imageBin.setImageResource(R.drawable.trashbin);
                                binFlag = false;
                            }
                            selected_item.setLayoutParams(lp);
                            break;
                        case MotionEvent.ACTION_UP:
                            touchFlag = false;
                            imageBin.setImageResource(R.drawable.trashbin);
                            imageDrop.setBackgroundColor(0xFFE1E1E1);
                            //смотрим, находится ли перемещаемый объект в зоне расстановки в момент, когда его отпустили
                            if (dropFlag) {
                                //проверяем, в первый раз ли выбранный объект попал в область расстановки
                                if(selected_item.getTag().toString().split(" ").length <= 1){
                                    selected_item.setTag(selected_item.getTag().toString() + " used");
                                    //добавляем выбранный объект в массив со всеми объектами
                                    ObjDvor objDvor = new ObjDvor(selected_type, selected_item, percentX, percentY, leftX, topY);
                                    objects.add(objDvor);
                                    //создаём новый объект на панели объектов
                                    View img = layoutInflater.inflate(selected_template, null, false);
                                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(selected_size_x, selected_size_y);
                                    params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
                                    params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
                                    params.leftMargin= selected_margin_left;
                                    params.bottomMargin = 30;
                                    img.setOnTouchListener(MainActivity.this);
                                    rl.addView(img, params);

                                }
                                else{
                                    //обновляем координаты объекта
                                    for(ObjDvor od : objects) {
                                        if(selected_item == od.getView()){
                                            od.updateCoords( percentX, percentY, leftX, topY);
                                            break;
                                        }
                                    }
                                }
                                dropFlag = false;
                            }
                            //проверяем, находится ли перемещаемый объект в зоне корзины в момент, когда его отпустили И бывал ли он в зоне расстановки до этого
                            else if(binFlag && selected_item.getTag().toString().split(" ").length >= 2) {
                                //удаляем объект
                                selected_item.setVisibility(View.INVISIBLE);
                                for(ObjDvor od : objects) {
                                    if(od.getView() == selected_item){
                                        objects.remove(od);
                                        break;
                                    }
                                }
                            }
                            //если перемещаемый объект находится в свободной области в момент, когда его отпустили
                            else {
                                //возвращаем на место
                                selected_item.setLayoutParams(imageParams);
                            }
                            break;
                        default:
                            break;
                    }
                }
                return true;
            }
        });
    }
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                touchFlag = true;
                offsetX = (int) event.getX();
                offsetY = (int) event.getY();
                selected_item = v;
                //если нажатым объектом является куст
                if(v.getTag().toString().split(" ")[0].equals("kust")) {
                    //устанавливаем параметры выбранного объекта
                    selected_type = "kust";
                    selected_margin_left = 300;
                    selected_template = R.layout.template1;
                    selected_size_x = 100;
                    selected_size_y = 100;
                }
                //если нажатым объектом является скамейка
                else if(v.getTag().toString().split(" ")[0].equals("skameyka")) {
                    //устанавливаем параметры выбранного объекта
                    selected_type = "skameyka";
                    selected_margin_left = 600;
                    selected_template = R.layout.template2;
                    selected_size_x = 150;
                    selected_size_y = 100;
                }
                //запоминаем, как на момент выделения расположен выбранный объект
                imageParams = v.getLayoutParams();
                break;
            case MotionEvent.ACTION_UP:
                selected_item = null;
                touchFlag = false;
                break;
            default:
                break;
        }
        return false;
    }

    private void init() {
        imageDrop = (ImageView) findViewById(R.id.ImgDrop);
        imageBin = (ImageView) findViewById(R.id.bin);
        imageDone = findViewById(R.id.done);
        rl = findViewById(R.id.rl);
        layoutInflater = LayoutInflater.from(getApplicationContext());
        rotate = findViewById(R.id.rotate);
        mDataBase = FirebaseDatabase.getInstance().getReference(VARIANTS_KEY);
    }
}