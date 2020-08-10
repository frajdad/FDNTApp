package fdnt.app.android.ui.main.recview;

/**
* Class which represents the members of management or office staff
* */
public class Person {

	protected String role;
	protected String email;
	protected String phone;
	protected String imageName;
	protected Assignment assignment;
	
	public Person(){}
	
	public Person (String role, String email, String phone, String imageName, Assignment assignment){
		this.role = role;
		this.email = email;
		this.phone = phone;
		this.imageName = imageName;
		this.assignment = assignment;
	}
	
	public String getRole () {
		return role;
	}
	
	public void setRole (String role) {
		this.role = role;
	}
	
	public String getEmail () {
		return email;
	}
	
	public void setEmail (String email) {
		this.email = email;
	}
	
	public String getPhone () {
		return phone;
	}
	
	public void setPhone (String phone) {
		this.phone = phone;
	}
	
	public String getImageName () {
		return imageName;
	}
	
	public void setImageName (String imageName) {
		this.imageName = imageName;
	}
	
	public Assignment getAssignment () {
		return assignment;
	}
	
	public void setAssignment (Assignment assignment) {
		this.assignment = assignment;
	}
	
}
