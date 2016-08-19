package first.project.com.firstproject;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class DetailViewActivity extends AppCompatActivity implements View.OnLongClickListener{
    private Uri mImageCaptureUri;
    private ImageView mImageView;

    private static final int PICK_FROM_CAMERA = 4;
    private static final int PICK_FROM_FILE = 3;
    TextView number;
    ImageView banner;
    Contact object;
    Context mContext;
    FloatingActionButton add,loc;
    CircleImageView pick;
    FloatingActionButton change;
    DataBaseHandler dbhelper;
    ViewGroup insertPoint;
    CollapsingToolbarLayout collapsingToolbarLayout;
    Toolbar toolbar;
    AppBarLayout appBarLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_view);



        mContext = this;
        dbhelper = new DataBaseHandler(mContext);
        InitView();


        change = (FloatingActionButton)findViewById(R.id.change);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

                System.out.println("vertical offset  "  + verticalOffset);
                if (verticalOffset <= -collapsingToolbarLayout.getHeight() + toolbar.getHeight()) {
                    pick.setVisibility(View.VISIBLE);
                    System.out.println("if part");
                }

                else
                {
                    pick.setVisibility(View.INVISIBLE);

                }

            }

        });

        if(getIntent().hasExtra("object")) {
            String received =  getIntent().getStringExtra("object");
            object = dbhelper.getContactbyId(received);
            getContactDetails(object);
        }
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent  = new Intent(DetailViewActivity.this,AddActivity.class);
                intent.putExtra("id",object.getId());
                startActivityForResult(intent,1);
            }
        });



        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String [] items           = new String [] {"From Camera", "From SD Card"};
                ArrayAdapter<String> adapter  = new ArrayAdapter<String> (mContext, android.R.layout.select_dialog_item,items);
                AlertDialog.Builder builder     = new AlertDialog.Builder(mContext);
                builder.setAdapter( adapter, new DialogInterface.OnClickListener() {
                    public void onClick( DialogInterface dialog, int item ) {
                        if (item == 0) {
                            Intent intent    = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),"ContactsPictures");
                            File imagefile        = new File(file.getPath() + File.pathSeparator +
                                    "contact" + object.getId()+ ".png");
                            mImageCaptureUri = Uri.fromFile(imagefile);
                            try {
                                intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
                                intent.putExtra("return-data", true);
                                startActivityForResult(intent, PICK_FROM_CAMERA);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            dialog.cancel();
                        } else {
                            Intent intent = new Intent();
                            intent.setType("image/*");
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            startActivityForResult(Intent.createChooser(intent, "Complete action using"), PICK_FROM_FILE);
                        }
                    }
                } );
                final AlertDialog dialog = builder.create();

                dialog.show();


            }
        });

    }
   public void InitView() {

       toolbar = (Toolbar)findViewById(R.id.toolbar);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
       appBarLayout = (AppBarLayout)findViewById(R.id.appbar);
       banner = (ImageView)findViewById(R.id.banner);
       add = (FloatingActionButton)findViewById(R.id.add);
       pick = (CircleImageView) findViewById(R.id.pick);


    }

        void getContactDetails(final Contact contact){

            System.out.println("starting asyncatask");

        new AsyncTask<String, Void, ContactDetails>() {
                @Override
                protected ContactDetails doInBackground(String... params) {
                ContactDetails Details = dbhelper.getContactInfo(contact);
                System.out.println(" details got   " + Details.toString());

                    return Details;
                }

                @Override
                protected void onPostExecute(ContactDetails result) {

                    setData(result);


                }

                @Override
                protected void onPreExecute() {}

                @Override
                protected void onProgressUpdate(Void... values) {}
            }.execute();
    }

    private void setData(ContactDetails datareceived){
        try {

            System.out.println("setdata called"  + datareceived.toString());

            collapsingToolbarLayout.setTitle(datareceived.getContact().getName());
            if (datareceived.getContact().getImageuri() != null) {

                try {
                    Bitmap bitmap = MediaStore.Images.Media
                            .getBitmap(getContentResolver(),
                                    Uri.parse(datareceived.getContact().getImageuri()));
                    banner.setImageBitmap(bitmap);
                    pick.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            LayoutInflater vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            insertPoint = (ViewGroup) findViewById(R.id.linearlayout);
            insertPoint.removeAllViews();
            if (datareceived.getContact().getHaswebsite() == 1) {
                final ArrayList<Data> websites = datareceived.getWebsites();
                View websitesarray[] = new View[websites.size()];
                for (int i = 0; i < websites.size(); i++) {
                    websitesarray[i] = vi.inflate(R.layout.contactitem_common, null);
                    websitesarray[i].setOnLongClickListener(this);
                    websitesarray[i].setTag(websites.get(i));
                    TextView textViewtype = (TextView) websitesarray[i].findViewById(R.id.type);
                    textViewtype.setText(websites.get(i).getType());
                    TextView textView = (TextView) websitesarray[i].findViewById(R.id.contact);
                    textView.setText(websites.get(i).getData());
                    ImageView mail = (ImageView) websitesarray[i].findViewById(R.id.mail);
                    insertPoint.addView(websitesarray[i], i, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                }
            }
            if(datareceived.getContact().getHasorganziation() == 1){
                View view  =    vi.inflate(R.layout.contactitem_common, null);
                TextView textViewtype = (TextView) view.findViewById(R.id.type);
                textViewtype.setText(datareceived.getContact().getCompany());
                TextView textView = (TextView) view.findViewById(R.id.contact);
                textView.setText(datareceived.getContact().getTitle());
                ImageView mail = (ImageView) view.findViewById(R.id.mail);
                insertPoint.addView(view, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            }


            if(datareceived.getContact().getHasphoneticname() == 1){
                 View view  =    vi.inflate(R.layout.contactitem_common, null);
                TextView textViewtype = (TextView) view.findViewById(R.id.type);
                textViewtype.setText("PhoneticName");
                TextView textView = (TextView) view.findViewById(R.id.contact);
                textView.setText(datareceived.getContact().getPhoneticfamilyname() +  " " + datareceived.getContact().getPhoneticmiddlename() + " " + datareceived.getContact().getPhoneticgivenname() );
                ImageView mail = (ImageView) view.findViewById(R.id.mail);
                insertPoint.addView(view, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            }

            if (datareceived.getContact().getHasevents() == 1) {
                final ArrayList<Data> events = datareceived.getEvents();
                View eventsarray[] = new View[events.size()];

                for (int i = 0; i < events.size(); i++) {
                    eventsarray[i] = vi.inflate(R.layout.contactitem_common, null);
                    eventsarray[i].setTag(events.get(i));
                    eventsarray[i].setOnLongClickListener(this);
                    TextView textViewtype = (TextView) eventsarray[i].findViewById(R.id.type);
                    textViewtype.setText(events.get(i).getType());
                    TextView textView = (TextView) eventsarray[i].findViewById(R.id.contact);
                    textView.setText(events.get(i).getData());
                    ImageView mail = (ImageView) eventsarray[i].findViewById(R.id.mail);
                    insertPoint.addView(eventsarray[i], i, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                }

            }
            if (datareceived.getContact().getHasnickname() == 1) {
                final ArrayList<Data> nickname = datareceived.getNicknames();
                View nicknamearray[] = new View[nickname.size()];

                for (int i = 0; i < nickname.size(); i++) {
                    nicknamearray[i] = vi.inflate(R.layout.contactitem_common, null);
                    nicknamearray[i].setTag(nickname.get(i));
                    nicknamearray[i].setOnLongClickListener(this);
                    TextView textViewtype = (TextView) nicknamearray[i].findViewById(R.id.type);
                    textViewtype.setText(nickname.get(i).getType());
                    TextView textView = (TextView) nicknamearray[i].findViewById(R.id.contact);
                    textView.setText(nickname.get(i).getData());
                    ImageView mail = (ImageView) nicknamearray[i].findViewById(R.id.mail);
                    insertPoint.addView(nicknamearray[i], i, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                }
            }

            if (datareceived.getContact().getHasaddress() == 1) {
                final ArrayList<Data> address = datareceived.getAddress();
                View addressarray[] = new View[address.size()];

                for (int i = 0; i < address.size(); i++) {
                    addressarray[i] = vi.inflate(R.layout.contactitem_common, null);
                    addressarray[i].setTag(address.get(i));
                    addressarray[i].setOnLongClickListener(this);
                    TextView textViewtype = (TextView) addressarray[i].findViewById(R.id.type);
                    textViewtype.setText(address.get(i).getType() + " Address");
                    TextView textView = (TextView) addressarray[i].findViewById(R.id.contact);
                    textView.setText(address.get(i).getData());
                    ImageView mail = (ImageView) addressarray[i].findViewById(R.id.mail);
                    insertPoint.addView(addressarray[i], i, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                }
            }
            if (datareceived.getContact().getHasnote() == 1) {
                final ArrayList<Data> notes = datareceived.getNotes();
                View notearray[] = new View[notes.size()];

                for (int i = 0; i < notes.size(); i++) {
                    notearray[i] = vi.inflate(R.layout.contactitem_common, null);
                    notearray[i].setTag(notes.get(i));
                    notearray[i].setOnLongClickListener(this);
                    TextView textViewtype = (TextView) notearray[i].findViewById(R.id.type);
                    textViewtype.setText(notes.get(i).getType());
                    TextView textView = (TextView) notearray[i].findViewById(R.id.contact);
                    textView.setText(notes.get(i).getData());
                    ImageView mail = (ImageView) notearray[i].findViewById(R.id.mail);
                    insertPoint.addView(notearray[i], i, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                }

            }
            if (datareceived.getContact().getHasmail().equals("1")) {

                System.out.println("email inside");
                final ArrayList<Data> mailids = datareceived.getMailids();
                View mailarray[] = new View[mailids.size()];
                for (int i = 0; i < mailids.size(); i++) {
                    mailarray[i] = vi.inflate(R.layout.contactitem_mail, null);

                    final Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", mailids.get(i).getData(), null));
                    mailarray[i].setTag(mailids.get(i));
                    mailarray[i].setOnLongClickListener(this);
                    TextView textViewtype = (TextView) mailarray[i].findViewById(R.id.type);
                    textViewtype.setText(mailids.get(i).getType());

                    TextView textView = (TextView) mailarray[i].findViewById(R.id.contact);
                    textView.setText(mailids.get(i).getData());
                    ImageView mail = (ImageView) mailarray[i].findViewById(R.id.mail);
                    mail.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
                            emailIntent.putExtra(Intent.EXTRA_TEXT, "");
                            startActivity(Intent.createChooser(emailIntent, ""));
                        }
                    });
                    insertPoint.addView(mailarray[i], i, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                }
            }



            final ArrayList<Data> numbers = datareceived.getNumbers();
            View numbersarray[] = new View[numbers.size()];
            for (int i = 0; i < numbers.size(); i++) {
                numbersarray[i] = vi.inflate(R.layout.contactitem_phone, null);
                final Intent intent = new Intent();
                numbersarray[i].setTag(numbers.get(i));
                intent.setData(Uri.parse("tel:" + numbers.get(i).getData()));
                numbersarray[i].setOnLongClickListener(this);
                final Intent msgintent = new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", numbers.get(i).getData(), null));
                TextView textViewtype = (TextView) numbersarray[i].findViewById(R.id.type);
                textViewtype.setText(numbers.get(i).getType());
                TextView textView = (TextView) numbersarray[i].findViewById(R.id.contact);
                textView.setText(numbers.get(i).getData());
                ImageView call = (ImageView) numbersarray[i].findViewById(R.id.call);
                ImageView message = (ImageView) numbersarray[i].findViewById(R.id.message);
                call.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(intent);
                    }
                });
                message.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // The number on which you want to send SMS
                        startActivity(msgintent);
                    }
                });
                insertPoint.addView(numbersarray[i], i, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            }


        }catch (Exception mException){
            mException.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bitmap   = null;
        String path     = "";

        switch (requestCode) {
            case 1: {
                if (resultCode == Activity.RESULT_OK) {
                    object =  dbhelper.getContactbyId(data.getStringExtra("id"));
                    System.out.println("  new contact inn start activity reslt" +dbhelper.getContactbyId(data.getStringExtra("id")));
                    getContactDetails(object);
                    Toast.makeText(mContext, "rsultok", Toast.LENGTH_LONG).show();
                }
                if (resultCode == Activity.RESULT_CANCELED) {

                    System.out.println( " back pressde  called ");

                }
                break;
            }
            case 2:
                if (requestCode == 2 && resultCode == RESULT_OK && data != null && data.getData() != null) {
                    object = dbhelper.getContactbyId(object.getId());
                    Uri uri = data.getData();
                    getContactDetails(object);
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(DataBaseHandler.KEY_PHOTO_URI,uri.toString());
                    dbhelper.updateContact(contentValues,object.getContactid());
                    Toast.makeText(mContext, "Profile pic updated", Toast.LENGTH_LONG).show();

                }
                if (resultCode == Activity.RESULT_CANCELED) {

                }
                break;


            case 3:
                if(resultCode == RESULT_OK) {
                    mImageCaptureUri = data.getData();
                    path = getRealPathFromURI(mImageCaptureUri); //from Gallery
                    if (path == null)
                        path = mImageCaptureUri.getPath(); //from File Manager
                    if (path != null)
                        bitmap = BitmapFactory.decodeFile(path);
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(DataBaseHandler.KEY_PHOTO_URI, mImageCaptureUri.toString());
                    if (dbhelper.updateContact(contentValues, object.getId())) {
                        Toast.makeText(mContext, "Updated successfully", Toast.LENGTH_SHORT).show();
                        banner.setImageBitmap(bitmap);
                        pick.setImageBitmap(bitmap);
                    } else {

                        Toast.makeText(mContext, "Try Again", Toast.LENGTH_SHORT).show();

                    }
                }
                break;

            case 4:
                if(resultCode == RESULT_OK) {
                    path = mImageCaptureUri.getPath();
                    bitmap = BitmapFactory.decodeFile(path);
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(DataBaseHandler.KEY_PHOTO_URI, mImageCaptureUri.toString());
                    if (dbhelper.updateContact(contentValues, object.getId())) {
                        Toast.makeText(mContext, "Updated successfully", Toast.LENGTH_SHORT).show();
                        banner.setImageBitmap(bitmap);
                        pick.setImageBitmap(bitmap);
                    } else {
                        Toast.makeText(mContext, "Try Again", Toast.LENGTH_SHORT).show();
                    }
                    banner.setImageBitmap(bitmap);
                }

                break;

        }

    }

    @Override
    public boolean onLongClick(final View view) {

        Toast.makeText(mContext,"long clicked",Toast.LENGTH_LONG).show();
        PopupMenu popup = new PopupMenu(mContext, view);
        //Inflating the Popup using xml file
        popup.getMenuInflater().inflate(R.menu.option, popup.getMenu());

        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.delete:
                        new AlertDialog.Builder(mContext)
                                .setTitle("Delete entry")
                                .setMessage("Are you sure you want to delete this entry?")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        try {
                                            Data data = (Data) view.getTag();
                                            if(dbhelper.deleteContactInfo(data.getId())) {
                                                insertPoint.removeView(view);
                                                Toast.makeText(mContext, "delted successfully", Toast.LENGTH_LONG).show();
                                            }
                                            else {
                                                Toast.makeText(mContext, "unable to delete", Toast.LENGTH_LONG).show();
                                            }
                                        }catch (Exception mException){
                                        }
                                    }
                                })
                                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // do nothing
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();

                        break;
                    case R.id.update:
                        try{
                            final Dialog dialog = new Dialog(mContext);
                            dialog.setContentView(R.layout.updatedialog);
                            dialog.setTitle("Update");
                            final Data data = (Data)view.getTag();
                            EditText type = (EditText) dialog.findViewById(R.id.type);
                            type.setText(data.getType());
                            final EditText contact = (EditText) dialog.findViewById(R.id.contact);
                            contact.setText(data.getData());
                            Button update = (Button)dialog.findViewById(R.id.update);
                            dialog.show();
                            update.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    ContentValues contentValues = new ContentValues();
                                    contentValues.put(DataBaseHandler.ContactInfo.KEY_CONTACT,contact.getText().toString());
                                    if(dbhelper.updateContactInfo(contentValues,data.getId())) {
                                        Toast.makeText(mContext,"Updated successfully",Toast.LENGTH_LONG).show();
                                        getContactDetails(object);
                                    }
                                    else
                                        Toast.makeText(mContext,"Try Again",Toast.LENGTH_LONG).show();
                                }
                            });
                        }catch (Exception mException){
                            mException.printStackTrace();
                        }


                        break;
                }

                return true;
            }
        });

        popup.show();//showing popup menu

        return false;

    }


    public String getRealPathFromURI(Uri contentUri) {
        String [] proj      = {MediaStore.Images.Media.DATA};
        Cursor cursor       = managedQuery( contentUri, proj, null, null,null);

        if (cursor == null) return null;

        int column_index    = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        cursor.moveToFirst();

        return cursor.getString(column_index);
    }


    @Override
    protected void onResume() {
        super.onResume();

    }


    @Override
    public void finish() {

        super.finish();
        overridePendingTransition(R.anim.left_in, R.anim.right_out);

    }
}
