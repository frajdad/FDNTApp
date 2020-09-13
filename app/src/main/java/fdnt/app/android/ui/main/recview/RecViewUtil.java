package fdnt.app.android.ui.main.recview;
// wspólne metody dla obu adapterów

import android.content.Context;

import com.google.gson.Gson;

import org.json.JSONArray;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

import fdnt.app.android.R;
import fdnt.app.android.utils.JsonConvert;

/**
 * Class contains static methods which are shared, by package.
 */
public class RecViewUtil {

    /**List of People, who are in office or management.*/
    protected static ArrayList<Person> staff = new ArrayList<Person>();

    /**
     * Method returns List of People, who have given assignment.
     * @param assignment Specifies assignment, which is filter.
     * @return List of filtered People, whose assignment is same as parameter.
     * */
    public static ArrayList<Person> getStaffWithGivenAssignment(Assignment assignment) {
        ArrayList<Person> output = new ArrayList<Person>();
        for(Person p : staff)
            if(p.assignment == assignment) output.add (p);
        return output;
    }
    
    /**
     * Method loads List of People from staff.json and puts it in staff List.
     * @param context Context, which allows for method to read resources.
     * */
    public static void loadStaffDataFromFile(Context context) {
        staff = new ArrayList<Person> ();
        InputStream stream = context.getResources().openRawResource (R.raw.staff);
        Scanner scanner = new Scanner(stream);
        String json = "";
        while(scanner.hasNext ()) json += scanner.next ();
        //Person[] temp = new Gson().fromJson (json, Person[].class);
        Person[] temp = new Person[0];
        try {
            temp = new Gson().fromJson(json, Person[].class);
        } catch (Exception e) {
            e.printStackTrace ();
        }
        for(Person p : temp) {
            //p.role = p.role.replace ('$', ' ');
            staff.add (p);
        }
        scanner.close ();
        try {
            stream.close ();
        } catch (IOException e) {
            e.printStackTrace ();
        }
    }

    public static ArrayList<Article> deserializeArticleJSON(Context context, int path) {
        ArrayList<Article> arr = new ArrayList<Article>();
        InputStream stream = context.getResources().openRawResource(path);
        Scanner scanner = new Scanner(stream);
        String json = "";

        while(scanner.hasNext()) {
            json += scanner.next();
        }

        Article[] temp = new Gson().fromJson(json, Article[].class);

        for(Article a : temp) {
            a.header = a.header.replace('$',' ');
            a.content = a.content.replace('$',' ');
            arr.add(a);
        }
        scanner.close();

        try {
            stream.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return arr;
    }
}
