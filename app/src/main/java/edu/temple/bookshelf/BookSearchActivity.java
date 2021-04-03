package edu.temple.bookshelf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class BookSearchActivity extends AppCompatActivity {

    EditText urlEditText;
    Button searchButton;
    TextView outputTextView;

    Handler downloadHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            outputTextView.setText((String) msg.obj);
            return false;
        }
    });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_search);

        urlEditText = findViewById(R.id.urlEditText);
        searchButton = findViewById(R.id.searchButton);
        outputTextView = findViewById(R.id.outputTextView);

        searchButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                new Thread(){
                    @Override
                    public void run(){
                        try {
                            URL url = new URL(sanitizeURL(urlEditText.getText().toString()));
                            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));

                            Message msg = Message.obtain();
                            StringBuilder builder = new StringBuilder();
                            String tmpString;

                            while ((tmpString = reader.readLine()) != null) {
                                builder.append(tmpString);
                            }

                            msg.obj = builder.toString();
                            downloadHandler.sendMessage(msg);
                        } catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
        });
    }
    // Since we're hitting an API, we need to query it properly
    // This add the search term to the end of the query structure
    private String sanitizeURL(String url){
        if(url.startsWith("https://kamorris.com/lab/cis3515/search.php?term="))
            return url;
        else {
            urlEditText.setText("https://kamorris.com/lab/cis3515/search.php?term=" + url);
            return "https://kamorris.com/lab/cis3515/search.php?term=" + url;
        }
    }
}