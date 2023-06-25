package com.example.mpip;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.mpip.model.Contact;
import com.example.mpip.model.contactDB;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ContactsForm extends AppCompatActivity {
    EditText editName, editPhone;
    Button saveContact;
    contactDB contactDB2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_form);


        editName = findViewById(R.id.editTextName);
        editPhone = findViewById(R.id.editTextPhone);
        saveContact = findViewById(R.id.buttonSave);

        contactDB contactDB2 = com.example.mpip.model.contactDB.getDB(this);

        saveContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              String name = editName.getText().toString();
              String phone = (editPhone.getText().toString());

                contactDB2.contactDao().addContact(
                        new Contact(name,phone)
                );
                editName.setText("");
                editPhone.setText("");
            }
        });




    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ContactsForm.this, SecondActivity.class);
        startActivity(intent);
    }

    private String formatNumber(String phoneNumber) {
        char[] charArray = phoneNumber.toCharArray();
//        071798356
//        "+389-71-798-356"
        StringBuilder formattedNumber = new StringBuilder("+389-" + charArray[1] + charArray[2] + "-" + charArray[3] + charArray[4] + charArray[5] + "-" + charArray[6] + charArray[7] + charArray[8]);

        return formattedNumber.toString();
    }
}