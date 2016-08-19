package first.project.com.firstproject;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by user on 8/3/16.
 */
public class Contact implements Parcelable {

    String contactid,name,firstname,middlename,lastname,prefix,suffix,phoneticfamilyname,phoneticmiddlename,phoneticgivenname,company,title;
    String id;

    String imageuri = "0" ;
    String hasmail = "0";
    String address = "Not Available";
    int hasnote = 0,hasaddress = 0, hasevents=0,hasnickname =0,haswebsite=0,hasorganziation=0,hasphoneticname = 0,haswhatsappaccount = 0;
    boolean isSelected;

    public Contact() {
    }

    private Contact(Parcel in) {
        contactid = in.readString();
        name = in.readString();
        firstname = in.readString();
        middlename = in.readString();
        lastname = in.readString();
        prefix = in.readString();
        suffix = in.readString();
        phoneticfamilyname = in.readString();
        phoneticmiddlename = in.readString();
        phoneticgivenname = in.readString();
        company = in.readString();
        title = in.readString();
        id = in.readString();
        imageuri = in.readString();
        hasmail = in.readString();
        address = in.readString();
        hasnote = in.readInt();
        hasaddress = in.readInt();
        hasevents = in.readInt();
        hasnickname = in.readInt();
        haswebsite = in.readInt();
        hasorganziation = in.readInt();
        hasphoneticname = in.readInt();
        haswhatsappaccount = in.readInt();
        isSelected = in.readByte() != 0;
    }

    public static final Creator<trial> CREATOR = new Creator<trial>() {
        @Override
        public trial createFromParcel(Parcel in) {
            return new trial(in);
        }

        @Override
        public trial[] newArray(int size) {
            return new trial[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(contactid);
        parcel.writeString(name);
        parcel.writeString(firstname);
        parcel.writeString(middlename);
        parcel.writeString(lastname);
        parcel.writeString(prefix);
        parcel.writeString(suffix);
        parcel.writeString(phoneticfamilyname);
        parcel.writeString(phoneticmiddlename);
        parcel.writeString(phoneticgivenname);
        parcel.writeString(company);
        parcel.writeString(title);
        parcel.writeString(id);
        parcel.writeString(imageuri);
        parcel.writeString(hasmail);
        parcel.writeString(address);
        parcel.writeInt(hasnote);
        parcel.writeInt(hasaddress);
        parcel.writeInt(hasevents);
        parcel.writeInt(hasnickname);
        parcel.writeInt(haswebsite);
        parcel.writeInt(hasorganziation);
        parcel.writeInt(hasphoneticname);
        parcel.writeInt(haswhatsappaccount);
        parcel.writeByte((byte) (isSelected ? 1 : 0));
    }


    public String getContactid() {
        return contactid;
    }

    public void setContactid(String contactid) {
        this.contactid = contactid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getMiddlename() {
        return middlename;
    }

    public void setMiddlename(String middlename) {
        this.middlename = middlename;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getPhoneticfamilyname() {
        return phoneticfamilyname;
    }

    public void setPhoneticfamilyname(String phoneticfamilyname) {
        this.phoneticfamilyname = phoneticfamilyname;
    }

    public String getPhoneticmiddlename() {
        return phoneticmiddlename;
    }

    public void setPhoneticmiddlename(String phoneticmiddlename) {
        this.phoneticmiddlename = phoneticmiddlename;
    }

    public String getPhoneticgivenname() {
        return phoneticgivenname;
    }

    public void setPhoneticgivenname(String phoneticgivenname) {
        this.phoneticgivenname = phoneticgivenname;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageuri() {
        return imageuri;
    }

    public void setImageuri(String imageuri) {
        this.imageuri = imageuri;
    }

    public String getHasmail() {
        return hasmail;
    }

    public void setHasmail(String hasmail) {
        this.hasmail = hasmail;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getHasnote() {
        return hasnote;
    }

    public void setHasnote(int hasnote) {
        this.hasnote = hasnote;
    }

    public int getHasaddress() {
        return hasaddress;
    }

    public void setHasaddress(int hasaddress) {
        this.hasaddress = hasaddress;
    }

    public int getHasevents() {
        return hasevents;
    }

    public void setHasevents(int hasevents) {
        this.hasevents = hasevents;
    }

    public int getHasnickname() {
        return hasnickname;
    }

    public void setHasnickname(int hasnickname) {
        this.hasnickname = hasnickname;
    }

    public int getHaswebsite() {
        return haswebsite;
    }

    public void setHaswebsite(int haswebsite) {
        this.haswebsite = haswebsite;
    }

    public int getHasorganziation() {
        return hasorganziation;
    }

    public void setHasorganziation(int hasorganziation) {
        this.hasorganziation = hasorganziation;
    }

    public int getHasphoneticname() {
        return hasphoneticname;
    }

    public void setHasphoneticname(int hasphoneticname) {
        this.hasphoneticname = hasphoneticname;
    }

    public int getHaswhatsappaccount() {
        return haswhatsappaccount;
    }

    public void setHaswhatsappaccount(int haswhatsappaccount) {
        this.haswhatsappaccount = haswhatsappaccount;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
