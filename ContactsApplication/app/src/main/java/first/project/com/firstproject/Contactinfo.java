package first.project.com.firstproject;

/**
 * Created by user on 8/4/16.
 */
public class Contactinfo {

    String contactid = null,type =null,contact = null,contenttype=null;

    public Contactinfo() {
    }

    @Override
    public String toString() {
        return "Contactinfo{" +
                "contactid='" + contactid + '\'' +
                ", type='" + type + '\'' +
                ", contact='" + contact + '\'' +
                ", contenttype='" + contenttype + '\'' +
                '}';
    }

    public Contactinfo(String contactid, String type, String contact, String contenttype) {
        this.type = type;
        this.contact = contact;
        this.contactid = contactid;
        this.contenttype = contenttype;
    }

    public String getContenttype() {
        return contenttype;
    }

    public void setContenttype(String contenttype) {
        this.contenttype = contenttype;
    }

    public String getContactid() {
        return contactid;
    }

    public void setContactid(String contactid) {
        this.contactid = contactid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
}
