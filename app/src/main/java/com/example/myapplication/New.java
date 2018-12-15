package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class New extends Activity {
    EditText name_tx, last_name_tx, year_tx, phone_tx, email_tx;
    Button save;
    DatabaseReference mRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_data);

        name_tx = findViewById(R.id.name_text);
        last_name_tx = findViewById(R.id.last_name_text);
        year_tx = findViewById(R.id.year_text);
        phone_tx = findViewById(R.id.phone_text);
        email_tx = findViewById(R.id.email_text);

        save = findViewById(R.id.save);
        mRef = FirebaseDatabase.getInstance().getReference("Data");

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Add_Data();
            }
        });
    }

    public void Add_Data(){
        String name = name_tx.getText().toString();
        String last_name = last_name_tx.getText().toString();
        String email = email_tx.getText().toString();
        Integer year = Integer.valueOf(year_tx.getText().toString());
        Long phone = Long.valueOf(phone_tx.getText().toString());

        User user = new User(name, last_name, email, year, phone);

        mRef.push().setValue(user);

        Toast.makeText(getApplication(),"Save Successfully", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}

class User{
    private String name, last_name, email;
    Integer year;
    Long phone;

    public User(String name, String last_name, String email, Integer year, Long phone) {
        this.name = name;
        this.last_name = last_name;
        this.email = email;
        this.year = year;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Long getPhone() {
        return phone;
    }

    public void setPhone(Long phone) {
        this.phone = phone;
    }
}
