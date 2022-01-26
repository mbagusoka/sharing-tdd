package com.sharing.tdd.company.usecases;

import com.sharing.tdd.company.entities.CompanyRepository;
import com.sharing.tdd.user.entities.User;
import com.sharing.tdd.user.entities.UserRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
public class CreateCompanyUseCase {

    private final UserRepository userRepository;

    private final CompanyRepository companyRepository;

    public void createCompany(@NonNull CreateCompanyCmd cmd) {
        User user = getUser(cmd);

        companyRepository.findByName(cmd.getCompanyName()).ifPresent(company -> {
            throw new EntityExistsException(String.format("Company %s is already exist", cmd.getCompanyName()));
        });

        companyRepository.save(cmd.toEntity(user));
    }

    private User getUser(CreateCompanyCmd cmd) {
        return userRepository.findById(cmd.getUserId())
            .orElseThrow(() -> new EntityNotFoundException(
                String.format("User with Id %s is not found", cmd.getUserId()))
            );
    }
}
