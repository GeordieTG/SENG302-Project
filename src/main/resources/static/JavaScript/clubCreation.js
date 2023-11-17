let oldImage = null;

/**
 * Uses a rest controller endpoint to create the club in the DB without resetting the page
 */
async function createClub() {

    clearErrors()

    let name = document.getElementById("name").value
    let sport = document.getElementById("sport").value
    let location = createLocation()

    let clubData = {
        club: {
            name: name.trim(),
            sport: sport === "-1" ? "NONE" : sport,
            location: location,
        },
        teamIds: getSelectedTeamIds(),

    }

    let club = await fetch(`${getCorrectURL()}/create-club`, {
        method: 'POST',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'X-CSRF-TOKEN': TOKEN
        },
        body: JSON.stringify(clubData)
    })
        .then(response => {
            if (response.ok) {
                return response.json();
            } else {
                displayErrors(response.json())
                throw new Error('Club not created!');
            }
        })
    await uploadImage(club.id, "club")
    window.location.href = "viewClub?id=" + club.id
}

/**
 * Uses a rest controller endpoint to create a location in the DB so that the information can be
 * passed into the ClubDto
 */
function createLocation() {

    let country = document.getElementById("country").value
    let city = document.getElementById("city").value
    let suburb = document.getElementById("suburb").value
    let postcode = document.getElementById("postcode").value
    let address1 = document.getElementById("address1").value
    let address2 = document.getElementById("address2").value

    return {
        line1: address1.trim(),
        line2: address2.trim(),
        country: country.trim(),
        city: city.trim(),
        suburb: suburb.trim(),
        postcode: postcode.trim()
    }
}

/**
 * Uses a rest controller endpoint to update the club in the DB without resetting the page
 *
 * @param clubId id of the club being edited
 */
async function editClub(clubId) {

    clearErrors();

    let name = document.getElementById("name").value;
    let sport = document.getElementById("sport").value;
    let imageArray = document.getElementById("picture-id").src.split("/");
    let image = imageArray.slice(-3).join("/")
    if (imageArray[imageArray.length - 2] === "images") {
        image = imageArray.slice(-2).join("/")
    }
    let location = createLocation();

    //May need to change how sport field gets handled when changing sport disabled
    let clubData = {
        club: {
            name: name.trim(),
            id: clubId,
            sport: sport === "-1" ? "NONE" : sport,
            location: location,
            image: image
        },
        teamIds: getSelectedTeamIds(),
    }

    let club = await fetch(`${getCorrectURL()}/edit-club?id=` + clubId, {
        method: 'POST',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'X-CSRF-TOKEN': TOKEN
        },
        body: JSON.stringify(clubData)
    })
        .then(response => {
            if (response.ok) {
                return response.json();
            } else {
                displayErrors(response.json())
                throw new Error('Club could not be updated!');
            }
        });
    if (image !== oldImage) {
        await uploadImage(clubId, "club");
    }
    window.location.href = "viewClub?id=" + clubId;
}


/**
 * Gets the correct starting url depending on where the code is run
 * @returns {string}
 */
function getCorrectURL() {
    let url = window.location.href;
    let result = "";
    if (url.includes("//localhost")) {
        result = "";
    } else if (url.includes("csse-s302g4.canterbury.ac.nz/test")) {
        result = "/test";
    } else if (url.includes("csse-s302g4.canterbury.ac.nz/prod")) {
        result = "/prod";
    }

    return result;
}

/**
 * Gets the spring security token and saves it as a global variable to be used in the post requests
 * @param csrfToken
 */
function createGlobal(csrfToken) {
    TOKEN = csrfToken
}

/**
 * Displays the error messages
 * @param response The returned object from the POST request
 */
function displayErrors(response) {
    response.then(data => {
        for (let key in data) {
            document.getElementById(key).style.borderColor = "red"
            document.getElementById(key + "_error").textContent = data[key]
        }
    })
}

/**
 * Clears the error messages
 */
function clearErrors() {
    document.getElementById("name").style.borderColor = "#ced4da"
    document.getElementById("name_error").textContent = ""
    document.getElementById("sport").style.borderColor = "#ced4da"
    document.getElementById("sport_error").textContent = ""
    document.getElementById("country").style.borderColor = "#ced4da"
    document.getElementById("country_error").textContent = ""
    document.getElementById("city").style.borderColor = "#ced4da"
    document.getElementById("city_error").textContent = ""
    document.getElementById("teamSelectDisplay").style.borderColor = "#ced4da"
    document.getElementById("teams_error").textContent = ""
}

/**
 * Checking a div is empty for sport selection
 */
function checkSelectedDivEmpty() {
    var selectedTeamsDiv = document.getElementById("selectedTeams");
    let sportsDisplay = document.getElementById("sportDisplayDiv");
    let sportSelect = document.getElementById("sportSelect");
    if (selectedTeamsDiv && selectedTeamsDiv.innerHTML.trim() === "") {
        sportsDisplay.hidden = true;
        sportSelect.hidden = false;
    } else {
        document.getElementById("sportDisplay").value = document.getElementById("sport").value;
        sportSelect.hidden = true;
        sportsDisplay.hidden = false;
    }
}

function setGlobalImage(image) {
    oldImage = image
}
