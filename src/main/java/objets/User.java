package objets;

 public class User {

    private String login;
    private String password;
    private Filiere filiere;

    public User( String login, String password, String filiere) {

        this.setLogin( login );
        this.setPassword( password );
        this.setFiliere( filiere );
    }
    
    public String getLogin() {
        return login;
    }
    
    public void setLogin(String login) {
        this.login = login;
    }
    
    private void setFiliere(String filiere) {
		this.filiere = new Filiere (filiere); 
	}

	public Filiere getFiliere() {
        return filiere;
    }
	
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User[login=" + login + ", password=" + 
               password + ", filiere=" + filiere + "]";
    }
    
}