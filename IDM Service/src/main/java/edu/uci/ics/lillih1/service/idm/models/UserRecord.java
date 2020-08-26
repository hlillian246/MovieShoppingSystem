package edu.uci.ics.lillih1.service.idm.models;

public class UserRecord {
    private int id;
    private String email;
    private String plevel;

    public UserRecord(int id, String email, String plevel) {
        this.id = id;
        this.email = email;
        this.plevel = plevel;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPlevel() {
        return plevel;
    }

    public void setPlevel(String plevel) {
        this.plevel = plevel;
    }

    @Override
    public String toString() {
        return "id: " + id + ", email: " + email + ", plevel: " + plevel;
    }
}
