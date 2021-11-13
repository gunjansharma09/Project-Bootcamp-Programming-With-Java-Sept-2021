package com.bootcampproject.bootcamp_project.entity;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User extends AuditEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    @NotNull
    @Email
    //(regexp = ".+@.+\\..+")
    // @Pattern(regexp="[A-Za-z0-9._%-+]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}")
    private String email;

    @NotNull
    @Size(min = 2, max = 16)
    private String firstName;
    private String middleName;

    @NotNull
    @Size(min = 2, max = 16)
    private String lastName;
    // @Pattern(regexp = "((?=.\\d)(?=.[a-z])(?=.[A-Z])(?=.[@#$%]).{8,64})", message = "Password must be 8 characters long")


    @NotNull
    @NotBlank(message = "password is mandatory")
    private String password;
    private Boolean isDeleted = false;

    private Boolean isActive = false;
    private Boolean isExpired = false;
    private Boolean isLocked = false;
    private Integer invalidAttemptCount = 0;

    private String forgotPasswordToken;
    private Long forgotPasswordGeneratedTokenAt;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn(name = "user_id")
    private Seller seller;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    //@PrimaryKeyJoinColumn(name = "user_id")
    private Customer customer;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "userId", referencedColumnName = "id") // 1 mapping table bn jati h one to many ya many to many se.. is annotation se vo mapping table ni bnegi

    List<Address> addresses;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "userId", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "roleID", referencedColumnName = "id"))
    private List<Role> roles;

}

