package fdnt.app.android;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Store;

import fdnt.app.android.ui.main.DlaDarczyncy;

import static fdnt.app.android.Dane.pop3Session;


public class Poczta extends Fragment implements View.OnClickListener {

    public static DlaDarczyncy newInstance() {
        return new DlaDarczyncy();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_poczta, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        buttonWriteNew = getActivity().findViewById(R.id.buttonWriteNew);
        loadEmails(getEmails("INBOX", 5));
    }

    private void loadEmails(ArrayList<String> emails) {
        list = getActivity().findViewById(R.id.listInbox);

        adapter = new ArrayAdapter<String>(getActivity(), R.layout.email_list_item, emails);
        list.setAdapter(adapter);

        //Adding click listener
        buttonWriteNew.setOnClickListener(this);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println(parent.getItemAtPosition(position));
            }
        });
    }

    //Send button
    private Button buttonWriteNew;

    //Inbox positions
    private ListView list ;
    private ArrayAdapter<String> adapter ;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonWriteNew:
                Intent intent = new Intent(getActivity(), ONas.class);
                startActivity(intent);
                break;
            case R.id.odebrane:
                loadEmails(getEmails("INBOX", 5));
                break;
            case R.id.robocze:
                loadEmails(getEmails("DRAFTS", 5));
                break;
            case R.id.wyslane:
                loadEmails(getEmails("SENT", 5));
                break;
        }
    }

    private static ArrayList<String> getEmails(String box, int number)
    {
        ArrayList<String> result = new ArrayList<String>();
        try {
            PoczaLogowanie.openSessions();

            Store store = pop3Session.getStore("pop3");
            store.connect();

            //create the folder object and open it
            Folder emailFolder = store.getFolder(box);
            emailFolder.open(Folder.READ_ONLY);

            // retrieve the messages from the folder in an array and print it
            Message[] messages = emailFolder.getMessages();
            for (int i = 0, n = messages.length; i < number; i++) {
                Message message = messages[i];
                result.add(message.getSubject());
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

        return result;
    }
}
