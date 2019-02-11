package tsp.rest.json.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import tsp.database.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {

    private Integer id;
    private String login;
    private String password;
    private String firstName;
    private String lastName;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+4")
    private Date registrationDate;
    private Integer role;

    public static UserResponse transform(UserEntity entity) {
        return new UserResponse(
                entity.getId(),
                entity.getLogin(),
                entity.getPassword(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getRegDate(),
                entity.getRole()
        );
    }
}
