package edu.temple.bookshelf;

import android.os.Parcel;
import android.os.Parcelable;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class BookList implements Parcelable {

    private ArrayList<Book> books;

    public BookList(){
        books = new ArrayList<>();
    }
    protected BookList(Parcel in) {
        books = in.createTypedArrayList(Book.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(books);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<BookList> CREATOR = new Creator<BookList>() {
        @Override
        public BookList createFromParcel(Parcel in) {
            return new BookList(in);
        }

        @Override
        public BookList[] newArray(int size) {
            return new BookList[size];
        }
    };

    public void add(Book book){
        books.add(book);
    }

    public Book get(int pos){
        return books.get(pos);
    }

    public int size(){ return books.size(); }
}
