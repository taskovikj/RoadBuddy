package com.example.mpip;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mpip.repository.Contact;
import com.example.mpip.repository.contactDB;

public class ContactsForm extends AppCompatActivity {
    EditText editName, editPhone;
    Button saveContact, editContacts;
    contactDB contactDB2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_form);

        editName = findViewById(R.id.editTextName);
        editPhone = findViewById(R.id.editTextPhone);
        saveContact = findViewById(R.id.buttonSave);
        editContacts = findViewById(R.id.contactList);

        contactDB2 = contactDB.getDB(this);

        saveContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editName.getText().toString();
                String phone = editPhone.getText().toString();

                if (isNameValid(name) && isPhoneNumberValid(phone)) {
                    contactDB2.contactDao().addContact(new Contact(name, phone));
                    editName.setText("");
                    editPhone.setText("");
                } else {
                    // Show an error message indicating invalid information
                    if (!isNameValid(name)) {
                        Toast.makeText(ContactsForm.this, "Invalid name", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ContactsForm.this, "Invalid phone number", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        editContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(ContactsForm.this, ContactListActivity.class);
                startActivity(intent);
            }
        });
    }

    private boolean isNameValid(String name) {
        return name.matches(".*[a-zA-Z].*");
    }

    private boolean isPhoneNumberValid(String phoneNumber) {
        return phoneNumber.matches("^07\\d{7}$");
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ContactsForm.this, SecondActivity.class);
        startActivity(intent);
    }

}
