package it.jaschke.alexandria;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Class implementing Parcelable for saving book details
 */
public class ParcelableBookDetails implements Parcelable {

    String ean;
    String bookTitle;
    String bookSubTitle;
    String description;
    String authors;
    String imageUrl;
    String categories;

    public ParcelableBookDetails( String ean,String bookTitle, String bookSubTitle, String description, String authors, String imageUrl, String categories) {
        this.ean=ean;
        this.bookTitle = bookTitle;
        this.bookSubTitle = bookSubTitle;
        this.description = description;
        this.authors = authors;
        this.imageUrl = imageUrl;
        this.categories = categories;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ean);
        dest.writeString(bookTitle);
        dest.writeString(bookSubTitle);
        dest.writeString(description);
        dest.writeString(authors);
        dest.writeString(imageUrl);
        dest.writeString(categories);
    }

    private  ParcelableBookDetails(Parcel in)
    {
        ean=in.readString();
        bookTitle = in.readString();
        bookSubTitle = in. readString();
        description = in.readString();
        authors = in.readString();
        imageUrl = in.readString();
        categories = in.readString();
    }


    public static final Parcelable.Creator<ParcelableBookDetails> CREATOR = new Parcelable.Creator<ParcelableBookDetails>() {
        @Override
        public ParcelableBookDetails createFromParcel(Parcel source) {
            return new ParcelableBookDetails(source);
        }

        @Override
        public ParcelableBookDetails[] newArray(int size) {
            return new ParcelableBookDetails[size];
        }
    };
}
