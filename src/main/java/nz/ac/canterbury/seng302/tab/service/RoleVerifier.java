package nz.ac.canterbury.seng302.tab.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

/**
 * Support class to verify user role based on the DB query
 * Intended as a Server-DB level authority checker.
 * Cookie-level authority checker can be implement above this class if required.
 * Created as a separate class for low maintenance
 */
@Service
public class RoleVerifier {

    /*
     * Set up required variables
     */
    @Autowired
    private TeamRoleService teamRoleService;

    /**
     * Checks if the role of the given user is manager for the given team
     *
     * @param userId Long TabUser user_id
     * @param teamId Long Team team_id
     * @return boolean true if manager, false otherwise
     */
    public boolean verifyManager(Long userId, Long teamId) {
        return teamRoleService.getUserRole(teamId, userId).equals("Manager");
    }

    /**
     * Checks if the role of the given user is a coach for the given team
     *
     * @param userId Long TabUser user_id
     * @param teamId Long Team team_id
     * @return boolean true if coach, false otherwise
     */
    public boolean verifyCoach(Long userId, Long teamId, Model model) {
        boolean state = teamRoleService.getUserRole(teamId, userId).equals("Coach");
        model.addAttribute("coach", state);
        return state;
    }

    /**
     * To count number of each role within an array of members
     * [manager, coach, member, other, total]
     *
     * @param roles array list of strings of the roles
     * @return An array with each index representing the count of roles
     *         i.e. index(0) = # of managers
     */
    public List<Integer> countRoles(List<String> roles) {
        int manager = 0;
        int coach = 0;
        int member = 0;
        int other = 0;
        List<Integer> summary = new ArrayList<>();

        for (String role : roles) {
            switch (role) {
                case "Manager" -> manager += 1;
                case "Coach" -> coach += 1;
                case "Member" -> member += 1;
                default -> other += 1;
            }
        }

        summary.add(manager);
        summary.add(coach);
        summary.add(member);
        summary.add(other);
        summary.add(manager + coach + member + other);

        return summary;
    }

    /**
     * Checks if a given list has at least one manager but maximum 3 managers at a time
     *
     * @param roles array list of strings of the roles
     * @return boolean: true if at least one, false otherwise
     */
    public boolean managerCountChecker(List<String> roles) {
        List<Integer> counter = countRoles(roles);
        return counter.get(0) >= 1 && counter.get(0) <= 3;
    }

}
