package com.example.a38938.ttms1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by 38938 on 2018/4/15.
 */

public class Play_Activity_modify extends AppCompatActivity {
    private Button play_modify_save;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play_activity_modify);

        play_modify_save = (Button)findViewById(R.id.play_modify_save);

        play_modify_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Play_Activity_modify.this,"已保存",Toast.LENGTH_LONG).show();
            }
        });
    }
}
