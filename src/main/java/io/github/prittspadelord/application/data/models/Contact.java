package io.github.prittspadelord.application.data.models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Contact {

    public enum NamePrefix {
        DR, MR, MRS, MS, SIR
    }

    private long id;
    private long userId;

    private NamePrefix namePrefix;
    @NotNull private String firstName;
    private String lastName;

    private String homePhoneNumberCountryCode;
    private String homePhoneNumber;

    private String mobilePhoneNumberCountryCode;
    private String mobilePhoneNumber;

    private String personalEmailAddress;
    private String workEmailAddress;

    private String addressLine1;
    private String addressLine2;
    private String city;
    private String province;
    private String country;
    private String postalCode;
}