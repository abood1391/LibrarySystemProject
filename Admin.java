package entities;

public class Admin extends Person {
    private String password;

    public Admin(String name, String password) {
        super(name);
        this.password = password;
    }

    public boolean login(String inputPassword) {
        return password.equals(inputPassword);
    }
}
