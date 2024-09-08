package GUI;

class Staff extends User {
    protected boolean isStaff;

    public Staff(int userID, String email, String password, String forename, String surname, boolean isStaff) {
        super(userID, email, password, forename, surname);
        this.isStaff = isStaff;
    }

    public boolean isStaff() {
        return isStaff;
    }



}