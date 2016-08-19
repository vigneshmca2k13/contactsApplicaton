package first.project.com.firstproject;

import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class AddContactAcitvity extends AppCompatActivity {
    EditText prefix,firstname,middlename,lastname,suffix,phone,email;
    Spinner phonetype,emailtype;
    String phonetypes[],emailtypes[];
    DataBaseHandler dbhelper;
    Toolbar toolbar;
    Context mContext;
    String phonetpeselected,mailtypeselected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact_acitvity);
        initView();
        mContext = this;
        dbhelper =  new DataBaseHandler(this);
        toolbar.setTitle("Add Contact");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        phonetypes = getResources().getStringArray(R.array.Phone_withoutselect);
        emailtypes = getResources().getStringArray(R.array.Mailwithoutselect);

        phonetype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                phonetpeselected = phonetypes[i];
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
       emailtype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
           @Override
           public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mailtypeselected = emailtypes[i];
           }

           @Override
           public void onNothingSelected(AdapterView<?> adapterView) {

           }
       });

    }
    void initView(){
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        prefix = (EditText)findViewById(R.id.prefix);
        firstname = (EditText)findViewById(R.id.firstname);
        middlename = (EditText)findViewById(R.id.middlename);
        lastname = (EditText)findViewById(R.id.lastname);
        suffix = (EditText)findViewById(R.id.suffix);
        phone = (EditText)findViewById(R.id.phone);
        email = (EditText)findViewById(R.id.email);
        phonetype = (Spinner)findViewById(R.id.phonetype);
        emailtype = (Spinner)findViewById(R.id.emailtype);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.addcontact,menu);

        MenuItem save = menu.findItem(R.id.save);
        save.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                if(firstname.getText().toString().length() > 0  || prefix.getText().toString().length() > 0 ||middlename.getText().toString().length() > 0 ||lastname.getText().toString().length() > 0 || suffix.getText().toString().length() > 0)
                    addContacttoDatabase();
                else
                    Toast.makeText(mContext,"Name fields required!",Toast.LENGTH_LONG).show();

                return true;
            }
        });



        return true;
    }

    Boolean addContacttoDatabase() {
        Contact contact = new Contact();
        String temp;
        ContentValues contentValues = new ContentValues();
        temp = firstname.getText().toString();
        contact.setFirstname(temp.length() == 0 ? "" : temp);
        temp = middlename.getText().toString();
        contact.setMiddlename(temp.length() == 0 ? "" : temp);
        temp = lastname.getText().toString();
        contact.setLastname(temp.length() == 0 ? "" : temp);
        temp = prefix.getText().toString();
        contact.setPrefix(temp.length() == 0 ? "" : temp);
        temp = suffix.getText().toString();
        contact.setSuffix(temp.length() == 0 ? "" : temp);
        contact.setName(contact.getPrefix() + "" + contact.firstname + " " + contact.getMiddlename() + " " + contact.getLastname() + " " + contact.getSuffix());
        String id = null;
        id = dbhelper.addContact(contact);

        System.out.println("  inserted   " + id);

        if (phone.getText().toString().length() > 0) {

            Contactinfo contactinfo = new Contactinfo();
            contactinfo.setContact(phone.getText().toString());
            contactinfo.setContenttype(phonetpeselected);
            contactinfo.setType(DataBaseHandler.ContactInfo.KEY_TYPE_PHONE);
            contactinfo.setContactid(id);
            dbhelper.addContactInfo(contactinfo);
        }

        if (email.getText().toString().length() > 0) {

            Contactinfo contactinfo = new Contactinfo();
            contactinfo.setContact(phone.getText().toString());
            contactinfo.setContenttype(phonetpeselected);
            contactinfo.setType(DataBaseHandler.ContactInfo.KEY_TYPE_PHONE);
            contactinfo.setContactid(id);

            if (dbhelper.addContactInfo(contactinfo))
                contentValues.put(DataBaseHandler.KEY_HAVE_MAIL, 1);

            dbhelper.updateContact(contentValues, id);
        }


        if (id != null || id.equals("-1")) {
            Toast.makeText(getApplicationContext(), "Contact added Successfully", Toast.LENGTH_LONG).show();
            finish();
        }


        return true;

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

