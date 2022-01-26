package com.sharing.tdd.company.usecases;

import com.sharing.tdd.company.entities.Company;
import com.sharing.tdd.user.entities.User;
import lombok.Value;

@Value(staticConstructor = "valueOf")
public class CreateCompanyCmd {

    String companyName;

    long userId;

    public Company toEntity(User user) {
        Company company = new Company();
        company.setName(companyName);
        company.setUser(user);

        return company;
    }
}
