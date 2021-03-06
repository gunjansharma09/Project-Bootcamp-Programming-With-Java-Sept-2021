package com.bootcampproject.bootcamp_project.entity;

import com.bootcampproject.bootcamp_project.enums.Status;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
public class OrderStatus extends AuditEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "orderProductId")
    private OrderProduct orderProduct;

    @Enumerated(EnumType.STRING)
    private Status FROM_STATUS;
    @Enumerated(EnumType.STRING)
    private Status TO_STATUS;
    private String transitionNotesComment;
}
