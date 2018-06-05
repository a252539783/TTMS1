package com.example.a38938.ttms1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by 38938 on 2018/4/15.
 */

public class Stdio_Activity_modify extends AppCompatActivity {
    private Button stdio_modify_save;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stdio_activity_modify);

        stdio_modify_save = (Button)findViewById(R.id.stdio_modify_save);

        stdio_modify_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Stdio_Activity_modify.this,"已保存",Toast.LENGTH_LONG).show();
            }
        });
    }
}
