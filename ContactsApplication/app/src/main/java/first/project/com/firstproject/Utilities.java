package first.project.com.firstproject;

import android.provider.ContactsContract;

import java.util.HashMap;


public class Utilities {


    public static HashMap<Integer,String> EMAIL = new HashMap<Integer, String>();
    public static HashMap<Integer,String> PHONE = new HashMap<Integer, String>();
    public static HashMap<Integer,String> ADDRESS = new HashMap<Integer, String>();
    public static HashMap<Integer,String> Nickname = new HashMap<Integer, String>();

   static {
       PHONE.put( ContactsContract.CommonDataKinds.Phone.TYPE_ASSISTANT,"Assistant");
       PHONE.put( ContactsContract.CommonDataKinds.Phone.TYPE_CALLBACK,"Callback");
       PHONE.put( ContactsContract.CommonDataKinds.Phone.TYPE_CAR,"Car");
       PHONE.put( ContactsContract.CommonDataKinds.Phone.TYPE_COMPANY_MAIN,"Company Main");
       PHONE.put( ContactsContract.CommonDataKinds.Phone.TYPE_FAX_HOME,"FaxHome");
       PHONE.put( ContactsContract.CommonDataKinds.Phone.TYPE_ISDN,"ISDN");
       PHONE.put( ContactsContract.CommonDataKinds.Phone.TYPE_MAIN,"Main");
       PHONE.put( ContactsContract.CommonDataKinds.Phone.TYPE_MMS,"Main");
       PHONE.put( ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE,"Mobile");
       PHONE.put( ContactsContract.CommonDataKinds.Phone.TYPE_OTHER,"Other");
       PHONE.put( ContactsContract.CommonDataKinds.Phone.TYPE_OTHER_FAX,"Other Fax");
       PHONE.put( ContactsContract.CommonDataKinds.Phone.TYPE_PAGER,"Pager");
       PHONE.put( ContactsContract.CommonDataKinds.Phone.TYPE_RADIO,"Radio");
       PHONE.put( ContactsContract.CommonDataKinds.Phone.TYPE_TELEX,"TELEX");
       PHONE.put( ContactsContract.CommonDataKinds.Phone.TYPE_TTY_TDD,"TTY_TTD");
       PHONE.put( ContactsContract.CommonDataKinds.Phone.TYPE_WORK,"Work");
       PHONE.put( ContactsContract.CommonDataKinds.Phone.TYPE_WORK_MOBILE,"Work Mobile");
       PHONE.put( ContactsContract.CommonDataKinds.Phone.TYPE_WORK_PAGER,"Work Pager");
       EMAIL.put( ContactsContract.CommonDataKinds.Email.TYPE_HOME,"Home");
       EMAIL.put( ContactsContract.CommonDataKinds.Email.TYPE_MOBILE,"Mobile");
       EMAIL.put( ContactsContract.CommonDataKinds.Email.TYPE_OTHER,"Other");
       EMAIL.put( ContactsContract.CommonDataKinds.Email.TYPE_WORK,"Work");
       ADDRESS.put(ContactsContract.CommonDataKinds.StructuredPostal.TYPE_HOME,"Home");
       ADDRESS.put(ContactsContract.CommonDataKinds.StructuredPostal.TYPE_OTHER,"Other");
       ADDRESS.put(ContactsContract.CommonDataKinds.StructuredPostal.TYPE_WORK,"Work");
       Nickname.put(1,"Default");
       Nickname.put(2,"OtherName");
       Nickname.put(3,"MaidenName");
       Nickname.put(4,"ShortName");
       Nickname.put(5,"Initials");

   }


}
