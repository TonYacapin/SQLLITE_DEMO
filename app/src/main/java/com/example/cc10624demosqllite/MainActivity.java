package com.example.cc10624demosqllite;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button btnSAVE, btnVIEWRECORDS;
    private EditText txtname, txtlocation, txtCourse;

    private DBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSAVE = findViewById(R.id.btnSAVE);
        btnVIEWRECORDS = findViewById(R.id.btn_ViewRecords);
        txtname = findViewById(R.id.TV_NAME);
        txtlocation = findViewById(R.id.TV_LOCATION);
        txtCourse = findViewById(R.id.TV_COURSE);

        dbHandler = new DBHandler(this);

        btnSAVE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtname.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Name is Required", Toast.LENGTH_SHORT).show();
                } else if (txtlocation.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Location is Required", Toast.LENGTH_SHORT).show();
                } else if (txtCourse.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Course is Required", Toast.LENGTH_SHORT).show();
                } else {
                    String name = txtname.getText().toString();
                    String location = txtlocation.getText().toString();
                    String course = txtCourse.getText().toString();

                    dbHandler.saveStudentInfo(name, location, course);

                    Toast.makeText(getApplicationContext(), "Saved Successfully", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
