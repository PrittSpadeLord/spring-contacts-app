package io.github.prittspadelord.application.data.models;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Contact {

    public enum NamePrefix {
        MR, MS, MRS, DR, SIR
    }

    private long id;

    @Nullable private NamePrefix namePrefix;
    @NotNull @Pattern(regexp = "[a-zA-Z]+") private String firstName; //more discussion on whether to allow accented characters or not
    @Nullable @Pattern(regexp = "[a-zA-Z]+") private String lastName; //plus where do we draw the line?

    @Nullable @Pattern(regexp = "[0-9]{1,4}") private String homePhoneNumberCountryCode;
    @Nullable @Pattern(regexp = "[0-9]{10}") private String homePhoneNumber;

    @Nullable @Pattern(regexp = "[0-9]{1,4}") private String mobilePhoneNumberCountryCode;
    @Nullable @Pattern(regexp = "[0-9]{10}") private String mobilePhoneNumber;

    @Email @Nullable private String personalEmailAddress;
    @Email @Nullable private String workEmailAddress;

    @Nullable @Pattern(regexp = "[a-zA-Z ,0-9]+") private String addressLine1;
    @Nullable @Pattern(regexp = "[a-zA-Z ,0-9]+") private String addressLine2;
    @Nullable @Pattern(regexp = "[a-zA-Z ]+") private String city;
    @Nullable @Pattern(regexp = "[a-zA-Z ]+") private String province;
    @Nullable @Pattern(regexp = "[a-zA-Z ]+") private String country;
    @Nullable @Pattern(regexp = "[0-9]{6}") private String postalCode;
}