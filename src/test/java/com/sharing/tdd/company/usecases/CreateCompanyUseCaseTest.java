package com.sharing.tdd.company.usecases;

import com.sharing.tdd.company.entities.Company;
import com.sharing.tdd.company.entities.CompanyRepository;
import com.sharing.tdd.user.entities.User;
import com.sharing.tdd.user.entities.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CreateCompanyUseCaseTest {

    private final CreateCompanyCmd cmd = CreateCompanyCmd.valueOf("dummy", 1L);

    @InjectMocks
    private CreateCompanyUseCase useCase;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CompanyRepository companyRepository;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void givenNullRequest_whenCreateCompany_shouldThrowException() {
        assertThatThrownBy(() -> useCase.createCompany(null))
            .isInstanceOf(NullPointerException.class)
            .hasMessage("cmd is marked non-null but is null");
    }

    @Test
    void givenRequest_whenCreateCompany_shouldCallUserRepository() {
        prepareAndExecute();

        verify(userRepository).findById(1L);
    }

    @Test
    void givenNonExistingUser_whenCreateCompany_shouldThrowException() {
        when(userRepository.findById(cmd.getUserId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.createCompany(cmd))
            .isInstanceOf(EntityNotFoundException.class)
            .hasMessage("User with Id 1 is not found");
    }

    @Test
    void givenRequest_whenCreateCompany_shouldCallCompanyRepository() {
        prepareAndExecute();

        verify(companyRepository).findByName(cmd.getCompanyName());
    }

    @Test
    void givenExistingCompany_whenCreateCompany_shouldThrowException() {
        stubCompany();
        stubUser();

        assertThatThrownBy(() -> useCase.createCompany(cmd))
            .isInstanceOf(EntityExistsException.class)
            .hasMessage("Company dummy is already exist");
    }

    @Test
    void givenRequest_whenCreateCompany_shouldCallSaveCompany() {
        prepareAndExecute();

        ArgumentCaptor<Company> captor = ArgumentCaptor.forClass(Company.class);
        verify(companyRepository).save(captor.capture());
        Company actual = captor.getValue();

        assertThat(actual.getName()).isEqualTo(cmd.getCompanyName());
        assertThat(actual.getUser()).isEqualTo(user);
    }

    private void prepareAndExecute() {
        stubUser();
        stubNotExistCompany();

        useCase.createCompany(cmd);
    }

    private void stubUser() {
        user = User.builder().name("user").build();
        when(userRepository.findById(cmd.getUserId())).thenReturn(Optional.of(user));
    }

    private void stubCompany() {
        Company injected = Company.builder().build();
        when(companyRepository.findByName(cmd.getCompanyName())).thenReturn(Optional.of(injected));
    }

    private void stubNotExistCompany() {
        when(companyRepository.findByName(cmd.getCompanyName())).thenReturn(Optional.empty());
    }
}
