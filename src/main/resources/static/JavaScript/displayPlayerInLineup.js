/**
 * Called when a player is selected for a position in the lineup. Makes a request to a rest controller (different to our
 * usual controllers) stored in PlayerInformationController to get the players profile image src. If no image is found
 * then a default image will be displayed instead.
 * @param selectElement the dropdown that the user has been selected from
 */
function displayUserProfile(selectElement) {
    let userId = selectElement.value;
    let identifier = selectElement.id.split('-')[0] + 'Img' + selectElement.id.split('-')[1]
    let icon = document.getElementById(identifier);
    fetch(`${getCorrectURL()}/profile-picture/${userId}`)
        .then(response => {
            if (response.ok) {
                return response.text();
            } else {
                throw new Error('Profile picture not found');
            }
        })
        .then(profilePictureSource => {
            icon.src = profilePictureSource;
        })
        .catch(error => {
            icon.src = 'images/default.jpg';
            if (userId !== '') {
                console.error(error);
            } else {
                console.info("Back to default")
            }

        });
}

/**
 * This code attaches event listeners to all the dropdowns
 * and creates a dictionary for all the dropdowns to store the previous value
 * when a change three different case occur
 * case 1: player to player, when a player has already been selected
 * in the dictionary for that dropdown that value is stored
 * then if a change occurs both selectedValue and previousSelectedValue should not be ""
 * so it deletes the new selected option from all the other dropdowns and adds back the previous one
 * case 2: no player to player, in the dictionary the prevSelectedValue should be ""
 * so it just deletes the new selected value from all other dropdowns
 * case 3: player to no player, when a user "deselects" a player
 * then it just add the old option back to all dropdowns
 *
 * After all cases it updates the prevSelections to the new selected value
 */
// Get all dropdown elements with the specified class name
const dropdowns = document.querySelectorAll('.players-select');
const prevSelections = {};

// Attach an event listener to each dropdown
dropdowns.forEach(dropdown => {
    prevSelections[dropdown.id] = "";
    dropdown.addEventListener('change', function () {
        const selectedValue = this.value;
        const prevSelectedValue = prevSelections[this.id];
        const prevSelectedOption = this.querySelector(`option[value="${prevSelectedValue}"]`);
        // Loop through all the dropdowns
        dropdowns.forEach(otherDropdown => {
            if (otherDropdown !== this) {
                // Check if both the current dropdown and other dropdown have selections
                if (selectedValue !== "" && prevSelectedValue !== "") {// player to player
                    const optionToRemove = otherDropdown.querySelector(`option[value="${selectedValue}"]`);
                    if (optionToRemove) {
                        optionToRemove.remove();
                    }
                    const optionToAdd = document.createElement('option');
                    optionToAdd.value = prevSelectedValue;
                    optionToAdd.textContent = prevSelectedOption.textContent;
                    otherDropdown.appendChild(optionToAdd);
                } else if (selectedValue !== "") { // No player -> player
                    // Remove the selected option from other dropdowns
                    const optionToRemove = otherDropdown.querySelector(`option[value="${selectedValue}"]`);
                    if (optionToRemove) {
                        optionToRemove.remove();
                    }
                } else if (prevSelectedValue !== "") { // Player -> no Player
                    // Add the previously selected option back to other dropdowns
                    const optionToAdd = document.createElement('option');
                    optionToAdd.value = prevSelectedValue;
                    optionToAdd.textContent = prevSelectedOption.textContent;
                    otherDropdown.appendChild(optionToAdd);
                }
            }
        });
        prevSelections[this.id] = selectedValue;
        checkForSubstitutes()
    });
});

/**
 * Checks all entries in the prevSelections dictionary (apart from the last 5 as they are substitution dropdowns) to
 * see if all positions in the starting line up have been selected. If they have then the substitution section is displayed.
 */
function checkForSubstitutes() {
    const keys = Object.keys(prevSelections);
    const keysToCheck = keys.slice(0, -5);

    let allPlayersSelected = true;
    for (const element of keysToCheck) {
        const key = element;
        if (prevSelections[key] === "") {
            allPlayersSelected = false;
            break;
        }
    }
    if (allPlayersSelected) {
        document.getElementById("substitutions").style.display = "block";
    }
}

/**
 * Ensures Line up is filled
 * If not filled a modal is popped and returns false
 * Else returns true and converts the dictionary to a string
 * @returns {boolean} Line-Up is filled or not
 */
function createLineupString() {
    let output = "";
    let groupSeparator = "-";
    let valueSeparator = ",";
    if (document.getElementById("formation-list-group").value === "None") {
        return false
    }
    document.getElementById("formationId").value = document.getElementById("formation-list-group").value
    // Loop through the dictionary and construct the output string
    let groups = {};
    let psValues = [];
    for (let key in prevSelections) {
        if (prevSelections.hasOwnProperty(key)) {
            if (!key.includes("s")) {
                let group = key.split("-")[1]; // Extract the group part from the key
                if (!groups[group]) {
                    groups[group] = [];
                }
                if (prevSelections[key] === "") {
                    let errorToast = new bootstrap.Toast(document.getElementById('lineUpErrorToast'));
                    errorToast.show();
                    return false;
                }
                groups[group].push(prevSelections[key]);
            } else {
                let psValue = prevSelections[key];
                if (psValue !== "") {
                    psValues.push(psValue);
                }
            }
        }
    }

    // Concatenate the values within each group and separate the groups
    for (let group in groups) {
        if (groups.hasOwnProperty(group)) {
            let values = groups[group].join(valueSeparator);
            output += values + groupSeparator;
        }
    }
 // remove final group separator
    let psOutput = psValues.join(valueSeparator);
    output += psOutput;


    // Remove the trailing group separator
    let saveFormation = document.getElementById("saveFormation");
    saveFormation.value = output;
    return true;
}

/**
 * Show confirmation for changing formation
 */
function showConfirmationModal() {
    const allValuesEmpty = Object.values(prevSelections).every(value => value === "None");
    if (!allValuesEmpty) {
        let confirmModal = document.getElementById('confirmModal');
        let bootstrapModal = new bootstrap.Modal(confirmModal);
        bootstrapModal.show();
    } else {
        document.getElementById('changeFormationBtn').click();
    }
}

/**
 * show confirmation for cancelling changes
 */
function showCancelButton() {
    if (document.getElementById("formation-list-group").value !== "None") {
        let cancelModal = document.getElementById('cancelModal');
        let bootstrapModal = new bootstrap.Modal(cancelModal);
        bootstrapModal.show();
    }
}
function showGarminActivities() {
        let garminModal = document.getElementById('activityModal');
        let bootstrapModal = new bootstrap.Modal(garminModal);
        bootstrapModal.show();

}

function getCorrectURL() {
    let url = window.location.href;
    let result = "";
    if (url.includes("csse-s302g4.canterbury.ac.nz/test")) {
        result = "/test";
    } else if (url.includes("csse-s302g4.canterbury.ac.nz/prod")) {
        result = "/prod";
    }

    return result;
}

function handleFormationChange(selectElement) {
    var selectedValue = selectElement.value;
    var cancelButton = document.getElementById("cancelButton");

    if (selectedValue === "None") {
        // If the selected value is "None," hide the cancel button
        cancelButton.style.display = "none";
    } else {
        // If a value other than "None" is selected, show the cancel button
        cancelButton.style.display = "block";
    }
}
