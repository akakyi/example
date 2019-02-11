package tsp.database.entity;

import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "authTokens")
public class AuthTokenEntity {

    @Setter
    private Integer id;

    @Setter
    private String token;

    @Setter
    private String userName;

    public AuthTokenEntity(String token, String userName) {
        this.token = token;
        this.userName = userName;
    }

    public AuthTokenEntity() {

    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    public Integer getId() {
        return id;
    }

    @Column(name = "token")
    public String getToken() {
        return token;
    }

    @Column(name = "userName")
    public String getUserName() {
        return userName;
    }
}
