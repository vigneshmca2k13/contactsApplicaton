package first.project.com.firstproject;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by user on 8/4/16.
 */
public class ContactDetails implements Parcelable{

    String name,imageuri;
    ArrayList<Data> numbers;
    ArrayList<Data> mailids ;
    ArrayList<Data> notes ;
    Contact contact ;
     String whatsapp;
    ArrayList<Data> address;
    ArrayList<Data> nicknames;
    ArrayList<Data> events;
    ArrayList<Data> websites;

    public ContactDetails() {
        contact =new Contact();
        numbers = new ArrayList<>();
        mailids = new ArrayList<>();
        notes = new ArrayList<>();
        address = new ArrayList<>();
        nicknames = new ArrayList<>();
        events = new ArrayList<>();
        websites = new  ArrayList<>();
    }

    protected ContactDetails(Parcel in) {
        name = in.readString();
        imageuri = in.readString();
        numbers = in.createTypedArrayList(Data.CREATOR);
        mailids = in.createTypedArrayList(Data.CREATOR);
        notes = in.createTypedArrayList(Data.CREATOR);
        contact = in.readParcelable(Contact.class.getClassLoader());
        whatsapp = in.readString();
        address = in.createTypedArrayList(Data.CREATOR);
        nicknames = in.createTypedArrayList(Data.CREATOR);
        events = in.createTypedArrayList(Data.CREATOR);
        websites = in.createTypedArrayList(Data.CREATOR);
    }

    public static final Creator<ContactDetails> CREATOR = new Creator<ContactDetails>() {
        @Override
        public ContactDetails createFromParcel(Parcel in) {
            return new ContactDetails(in);
        }

        @Override
        public ContactDetails[] newArray(int size) {
            return new ContactDetails[size];
        }
    };



    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(imageuri);
        parcel.writeTypedList(numbers);
        parcel.writeTypedList(mailids);
        parcel.writeTypedList(notes);
        parcel.writeParcelable(contact, i);
        parcel.writeString(whatsapp);
        parcel.writeTypedList(address);
        parcel.writeTypedList(nicknames);
        parcel.writeTypedList(events);
        parcel.writeTypedList(websites);
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageuri() {
        return imageuri;
    }

    public void setImageuri(String imageuri) {
        this.imageuri = imageuri;
    }

    public ArrayList<Data> getNumbers() {
        return numbers;
    }

    public void setNumbers(ArrayList<Data> numbers) {
        this.numbers = numbers;
    }

    public ArrayList<Data> getMailids() {
        return this.mailids;
    }

    public void setMailids(ArrayList<Data> mailids) {
        this.mailids = mailids;
    }

    public String getWhatsapp() {
        return whatsapp;
    }

    public void setWhatsapp(String whatsapp) {
        this.whatsapp = whatsapp;
    }

    public ArrayList<Data> getWebsites() {
        return websites;
    }

    public void setWebsites(ArrayList<Data> websites) {
        this.websites = websites;
    }

    public ArrayList<Data> getEvents() {
        return events;
    }

    public void setEvents(ArrayList<Data> events) {
        this.events = events;
    }

    public ArrayList<Data> getNicknames() {
        return nicknames;
    }

    public void setNicknames(ArrayList<Data> nicknames) {
        this.nicknames = nicknames;
    }

    public ArrayList<Data> getAddress() {
        return address;
    }

    public void setAddress(ArrayList<Data> address) {
        this.address = address;
    }

    public ArrayList<Data> getNotes() {
        return notes;
    }

    public void setNotes(ArrayList<Data> notes) {
        this.notes = notes;
    }
    @Override
    public String toString() {
        return "ContactDetails{" +
                "name='" + name + '\'' +
                ", imageuri='" + imageuri + '\'' +
                ", numbers=" + numbers +
                ", mailids=" + mailids +
                ", notes=" + notes +
                ", contact=" + contact +
                '}';
    }
    @Override
    public int describeContents() {
        return 0;
    }


}
