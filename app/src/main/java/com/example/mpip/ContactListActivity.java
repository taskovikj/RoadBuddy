package com.example.mpip;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mpip.repository.Contact;
import com.example.mpip.repository.contactDB;

import java.util.List;

public class ContactListActivity extends AppCompatActivity {

    private LinearLayout contactLayout;
    private contactDB contactDB2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);

        contactDB2 = contactDB.getDB(this);
        contactLayout = findViewById(R.id.contactLayout);

        List<Contact> contacts = contactDB2.contactDao().getAllContacts();

        for (Contact contact : contacts) {
            View contactView = LayoutInflater.from(this).inflate(R.layout.item_contact, null);

            TextView nameTextView = contactView.findViewById(R.id.nameTextView);
            Button deleteButton = contactView.findViewById(R.id.deleteButton);

            nameTextView.setText(contact.getName());

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    showDeleteConfirmationDialog(contact);
                }
            });

            contactLayout.addView(contactView);
        }
    }

    private void showDeleteConfirmationDialog(final Contact contact) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Contact");

        builder.setMessage("Are you sure you want to delete this contact?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteContact(contact);
            }
        });
        builder.setNegativeButton("No", null);
        builder.show();
    }

    private void deleteContact(Contact contact) {
        contactDB2.contactDao().deleteContact(contact);
        recreate();
    }

    public void onBackPressed() {

        super.onBackPressed();
        Intent intent = new Intent(ContactListActivity.this, ContactsForm.class);
        startActivity(intent);
    }
}
