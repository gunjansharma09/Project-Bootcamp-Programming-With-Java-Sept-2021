package com.bootcampproject.bootcamp_project.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
@Entity
@Data
public class Orders extends AuditEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private double amountPaid;
    private Date dateCreation;
    private String paymentMethod;
    private String customerAddressCity;
    private String customerAddressState;
    private String customerAddressCountry;
    private String customerAddressAddressLine;
    private int customerAddressZipCode;
    private String customerAddressLabel;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "customerUserId")
    private Customer customerId;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "Order_OrderProduct",
            joinColumns = @JoinColumn(name = "order_id",referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "orderProduct_id",referencedColumnName = "id"))
    private List<OrderProduct> orderProductId;
}
