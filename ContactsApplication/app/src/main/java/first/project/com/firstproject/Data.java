package first.project.com.firstproject;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by user on 8/7/16.
 */
public class Data implements Parcelable {

    String Type,data,id;

    public Data(String type, String data,String id) {
        this.Type = type;
        this.data = data;
        this.id = id;
    }

    protected Data(Parcel in) {
        Type = in.readString();
        data = in.readString();
        id = in.readString();
    }

    public static final Creator<Data> CREATOR = new Creator<Data>() {
        @Override
        public Data createFromParcel(Parcel in) {
            return new Data(in);
        }

        @Override
        public Data[] newArray(int size) {
            return new Data[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(Type);
        parcel.writeString(data);
        parcel.writeString(id);
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

}
