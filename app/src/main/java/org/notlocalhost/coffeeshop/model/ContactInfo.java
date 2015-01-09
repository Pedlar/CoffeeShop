package org.notlocalhost.coffeeshop.model;

public class ContactInfo {
    String phone;
    String formattedPhone;
    String twitter;
    String facebookUsername;
    String facebook;
    String facebookName;

    public String getPhone() {
        return phone;
    }

    public String getFormattedPhone() {
        return formattedPhone;
    }

    public String getTwitter() {
        return twitter;
    }

    public String getFacebookUsername() {
        return facebookUsername;
    }

    public String getFacebook() {
        return facebook;
    }

    public String getFacebookName() {
        return facebookName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ContactInfo that = (ContactInfo) o;

        if (facebook != null ? !facebook.equals(that.facebook) : that.facebook != null)
            return false;
        if (facebookName != null ? !facebookName.equals(that.facebookName) : that.facebookName != null)
            return false;
        if (facebookUsername != null ? !facebookUsername.equals(that.facebookUsername) : that.facebookUsername != null)
            return false;
        if (formattedPhone != null ? !formattedPhone.equals(that.formattedPhone) : that.formattedPhone != null)
            return false;
        if (phone != null ? !phone.equals(that.phone) : that.phone != null) return false;
        if (twitter != null ? !twitter.equals(that.twitter) : that.twitter != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = phone != null ? phone.hashCode() : 0;
        result = 31 * result + (formattedPhone != null ? formattedPhone.hashCode() : 0);
        result = 31 * result + (twitter != null ? twitter.hashCode() : 0);
        result = 31 * result + (facebookUsername != null ? facebookUsername.hashCode() : 0);
        result = 31 * result + (facebook != null ? facebook.hashCode() : 0);
        result = 31 * result + (facebookName != null ? facebookName.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ContactInfo{" +
                "phone='" + phone + '\'' +
                ", formattedPhone='" + formattedPhone + '\'' +
                ", twitter='" + twitter + '\'' +
                ", facebookUsername='" + facebookUsername + '\'' +
                ", facebook='" + facebook + '\'' +
                ", facebookName='" + facebookName + '\'' +
                '}';
    }
}
