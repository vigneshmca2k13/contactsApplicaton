package first.project.com.firstproject;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.StructuredPostal;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import first.project.com.firstproject.adapter.PhoneContactsAdapter;
import first.project.com.firstproject.listeners.Listener;


public class ContactsFragment extends Fragment {
    ArrayList<Contact> list = new ArrayList<>();
    ArrayList<Contact> selecteditems = new ArrayList<>();

    int selected = 0;
    Listener.OnItemClickListener itemClickListener;
    Listener.OnItemLongClickListener itemLongclicklistener;
    ArrayList<Contact> contactslist = new ArrayList<>();
    ArrayList<Contactinfo>  contactinfos = new ArrayList<>();
    ProgressDialog mProgressDialog;
    RecyclerView recyclerview;
    View rootView;
    Handler handler;
    SharedPreferences myPref;
    PhoneContactsAdapter adapter;
    SharedPreferences.Editor editor;
    DataBaseHandler dbhelper;
    Menu menu;
    FloatingActionButton addcontact;
    Context mContext;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.contacts_fragment, container, false);
        initView();
        mContext = getActivity();
        dbhelper = new DataBaseHandler(getActivity());
        myPref = getActivity().getSharedPreferences("mypref", Context.MODE_PRIVATE);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerview.setLayoutManager(layoutManager);
            
        Fastscroller fastScroller = (Fastscroller) rootView.findViewById(R.id.fast_scroller);
        fastScroller.setRecyclerView(recyclerview);

        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setAddDuration(1000);
        itemAnimator.setRemoveDuration(1000);

        recyclerview.setItemAnimator(itemAnimator);
        handler = new Handler() {

            public void handleMessage(Message msg) {

                String aResponse = msg.getData().getString("message");

                if ((null != aResponse)) {

                    // ALERT MESSAGE
                    Toast.makeText(
                            getActivity(),
                            "Server Response: "+aResponse,
                            Toast.LENGTH_SHORT).show();
                }
                else
                {

                    // ALERT MESSAGE
                    Toast.makeText(
                            getActivity(),
                            "Not Got Response From Server.",
                            Toast.LENGTH_SHORT).show();
                }

            }
        };

        getActivity().getContentResolver().registerContentObserver(ContactsContract.Contacts.CONTENT_URI, true,
                new MyObserver(handler));


        //initSwipe();
        if(!myPref.getBoolean("loaded",false)) {
            readContacts();
            editor = myPref.edit();
            editor.putBoolean("loaded",true);
            editor.commit();
        }
        else{
            readContactsFromDatabase();

        }
        addcontact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),AddContactAcitvity.class);
                startActivity(intent);
            }
        });
        return rootView;
    }
    void readContactsFromDatabase(){

        new AsyncTask<String, Void, ArrayList<Contact>>() {

            @Override
            protected  ArrayList<Contact> doInBackground(String... params) {

                ArrayList<Contact> contactlist = dbhelper.getAllContacts();

                return contactlist;
            }

            @Override
            protected void onPostExecute( ArrayList<Contact> result) {


                ArrayList<Contact> list = dbhelper.getAllContacts();
                adapter = new PhoneContactsAdapter(mContext,list);
                setListener();
                recyclerview.setAdapter(adapter);

            }

            @Override
            protected void onPreExecute() {}

            @Override
            protected void onProgressUpdate(Void... values) {}

        }.execute();
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onStart() {
        super.onStart();

        readContactsFromDatabase();

    }

    void initView() {
        recyclerview = (RecyclerView) rootView.findViewById(R.id.contactlist);
        addcontact = (FloatingActionButton)rootView.findViewById(R.id.addcontact);
    }
    void readContacts() {
        new AsyncTask<Void, String, ArrayList<Contact>>() {
            int total;
            HashMap<String,ContentValues> contentValuesList = new HashMap<String, ContentValues>();
            @Override
            protected ArrayList<Contact> doInBackground(Void... params) {
                long millis = System.currentTimeMillis() % 1000;

                int completed = 1;
                ContentResolver cr = getActivity().getContentResolver();
                Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                        new String[]{ContactsContract.Contacts.PHOTO_URI, ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.Contacts._ID}, null, null, null);

                total = cur.getCount();
                try {
                    if (cur.moveToFirst()) {

                        while (!(cur.isLast())) {
                            ContentValues contentValues = new ContentValues();
                            Contact eachcontact = new Contact();
                            String phorouri = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.PHOTO_URI));
                            String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                            eachcontact.setName(name);
                            eachcontact.setContactid((cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID))));
                            eachcontact.setImageuri(phorouri);
                            String contactid, id = null;

                            contactid = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));

                            Cursor Datacursor = getActivity().getContentResolver().query(ContactsContract.Data.CONTENT_URI, null, ContactsContract.Data.CONTACT_ID + " = ?", new String[]{contactid}, null);
                            id = dbhelper.addContact(eachcontact);

                            if ( Datacursor.moveToFirst()) {
                                while (!(Datacursor.isAfterLast())) {
                                    //structured name
                                    if (Datacursor.getString(Datacursor.getColumnIndex(ContactsContract.Data.MIMETYPE)).equals(ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)) {

                                        String firstname = Datacursor.getString(Datacursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME));
                                        String middlename = Datacursor.getString(Datacursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.MIDDLE_NAME));
                                        String lastname = Datacursor.getString(Datacursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME));
                                        String prefix = Datacursor.getString(Datacursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.PREFIX));
                                        String suffix = Datacursor.getString(Datacursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.SUFFIX));
                                        String phoneticfamilyname = Datacursor.getString(Datacursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.PHONETIC_FAMILY_NAME));
                                        String phoneticmiddlename = Datacursor.getString(Datacursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.PHONETIC_MIDDLE_NAME));
                                        String phoneticgivenname = Datacursor.getString(Datacursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.PHONETIC_GIVEN_NAME));
                                        eachcontact.setFirstname(firstname == null ? "" : firstname);
                                        eachcontact.setMiddlename(middlename == null ? "" : middlename);
                                        eachcontact.setLastname(lastname == null ? "" : lastname);
                                        eachcontact.setPrefix(prefix == null ? "" : prefix);
                                        eachcontact.setSuffix(suffix == null ? "" : suffix);
                                        eachcontact.setPhoneticfamilyname(phoneticfamilyname == null ? "" : phoneticfamilyname);
                                        eachcontact.setPhoneticgivenname(phoneticgivenname == null ? "" : phoneticgivenname);
                                        eachcontact.setPhoneticmiddlename(phoneticmiddlename == null ? "" : phoneticmiddlename);
                                        contentValues.put(DataBaseHandler.KEY_FIRSTNAME, eachcontact.getFirstname());
                                        contentValues.put(DataBaseHandler.KEY_MIDDLENAME, eachcontact.getMiddlename());
                                        contentValues.put(DataBaseHandler.KEY_LASTNAME, eachcontact.getLastname());
                                        contentValues.put(DataBaseHandler.KEY_PREFIX, eachcontact.getPrefix());
                                        contentValues.put(DataBaseHandler.KEY_SUFFIX, eachcontact.getSuffix());
                                        contentValues.put(DataBaseHandler.KEY_PHONETIC_FAMILYNAME, eachcontact.getPhoneticfamilyname());
                                        contentValues.put(DataBaseHandler.KEY_PHONETIC_GIVENNAME, eachcontact.getPhoneticgivenname());
                                        contentValues.put(DataBaseHandler.KEY_PHONETIC_MIDDLENAME, eachcontact.getPhoneticmiddlename());


                                        if (!(eachcontact.getPhoneticfamilyname().equals("")) || !(eachcontact.getPhoneticgivenname().equals("")) || !(eachcontact.getPhoneticmiddlename().equals(""))) {
                                            contentValues.put(DataBaseHandler.KEY_HASPHONETICNAME, 1);
                                        }
                                    }
                                    //Organzation

                                    if (Datacursor.getString(Datacursor.getColumnIndex(ContactsContract.Data.MIMETYPE)).equals(ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE)) {

                                        String title = Datacursor.getString(Datacursor.getColumnIndex(ContactsContract.CommonDataKinds.Organization.TITLE));
                                        String Company = Datacursor.getString(Datacursor.getColumnIndex(ContactsContract.CommonDataKinds.Organization.COMPANY));
                                        eachcontact.setTitle(title == null ? "" : title);
                                        contentValues.put(DataBaseHandler.KEY_TITLE, eachcontact.getTitle());

                                        eachcontact.setCompany(Company == null ? "" : Company);
                                        contentValues.put(DataBaseHandler.KEY_COMPANY, eachcontact.getCompany());

                                        if (!(eachcontact.getTitle().equals("")) || !(eachcontact.getCompany().equals("")))
                                            contentValues.put(DataBaseHandler.KEY_HASORGANIZATION, 1);

                                    }
                                    //phone

                                    if (Datacursor.getString(Datacursor.getColumnIndex(ContactsContract.Data.MIMETYPE)).equals(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)) {
                                        String phone = Datacursor.getString(Datacursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                        Contactinfo contactinfo = new Contactinfo(id, DataBaseHandler.ContactInfo.KEY_TYPE_PHONE, phone, Utilities.PHONE.get(Datacursor.getInt(Datacursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE))));
                                        contactinfos.add(contactinfo);
                                        System.out.println(contactinfo.toString());


                                    }
                                    //email

                                    if (Datacursor.getString(Datacursor.getColumnIndex(ContactsContract.Data.MIMETYPE)).equals(ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)) {
                                        String email = Datacursor.getString(
                                                Datacursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                                        contentValues.put(DataBaseHandler.KEY_HAVE_MAIL, "1");
                                        Contactinfo contactinfo = new Contactinfo(id, DataBaseHandler.ContactInfo.KEY_TYPE_MAIL, email, Utilities.EMAIL.get(Datacursor.getInt(Datacursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE))));
                                        contactinfos.add(contactinfo);
                                    }


                                    //address
                                    if (Datacursor.getString(Datacursor.getColumnIndex(ContactsContract.Data.MIMETYPE)).equals(ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE)) {
                                        contentValues.put(DataBaseHandler.KEY_HASADDRESS, 1);

                                        StringBuilder string = new StringBuilder();
                                        if (Datacursor.getString(Datacursor.getColumnIndex(StructuredPostal.STREET)) != null)
                                            string.append(", " + Datacursor.getString(Datacursor.getColumnIndex(StructuredPostal.STREET)));
                                        if (Datacursor.getString(Datacursor.getColumnIndex(StructuredPostal.CITY)) != null)
                                            string.append(", " + Datacursor.getString(Datacursor.getColumnIndex(StructuredPostal.CITY)));
                                        if (Datacursor.getString(Datacursor.getColumnIndex(StructuredPostal.POSTCODE)) != null)
                                            string.append(", " + Datacursor.getString(Datacursor.getColumnIndex(StructuredPostal.POSTCODE)));
                                        if (Datacursor.getString(Datacursor.getColumnIndex(StructuredPostal.COUNTRY)) != null)
                                            string.append(", " + Datacursor.getString(Datacursor.getColumnIndex(StructuredPostal.COUNTRY)));
                                        if (Datacursor.getString(Datacursor.getColumnIndex(StructuredPostal.REGION)) != null)
                                            string.append(", " + Datacursor.getString(Datacursor.getColumnIndex(StructuredPostal.REGION)));

                                        Contactinfo contactinfo = new Contactinfo(id, DataBaseHandler.ContactInfo.KEY_TYPE_Address, string.toString(), Utilities.ADDRESS.get(Datacursor.getInt(Datacursor.getColumnIndex(StructuredPostal.TYPE))));
                                        contactinfos.add(contactinfo);

                                    }
                                    //nickname
                                    if (Datacursor.getString(Datacursor.getColumnIndex(ContactsContract.Data.MIMETYPE)).equals(ContactsContract.CommonDataKinds.Nickname.CONTENT_ITEM_TYPE)) {
                                        contentValues.put(DataBaseHandler.KEY_HASNICKNAME, 1);

                                        String nicknameName = Datacursor.getString(Datacursor.getColumnIndex(ContactsContract.CommonDataKinds.Nickname.NAME));
                                        Contactinfo contactinfo = new Contactinfo(id, DataBaseHandler.ContactInfo.KEY_TYPE_Nickname, nicknameName, "NickName");
                                        contactinfos.add(contactinfo);


                                    }


                                    //Notes

                                    if (Datacursor.getString(Datacursor.getColumnIndex(ContactsContract.Data.MIMETYPE)).equals(ContactsContract.CommonDataKinds.Note.CONTENT_ITEM_TYPE)) {
                                        contentValues.put(DataBaseHandler.KEY_HASNOTE, 1);
                                        String note = Datacursor.getString(Datacursor.getColumnIndex(ContactsContract.CommonDataKinds.Note.NOTE));

                                        Contactinfo contactinfo = new Contactinfo(id, DataBaseHandler.ContactInfo.KEY_TYPE_NOTE, note, "Notes");
                                        contactinfos.add(contactinfo);

                                    }


                                    //Events

                                    if (Datacursor.getString(Datacursor.getColumnIndex(ContactsContract.Data.MIMETYPE)).equals(ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE)) {
                                        contentValues.put(DataBaseHandler.KEY_HASEVENTS, 1);
                                        String startdate = Datacursor.getString(Datacursor.getColumnIndex(ContactsContract.CommonDataKinds.Event.START_DATE));
                                        String type = new String();
                                        switch (Datacursor.getInt(Datacursor.getColumnIndex(ContactsContract.CommonDataKinds.Event.TYPE))) {
                                            case ContactsContract.CommonDataKinds.Event.TYPE_ANNIVERSARY:
                                                type = "Anniversary";
                                                break;
                                            case ContactsContract.CommonDataKinds.Event.TYPE_BIRTHDAY:
                                                type = "Birthday";
                                                break;
                                            case ContactsContract.CommonDataKinds.Event.TYPE_OTHER:
                                                type = "Other";
                                                break;
                                            case ContactsContract.CommonDataKinds.Event.TYPE_CUSTOM:
                                                type = Datacursor.getString(Datacursor.getColumnIndex(ContactsContract.CommonDataKinds.Event.LABEL));
                                                break;
                                        }
                                        Contactinfo contactinfo = new Contactinfo(id, DataBaseHandler.ContactInfo.KEY_TYPE_EVENTS, type, startdate);
                                        contactinfos.add(contactinfo);
                                    }


                                    //Website
                                    if (Datacursor.getString(Datacursor.getColumnIndex(ContactsContract.Data.MIMETYPE)).equals(ContactsContract.CommonDataKinds.Website.CONTENT_ITEM_TYPE)) {
                                        contentValues.put(DataBaseHandler.KEY_HASWEBSITE, 1);
                                        String url = Datacursor.getString(Datacursor.getColumnIndex(ContactsContract.CommonDataKinds.Website.URL));
                                        String type = new String();
                                        switch (Datacursor.getInt(Datacursor.getColumnIndex(ContactsContract.CommonDataKinds.Website.TYPE))) {
                                            case ContactsContract.CommonDataKinds.Website.TYPE_BLOG:
                                                type = "Blog";
                                                break;
                                            case ContactsContract.CommonDataKinds.Website.TYPE_FTP:
                                                type = "FTP";
                                                break;
                                            case ContactsContract.CommonDataKinds.Website.TYPE_HOME:
                                                type = "Home";
                                                break;
                                            case ContactsContract.CommonDataKinds.Website.TYPE_CUSTOM:
                                                type = Datacursor.getString(Datacursor.getColumnIndex(ContactsContract.CommonDataKinds.Website.LABEL));
                                                break;
                                            case ContactsContract.CommonDataKinds.Website.TYPE_HOMEPAGE:
                                                type = "Homepage";
                                                break;
                                            case ContactsContract.CommonDataKinds.Website.TYPE_OTHER:
                                                type = "Other";
                                                break;
                                            case ContactsContract.CommonDataKinds.Website.TYPE_PROFILE:
                                                type = "Profile";
                                                break;
                                            case ContactsContract.CommonDataKinds.Website.TYPE_WORK:
                                                type = "Work";
                                                break;

                                        }
                                        Contactinfo contactinfo = new Contactinfo(id, DataBaseHandler.ContactInfo.KEY_TYPE_WEBSITE, url, "Website");
                                        contactinfos.add(contactinfo);

                                    }

                                    if(contentValues.size() > 0) {
                                        System.out.println("printing contetn values  " + contentValues.toString());

                                        contentValuesList.put(id, contentValues);
                                    }

                                    Datacursor.moveToNext();
                                }

                            }
                            publishProgress(++completed + " ");
                            cur.moveToNext();
                        }

                        dbhelper.addContactInfolist(contactinfos);
                        dbhelper.updateContacts(contentValuesList);
                    }
                }catch (Exception mException){
                    mException.printStackTrace();
                }

                return dbhelper.getAllContacts();

            }


            protected void onPostExecute(ArrayList<Contact> arraylist) {
                ArrayList<Contact> list = arraylist;
                adapter = new PhoneContactsAdapter(mContext,list);
                setListener();
                recyclerview.setAdapter(adapter);
                mProgressDialog.cancel();
            }




            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                mProgressDialog = new ProgressDialog(getActivity());
                mProgressDialog.setTitle("Processing");
                mProgressDialog.setIndeterminate(false);
                mProgressDialog.setMax(100);
                mProgressDialog.setMessage("Started Copying");
                mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                mProgressDialog.setCancelable(false);
                mProgressDialog.setCanceledOnTouchOutside(false);
                mProgressDialog.show();
            }
            @Override
            protected void onProgressUpdate(String... values) {
                mProgressDialog.setMessage(" " + values[0] + " Contacts copied successfully.");
            }
        }.execute();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menus,menu);
        this.menu = menu;
        final MenuItem itemOk = menu.findItem(R.id.action_delete);
        final MenuItem itemcancel = menu.findItem(R.id.action_cancel);
        itemOk.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                new AlertDialog.Builder(mContext)

                        .setTitle("Delete entry")
                        .setMessage("Are you sure you want to delete this entry?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    int i=0;
                                    ArrayList<Integer> deleteitems = new ArrayList<Integer>();

                                    for(Contact contact : selecteditems){
                                        if(dbhelper.deleteContactByContactId(contact.getId())){
                                                adapter.deleteItem(contact);
                                        }
                                        else{
                                            ++i;
                                            Toast.makeText(mContext,"Unable to delete "+ i +" items",Toast.LENGTH_LONG ).show();
                                        }
                                    }
                                    selecteditems.clear();
                                    Toast.makeText(mContext,"Contacts deleted successfully",Toast.LENGTH_LONG ).show();
                                }catch (Exception mException){
                                    mException.printStackTrace();
                                }
                                itemcancel.setVisible(false);
                                itemOk.setVisible(false);
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                unmarkSelected();
                                selecteditems.clear();

                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

                return true;
            }
        });
        itemcancel.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                unmarkSelected();
                itemcancel.setVisible(false);
                itemOk.setVisible(false);
                return true;
            }
        });

    }

    void toggledeleteButton(Boolean state){
        MenuItem deletebtn =(MenuItem)menu.findItem(R.id.action_delete);
        MenuItem cancelbtn =(MenuItem)menu.findItem(R.id.action_cancel);
        if(state) {
            deletebtn.setVisible(true);
            cancelbtn.setVisible(true);
        }
        else {
            deletebtn.setVisible(false);
            cancelbtn.setVisible(false);
        }}

    void setListener()
    {
        adapter.setItemClickListener(new Listener.OnItemClickListener() {
            @Override
            public void onItemClick(Contact item, int position,View view) {

                if(selecteditems.size()>0){

                    if (adapter.getItem(position).isSelected) {
                        adapter.getItem(position).isSelected = false;
                        if(selecteditems.contains(item))
                            selecteditems.remove(item);
                    } else {
                        adapter.getItem(position).isSelected = true;
                        if (!(selecteditems.contains(item))) {
                            selecteditems.add(item);
                        }
                    }
                    adapter.notifyItemChanged(position);
                    if (selecteditems.size() > 0)
                        toggledeleteButton(true);
                    else
                        toggledeleteButton(false);
                }
                else{
                    Intent intent = new Intent(getActivity(),DetailViewActivity.class);

                    intent.putExtra("object",item.getId());
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);

                }
            }
        });
        adapter.setItemClickLongListener(new Listener.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(Contact item, int position) {
                if (selecteditems.size() == 0) {
                    if (adapter.getItem(position).isSelected) {
                        adapter.getItem(position).isSelected = false;
                        selecteditems.remove(position);

                    } else {
                        adapter.getItem(position).isSelected = true;
                        if (!(selecteditems.contains(item))) {
                            selecteditems.add(item);
                        }
                    }
                    adapter.notifyItemChanged(position);
                    if (selecteditems.size() > 0)
                        toggledeleteButton(true);
                    else
                        toggledeleteButton(false);
                }
            }
        });
    }


        void unmarkSelected(){
         for (Contact contact : selecteditems ){
              adapter.getItem(adapter.getPosition(contact)).isSelected =  false;
             adapter.notifyItemChanged(adapter.getPosition(contact));
         }
            selecteditems.clear();


        }


    @Override
    public void onResume() {
        super.onResume();




    }


    private void initSwipe() {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();

                if (direction == ItemTouchHelper.LEFT) {
                    Toast.makeText(mContext, "swiped ledt", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, "swiped right", Toast.LENGTH_SHORT).show();
                }
            }








        @Override
        public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

            Bitmap icon;
            if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){


            }
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
      };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerview);


   Handler handler = new Handler(){


       @Override
       public void handleMessage(Message msg) {
           String aResponse = msg.getData().getString("message");
           super.handleMessage(msg);
       }
   };


    }

    public class MyObserver extends ContentObserver {
        public MyObserver(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            this.onChange(selfChange,null);
            System.out.println("function called  ");
            Message msgObj = handler.obtainMessage();
            Bundle b = new Bundle();
            b.putString("message", "function called");
            msgObj.setData(b);
            handler.sendMessage(msgObj);


        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            //Write your code here
            //Whatever is written here will be
            //executed whenever a change is made
            System.out.println("function called with uri    "+uri+"   ==  "+selfChange);
            Message msgObj = handler.obtainMessage();
            Bundle b = new Bundle();
            b.putString("message", "function called with uri "+ uri.toString() );
            msgObj.setData(b);
            handler.sendMessage(msgObj);
        }
    }


    @Override
    public void onPause() {
        super.onPause();

    }
}
