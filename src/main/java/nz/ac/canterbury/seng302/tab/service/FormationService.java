package nz.ac.canterbury.seng302.tab.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javassist.NotFoundException;
import nz.ac.canterbury.seng302.tab.entity.Formation;
import nz.ac.canterbury.seng302.tab.entity.Team;
import nz.ac.canterbury.seng302.tab.formobjects.FormationForm;
import nz.ac.canterbury.seng302.tab.repository.FormationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

/**
 * Formation Service to handle logic for the formation controller
 */
@Service
public class FormationService {

    @Autowired
    private FormationRepository formationRepository;

    @Autowired
    private TeamService teamService;

    /**
     * Adds a formation to persistence
     *
     * @param formationForm object to persist
     * @param teamId        object to associate with the formation
     * @return the saved formation object
     */
    public Formation addFormation(FormationForm formationForm, Long teamId)
        throws NotFoundException {
        Team team = teamService.getTeamById(teamId);
        Formation formation = new Formation(formationForm.getFormation(),
            formationForm.getSport(), team);
        return formationRepository.save(formation);
    }

    /**
     * Return all formations a given team has in a List.
     * If there are no formation, return an empty list instead.
     *
     * @param teamId Team team_id
     * @return List of Formations
     */
    public List<Formation> findAllFormationsByTeamId(Long teamId) {
        List<Formation> results = formationRepository.findAllFormationsByTeamId(teamId);
        if (results == null) {
            results = new ArrayList<>();
        }
        return results;
    }

    /**
     * Gets a formation entity by a given id
     *
     * @param id the id for a specific formation
     * @return A team entity which represents a formation in the database
     * @throws NotFoundException if formation is not found by given id
     */
    public Optional<Formation> getFormationById(long id) throws NotFoundException {
        try {
            return formationRepository.findById(id);
        } catch (Exception e) {
            throw new NotFoundException("Formation not found.");
        }
    }

    /**
     * Gets a formation entity by a given id
     *
     * @param id the id for a specific formation
     * @return A team entity which represents a formation in the database
     * @throws NotFoundException if formation is not found by given id
     */
    public Formation getFormationByIdNotOptional(long id) throws NotFoundException {
        try {
            return formationRepository.findByIdNotOptional(id);
        } catch (Exception e) {
            throw new NotFoundException("Formation not found.");
        }
    }


    /**
     * Takes in a sport and returns the associated field image. Used anywhere that a formation
     * needs to be displayed.
     *
     * @param sport sport of the field
     * @return the formations sport field image
     */
    public String getFormationImage(String sport) {
        String bgImage;
        if (sport == null) {
            return "images/fields/default.jpg";
        }

        switch (sport) {
            case "Baseball" -> bgImage = "images/fields/Baseball.png";
            case "Basketball" -> bgImage = "images/fields/Basketball.png";
            case "Netball" -> bgImage = "images/fields/Netball.jpg";
            case "Cricket" -> bgImage = "images/fields/Cricket.png";
            case "Football" -> bgImage = "images/fields/Football.jpg";
            case "Futsal" -> bgImage = "images/fields/Futsal.png";
            case "Hockey" -> bgImage = "images/fields/Hockey.png";
            case "Rugby", "RugbyLeague" -> bgImage = "images/fields/Rugby.png";
            case "Volleyball" -> bgImage = "images/fields/Volleyball.jpg";
            default -> bgImage = "images/fields/default.jpg";
        }
        return bgImage;
    }

    //Factorise below two converters to @convert SpringBoot bean when free


    /**
     * Converts a given formation position array into a string to be saved into the DB:
     * [["1","2","3","4"], ["4","5"], ["6","7","8","9"]] -> "1,2,3,4-5,6-7,8,9"
     *
     * @param positionArray position array in ArrayList String[]
     * @return formation position in String format
     */
    public String positionArrayToString(List<String[]> positionArray) {
        String convertedPosition = "";
        for (String[] layer : positionArray) { //["1","2","3","4"]
            String addComma = "";

            for (String userId : layer) {
                if (addComma.length() == 0) {
                    addComma = userId;
                } else {
                    addComma = addComma.concat(", ").concat(userId);
                }
            }
            if (convertedPosition.length() == 0) {
                convertedPosition = addComma;
            } else {
                convertedPosition = convertedPosition.concat("-").concat(addComma);
            }
        }
        return convertedPosition;
    }

    /**
     * Updates the given formation into the given formation Id entry
     *
     * @param formationId    Long formation Id of the formation to update
     * @param positionString String modified formation to update with
     */
    public String editPosition(Long formationId, String positionString) {
        formationRepository.updatePosition(formationId, positionString);
        return positionString;
    }

    /**
     * Add the lineup to the model for displaying the lineup
     *
     * @param lineup lineup to display
     * @param model  thymeleaf model
     */
    public void addLineUpToModel(String lineup, Model model) {
        if (lineup != null) {
            int lastIndex = lineup.lastIndexOf("-");
            if (lastIndex != -1) {
                String beforeLastHyphen = lineup.substring(0, lastIndex);
                String afterLastHyphen = lineup.substring(lastIndex + 1);

                model.addAttribute("lineup", beforeLastHyphen);
                model.addAttribute("subs", afterLastHyphen);
            } else {
                // Handle the case when there is no hyphen in the string
                model.addAttribute("lineup", List.of(lineup));
            }
        }
    }


}
