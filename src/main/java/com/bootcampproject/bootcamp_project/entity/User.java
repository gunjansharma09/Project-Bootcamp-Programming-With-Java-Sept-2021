package com.bootcampproject.bootcamp_project.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User extends AuditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    @NotNull
    @Email(regexp = ".+@.+\\..+")
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
   @NotBlank(message = "New password is mandatory")
    private String password;
    private Boolean isDeleted;

    private Boolean isActive;
    private Boolean isExpired;
    private Boolean isLocked;
    private Integer invalidAttemptCount;

    @OneToOne(mappedBy = "user")
    @PrimaryKeyJoinColumn(name = "user_id")
    private Seller seller;
    @OneToOne(mappedBy = "user")
    @PrimaryKeyJoinColumn(name = "user_id")
    private Customer customer;

    @OneToMany
    List<Address> addresses;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "userId",referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "roleID",referencedColumnName = "id"))
    private List<Role> roles;

    // private String role;



}

