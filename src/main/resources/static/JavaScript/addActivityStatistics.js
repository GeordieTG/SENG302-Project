/**
 * Used to increment the id of each row of the scoring table as it is created.
 * @type {number}
 */
let scoringRowCounter = 0;

/**
 * Appends a new row to the bottom of the scoring table to record scoring statistic. Called when the black "+" button
 * is clicked. Also removes "+" button from previous row.
 * @param button button that has been clicked (so that we can set its style to none and remove it)
 * @param teamId id of the team
 */
async function addScoringEvent(button, teamId) {
    button.style.display = "none";

    let activityId = document.getElementById("activity-id").value;
    let players = await fetch(`${getUrlBase()}/team-members/${teamId}/${activityId}`)
        .then(response => {
            if (response.ok) {
                return response.json();
            } else {
                throw new Error('Team not found');
            }
        });

    scoringRowCounter++;
    let newRow = "<tr>" + "<td><select class='form-select' id='playerScoreForms" + scoringRowCounter + "-scoredPlayerId' name='playerScoreForms[" + scoringRowCounter + "].scoredPlayerId'> <option value='-1'>Select Player</option>"

    players.forEach(function (player) {
        newRow += "<option value='" + player.id + "'>" + player.firstName + " " + player.lastName + "</option>";
    });

    newRow += "</div> </select> </td>" +
        "<td><input id='playerScoreForms" + scoringRowCounter + "-score' name='playerScoreForms[" + scoringRowCounter + "].score' class='form-control' placeholder='Score' type='number' max='250'></td>" +
        "<td><input id='playerScoreForms" + scoringRowCounter + "-scoreTime' name='playerScoreForms[" + scoringRowCounter + "].scoreTime' class='form-control' placeholder='Time' type='number' max='250'></td>" +
        "<input id='playerScoreForms" + scoringRowCounter + "-activityId' name='playerScoreForms[" + scoringRowCounter + "].activityId' class='form-control' type='hidden' value='" + activityId + "'>" +
        "<td>" +
        "<button id='addScoringEventButton' onclick='addScoringEvent(this, teamId.value)' class='btn btn-secondary' type='button'>+</button>" +
        "</td>" +
        "</tr>";

    let tableBody = document.getElementById("tableBody");
    tableBody.insertAdjacentHTML('beforeend', newRow);

    let tableContainer = document.getElementById("tableContainer");
    tableContainer.scrollTop = tableContainer.scrollHeight;
}

/**
 * Used to increment the id of each row of the substitution table as it is created.
 * @type {number}
 */
let substitutionRowCounter = 0;

/**
 * Appends a new row to the bottom of the substitution table to record substitution statistic. Called when the black "+" button
 * is clicked. Also removes "+" button from previous row.
 * @param button button that has been clicked (so that we can set its style to none and remove it)
 * @param teamId id of the team
 */
async function addSubstitutionEvent(button, teamId) {
    button.style.display = "none";

    let activityId = document.getElementById("activity-id").value;
    let players = await fetch(`${getUrlBase()}/team-members/${teamId}/${activityId}`)
        .then(response => {
            if (response.ok) {
                return response.json();
            } else {
                throw new Error('Team not found');
            }
        });

    substitutionRowCounter++;
    let newRow = "<tr>" + "<td><select class='form-select' id='substitutionForms[" + substitutionRowCounter + "].substitutePlayerId'  name='substitutionForms[" + substitutionRowCounter + "].substitutePlayerId'>"
        + " <option value='-1'>Select Player</option>"

    players.forEach(function (player) {
        newRow += "<option value='" + player.id + "'>" + player.firstName + " " + player.lastName + "</option>";
    });

    newRow += "</select> </td>"

    newRow += "<td><select class='form-select' id='substitutionForms[" + substitutionRowCounter + "].substitutedPlayerId' name='substitutionForms[" + substitutionRowCounter + "].substitutedPlayerId' > <option value='-1'>Select Player</option>"
    players.forEach(function (player) {
        newRow += "<option value='" + player.id + "'>" + player.firstName + " " + player.lastName + "</option>";
    });
    newRow += "</td>" +
        "<td><input id='substitutionForms[" + substitutionRowCounter + "].substituteTime' name='substitutionForms[" + substitutionRowCounter + "].substituteTime' class='form-control' placeholder='Time' type='number'></td>" +
        "<input id='substitutionForms" + substitutionRowCounter + "-activityId' name='substitutionForms[" + substitutionRowCounter + "].activityId' class='form-control' type='hidden' value='" + activityId + "'>" +
        "<td>" +
        "<button id='addSubstitutionEventButton' onclick='addSubstitutionEvent(this, teamId.value)' class='btn btn-secondary' type='button'>+</button>" +
        "</td>" +
        "</tr>";


    let tableBody = document.getElementById("substitutionTableBody");
    tableBody.insertAdjacentHTML('beforeend', newRow);

    let tableContainer = document.getElementById("substitutionTableContainer");
    tableContainer.scrollTop = tableContainer.scrollHeight;

}

/**
 * check the input from the team_score and oppoScore text box
 * Show it is invalid if they are not in the same format
 * first check the input of of two text boxes are in the required pattern
 * second check the format of two text box match each other
 * third remove the state if the user do not input anything in the text box
 */
