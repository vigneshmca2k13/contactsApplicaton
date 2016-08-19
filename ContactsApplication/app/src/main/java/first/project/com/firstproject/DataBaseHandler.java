package first.project.com.firstproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by user on 7/21/16.
 */
public class DataBaseHandler extends SQLiteOpenHelper {
    Context context;
    SQLiteDatabase db ;
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "contactdb";
    public static final String TABLE_NAME= "contacts";
    public static final String KEY_ID = "id";
    public static final String KEY_CONTACTID = "contactid";
    public static final String KEY_NAME = "name";
    public static final String KEY_HAVE_MAIL = "email";
    public static final String KEY_PHOTO_URI = "uri";
    public static final String KEY_HASNOTE = "hasnote";
    public static final String KEY_HASADDRESS = "hasaddress";
    public static final String KEY_HASEVENTS = "hasevents";
    public static final String KEY_HASNICKNAME = "hasnickname";
    public static final String KEY_HASWEBSITE = "haswebsite";
    public static final String KEY_HASORGANIZATION = "hasorganization";
    public static final String KEY_HASPHONETICNAME = "hasphoneticname";
    public static final String KEY_FIRSTNAME = "firstname";
    public static final String KEY_MIDDLENAME = "middlename";
    public static final String KEY_LASTNAME = "lastname";
    public static final String KEY_PREFIX = "prefix";
    public static final String KEY_SUFFIX = "suffix";
    public static final String KEY_PHONETIC_FAMILYNAME = "phoneticfamilyname";
    public static final String KEY_PHONETIC_MIDDLENAME = "phoneticmiddlename";
    public static final String KEY_PHONETIC_GIVENNAME = "phoneticgivenname";
    public static final String KEY_COMPANY= "company";
    public static final String KEY_TITLE = "title";
    public static final String KEY_HASWHATSAPPACCOUNT = "haswhatsappaccount";


    public DataBaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    public DataBaseHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE   " + TABLE_NAME + "("
                + KEY_ID  + " INTEGER PRIMARY KEY AUTOINCREMENT,"+ KEY_CONTACTID + " TEXT, " + KEY_NAME  +"  TEXT,"+ KEY_HASWHATSAPPACCOUNT   + "  INTEGER,"+ KEY_HAVE_MAIL   +"  TEXT," + KEY_HASADDRESS + "  INTEGER," +KEY_HASNOTE + "  INTEGER,"   +KEY_HASEVENTS + "  INTEGER,"  +KEY_HASNICKNAME + "  INTEGER,"  +KEY_HASWEBSITE + "  INTEGER," +KEY_HASORGANIZATION + "  INTEGER,"+ KEY_HASPHONETICNAME + "  INTEGER,"  +KEY_FIRSTNAME + "  TEXT," +KEY_MIDDLENAME + "  TEXT," +KEY_LASTNAME + "  TEXT," +KEY_PREFIX + "  TEXT," + KEY_SUFFIX + "  TEXT,"  + KEY_PHONETIC_FAMILYNAME + "  TEXT," + KEY_PHONETIC_GIVENNAME + "  TEXT," + KEY_PHONETIC_MIDDLENAME + "  TEXT,"  + KEY_COMPANY + "  TEXT," + KEY_TITLE+ "  TEXT," + KEY_PHOTO_URI +" TEXT)";
        String CREATE_CONTACTINFO_TABLE = "CREATE TABLE  " + ContactInfo.TABLE_NAME + "("
                + ContactInfo.KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"+ ContactInfo.KEY_CONTACTID + " TEXT, " + ContactInfo.KEY_CONTACT  + "  TEXT,"+ContactInfo.KEY_TYPE_CONTENTTYPE  + "  TEXT,"  + ContactInfo.KEY_TYPE +" TEXT)";
        sqLiteDatabase.execSQL(CREATE_CONTACTS_TABLE);
        sqLiteDatabase.execSQL(CREATE_CONTACTINFO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public  String addContact(Contact contact) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, contact.getName());
        values.put(KEY_CONTACTID, contact.getContactid());
        values.put(KEY_PHOTO_URI, contact.getImageuri());
        values.put(KEY_HAVE_MAIL,contact.getHasmail());
        values.put(KEY_HASNOTE,contact.getHasnote());
        values.put(KEY_HASADDRESS,contact.getHasaddress());
        values.put(KEY_HASNICKNAME,contact.getHasnickname());
        values.put(KEY_HASEVENTS,contact.getHasevents());
        values.put(KEY_HASWEBSITE,contact.getHaswebsite());
        values.put(KEY_FIRSTNAME,contact.getFirstname());
        values.put(KEY_MIDDLENAME,contact.getMiddlename());
        values.put(KEY_LASTNAME,contact.getLastname());
        values.put(KEY_PREFIX,contact.getPrefix());
        values.put(KEY_SUFFIX,contact.getSuffix());
        values.put(KEY_HASPHONETICNAME,contact.getHasphoneticname());
        values.put(KEY_PHONETIC_FAMILYNAME,contact.getPhoneticfamilyname());
        values.put(KEY_PHONETIC_GIVENNAME,contact.getPhoneticgivenname());
        values.put(KEY_PHONETIC_MIDDLENAME,contact.getPhoneticmiddlename());
        values.put(KEY_HASORGANIZATION,contact.getHasorganziation());
        values.put(KEY_TITLE,contact.getTitle());
        values.put(KEY_COMPANY,contact.getCompany());

