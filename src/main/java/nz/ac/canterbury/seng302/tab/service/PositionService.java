package nz.ac.canterbury.seng302.tab.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javassist.NotFoundException;
import nz.ac.canterbury.seng302.tab.entity.Formation;
import nz.ac.canterbury.seng302.tab.utility.ConvertingUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;


/**
 * Class to handle positioning within Formation
 */
@Service
public class PositionService {

    @Autowired
    protected FormationService formationService;

    /**
     * Adds a given user to the given formation position.
     * Returns true if successful, else throws exception or false
     * Checks for double entries and returns false if found to be one.
     *
     * @param userId           TabUser user_id of the User
     * @param positionLocation Index of the position
     * @param formationId      Formation id of the target formation
     * @return boolean
     */
    public boolean addUserToFormation(Long userId, Long formationId, int[] positionLocation)
        throws NotFoundException {
        //retrieving, converting, & modifying the formation
        String userIdString = Long.toString(userId);
        Optional<Formation> targetFormation = formationService.getFormationById(formationId);
        List<String[]> targetArray = ConvertingUtil.positionStringToArray(
            targetFormation.orElseThrow().getPosition());
        ArrayList<String> layer = new ArrayList<>(List.of(targetArray.get(positionLocation[0])));
        layer.set(positionLocation[1], userIdString);
        targetArray.set(positionLocation[0], layer.toArray(new String[0]));

        //check for duplicate entry
        int count = 0;
        for (String[] strings : targetArray) {
            ArrayList<String> checkLayer = new ArrayList<>(List.of(strings));
            for (String s : checkLayer) {
                if (s.equals(userIdString)) {
                    count += 1;
                }
            }
        }
        if (count > 1 && userId != 0) { //&& condition should be moved above :(
            return false;
        }
        //Update into DB
        String targetString = formationService.positionArrayToString(targetArray);
        targetString = formationService.editPosition(formationId, targetString);
        //check if saved successfully
        return Objects.equals(formationService.getFormationById(formationId)
            .orElseThrow().getPosition(), targetString);
    }

    /**
     * Finds the index of a given user within the given formation array
     *
     * @param userIdString String of the user_id
     * @param targetArray  Array form of the formation string
     * @return int[] of the index
     */
    @NonNull
    private int[] findUserIndex(@NonNull String userIdString,
                                @NonNull ArrayList<String[]> targetArray) {
        int[] result = new int[2];
        result[0] = -1;
        result[1] = -1;
        for (int i = 0; i < targetArray.size(); i++) {
            ArrayList<String> checkLayer = new ArrayList<>(List.of(targetArray.get(i)));
            for (int j = 0; j < checkLayer.size(); j++) {
                if (checkLayer.get(j).equals(userIdString)) {
                    result[0] = i;
                    result[1] = j;
                    break;
                }
            }
        }
        return result;
    }

    /**
     * Removes a given user from a given formation by iterating through the formation.
     * Removal refers to replacing the index with "0", indicating a null user
     *
     * @param userId      TabUser user_id of target User
     * @param formationId Formation id of the target formation
     * @return boolean true if successful, else false
     */
    public boolean removeUserFromFormation(Long userId, Long formationId) throws NotFoundException {
        String userIdString = Long.toString(userId);
        Optional<Formation> targetPosition = formationService.getFormationById(formationId);
        ArrayList<String[]> targetArray = (ArrayList<String[]>)
            ConvertingUtil.positionStringToArray(
                targetPosition.orElseThrow().getPosition());

        int[] userIndex = findUserIndex(userIdString, targetArray);
        if (userIndex[0] == -1 || userIndex[1] == -1) { // User Not in the formation
            return false;
        }
        return addUserToFormation(Long.parseLong("0"), formationId, userIndex);
    }


}
