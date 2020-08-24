package fdnt.app.android.post;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMultipart;

import fdnt.app.android.GlobalUtil;

public class AsyncMailLoad {
    public static List<MailItem> ITEMS = new ArrayList<MailItem>();
    public static Map<String, MailItem> ITEM_MAP = new HashMap<String, MailItem>();

    private static RecyclerView view;

    private static void addItem(MailItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
        if (view != null) {
            view.post(new Runnable()
            {
                @Override
                public void run() {
                    view.getAdapter().notifyItemInserted(ITEMS.size()-1);
                }
            });
        }
    }

    public static void setAdapterChanger(RecyclerView view) {
        AsyncMailLoad.view = view;
        view.post(new Runnable()
        {
            @Override
            public void run() {
                AsyncMailLoad.view.getAdapter().notifyDataSetChanged();
            }
        });
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

    public static void getEmails(String box, int number, Activity context)
    {
        try {
            SharedPreferences data = context.getSharedPreferences("post", Context.MODE_PRIVATE);
            String pass = data.getString("pass", "");
            String email = GlobalUtil.userEmail();
         //   MailLogging.openSessions(email, pass);

            Session session = Session.getDefaultInstance(new Properties());
            Store store = session.getStore("imaps");
            store.connect("mail.dzielo.pl", 993, email, pass);

            Folder[] fs = store.getDefaultFolder().list();

            for(Folder f: fs) {
                Log.d("boxes", f.getName());
            }


            //create the folder object and open it
            Folder emailFolder = store.getFolder(box);
            emailFolder.open(Folder.READ_ONLY);

            // retrieve the messages from the folder in an array and print it
            Message[] messages = emailFolder.getMessages();
            for (int i = messages.length - 1; i >= 0 && messages.length - i <= number ; i--) {
                Message message = messages[i];
                String personal = ((InternetAddress) message.getFrom()[0]).getPersonal();
                if (personal == null) {
                    personal = message.getFrom()[0].toString();
                }

                addItem(createItem(i, getTextFromMessage(message),
                        message.getSubject(),
                        personal,
                        formatDate(message.getSentDate())));
                System.out.println("item " + i);
            }

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
        return result.replaceAll("\n", "<br>");
    }

    static String formatDate(Date d) {
        return new SimpleDateFormat("dd.MM").format(d);
    }
}
