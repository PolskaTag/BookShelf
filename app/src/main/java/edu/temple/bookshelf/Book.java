package edu.temple.bookshelf;

import android.os.Parcel;
import android.os.Parcelable;

public class Book implements Parcelable {
    private String title;
    private String author;
    private int id;
    private String coverURL;
    private int duration;

    // Default book
    public Book(){}

    public Book(String title, String author){
        this.title = title;
        this.author = author;
    }


    public Book(int id, String title, String author, String coverURL){
        this.id = id;
        this.title = title;
        this.author = author;
        this.coverURL = coverURL;
    }

    public Book(int id, String title, String author, String coverURL, int duration){
        this.id = id;
        this.title = title;
        this.author = author;
        this.coverURL = coverURL;
        this.duration = duration;
    }

    protected Book(Parcel in){
        title = in.readString();
        author = in.readString();
        id = in.readInt();
        coverURL = in.readString();
    }

    public static final Creator<Book> CREATOR = new Creator<Book>(){
        @Override
        public Book createFromParcel(Parcel source) {
            return new Book(source);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    public int getId() { return id; }

    public String getTitle(){
        return title;
    }

    public String getAuthor(){
        return author;
    }

    public String getCoverImage(){ return coverURL; }

    public int getDuration(){ return duration; }

    @Override
    public int describeContents(){return 0;}

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(author);
        dest.writeString(coverURL);
        dest.writeInt(id);
        dest.writeInt(duration);
    }
}