function checkInputFormatOfStatistics() {
    let input1 = document.getElementById('teamScore');
    let input2 = document.getElementById('oppoScore');

    input1.addEventListener('input', validateInputFormat);
    input2.addEventListener('input', validateInputFormat);

    function validateInputFormat() {
        let value1 = input1.value.trim();
        let value2 = input2.value.trim();

        // Regular expressions for "x" and "x-x" formats
        let regexX = /^\d+$/; // Matches "3 vs 4" format
        let regexXDashX = /^\d+-\d+$/; // Matches "3-4 vs 4-5" format

        // show invalid if the input1 value is not in the format of "3 or 3-4"
        // eg. invalid if the user input the alphabet or special character
        if (!regexX.test(input1.value) && !regexXDashX.test(input1.value)) {
            input1.classList.add('is-invalid');
            document.getElementById('error-message').textContent = "Please use same number format for each side. Accepted characters are digits and '-'. e.g. '3-4' vs '4-3', '3' vs '8'";
        }
        // show invalid if the input2 value is not in the format of "3 or 3-4"
        // eg. invalid if the user input the alphabet or special character
        if (!regexX.test(input2.value) && !regexXDashX.test(input2.value)) {
            input2.classList.add('is-invalid');
            document.getElementById('error-message').textContent = "Please use same number format for each side. Accepted characters are digits and '-'. e.g. '3-4' vs '4-3', '3' vs '8'";
        }

        // check if the input values format match each other same format for both input boxes
        if (input1.value !== "" && input2.value !== "") {
            let isValid = (regexX.test(value1) && regexX.test(value2)) || (regexXDashX.test(value1) && regexXDashX.test(value2));

            if (isValid) {
                input1.classList.add("is-valid")
                input2.classList.add("is-valid")
                input1.classList.remove('is-invalid');
                input2.classList.remove('is-invalid');
                document.getElementById('error-message').textContent = "";
            } else {
                input1.classList.add('is-invalid');
                input2.classList.add('is-invalid');
                input1.classList.remove('is-valid');
                input2.classList.remove('is-valid');
                document.getElementById('error-message').textContent = "Please use same number format for each side. Accepted characters are digits and '-'. e.g. '3-4' vs '4-3', '3' vs '8'";
            }
        }
        // remove the invalid or invalid state if the user input nothing in both text boxes.
        if (input1.value === "") {
            input1.classList.remove('is-invalid');
            input1.classList.remove('is-valid');
        }
        if (input2.value === "") {
            input2.classList.remove('is-invalid');
            input2.classList.remove('is-valid');
        }
    }
}

function getUrlBase() {

    let url = window.location.href;
    let result = "";
    if (url.includes("csse-s302g4.canterbury.ac.nz/test")) {
        result = "/test";
    } else if (url.includes("csse-s302g4.canterbury.ac.nz/prod")) {
        result = "/prod";
    }
    return result

}

function setOutcome() {
    let outcomeValue = document.getElementById('outcome').value
    switch (outcomeValue) {
        case 'WON':
            document.getElementById('btnWin').checked = true
            break
        case 'DRAW':
            document.getElementById('btnDraw').checked = true
            break
        case 'LOSS':
            document.getElementById('btnLoss').checked = true
            break
    }
}

function displayOutgoingError(row) {
    document.getElementById('playerOutError').style.display = 'block'
    document.getElementById(`substitutionForms[${row}].substitutedPlayer`).style.borderColor = "red"

}
function displaySubTimeError(row) {
    document.getElementById('timeError').style.display = 'block'
    document.getElementById(`substitutionForms[${row}].substituteTime`).style.borderColor = "red"
}
function displayIncomingError(row) {

    document.getElementById('playerInError').style.display = 'block'
    document.getElementById(`substitutionForms[${row}].substitutePlayer`).style.borderColor = "red"
}

/**
 * Displays substitution errors
 * @param errors tuple of errors
 */
function displaySubErrorMessages(errors) {
    console.log(errors)
    for (let i = 0; i < errors.length; i++) {
        let row = errors[i].value0
        let message = errors[i].value1

        if (message === "SubtimeError") {
            displaySubTimeError(row)
        } else if (message === "incomingPlayerError") {
            displayIncomingError(row)
        } else if (message === "outgoingPlayerError") {
            displayOutgoingError(row)
        }

    }
}

/**
 * Displays the error messages for the score forms
 * @param errors list of tuples of the form (int row, String error)
 */
function displayScoreErrorMessages(errors) {
    for (let i = 0; i < errors.length; i++) {
        let row = errors[i].value0
        let message = errors[i].value1
        if (message === "Please select a player") {
            displayPlayerError(row)
        } else if (message === "Score must be positive") {
            displayScoreError(row)
        } else if (message === "Too many points") {
            displayTotalScoreError()
        } else if (message === "Score too large") {
            displayLargeScoreError()
        } else {
            displayTimeError(row)
        }
    }
}

function displayLargeScoreError() {
    document.getElementById("largeScoreError").style.display = "block"
}

/**
 * Makes the correct form player have a red border
 * @param id the id (row) of the fact
 */
function displayPlayerError(id) {
    document.getElementById("playerScoreForms-scoredPlayerId-" + id).style.borderColor = "red"
}

/**
 * Makes the correct form time have a red border
 * @param id the id (row) of the fact
 */
function displayTimeError(id) {
    document.getElementById("playerScoreForms-scoreTime-" + id).style.borderColor = "red"
}

/**
 * Makes the correct form score have a red border
 * @param id the id (row) of the form
 */
function displayScoreError(id) {
    document.getElementById("playerScoreForms-score-" + id).style.borderColor = "red"
}

/**
 * Displays an error message regarding the total number of points compared to the home score
 */
function displayTotalScoreError() {
    document.getElementById("totalScoreError").style.display = "block"
}


