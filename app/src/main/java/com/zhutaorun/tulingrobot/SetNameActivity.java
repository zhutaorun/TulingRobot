package com.zhutaorun.tulingrobot;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by zhutaorun on 15/9/4.
 */
public class SetNameActivity extends Activity {

    String Name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.setname);

        Button mBsetName = (Button) findViewById(R.id.Bastname);
        final EditText mSetName = (EditText) findViewById(R.id.setname);

        mBsetName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Name = mSetName.getText().toString(); //自定义名字
                SharedPreferences.Editor editor = getSharedPreferences("data",MODE_PRIVATE).edit();
                editor.putString("name", Name);
                editor.commit();
                Intent intent = new Intent(SetNameActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
