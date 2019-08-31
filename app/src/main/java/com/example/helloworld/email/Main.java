package com.example.helloworld.email;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.helloworld.R;

import java.util.ArrayList;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Store;

import androidx.appcompat.app.AppCompatActivity;

public class Main extends AppCompatActivity implements View.OnClickListener {

    //Send button
    private Button buttonWriteNew;

    //Inbox positions
    private ListView list ;
    private ArrayAdapter<String> adapter ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.email_main);

        //Initializing the views
        buttonWriteNew = (Button) findViewById(R.id.buttonWriteNew);
        list = (ListView) findViewById(R.id.listInbox);

        ArrayList<String> carL = check();
        adapter = new ArrayAdapter<String>(this, R.layout.email_list_item, carL);
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

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, Write.class);
        startActivity(intent);
    }

    private static ArrayList<String> check()
    {
        ArrayList<String> result = new ArrayList<String>();

        try {
            Store store = Data.pop3Session.getStore("pop3");
            store.connect();

            //create the folder object and open it
            Folder emailFolder = store.getFolder("INBOX");
            emailFolder.open(Folder.READ_ONLY);

            // retrieve the messages from the folder in an array and print it
            Message[] messages = emailFolder.getMessages();
            for (int i = 0, n = messages.length; i < n; i++) {
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
