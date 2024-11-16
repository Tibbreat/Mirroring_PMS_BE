package sep490.g13.pms_be.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import sep490.g13.pms_be.utils.enums.RoleEnums;


import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User extends Auditable<String> {
    private String username;

    @JsonIgnore
    private String password;
    private String idCardNumber;
    private String fullName;
    private String email;
    private String address;
    private String phone;
    private Date birthday;
    private String imageLink;

    private Boolean isActive;

    private String contractType;

    @Enumerated(EnumType.STRING)
    private RoleEnums role;

    @OneToMany(mappedBy = "teacherId")
    @JsonIgnore
    private Set<ClassTeacher> classTeachers = new HashSet<>();
}
