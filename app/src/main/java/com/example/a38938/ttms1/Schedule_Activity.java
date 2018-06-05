package com.example.a38938.ttms1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by 38938 on 2018/4/15.
 */

public class Schedule_Activity extends AppCompatActivity {
    private Button schedule_modify;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.schedule_activity);

        schedule_modify = (Button)findViewById(R.id.schedule_modify);
        schedule_modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent schedule_Modify = new Intent(Schedule_Activity.this,Schedule_Activity_modify.class);
                startActivityForResult(schedule_Modify,0);
            }
        });

    }
}
