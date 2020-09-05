package fdnt.app.android.post;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import fdnt.app.android.GlobalUtil;
import fdnt.app.android.R;

public class PostItemFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private RecyclerView mailRecyclerView;
    private Thread currentMailLoader;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public PostItemFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static PostItemFragment newInstance(int columnCount) {
        PostItemFragment fragment = new PostItemFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);

        Context context = view.getContext();
        RecyclerView recyclerView = view.findViewById(R.id.list);

        if (mColumnCount <= 1) {
            recyclerView.setLayoutManager(new WrapContentLinearLayoutManager(context));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }

        mailRecyclerView = recyclerView;

        loadEmails("INBOX");

        view.findViewById(R.id.button_inbox).setOnClickListener(buttonListener);
        view.findViewById(R.id.button_spam).setOnClickListener(buttonListener);
        view.findViewById(R.id.button_sent).setOnClickListener(buttonListener);
        view.findViewById(R.id.button_trash).setOnClickListener(buttonListener);
        view.findViewById(R.id.button_drafts).setOnClickListener(buttonListener);

        checked = view.findViewById(R.id.button_inbox);
        check(checked);

        return view;
    }

    private View checked;
    private final View.OnClickListener buttonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            uncheck(checked);
            check(view);
            checked = view;

            switch (view.getId()) {
                case R.id.button_inbox:
                    loadEmails("INBOX");
                    break;
                case R.id.button_drafts:
                    loadEmails("DRAFTS");
                    break;
                case R.id.button_sent:
                    loadEmails("SENT");
                    break;
                case R.id.button_spam:
                    loadEmails("SPAM");
                    break;
                case R.id.button_trash:
                    loadEmails("TRASH");
                    break;
            }
        }
    };

    private void loadEmails(final String box) {
        if (currentMailLoader != null) {
            currentMailLoader.interrupt();
        }
        AsyncMailLoad.ITEMS.clear();

        currentMailLoader = new Thread(new Runnable() {
            public void run()
            {
                AsyncMailLoad.getEmails(box,
                        Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(GlobalUtil.this_activity)
                                .getString("max_emails", "20")),
                        GlobalUtil.this_activity);
            }
        });
        currentMailLoader.start();

    //    if (AsyncMailLoad.ITEMS.isEmpty()) {
      //      AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
        //    alertDialog.setMessage("Wystąpił błąd! \n \n Możliwe przyczyny: \n - brak połączenia z internetem lub zbyt słabe połączenie \n - brak maili w tej skrzynce");
          //  alertDialog.show();
       // }
     //   else {
            mailRecyclerView.setAdapter(new MyPostItemRecyclerViewAdapter(AsyncMailLoad.ITEMS));
            AsyncMailLoad.setAdapterChanger(mailRecyclerView);
       // }
    }

    private void check(View view) {
        Button button = (Button) view;
        button.setTextColor(getResources().getColor(R.color.yellowMain));
        button.setBackgroundResource(R.drawable.mail_button_checked);
    }

    private void uncheck(View view) {
        Button button = (Button) view;
        button.setTextColor(getResources().getColor(R.color.checkedText));
        button.setBackgroundResource(R.drawable.mail_button_unchecked);
    }

    // Tworzymy wraper dla tej klasy aby uniknąć błędów przy obsłudze RecyclerView przez kilka wątków
    // Patrz: https://stackoverflow.com/questions/31759171/recyclerview-and-java-lang-indexoutofboundsexception-inconsistency-detected-in
    private class WrapContentLinearLayoutManager extends LinearLayoutManager {
        public WrapContentLinearLayoutManager(Context context) {
            super(context);
        }

        @Override
        public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
            try {
                super.onLayoutChildren(recycler, state);
            } catch (IndexOutOfBoundsException e) {
            }
        }
    }
}