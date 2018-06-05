package com.example.a38938.ttms1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by 38938 on 2018/4/15.
 */

public class Play_Activity extends AppCompatActivity {
    private Button user_modify;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play_activity);

        user_modify = (Button)findViewById(R.id.play_modify);
        user_modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent user_Modify = new Intent(Play_Activity.this,Play_Activity_modify.class);
                startActivityForResult(user_Modify,0);
            }
        });

    }
}
