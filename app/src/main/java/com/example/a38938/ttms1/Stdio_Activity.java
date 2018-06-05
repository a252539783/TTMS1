package com.example.a38938.ttms1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by 38938 on 2018/4/15.
 */

public class Stdio_Activity extends AppCompatActivity {
    private Button stdio_modify;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stdio_activity);

        stdio_modify = (Button)findViewById(R.id.stdio_modify);
        stdio_modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent stdio_Modify = new Intent(Stdio_Activity.this,Stdio_Activity_modify.class);
                startActivityForResult(stdio_Modify,0);
            }
        });

    }
}
