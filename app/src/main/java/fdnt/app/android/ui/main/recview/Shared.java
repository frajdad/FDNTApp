package fdnt.app.android.ui.main.recview;
// wspólne metody dla obu adapterów

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

import fdnt.app.android.Dane;
import fdnt.app.android.R;
import fdnt.app.android.post.MailSender;

public class Shared {
    
    //List contains management and office staff informations
    protected static ArrayList<Person> staff = new ArrayList<Person> ();
    
    public static ArrayList<Person> getStaffWithGivenAssignment(Assignment assignment){
        ArrayList<Person> output = new ArrayList<Person>();
        for(Person p : staff)
            if(p.assignment == assignment) output.add (p);
        return output;
    }
    
    public static void loadStaffDataFromFile(Context context) {
        InputStream stream = context.getResources ().openRawResource (R.raw.staff);
        Scanner scanner = new Scanner (stream);
        String json = "";
        while(scanner.hasNext ()) json += scanner.next ();
        Person[] temp = new Gson().fromJson (json, Person[].class);
        for(Person p : temp) {
            p.role = p.role.replace ('$', ' ');
            staff.add (p);
        }
        scanner.close ();
        try {
            stream.close ();
        } catch (IOException e) {
            e.printStackTrace ();
        }
    }
    
    public static void sendMail(String mail, Context context) {
        if(Dane.ifLoggedToPost()) {
            Intent intent = new Intent(Dane.this_activity, MailSender.class);
            intent.putExtra("to", mail);
            Dane.this_activity.startActivity(intent);
        }
        else {
            String mailURL = "mailto:" + mail;
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(mailURL));
            context.startActivity(intent);
        }
    }
}
