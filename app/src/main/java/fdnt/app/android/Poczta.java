package fdnt.app.android;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Store;

import static fdnt.app.android.Dane.pop3Session;


public class Poczta extends AppCompatActivity implements View.OnClickListener {

    //Send button
    private Button buttonWriteNew;

    private String email = "lukasz.kaminski@dzielo.pl";
    private String pass = "a02b100x3";

    //Inbox positions
    private ListView list ;
    private ArrayAdapter<String> adapter ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poczta);


        //Initializing the views
        buttonWriteNew = (Button) findViewById(R.id.buttonWriteNew);
        list = (ListView) findViewById(R.id.listInbox);

     //   ArrayList<String> carL = check();
        //adapter = new ArrayAdapter<String>(this, R.layout.email_list_item,  R.id.item_subject, carL);
       // list.setAdapter(adapter);
      //  View item = getLayoutInflater().inflate(R.layout.email_list_item, list);



        //Adding click listener
        buttonWriteNew.setOnClickListener(this);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println(parent.getItemAtPosition(position));
            }
        });
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, ONas.class);
        startActivity(intent);
    }

    private static ArrayList<String> check()
    {
        ArrayList<String> result = new ArrayList<String>();

        try {
            Store store = pop3Session.getStore("pop3");
            store.connect();

            //create the folder object and open it
            Folder emailFolder = store.getFolder("INBOX");
            emailFolder.open(Folder.READ_ONLY);

            // retrieve the messages from the folder in an array and print it
            Message[] messages = emailFolder.getMessages();
            for (int i = 0, n = messages.length; i < 5; i++) {
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
