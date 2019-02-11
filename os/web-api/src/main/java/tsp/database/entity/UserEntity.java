package tsp.database.entity;


import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;
import tsp.rest.json.request.user.UserCreateJson;
import tsp.rest.json.response.UserResponse;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Entity
@Table(name = "users")
@Setter
@NoArgsConstructor
public class UserEntity {

    private Integer id;
    private String login;
    private String password;
    private String firstName;
    private String lastName;
    private Date regDate;
    private Integer role;

    public UserEntity(String login, String password, String firstName, String lastName, Integer role) {
        this.login = login;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
    }

    public UserEntity(Integer id, String login, String firstName, String lastName, Date regDate, Integer role) {
        this.id = id;
        this.login = login;
        this.firstName = firstName;
        this.lastName = lastName;
        this.regDate = regDate;
        this.role = role;
    }


    public static UserEntity transform(UserCreateJson request) {
        return new UserEntity(
                request.getLogin(),
                request.getPassword(),
                request.getFirstName(),
                request.getLastName(),
                request.getRole()
        );
    }

    public static UserEntity transform(UserResponse request) {
        return new UserEntity(
                request.getId(),
                request.getLogin(),
                request.getFirstName(),
                request.getLastName(),
                request.getRegistrationDate(),
                request.getRole()
        );
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    public Integer getId() {
        return id;
    }

    @Column(name = "login")
    public String getLogin() {
        return login;
    }

    @Column(name = "password")
    public String getPassword() {
        return password;
    }

    @Column(name = "first_name")
    public String getFirstName() {
        return firstName;
    }

    @Column(name = "last_name")
    public String getLastName() {
        return lastName;
    }

    @Column(name = "reg_date")
    @Generated(value = GenerationTime.INSERT)
    @Temporal(TemporalType.TIMESTAMP)
    public Date getRegDate() {
        return regDate;
    }

    @Column(name = "role")
    public Integer getRole() {
        return role;
    }
}
