package fdnt.app.android.post;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Store;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMultipart;

import fdnt.app.android.Dane;
import fdnt.app.android.R;

import static fdnt.app.android.Dane.pop3Session;

/**
 * A fragment representing a list of Items.
 */
public class PostItemFragment extends Fragment {
    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;

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

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }

            getEmails("INBOX", COUNT);
            recyclerView.setAdapter(new MyPostItemRecyclerViewAdapter(ITEMS));
        }
        return view;
    }

    /*
     * Rzeczy poświęcone ładowaniu maili
     */

    public final List<MailItem> ITEMS = new ArrayList<MailItem>();

    public final Map<String, MailItem> ITEM_MAP = new HashMap<String, MailItem>();

    private static final int COUNT = 20;


    private void addItem(MailItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static MailItem createItem(int position, String content, String subject, String sender, String date) {
        return new MailItem(String.valueOf(position), content, subject, sender, date);
    }

    public static class MailItem {
        public final String id;
        public final String content;
        public final String subject;
        public final String sender;
        public final String date;

        public MailItem(String id, String content, String subject, String sender, String date) {
            this.id = id;
            this.content = content;
            this.subject = subject;
            this.sender = sender;
            this.date = date;
        }
    }

    private void getEmails(String box, int number)
    {
        try {
            SharedPreferences data = getActivity().getSharedPreferences("post", Context.MODE_PRIVATE);
            String pass = data.getString("pass", "");
            Log.d("pref", pass);
            String email = Dane.userEmail();
            MailLogging.openSessions(email, pass);

            Store store = pop3Session.getStore("pop3");
            store.connect();

            //create the folder object and open it
            Folder emailFolder = store.getFolder(box);
            emailFolder.open(Folder.READ_ONLY);

            // retrieve the messages from the folder in an array and print it
            Message[] messages = emailFolder.getMessages();
            for (int i = messages.length - 1; i >= 0 && messages.length - i <= number ; i--) {
                Message message = messages[i];
                addItem(createItem(i, getTextFromMessage(message),
                        message.getSubject(),
                        ((InternetAddress) message.getFrom()[0]).getPersonal(),
                        formatDate(message.getSentDate())));
            }

            //close the store and folder objects
            emailFolder.close(false);
            store.close();

        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getTextFromMessage(Message message) throws MessagingException, IOException {
        String result = "";
        if (message.isMimeType("text/*")) {
            result = message.getContent().toString();
        } else if (message.isMimeType("multipart/*")) {
            MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
            result = getTextFromMimeMultipart(mimeMultipart);
        }
        return result;
    }

    private static String getTextFromMimeMultipart(
            MimeMultipart mimeMultipart)  throws MessagingException, IOException {
        String result = "";
        int count = mimeMultipart.getCount();
        for (int i = 0; i < count; i++) {
            BodyPart bodyPart = mimeMultipart.getBodyPart(i);
            if (bodyPart.isMimeType("text/plain")) {
                result = result + "\n" + bodyPart.getContent();
                break;
            } else if (bodyPart.isMimeType("text/html")) {
                String html = (String) bodyPart.getContent();
                result = result + "\n" + html;
            } else if (bodyPart.getContent() instanceof MimeMultipart){
                result = result + getTextFromMimeMultipart((MimeMultipart)bodyPart.getContent());
            }
        }
        return result;
    }

    static String formatDate(Date d) {
        return new SimpleDateFormat("dd.MM").format(d);
    }
}