package com.example.a38938.ttms1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by 38938 on 2018/4/15.
 */

public class Users_Activity extends AppCompatActivity {
    private Button user_modify;
    private Button user_delete;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.users_activity);

        user_modify = (Button)findViewById(R.id.user_modify);
        user_delete = (Button)findViewById(R.id.user_delete);
        user_modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent user_Modify = new Intent(Users_Activity.this,Users_Activity_modify.class);
                startActivityForResult(user_Modify,0);
            }
        });

    }
}
