package edu.temple.bookshelf;

import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class BookshelfAdapter extends BaseAdapter{

    Context context;
    BookList books;

    public BookshelfAdapter (Context context, BookList books){
        this.context = context;
        this.books = books;
    }

    @Override
    public int getCount() {
        return books.size();
    }

    @Override
    public Object getItem(int position) {
        return books.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView titleTextView;
        TextView authorTextView;

        if(!(convertView instanceof LinearLayout)){
            convertView = LayoutInflater.from(context).inflate(R.layout.bookshelf_adapter, parent, false);
        }

        titleTextView = convertView.findViewById(R.id.titleTextView);
        authorTextView = convertView.findViewById(R.id.authorTextView);

        titleTextView.setText(((Book) getItem(position)).getTitle());
        authorTextView.setText(((Book) getItem(position)).getAuthor());

        return convertView;
    }
}