package edu.temple.bookshelf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import edu.temple.audiobookplayer.AudiobookService;

// 635 mb needs a handler 50min
public class MainActivity extends AppCompatActivity implements BookListFragment.BookSelectedInterface, ControlFragment.ControlInterface {

    FragmentManager fragmentManager;
    boolean twoPane;
    BookDetailsFragment bookDetailsFragment;
    BookList myBooks = new BookList();
    Book selectedBook;
    JSONArray bookArray;
    JSONObject bookObject;
    AudiobookService.BookProgress bookProgress;
    boolean firstPlay = true;

    private final String SELECTED_BOOK = "selectedBook";

    AudiobookService.MediaControlBinder mediaControlBinder;
    boolean isConnected;

    // Our handler
    Handler handler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
//            if(mediaControlBinder.isPlaying()) {
                Log.d("log", String.valueOf(msg.what));
                Message message = Message.obtain(msg);
                bookProgress = (AudiobookService.BookProgress) msg.obj;
//                update(bookProgress.getProgress());
                return true;
//            }
//            return false;
        }
    });

    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            mediaControlBinder = (AudiobookService.MediaControlBinder) binder;
            mediaControlBinder.setProgressHandler(handler);
            isConnected = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isConnected = false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent serviceIntent = new Intent(MainActivity.this, AudiobookService.class);
        serviceIntent.putExtra(SELECTED_BOOK, selectedBook);
        bindService(serviceIntent, serviceConnection, BIND_AUTO_CREATE);

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
                        bookObject.getString("cover_url"),
                        bookObject.getInt("duration")
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
        Fragment fragment2;
        fragment = fragmentManager.findFragmentById(R.id.contentFrame);
        fragment2 = fragmentManager.findFragmentById(R.id.controlFrame);

        // Display container 1 Only
        if(fragment instanceof BookDetailsFragment){
            fragmentManager.popBackStack();
        } else if (!(fragment instanceof BookListFragment)){
            fragmentManager.beginTransaction()
                    .add(R.id.contentFrame, BookListFragment.newInstance(myBooks))
                    .add(R.id.controlFrame, ControlFragment.newInstance(selectedBook))
                    .commit();
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
            fragmentManager.beginTransaction().replace(R.id.contentFrame, bookDetailsFragment).addToBackStack(null).commit();
        }
    }
    @Override
    public void bookSelected(int index){
        selectedBook = myBooks.get(index);
        firstPlay = true;
        if(twoPane){
            bookDetailsFragment.showBook(selectedBook);
        } else {
            fragmentManager.beginTransaction().replace(R.id.contentFrame, BookDetailsFragment.newInstance(selectedBook)).addToBackStack(null).commit();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(serviceConnection);
    }

    @Override
    public void play() {
        if(!mediaControlBinder.isPlaying() && selectedBook != null)
            if(!firstPlay){
                mediaControlBinder.play(bookProgress.getBookId(), bookProgress.getProgress());
            } else {
                mediaControlBinder.play(selectedBook.getId());
                firstPlay = false;
            }
    }

    @Override
    public void stop() {
        mediaControlBinder.stop();
    }

    @Override
    public void pause() {
        mediaControlBinder.pause();
    }

    @Override
    public void update(int prog) {
        mediaControlBinder.seekTo(prog);
    }

    @Override
    public String nowPlaying() {
        if(selectedBook != null) {
            return selectedBook.getTitle();
        } else {
            return "";
        }
    }
}