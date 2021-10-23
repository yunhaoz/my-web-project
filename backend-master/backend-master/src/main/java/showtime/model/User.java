package showtime.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import showtime.service.UserDeserializerService;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@JsonDeserialize(using = UserDeserializerService.class)
@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    @NotNull
    private int id;

    @Column(name = "email", nullable = false, unique = true)
    @NotNull
    private String email;

    @Column(name = "username", nullable = false, unique = true)
    @NotNull
    private String username;

    @Column(name = "password", nullable = false, unique = true)
    @NotNull
    private String password;

    @Column(name = "fname", nullable = false)
    @NotNull
    private String fname;

    @Column(name = "lname", nullable = false)
    @NotNull
    private String lname;

    @Column(name = "latitude", nullable = false, unique = true)
    @NotNull
    private double latitude;

    @Column(name = "longitude", nullable = false, unique = true)
    @NotNull
    private double longitude;

    public User() {

    }

    @Override
    public String toString() {
        return String.format("{id=%d,email=%s,username=%s,password=%s" +
                ",fname=%s,lname=%s,latitude=%f,longitude=%f}",
                id, email, username, password, fname, lname, latitude, longitude);
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String firstName) {
        this.fname = firstName;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lastName) {
        this.lname = lastName;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
