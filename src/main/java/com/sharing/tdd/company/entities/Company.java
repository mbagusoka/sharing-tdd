package com.sharing.tdd.company.entities;

import com.sharing.tdd.user.entities.User;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "companies")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Company {

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private String name;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Company company = (Company) o;

        return Objects.equals(id, company.id);
    }

    @Override
    public int hashCode() {
        return 56842787;
    }
}
