package com.example.mpip;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.room.Room;


import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mpip.model.Contact;
import com.example.mpip.model.contactDB;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class SecondActivity extends AppCompatActivity {

    private final static int REQUEST_CODE_LOCATION = 100;
    private final static int REQUEST_CODE_SMS = 101;

    FusedLocationProviderClient fusedLocationProviderClient;
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    TextView name;
    Button singOutBTN, buttonNext, sendLocation;
    String lat, lon, add, city, country;
    TextView txtTst;
    contactDB contactDB2;
    private static final String channelId = "my_channel_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        contactDB2 = contactDB.getDB(this);

        name = findViewById(R.id.name);
        singOutBTN = findViewById(R.id.signOutBtn);
        buttonNext = findViewById(R.id.contactsOpt);
        sendLocation = findViewById(R.id.sendLocation);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        txtTst = findViewById(R.id.topTextView);


        // Check if the device is running on Android Oreo or higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence channelName = "My Channel";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }



        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this, gso);

        GoogleSignInAccount acc = GoogleSignIn.getLastSignedInAccount(this);
        if (acc != null) {
            String userName = acc.getDisplayName();
            name.setText(userName);
        }

        sendLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLastLocation();
            }
        });

        singOutBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
            }
        });

        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent intent = new Intent(SecondActivity.this, ContactsForm.class);
                startActivity(intent);
            }
        });
    }

    private void sendSmsToAllContacts() {
        ArrayList<Contact> contacts = (ArrayList<Contact>) contactDB2.contactDao().getAllContacts();

        for (Contact contact : contacts) {
            String phoneNumber = contact.getPhoneNumber();
            String GoogleMapsLink = "https://www.google.com/maps?q=" + lat + "," + lon;
            String message = "Hello, "+ contact.getName()+" I have a problem with my car. Here is my location " +GoogleMapsLink;

            // Send SMS
            try {
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(phoneNumber, null, message, null, null);
                Toast.makeText(this, "SMS sent to " + contact.getName(), Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(this, "Failed to send SMS to " + contact.getName(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }

        if (contacts.size() > 0) {
            // Create the notification builder
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                    .setContentTitle("Message Sent")
                    .setContentText("All messages have been sent successfully!")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);

            // Generate a unique notification ID
            int notificationId = new Random().nextInt();

            // Show the notification
            NotificationManager notificationManager = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                notificationManager = getSystemService(NotificationManager.class);
            }
            notificationManager.notify(notificationId, builder.build());
        }

    }

    private void getLastLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                Geocoder geocoder = new Geocoder(SecondActivity.this, Locale.getDefault());
                                List<Address> addresses = null;

                                try {
                                    addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                    lat = String.valueOf(addresses.get(0).getLatitude());
                                    lon = String.valueOf(addresses.get(0).getLongitude());
                                    add = String.valueOf(addresses.get(0).getAddressLine(0));
                                    city = String.valueOf(addresses.get(0).getLocality());
                                    country = String.valueOf(addresses.get(0).getCountryName());
                                    txtTst.setText(lat + " " + lon);
                                    if (ContextCompat.checkSelfPermission(SecondActivity.this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
                                        sendSmsToAllContacts();
                                    } else {
                                        requestSmsPermission();
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
        } else {
            requestLocationPermission();
        }
    }

    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(SecondActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_LOCATION);
    }

    private void requestSmsPermission() {
        ActivityCompat.requestPermissions(SecondActivity.this, new String[]{Manifest.permission.SEND_SMS}, REQUEST_CODE_SMS);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            } else {
                Toast.makeText(SecondActivity.this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == REQUEST_CODE_SMS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                sendSmsToAllContacts();
            } else {
                Toast.makeText(SecondActivity.this, "SMS permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    void signOut() {
        gsc.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(Task<Void> task) {
                finish();
                startActivity(new Intent(SecondActivity.this, MainActivity.class));
            }
        });
    }
}
