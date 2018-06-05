package com.example.a38938.ttms1;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by 38938 on 2018/4/15.
 */

public class Tickets_Sale_Activity extends AppCompatActivity {
    private Button ticket_buy;
    private Button bnt_11;
    private Button bnt_12;
    private Button bnt_13;
    private Button bnt_14;
    private Button bnt_21;
    private Button bnt_22;
    private Button bnt_23;
    private Button bnt_24;
    private Button bnt_31;
    private Button bnt_32;
    private Button bnt_33;
    private Button bnt_34;
    private Button bnt_41;
    private Button bnt_42;
    private Button bnt_43;
    private Button bnt_44;
    private Button bnt_51;
    private Button bnt_52;
    private Button bnt_53;
    private Button bnt_54;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tickets_sale_activity);

        ticket_buy = (Button)findViewById(R.id.ticket_save);
        bnt_11 = (Button)findViewById(R.id.bnt_11);
        bnt_12 = (Button)findViewById(R.id.bnt_12);
        ticket_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Tickets_Sale_Activity.this,"已购买",Toast.LENGTH_LONG).show();
            }
        });
        bnt_11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bnt_11.setBackgroundColor(Color.parseColor("#76EE00"));
            }
        });

        bnt_12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bnt_12.setBackgroundColor(Color.parseColor("#76EE00"));
            }
        });
    }
}
