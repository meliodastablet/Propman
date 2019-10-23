package com.example.senior1;


import android.os.Parcel;
import android.os.Parcelable;

public class ParcelableProperty implements Parcelable {
    private Property property;

    public Property getProperty() {
        return property;
    }

    public ParcelableProperty(Property property) {
        super();
        this.property = property;
    }

    // Writing to Parcel and Reading from parcel must be done in the same order
    // This function reads from the parcel and it is used by CREATOR
    public ParcelableProperty(Parcel parcel) {
        property = new Property();
        property.setId(parcel.readInt());
        property.setTitle(parcel.readString());
        property.setPrice(parcel.readString());
        property.setOdasalon(parcel.readString());
        property.setM2(parcel.readInt());
        property.setDescription(parcel.readString());
        property.setAddress(parcel.readString());
        property.setImage(parcel.readString());



    }


    /*
     * you can use hashCode() here. Ignoring it
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /*
     * Actual object Serialization/flattening happens here. You need to
     * individually Parcel each property of your object.
     */
    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(property.getId());
        parcel.writeString(property.getTitle());
        parcel.writeString(property.getPrice());
        parcel.writeString(property.getOdasalon());
        parcel.writeInt(property.getM2());
        parcel.writeString(property.getDescription());
        parcel.writeString(property.getAddress());
        parcel.writeString(property.getImage());

    }

    /*
     * Parcelable interface must also have a static field called CREATOR,
     * which is an object implementing the Parcelable.Creator interface.
     * Used to un-marshal or de-serialize object from Parcel.
     */
    public static final Parcelable.Creator<ParcelableProperty> CREATOR =
            new Parcelable.Creator<ParcelableProperty>() {
                public ParcelableProperty createFromParcel(Parcel parcel) {
                    return new ParcelableProperty(parcel);
                }

                public ParcelableProperty[] newArray(int size) {
                    return new ParcelableProperty[size];
                }
            };

}