        Long check = db.insert(TABLE_NAME, null, values);
        db.close();

        return String.valueOf(check);
    }

    public  Contact getContactbyId(String id){

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor =  db.query(TABLE_NAME,null,KEY_ID +"= ? ",new String[]{id},null,null,null);
        if(cursor.moveToFirst())
        {
            Contact contact = new Contact();
            contact.setCompany(cursor.getString(cursor.getColumnIndex(DataBaseHandler.KEY_COMPANY)));
            contact.setName(cursor.getString(cursor.getColumnIndex(DataBaseHandler.KEY_NAME)));
            contact.setFirstname(cursor.getString(cursor.getColumnIndex(DataBaseHandler.KEY_FIRSTNAME)));
            contact.setContactid(cursor.getString(cursor.getColumnIndex(DataBaseHandler.KEY_CONTACTID)));
            contact.setMiddlename(cursor.getString(cursor.getColumnIndex(DataBaseHandler.KEY_MIDDLENAME)));
            contact.setLastname(cursor.getString(cursor.getColumnIndex(DataBaseHandler.KEY_LASTNAME)));
            contact.setPrefix(cursor.getString(cursor.getColumnIndex(DataBaseHandler.KEY_PREFIX)));
            contact.setSuffix(cursor.getString(cursor.getColumnIndex(DataBaseHandler.KEY_SUFFIX)));
            contact.setPhoneticfamilyname(cursor.getString(cursor.getColumnIndex(DataBaseHandler.KEY_PHONETIC_FAMILYNAME)));
            contact.setPhoneticmiddlename(cursor.getString(cursor.getColumnIndex(DataBaseHandler.KEY_PHONETIC_MIDDLENAME)));
            contact.setPhoneticgivenname(cursor.getString(cursor.getColumnIndex(DataBaseHandler.KEY_PHONETIC_GIVENNAME)));
            contact.setTitle(cursor.getString(cursor.getColumnIndex(DataBaseHandler.KEY_TITLE)));
            contact.setImageuri(cursor.getString(cursor.getColumnIndex(DataBaseHandler.KEY_PHOTO_URI)));
            contact.setHasmail(cursor.getString(cursor.getColumnIndex(DataBaseHandler.KEY_HAVE_MAIL)));
            contact.setHasnote(cursor.getInt(cursor.getColumnIndex(DataBaseHandler.KEY_HASNOTE)));
            contact.setHasaddress(cursor.getInt(cursor.getColumnIndex(DataBaseHandler.KEY_HASADDRESS)));
            contact.setHasevents(cursor.getInt(cursor.getColumnIndex(DataBaseHandler.KEY_HASEVENTS)));
            contact.setHasnickname(cursor.getInt(cursor.getColumnIndex(DataBaseHandler.KEY_HASNICKNAME)));
            contact.setHaswebsite(cursor.getInt(cursor.getColumnIndex(DataBaseHandler.KEY_HASWEBSITE)));
            contact.setHasorganziation(cursor.getInt(cursor.getColumnIndex(DataBaseHandler.KEY_HASORGANIZATION)));
            contact.setHasphoneticname(cursor.getInt(cursor.getColumnIndex(DataBaseHandler.KEY_HASPHONETICNAME)));
            contact.setId(cursor.getString(cursor.getColumnIndex(DataBaseHandler.KEY_ID)));
            db.close();
            return contact;
        }
        db.close();
        return  null;
    }
    public  void addContacts(ArrayList<Contact> contacts) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        for(Contact contact : contacts ) {
            ContentValues values = new ContentValues();
            values.put(KEY_NAME, contact.getName());
            values.put(KEY_CONTACTID, contact.getContactid());
            values.put(KEY_PHOTO_URI, contact.getImageuri());
            values.put(KEY_HAVE_MAIL,contact.getHasmail());
            values.put(KEY_HASNOTE,contact.getHasnote());
            values.put(KEY_HASADDRESS,contact.getHasaddress());
            values.put(KEY_HASNICKNAME,contact.getHasnickname());
            values.put(KEY_HASEVENTS,contact.getHasevents());
            values.put(KEY_HASWEBSITE,contact.getHaswebsite());
            values.put(KEY_FIRSTNAME,contact.getFirstname());
            values.put(KEY_MIDDLENAME,contact.getMiddlename());
            values.put(KEY_LASTNAME,contact.getLastname());
            values.put(KEY_PREFIX,contact.getPrefix());
            values.put(KEY_SUFFIX,contact.getSuffix());
            values.put(KEY_HASPHONETICNAME,contact.getHasphoneticname());
            values.put(KEY_PHONETIC_FAMILYNAME,contact.getPhoneticfamilyname());
            values.put(KEY_PHONETIC_GIVENNAME,contact.getPhoneticgivenname());
            values.put(KEY_PHONETIC_MIDDLENAME,contact.getPhoneticmiddlename());
            values.put(KEY_HASORGANIZATION,contact.getHasorganziation());
            values.put(KEY_TITLE,contact.getTitle());
            values.put(KEY_COMPANY,contact.getCompany());

            Long check = db.insert(TABLE_NAME, null, values);
        }
        db.endTransaction();
        db.close();

    }
    public Boolean updateContact(ContentValues contentValues,String id){

        SQLiteDatabase db = this.getWritableDatabase();

        int check =  db.update(TABLE_NAME,contentValues,KEY_ID + " = ?" ,new String[]{id});

        db.close();
        if(check != -1)
            return true;
        else
            return false;
    }


    public void updateContacts(HashMap<String,ContentValues> contentValues) {

        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        for (String key : contentValues.keySet()){
            System.out.println(" updating  each contact");
            int check = db.update(TABLE_NAME, contentValues.get(key), KEY_ID + " = ?", new String[]{key});
        }
        db.setTransactionSuccessful();
        db.endTransaction();

        db.close();

    }
    public Boolean updateContactInfo(ContentValues contentValues,String id){

        SQLiteDatabase db = this.getWritableDatabase();

        int check =  db.update(ContactInfo.TABLE_NAME,contentValues,ContactInfo.KEY_ID + " = ?" ,new String[]{id});
        db.close();
        if(check != -1)
            return true;
        else
            return false;


    }
    public  Boolean addContactInfo(Contactinfo contactinfo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ContactInfo.KEY_CONTACTID, contactinfo.getContactid());
        values.put(ContactInfo.KEY_TYPE, contactinfo.getType());
        values.put(ContactInfo.KEY_CONTACT, contactinfo.getContact());
        values.put(ContactInfo.KEY_TYPE_CONTENTTYPE,contactinfo.getContenttype());
        Long check = db.insert(ContactInfo.TABLE_NAME, null, values);

        db.close();
        if(check != -1)
            return true;
        else
            return false;



    }


        public  void addContactInfolist(ArrayList<Contactinfo> contactinfos) {
            SQLiteDatabase db = this.getWritableDatabase();
            db.beginTransaction();
            for(Contactinfo contactinfo: contactinfos ) {

                ContentValues values = new ContentValues();
                values.put(ContactInfo.KEY_CONTACTID, contactinfo.getContactid());
                values.put(ContactInfo.KEY_TYPE, contactinfo.getType());
                values.put(ContactInfo.KEY_CONTACT, contactinfo.getContact());
                values.put(ContactInfo.KEY_TYPE_CONTENTTYPE,contactinfo.getContenttype());
                Long check = db.insert(ContactInfo.TABLE_NAME, null, values);

                if(check != -1)
                    System.out.println(" interting contact info  "+ contactinfo.toString());

            }
            db.setTransactionSuccessful();
            db.endTransaction();
            db.close();
        }



    public ArrayList<Contact> getAllContacts() {
        db = this.getReadableDatabase();

        ArrayList<Contact> ContactList = new ArrayList<Contact>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NAME + " ORDER BY " + KEY_NAME + " COLLATE NOCASE ASC";
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list=
        if(cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                Contact contact = new Contact();
                contact.setCompany(cursor.getString(cursor.getColumnIndex(DataBaseHandler.KEY_COMPANY)));
                contact.setName(cursor.getString(cursor.getColumnIndex(DataBaseHandler.KEY_NAME)));
                contact.setFirstname(cursor.getString(cursor.getColumnIndex(DataBaseHandler.KEY_FIRSTNAME)));
                contact.setContactid(cursor.getString(cursor.getColumnIndex(DataBaseHandler.KEY_CONTACTID)));
                contact.setMiddlename(cursor.getString(cursor.getColumnIndex(DataBaseHandler.KEY_MIDDLENAME)));
                contact.setLastname(cursor.getString(cursor.getColumnIndex(DataBaseHandler.KEY_LASTNAME)));
                contact.setPrefix(cursor.getString(cursor.getColumnIndex(DataBaseHandler.KEY_PREFIX)));
                contact.setSuffix(cursor.getString(cursor.getColumnIndex(DataBaseHandler.KEY_SUFFIX)));
                contact.setPhoneticfamilyname(cursor.getString(cursor.getColumnIndex(DataBaseHandler.KEY_PHONETIC_FAMILYNAME)));
                contact.setPhoneticmiddlename(cursor.getString(cursor.getColumnIndex(DataBaseHandler.KEY_PHONETIC_MIDDLENAME)));
                contact.setPhoneticgivenname(cursor.getString(cursor.getColumnIndex(DataBaseHandler.KEY_PHONETIC_GIVENNAME)));
                contact.setTitle(cursor.getString(cursor.getColumnIndex(DataBaseHandler.KEY_TITLE)));
                contact.setImageuri(cursor.getString(cursor.getColumnIndex(DataBaseHandler.KEY_PHOTO_URI)));
                contact.setHasmail(cursor.getString(cursor.getColumnIndex(DataBaseHandler.KEY_HAVE_MAIL)));
                contact.setHasnote(cursor.getInt(cursor.getColumnIndex(DataBaseHandler.KEY_HASNOTE)));
                contact.setHasaddress(cursor.getInt(cursor.getColumnIndex(DataBaseHandler.KEY_HASADDRESS)));
                contact.setHasevents(cursor.getInt(cursor.getColumnIndex(DataBaseHandler.KEY_HASEVENTS)));
                contact.setHasnickname(cursor.getInt(cursor.getColumnIndex(DataBaseHandler.KEY_HASNICKNAME)));
                contact.setHaswebsite(cursor.getInt(cursor.getColumnIndex(DataBaseHandler.KEY_HASWEBSITE)));
                contact.setHasorganziation(cursor.getInt(cursor.getColumnIndex(DataBaseHandler.KEY_HASORGANIZATION)));
                contact.setHasphoneticname(cursor.getInt(cursor.getColumnIndex(DataBaseHandler.KEY_HASPHONETICNAME)));
                contact.setId(cursor.getString(cursor.getColumnIndex(DataBaseHandler.KEY_ID)));
                contact.setHaswhatsappaccount(cursor.getInt(cursor.getColumnIndex(DataBaseHandler.KEY_HASWHATSAPPACCOUNT)));
                ContactList.add(contact);
                cursor.moveToNext();
            }
        }
            cursor.close();

        db.close();
        return ContactList;
    }
