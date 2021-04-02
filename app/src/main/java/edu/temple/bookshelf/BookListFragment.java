package edu.temple.bookshelf;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * A fragment representing a list of Books.
 */
public class BookListFragment extends Fragment {

    private static final String BOOK_LIST = "booklist";
    private BookList books;

    BookSelectedInterface parentActivity;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public BookListFragment() {
    }

    public static BookListFragment newInstance(BookList books) {
        BookListFragment fragment = new BookListFragment();
        Bundle args = new Bundle();
        args.putParcelable(BOOK_LIST, books);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            books = getArguments().getParcelable(BOOK_LIST);
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof BookSelectedInterface){
            parentActivity = (BookSelectedInterface) context;
        } else {
            throw new RuntimeException();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ListView listView = (ListView)inflater.inflate(R.layout.fragment_book_list, container, false);
//        ListView listView = v.findViewById(R.id.listView);

        listView.setAdapter(new BookshelfAdapter(getContext(), books));

        listView.setOnItemClickListener((parent, view, position, id) -> {
            parentActivity.bookSelected(position);
        });
        return listView;
    }
    interface BookSelectedInterface{
        void bookSelected(int index);
    }
}