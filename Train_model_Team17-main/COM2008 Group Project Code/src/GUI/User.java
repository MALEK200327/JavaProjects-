package GUI;

class User {
    private Integer userID;
    private String email;
    private String password;
    private String forename;
    private String surname;
    private int role;

    //Constructor
    public User(Integer userID, String email, String password, String forename, String surname, int role) {
        this.userID = userID;
        this.email = email;
        this.password = password;
        this.forename = forename;
        this.surname = surname;
        this.role = role;
    }

    public User(Integer userID, String email, String password, String forename, String surname) {
        this.userID = userID;
        this.email = email;
        this.password = password;
        this.forename = forename;
        this.surname = surname;
        this.role = 0;
    }

    public int getUserID() {
        return userID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getForename() {
        return forename;
    }

    public void setForename(String forename) {
        this.forename = forename;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getSurname() {
        return surname;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    





}