//    public ArrayList<Contactinfo> getAllContactinfos() throws Exception{
//        SQLiteDatabase db = this.getReadableDatabase();
//
//        ArrayList<Contactinfo> ContactinfoList = new ArrayList<Contactinfo>();
//        // Select All Query
//        String selectQuery = "SELECT  * FROM " + ContactInfo.TABLE_NAME;
//        try{
//            Cursor cursor = db.rawQuery(selectQuery, null);
//            // looping through all rows and adding to list
//            if (cursor.moveToFirst()) {
//                while (cursor.moveToNext()) {
//                    Contactinfo contactinfo = new Contactinfo(cursor.getString(cursor.getColumnIndex(ContactInfo.KEY_CONTACTID)),cursor.getString(cursor.getColumnIndex(ContactInfo.KEY_TYPE)),cursor.getString(cursor.getColumnIndex(ContactInfo.KEY_CONTACT)));
//                    // Adding contact to list
//                    ContactinfoList.add(contactinfo);
//                }
//            }
//        }catch (Exception mException){
//            throw mException;
//        }
//        db.close();
//        return ContactinfoList;
//    }


    ArrayList<ContactDetails> createcontactdetailedlist(){
        ArrayList<ContactDetails> list = new ArrayList<>();

            ArrayList<Contact> contactlist = new ArrayList<>();

            contactlist = getAllContacts();

                    for(Contact contact : contactlist){
                        list.add(getContactInfo(contact));
                    }

        return list;
    }

    ContactDetails getContactInfo(Contact contact){

        System.out.println("increating contact info   " + contact.toString());
        ContactDetails contactDetails = new ContactDetails();

        contactDetails.setContact(contact);
        db = this.getReadableDatabase();
        Cursor mCursornumber =     db.query(ContactInfo.TABLE_NAME,null,ContactInfo.KEY_CONTACTID + "= ?  AND " + ContactInfo.KEY_TYPE + " = ? ",new String[]{contact.getId(),ContactInfo.KEY_TYPE_PHONE},null,null,null );

        System.out.println("reading numbers"   + mCursornumber.getCount());
        if(mCursornumber.moveToFirst()) {
            while (!mCursornumber.isAfterLast()) {
                contactDetails.numbers.add(new Data(mCursornumber.getString(mCursornumber.getColumnIndex(ContactInfo.KEY_TYPE_CONTENTTYPE)), mCursornumber.getString(mCursornumber.getColumnIndex(ContactInfo.KEY_CONTACT)), mCursornumber.getString(mCursornumber.getColumnIndex(ContactInfo.KEY_ID))));



                mCursornumber.moveToNext();
            }
        }
        mCursornumber.close();
        Cursor mCursoremail = db.rawQuery("SELECT * FROM "+ ContactInfo.TABLE_NAME +
                " WHERE " + "contacts_info.type  = " + "2  AND  contacts_info.contactid = " + contact.getId() , null);
        if(mCursoremail.moveToFirst())     {
            System.out.println("reading mails");
            while (!mCursoremail.isAfterLast()) {

                contactDetails.mailids.add(new Data(mCursoremail.getString(mCursoremail.getColumnIndex(ContactInfo.KEY_TYPE_CONTENTTYPE)), mCursoremail.getString(mCursoremail.getColumnIndex(ContactInfo.KEY_CONTACT)), mCursoremail.getString(mCursoremail.getColumnIndex(ContactInfo.KEY_ID))));
                mCursoremail.moveToNext();
            }
        }

        mCursoremail.close();
        Cursor mCursoraddress = db.rawQuery("SELECT * FROM "+ ContactInfo.TABLE_NAME +
                " WHERE " + "contacts_info.type  = " + ContactInfo.KEY_TYPE_Address +"  AND  contacts_info.contactid = " + contact.getId() , null);
        if(mCursoraddress.moveToFirst()) {
            while (!mCursoraddress.isAfterLast()) {
                contactDetails.address.add(new Data(mCursoraddress.getString(mCursoraddress.getColumnIndex(ContactInfo.KEY_TYPE_CONTENTTYPE)), mCursoraddress.getString(mCursoraddress.getColumnIndex(ContactInfo.KEY_CONTACT)),mCursoraddress.getString(mCursoraddress.getColumnIndex(ContactInfo.KEY_ID))));
                mCursoraddress.moveToNext();
            }
        }

        Cursor mCursornickname = db.rawQuery("SELECT * FROM "+ ContactInfo.TABLE_NAME +
                " WHERE " + "contacts_info.type  = " + ContactInfo.KEY_TYPE_Nickname +"  AND  contacts_info.contactid = " + contact.getId() , null);

        if(mCursornickname.moveToFirst() ) {
            while (!mCursornickname.isAfterLast()) {
                contactDetails.nicknames.add(new Data(mCursornickname.getString(mCursornickname.getColumnIndex(ContactInfo.KEY_TYPE_CONTENTTYPE)), mCursornickname.getString(mCursornickname.getColumnIndex(ContactInfo.KEY_CONTACT)),mCursornickname.getString(mCursornickname.getColumnIndex(ContactInfo.KEY_ID))));
                mCursornickname.moveToNext();
            }
        }
        Cursor mCursornotes = db.rawQuery("SELECT * FROM "+ ContactInfo.TABLE_NAME +
                " WHERE " + "contacts_info.type  = " + ContactInfo.KEY_TYPE_NOTE +"  AND  contacts_info.contactid = " + contact.getId() , null);

        if(mCursornotes.moveToFirst() ) {
            while (!mCursornotes.isAfterLast()) {
                contactDetails.notes.add(new Data(mCursornotes.getString(mCursornotes.getColumnIndex(ContactInfo.KEY_TYPE_CONTENTTYPE)), mCursornotes.getString(mCursornotes.getColumnIndex(ContactInfo.KEY_CONTACT)),mCursornotes.getString(mCursornotes.getColumnIndex(ContactInfo.KEY_ID))));
                mCursornotes.moveToNext();
            }
        }

        Cursor mCursorevents = db.rawQuery("SELECT * FROM "+ ContactInfo.TABLE_NAME +
                " WHERE " + "contacts_info.type  = " + ContactInfo.KEY_TYPE_EVENTS +"  AND  contacts_info.contactid = " + contact.getId() , null);


        if(mCursorevents.moveToFirst()) {

            while (!mCursorevents.isAfterLast()) {
                contactDetails.events.add(new Data(mCursorevents.getString(mCursorevents.getColumnIndex(ContactInfo.KEY_TYPE_CONTENTTYPE)), mCursorevents.getString(mCursorevents.getColumnIndex(ContactInfo.KEY_CONTACT)),mCursorevents.getString(mCursorevents.getColumnIndex(ContactInfo.KEY_ID))));
                mCursorevents.moveToNext();
            }
        }

        Cursor mCursorwebsite = db.rawQuery("SELECT * FROM "+ ContactInfo.TABLE_NAME +
                " WHERE " + "contacts_info.type  = " + ContactInfo.KEY_TYPE_WEBSITE +"  AND  contacts_info.contactid = " + contact.getId() , null);


        if(mCursorwebsite.moveToFirst()) {

            while (!mCursorwebsite.isAfterLast()) {
                contactDetails.websites.add(new Data(mCursorwebsite.getString(mCursorwebsite.getColumnIndex(ContactInfo.KEY_TYPE_CONTENTTYPE)), mCursorwebsite.getString(mCursorwebsite.getColumnIndex(ContactInfo.KEY_CONTACT)),mCursorwebsite.getString(mCursorwebsite.getColumnIndex(ContactInfo.KEY_ID))));
                mCursorwebsite.moveToNext();
            }
        }


    db.close();
    return contactDetails;
    }

    public Boolean deleteContactInfo(String id){

        db = this.getReadableDatabase();

        int check = db.delete(ContactInfo.TABLE_NAME,ContactInfo.KEY_ID + " = ?",new String[]{id});
        db.close();
        if(check != -1)
            return true;
        else
            return false;

    }

    public Boolean deleteContactInfoByContactId(String id){

        db = this.getWritableDatabase();

        int check = db.delete(ContactInfo.TABLE_NAME,ContactInfo.KEY_CONTACTID + " = ?",new String[]{id});
        db.close();
        if(check != -1)
            return true;
        else
            return false;

    }

    public Boolean deleteContactByContactId(String id){

        db = this.getWritableDatabase();

        int check = db.delete(TABLE_NAME,KEY_ID + " = ?",new String[]{id});
        db.close();
        if(check != -1)
            return true;
        else
            return false;

    }
    public int getContactsCount(){
        int count = 0;
        String countQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            Cursor cursor = db.rawQuery(countQuery, null);
            count = cursor.getCount();
            cursor.close();

        }catch (Exception mException){
            throw mException;
        }
        // return count
        db.close();
        return count;
    }

    public int getContactinfoCount(){
        int count = 0;
        String countQuery = "SELECT  * FROM " + ContactInfo.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            Cursor cursor = db.rawQuery(countQuery, null);
            count = cursor.getCount();
            cursor.close();
        }catch (Exception mException){
            throw mException;
        }db.close();

        // return count
        return count;
    }

    public void deleteAll() throws Exception{
        try {
            String query = "DELETE FROM " + DataBaseHandler.TABLE_NAME + ";";
            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL(query);
        }catch (Exception mException){
            throw mException;
        }
        // return count
    }
