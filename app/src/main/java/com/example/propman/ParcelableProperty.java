package com.example.propman;

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
        property.setTitle(parcel.readString());
        property.setPrice(parcel.readString());
        property.setRooms(parcel.readString());
        property.setarea(parcel.readString());
        property.setDescription(parcel.readString());
        property.setAddress(parcel.readString());
        property.setImagefilepath(parcel.readString());
        property.setUniquepropertyid(parcel.readString());

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
        parcel.writeString(property.getTitle());
        parcel.writeString(property.getPrice());
        parcel.writeString(property.getRooms());
        parcel.writeString(property.getarea());
        parcel.writeString(property.getDescription());
        parcel.writeString(property.getAddress());
        parcel.writeString(property.getImagefilepath());
        parcel.writeString(property.getUniquepropertyid());



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
