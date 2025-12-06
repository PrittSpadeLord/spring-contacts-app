package io.github.prittspadelord.application.data.dbmodels;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {
    private long id;
    private String username;
    private String nickname;
    private String hashedPassword; //serious debate needed on whether we need it here or not
    private long recentPasswordUpdateTimestamp;
}