//
//    public  void deleteSelected(int id) throws Exception{
//        try {
//            String query = "DELETE " + "FROM " + DataBaseHandler.TABLE_NAME + " WHERE id = " + id;
//            SQLiteDatabase db = this.getWritableDatabase();
//            db.execSQL(query);
//        }catch (Exception mException){
//            throw  mException;
//        }
//    }
//
//    public  void  updateData(Note note) throws Exception{
//        try {
//            SQLiteDatabase db = this.getWritableDatabase();
//            ContentValues content = new ContentValues();
//            content.put(KEY_NOTE, note.get_note());
////
//            db.update(TABLE_NAME, content, KEY_ID + " = ?",
//                    new String[]{String.valueOf(note.get_id())});
//        }catch (Exception mException){
//            throw  mException;
//        }



    interface ContactInfo{
        public static final String TABLE_NAME= "contacts_info";
        public static final String KEY_ID = "id";
        public static final String KEY_CONTACTID = "contactid";
        public static final String KEY_CONTACT = "contact";
        public static final String KEY_TYPE = "type";
        public static final String KEY_TYPE_MAIL = "2";
        public static final String KEY_TYPE_PHONE = "1";
        public static final String KEY_TYPE_CONTENTTYPE = "contenttype";
        public static final String KEY_TYPE_NOTE = "3";
        public static final String KEY_TYPE_Address = "4";
        public static final String KEY_TYPE_Nickname = "5";
        public static final String KEY_TYPE_EVENTS = "6";
        public static final String KEY_TYPE_WEBSITE = "7";
        public static final  String KEY_TYPE_WHATSAPP = "8";
    }
    }



