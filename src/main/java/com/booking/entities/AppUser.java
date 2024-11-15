package com.booking.entities;


import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class AppUser implements UserDetails {

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    String imageName;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID) //UUID
    private String id;

    String phoneNumber;

    @Embedded
    Address address;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    private String firstname;
    private String lastname;
    private LocalDate dateOfBirth;
    private String password;
    @Column(name = "email", unique = true)
    private String email;

    private boolean enabled;


    transient Set<String> authorities;//not persistant injected in creation depending on instance type(client or admin)


    public void setAuthorities(Set<String> authorities) {
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {


        return authorities.stream().map((authority) -> new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return authority;

            }
        }).collect(Collectors.toList());

    }


    public void setPassword(String password) {
        this.password = password;
    }


    @Override
    public String getPassword() {
        return this.password;
    }


    @Override
    public String getUsername() {

        return this.email;//for authentication

    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

}
