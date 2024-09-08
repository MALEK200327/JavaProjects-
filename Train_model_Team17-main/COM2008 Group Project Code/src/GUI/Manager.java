package GUI;

class Manager extends Staff {

    public Manager(int userID, String email, String password, String forename, String surname, boolean isStaff) {
        super(userID, email, password, forename, surname, isStaff);
    }

    public void changeStaffStatus(Staff staff, boolean staffStatus) {
        staff.isStaff = staffStatus;
    }

}