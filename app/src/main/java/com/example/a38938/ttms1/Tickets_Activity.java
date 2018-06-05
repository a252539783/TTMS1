package com.example.a38938.ttms1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by 38938 on 2018/4/15.
 */

public class Tickets_Activity extends AppCompatActivity {
    private Button buy;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tickets_activity);

        buy = (Button)findViewById(R.id.buy);
        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ticket_Buy = new Intent(Tickets_Activity.this,Tickets_Sale_Activity.class);
                startActivityForResult(ticket_Buy,0);
            }
        });

    }
}
