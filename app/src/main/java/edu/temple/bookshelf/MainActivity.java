package edu.temple.bookshelf;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements BookListFragment.BookSelectedInterface {

    FragmentManager fragmentManager;
    boolean twoPane;
    BookDetailsFragment bookDetailsFragment;
    BookList myBooks = new BookList();
    Book selectedBook;
    JSONArray bookArray;
    JSONObject bookObject;

//    String[] titles = {
//            "12 Rules for Life", "The Adventures of Huckleberry Finn",
//            "Tom Sawyer Abroad", "Republic", "Meditations",
//            "Mistborn: Shadows of Self", "Why Fish Don't Exist"
//    };
//    String[] authors = {
//            "Jordan Peterson", "Mark Twain", "Mark Twain", "Plato",
//            "Socrates", "Brandon Sanderson", "Lulu Miller"
//    };

    private final String SELECTED_BOOK = "selectedBook";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Make a bundle and grab the data from Intent
        Bundle bundle = getIntent().getExtras();
        String bookArrayString = bundle.getString("jsonArray");
        try {
            JSONArray bookArray = new JSONArray(bookArrayString);

            // For each Book in bookArray, make a book
            for(int i = 0; i < bookArray.length(); i++){
                bookObject = bookArray.getJSONObject(i);
                myBooks.add(new Book(
                        bookObject.getInt("id"),
                        bookObject.getString("title"),
                        bookObject.getString("author"),
                        bookObject.getString("cover_url")
                ));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Test books
//        for(int i = 0; i < titles.length; i++){
//            myBooks.add(new Book(titles[i], authors[i]));
//        }

        // Get selected book if not null
        if(savedInstanceState != null){
            selectedBook = savedInstanceState.getParcelable(SELECTED_BOOK);
        }

        // our flag for 2 containers
        twoPane = findViewById(R.id.container2) != null;

        fragmentManager = getSupportFragmentManager();

        Fragment fragment;
        fragment = fragmentManager.findFragmentById(R.id.container1);

        // Display container 1 Only
        if(fragment instanceof BookDetailsFragment){
            fragmentManager.popBackStack();
        } else if (!(fragment instanceof BookListFragment)){
            fragmentManager.beginTransaction().add(R.id.container1, BookListFragment.newInstance(myBooks)).commit();
        }

        // if not true make another bookdetailsFrag
        // else we came back around and am changing book
        if(selectedBook == null){
            bookDetailsFragment = new BookDetailsFragment();
        } else {
            bookDetailsFragment = BookDetailsFragment.newInstance(selectedBook);
        }

        // If we have two containers, replace container 2 with the selected bookDetails
        // Else we only have one container and we need to display the details while retaining info on the list
        if(twoPane){
            fragmentManager.beginTransaction().replace(R.id.container2, bookDetailsFragment).commit();
        } else if (selectedBook != null) {
            fragmentManager.beginTransaction().replace(R.id.container1, bookDetailsFragment).addToBackStack(null).commit();
        }
    }
    @Override
    public void bookSelected(int index){
        selectedBook = myBooks.get(index);
        if(twoPane){
            bookDetailsFragment.showBook(selectedBook);
        } else {
            fragmentManager.beginTransaction().replace(R.id.container1, BookDetailsFragment.newInstance(selectedBook)).addToBackStack(null).commit();
        }
    }
    @Override
    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putParcelable(SELECTED_BOOK, selectedBook);
    }

    // Fixes problem where selected book still showing after back button pressed
    @Override
    public void onBackPressed(){
        selectedBook = null;
        super.onBackPressed();
    }
}