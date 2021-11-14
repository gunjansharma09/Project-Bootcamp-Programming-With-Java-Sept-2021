package com.bootcampproject.bootcamp_project.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Seller extends AuditEntity implements Serializable {
    //	USER_ID
    @Id
    @Column(name = "user_id")
    private Long id;

    @MapsId
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")   //same name as id @Column
    private User user;
    @NotNull
    @NotBlank
    @Column(unique = true)
    private String gst;
    private String companyContact;
    private String companyName;


}

