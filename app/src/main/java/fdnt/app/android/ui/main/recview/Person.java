package fdnt.app.android.ui.main.recview;

/*
* Class which represents the members of management or office staff
* */
public class Person {

	public String role;
	public String email;
	public String phone;
	public int imageID;
	public Assignment assignment;
	
	public Person(){}
	
	public Person (String role, String email, String phone, int imageID, Assignment assignment){
		this.role = role;
		this.email = email;
		this.phone = phone;
		this.imageID = imageID;
		this.assignment = assignment;
	}
	
}
