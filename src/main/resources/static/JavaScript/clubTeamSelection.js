let CLUBSPORT = null;

/**
 * Retrieves selected checkbox values from the 'select team' dropdown.
 * Each checkbox value format in "teamId,teamName,teamProfileImage", created using Team.getBriefString()
 * @returns {*[]} selected teams
 */
function getSelectedTeams() {
    const selectedTeams = [];
    const checkboxes = document.querySelectorAll('[type="checkbox"]');

    checkboxes.forEach((checkbox) => {
        if (checkbox.checked && checkbox.id !== "flexSwitchCheckDefault") {
            selectedTeams.push(checkbox.value);
        }
    });

    return selectedTeams;
}


/**
 * Retrieves team ids from the checked team select checkboxes
 * @returns a list of team ids only
 */
function getSelectedTeamIds() {
    const selectedTeamsId = [];
    const checkboxes = document.querySelectorAll('[type="checkbox"]');

    checkboxes.forEach((checkbox) => {
        if (checkbox.checked) {
            const teamData = checkbox.value.split(",");
            selectedTeamsId.push(parseInt(teamData[0]));
        }
    });

    return selectedTeamsId;
}

/**
 * Re-renders selected teams section based on the given list of checked teams
 * @param teams List of teams selected
 */
function renderSelectedTeams(teams) {
    const teamSelectDisplay = document.getElementById("teamSelectDisplay");
    document.getElementById("selectedTeams").remove();
    const selectedTeams = document.createElement("div");
    selectedTeams.id = "selectedTeams";
    selectedTeams.setAttribute("class", "container border border-secondary");
    teamSelectDisplay.appendChild(selectedTeams);

    //Re-render selected teams display
    for (let i = 0; i < teams.length; i++) {
        const teamData = teams[i].split(",");
        const selectedContainer = document.createElement("div");
        const selected = document.createElement("img");
        selected.setAttribute("class", "rounded-circle profile-picture");
        selected.style.height = "3rem";
        selected.style.width = " 3rem";
        selected.setAttribute("src", teamData[2]);
        selected.setAttribute("value", teamData[1]);
        selected.id = teamData[1];
        const teamNameBadge = document.createElement("span");
        teamNameBadge.setAttribute("class", "badge bg-primary");
        teamNameBadge.textContent = teamData[1];
        selectedContainer.appendChild(selected);
        selectedContainer.appendChild(teamNameBadge);
        selectedTeams.appendChild(selectedContainer);
    }

    if (teams.length <= 0) {
        resetTeamSelection();
    }
}

/**
 * Setter for CLUBSPORT global variable.
 * Used for filtering available teams for the club
 * @param clubSport String
 */
function setClubSport(clubSport) {
    if (clubSport !== "-1") {
        CLUBSPORT = clubSport;
    } else {
        CLUBSPORT = null;
    }
    if (clubSport === 'Onload') {
        clubSport = document.getElementById('sport').value;
        CLUBSPORT = clubSport
        filterTeams('');
    } else {
        filterTeams('');
        resetTeamSelection();
    }

}

/**
 * Recreates default select team ui
 */
function resetTeamSelection() {
    const teamSelectDisplay = document.getElementById("teamSelectDisplay");

    //remove profiler image render container and replace with an empty div
    document.getElementById("selectedTeams").remove();
    const selectedTeams = document.createElement("div");
    selectedTeams.id = "selectedTeams";
    teamSelectDisplay.appendChild(selectedTeams);

    //reset name search field
    document.getElementById("teamSearchInput").value = "";

    document.getElementById("teamCheckboxes").setAttribute("class", "container py-2 collapse");

    //reset checkboxes
    const checkboxes = document.querySelectorAll('[type="checkbox"]');

    checkboxes.forEach((checkbox) => {
        checkbox.checked = false;
    });


}

/**
 * Hides teams with different sport to the Club and name does not match the search.
 */
function filterTeams(searchName) {
    const parent = document.getElementById("ownedTeamsOptions");
    const ownedOptions = parent.children;
    let hiddenList = []
    let visibleList = []
    for (let i = 0; i < ownedOptions.length; i++) {
        let ownedOptionSport = document.getElementById(ownedOptions[i].id + "-sport").value;
        let ownedOptionString = document.getElementById(ownedOptions[i].id + "-checkbox");
        const name = ownedOptionString.value.split(",")[1];
        searchName = searchName.trim().toLowerCase();
        if (ownedOptionSport !== CLUBSPORT || (searchName !== "" && !name.toLowerCase().includes(searchName))) {
            ownedOptions[i].style.display = "none";
            hiddenList.push(ownedOptions[i])
        } else {
            ownedOptions[i].style.display = "flex";
            visibleList.push(ownedOptions[i])
        }
    }
    const reorderedList = visibleList.concat(hiddenList);

    for (let i = 0; i < reorderedList.length; i++) {
        parent.appendChild(reorderedList[i]);
    }

}