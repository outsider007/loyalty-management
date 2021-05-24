package ru.kuznetsov.loyaltymanagement.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "customers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "middle_name")
    private String middleName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "gender")
    private String gender;
    @Column(name = "birthday")
    private LocalDate birthday;
    @Column(name = "registered_date")
    private LocalDate registeredDate;

    @OneToOne
    @JoinColumn(name = "id", referencedColumnName = "customer_id")
    private Balance balance;
}
