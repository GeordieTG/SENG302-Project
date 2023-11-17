package nz.ac.canterbury.seng302.tab.service;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;


/**
 * Link between users and teams for team roles
 */
@Embeddable
public class RoleId implements Serializable {
    @Column(name = "user_id")
    Long userId;

    @Column(name = "team_id")
    Long teamId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

}
