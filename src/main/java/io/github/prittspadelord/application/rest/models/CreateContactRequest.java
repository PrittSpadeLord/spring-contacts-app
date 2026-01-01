package io.github.prittspadelord.application.rest.models;

import io.github.prittspadelord.application.data.models.Contact;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateContactRequest {

    private Contact.NamePrefix namePrefix;
    @NotNull @Pattern(regexp = "[a-zA-Z]+") private String firstName; //more discussion on whether to allow accented characters or not
    @Pattern(regexp = "[a-zA-Z]+") private String lastName; //plus where do we draw the line?

    @Pattern(regexp = "[0-9]{1,4}") private String homePhoneNumberCountryCode;
    @Pattern(regexp = "[0-9]{10}") private String homePhoneNumber; //google phone validator something

    @Pattern(regexp = "[0-9]{1,4}") private String mobilePhoneNumberCountryCode;
    @Pattern(regexp = "[0-9]{10}") private String mobilePhoneNumber;

    @Email private String personalEmailAddress;
    @Email private String workEmailAddress;

    @Pattern(regexp = "[a-zA-Z ,0-9]+") private String addressLine1;
    @Pattern(regexp = "[a-zA-Z ,0-9]+") private String addressLine2;
    @Pattern(regexp = "[a-zA-Z ]+") private String city;
    @Pattern(regexp = "[a-zA-Z ]+") private String province;
    @Pattern(regexp = "[a-zA-Z ]+") private String country;
    @Pattern(regexp = "[0-9]{6}") private String postalCode; //only works in india?
}