/**
 * Allows us to increment the id's of the elements as new rows are added
 * @type {number}
 */
let rowCounter = 0;

/**
 * Used to dynamically add a new row to the activity facts table when the user clicks the '+' button.
 * @param button the '+' button needs to be passed in so that we can remove the old one, and create a new one on the
 * bottom row
 * @returns {Promise<void>}
 */
async function addFactEvent(button) {
    button.style.display = "none";

    rowCounter++;

    let activityId = document.getElementById("activity-id").value;

    let newRow = "<tr><td><textarea id='fact-description-" + rowCounter + "' name ='factForms[" + rowCounter + "].description' class='form-control' placeholder='Description' type='text'>" +
        "</textarea></td><td><input id='fact-time-" + rowCounter + "' name ='factForms[" + rowCounter + "].timeOccurred' class='form-control' placeholder='Time' type='number'></td>" +
        "<input id='factForms" + rowCounter + ".activityId' name='factForms[" + rowCounter + "].activityId' class='form-control' type='hidden' value='" + activityId + "'>" +
        "<td><button id='addFactEventButton' onclick='addFactEvent(this)' type='button'  class='btn btn-secondary' style='font-size: 18px'>+</button></td> </tr>"

    let tableBody = document.getElementById("tableBody");
    tableBody.insertAdjacentHTML('beforeend', newRow);

    let tableContainer = document.getElementById("tableContainer");
    tableContainer.scrollTop = tableContainer.scrollHeight;
}

/**
 * Displays the error messages for the facts
 * @param errors list of tuples of the form (int row, String error)
 */
function displayErrorMessages(errors) {
    for (let i = 0; i < errors.length; i++) {
        let row = errors[i].value0
        let message = errors[i].value1
        if (message === "Must have a description") {
            displayDescriptionError(row)
        } else {
            displayTimeError(row)
        }
    }
}

/**
 * Makes the correct fact description have a red border
 * @param id the id (row) of the fact
 */
function displayDescriptionError(id) {
    document.getElementById("fact-description-" + id).style.borderColor = "red"
}

/**
 * Makes the correct fact time have a red border
 * @param id the id (row) of the fact
 */
function displayTimeError(id) {
    document.getElementById("fact-time-" + id).style.borderColor = "red"
}
