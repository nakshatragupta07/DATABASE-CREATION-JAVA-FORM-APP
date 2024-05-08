package com.cscorner.database_form;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    EditText name, rsno, mark;
    Button btnAdd, btnDelete, btnUpdate, btnView, btnViewAll;
    SQLiteDatabase db;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name = (EditText) findViewById(R.id.editTextText1);
        mark = (EditText) findViewById(R.id.editTextText2);
        rsno = (EditText) findViewById(R.id.editTextText3);
        btnAdd = (Button) findViewById(R.id.button1);
        btnView = (Button) findViewById(R.id.button2);
        btnViewAll = (Button) findViewById(R.id.button3);
        btnUpdate = (Button) findViewById(R.id.button4);
        btnDelete = (Button) findViewById(R.id.button5);

        db = openOrCreateDatabase("Students", Context.MODE_PRIVATE, null);

        db.execSQL("CREATE TABLE IF NOT EXISTS student(name VARCHAR,regno VARCHR,mark VARCHAR);");

        btnAdd.setOnClickListener(new OnClickListener() {

            @Override

            public void onClick(View arg0) {
                if (rsno.getText().toString().trim().length() == 0 || name.getText().toString().trim().length() == 0 || mark.getText().toString().trim().length() == 0) {

                    showMessage("Error", "Please enter all values");
                    return;

                }

                db.execSQL("INSERT INTO student VALUES('" + rsno.getText() + "','" + name.getText() + "','" + mark.getText() + "');");


                showMessage("Success", "Record added");
                clearText();
            }

        });

        btnDelete.setOnClickListener(new OnClickListener() {

            @Override

            public void onClick(View v) {

// TODO Auto-generated method stub

                if (rsno.getText().toString().trim().length() == 0) {

                    showMessage("Error", "Please enter Reg. No.");
                    return;
                }

                @SuppressLint("Recycle") Cursor c = db.rawQuery("SELECT *FROM student WHERE regno='" + rsno.getText() + "'", null);
                if (c.moveToFirst()) {
                    db.execSQL("DELETE FROM student WHERE regno='" + rsno.getText() + "'");
                    showMessage("Success", "Record Deleted");
                } else {
                    showMessage("Error", "Invalid Reg. No.");
                }
                clearText();
            }
        });
        btnUpdate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
// TODO Auto-generated method stub
                if (rsno.getText().toString().trim().length() == 0) {
                    showMessage("Error", "Please enter Reg. No.");
                    System.out.println("In update");
                    return;
                }
                @SuppressLint("Recycle") Cursor c = db.rawQuery("SELECT * FROM student WHERE regno='" + rsno.getText() + "'", null);
                if (c.moveToFirst()) {
                    db.execSQL("UPDATE student SET name='" + name.getText() + "',mark='" + mark.getText() + "' WHERE regno='" + rsno.getText() + "'");

                    showMessage("Success", "Record Modified");
                } else {
                    showMessage("Error", "Invalid Reg. No.");
                }
                clearText();
            }
        });
        btnView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (rsno.getText().toString().trim().length() == 0) {
                    showMessage("Error", "Please enter Reg. No.");
                    return;
                }
                @SuppressLint("Recycle") Cursor c = db.rawQuery("SELECT * FROM student WHERE regno='" + rsno.getText() + "'", null);
                if (c.moveToFirst()) {
                    name.setText(c.getString(1));
                    mark.setText(c.getString(2));
                } else {
                    showMessage("Error", "Invalid Reg. No.");
                    clearText();
                }
            }
        });
        btnViewAll.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
// TODO Auto-generated method stub
                @SuppressLint("Recycle") Cursor c = db.rawQuery("SELECT * FROM student", null);
                if (c.getCount() == 0) {
                    showMessage("Error", "No records found");
                    return;
                }
                StringBuilder buffer = new StringBuilder();
                while (c.moveToNext()) {
                    buffer.append("Reg. No : ").append(c.getString(0)).append("\n");
                    buffer.append("Name : ").append(c.getString(1)).append("\n");
                    buffer.append("Mark : ").append(c.getString(2)).append("\n\n");
                }
                showMessage("Student Details", buffer.toString());
            }
        });
    }

    public void showMessage(String title, String message) {
        Builder builder = new Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

    public void clearText() {
        rsno.setText("");
        name.setText("");
        mark.setText("");
        rsno.requestFocus();
    }
}