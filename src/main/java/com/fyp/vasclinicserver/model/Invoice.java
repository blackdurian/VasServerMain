package com.fyp.vasclinicserver.model;

import com.fyp.vasclinicserver.model.audit.UserBaseEntity;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Set;

import static javax.persistence.FetchType.LAZY;

@Entity
public class Invoice extends UserBaseEntity {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private String id;
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    private User customer;
    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "appId", referencedColumnName = "id")
    private Appointment appointment;
    @ManyToMany
    @JoinTable(
            name = "invoice_injected_vaccines",
            joinColumns = @JoinColumn(name = "vaccines_id"),
            inverseJoinColumns = @JoinColumn(name = "app_id"))
    private Set<Vaccine> injectedVaccines;
    private Double price;
    private String remark;

    public Invoice() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getCustomer() {
        return customer;
    }

    public void setCustomer(User customer) {
        this.customer = customer;
    }

    public Appointment getAppointment() {
        return appointment;
    }

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }

    public Set<Vaccine> getInjectedVaccines() {
        return injectedVaccines;
    }

    public void setInjectedVaccines(Set<Vaccine> injectedVaccines) {
        this.injectedVaccines = injectedVaccines;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
