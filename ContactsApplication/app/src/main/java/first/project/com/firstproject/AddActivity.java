package first.project.com.firstproject;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class AddActivity extends AppCompatActivity {
    Spinner type, subtype;
    ContentValues contentValues = new ContentValues();
    ArrayAdapter<String> adapter;
    DataBaseHandler dbhelper;
    String types[], subtypes;
    EditText data;
    Toolbar toolbar;
    Button addContactinfo;
    Contactinfo contactinfo;
    String contactid;
    ArrayList<String> list = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Add Contact");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        contactinfo = new Contactinfo();
        if (getIntent().hasExtra("id")) contactid = getIntent().getStringExtra("id");
        dbhelper = new DataBaseHandler(this);
        types = getResources().getStringArray(R.array.type);
        type = (Spinner) findViewById(R.id.type);
        subtype = (Spinner) findViewById(R.id.subtype);
        data = (EditText) findViewById(R.id.data);
        addContactinfo = (Button) findViewById(R.id.add);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, list);
        subtype.setAdapter(adapter);
        type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 0) {
                    Toast.makeText(getApplicationContext(), types[i], Toast.LENGTH_LONG).show();
                    adapter.clear();
                    String array[];
                    switch (i) {

                        case 1:
                            array = getResources().getStringArray(R.array.Phone);
                            adapter.addAll(Arrays.asList(array));
                            contactinfo.setType(DataBaseHandler.ContactInfo.KEY_TYPE_PHONE);
                            break;
                        case 2:
                            array = getResources().getStringArray(R.array.Mail);
                            adapter.addAll(Arrays.asList(array));
                            contactinfo.setType(DataBaseHandler.ContactInfo.KEY_TYPE_MAIL);
                            break;
                        case 3:
                            array = getResources().getStringArray(R.array.Note);
                            adapter.addAll(Arrays.asList(array));
                            contactinfo.setType(DataBaseHandler.ContactInfo.KEY_TYPE_NOTE);
                            break;
                        case 4:
                            array = getResources().getStringArray(R.array.Address);
                            adapter.addAll(Arrays.asList(array));
                            contactinfo.setType(DataBaseHandler.ContactInfo.KEY_TYPE_Address);
                            break;
                        case 5:
                            array = getResources().getStringArray(R.array.NickName);
                            adapter.addAll(Arrays.asList(array));
                            contactinfo.setType(DataBaseHandler.ContactInfo.KEY_TYPE_Nickname);
                            break;
                        case 6:
                            array = getResources().getStringArray(R.array.Events);

                            adapter.addAll(Arrays.asList(array));
                            contactinfo.setType(DataBaseHandler.ContactInfo.KEY_TYPE_EVENTS);
                            break;
                        case 7:
                            array = getResources().getStringArray(R.array.Website);

                            adapter.addAll(Arrays.asList(array));
                            contactinfo.setType(DataBaseHandler.ContactInfo.KEY_TYPE_WEBSITE);
                            break;
                    }

                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        subtype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 0) {
                    Toast.makeText(getApplicationContext(), adapter.getItem(i), Toast.LENGTH_LONG).show();
                    contactinfo.setContenttype(adapter.getItem(i));
                    contentValues.clear();
                    switch (Integer.parseInt(contactinfo.getType())) {

                        case 2:
                            contentValues.put(DataBaseHandler.KEY_HAVE_MAIL, 1);
                            break;
                        case 3:
                            contentValues.put(DataBaseHandler.KEY_HASNOTE, 1);


                            break;
                        case 4:
                            contentValues.put(DataBaseHandler.KEY_HASADDRESS, 1);

                            break;
                        case 5:
                            contentValues.put(DataBaseHandler.KEY_HASNICKNAME, 1);
                            break;
                        case 6:
                            contentValues.put(DataBaseHandler.KEY_HASEVENTS, 1);
                            break;
                        case 7:
                            contentValues.put(DataBaseHandler.KEY_HASWEBSITE, 1);
                            break;
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        addContactinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (data.getText().toString().length() > 0 && contactinfo.getType() != null && contactinfo.getContenttype() != null) {
                    contactinfo.setContact(data.getText().toString());
                    contactinfo.setContactid(contactid);
                    System.out.println(" contactinfo inserting   " + contactinfo.toString());
                    if (dbhelper.addContactInfo(contactinfo)) {
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("id", contactid);
                        setResult(Activity.RESULT_OK, returnIntent);
                        if (!(contactinfo.getType().equals(DataBaseHandler.ContactInfo.KEY_TYPE_PHONE))) {
                            if (dbhelper.updateContact(contentValues, contactid)) {
                                returnIntent.putExtra("id", contactid);
                                Toast.makeText(getApplicationContext(), "Added Successfully", Toast.LENGTH_LONG).show();
                                finish();
                            }
                        } else {
                            System.out.println("coming to lese");
                            returnIntent.putExtra("id", contactid);
                            setResult(Activity.RESULT_OK, returnIntent);
                            finish();
                        }
                    } else {
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("id", contactid);
                        setResult(Activity.RESULT_CANCELED, returnIntent);
                        Toast.makeText(getApplicationContext(), "Try after Sometime", Toast.LENGTH_LONG).show();
                        finish();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "All fields Required", Toast.LENGTH_LONG).show();

                }
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
