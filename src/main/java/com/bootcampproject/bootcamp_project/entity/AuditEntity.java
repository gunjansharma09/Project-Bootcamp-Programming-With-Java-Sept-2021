package com.bootcampproject.bootcamp_project.entity;

import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;
import java.util.Date;


@MappedSuperclass //  jis kisi class ne auditEntity ko extend kia h .. us class me ye 4ro field isi annotation se aaengi.. ectend krne pe b ni aaegi agar ye annotatoion use ni krenege to.
@Setter
@EntityListeners(AuditingEntityListener.class) //help in auditing .
public class AuditEntity {
    @NotNull
    @CreatedDate
    private Date dateCreated;

    @LastModifiedDate
    private Date lastUpdated;
    @NotNull
    private String createdBy;
    private String updatedBy;
}
