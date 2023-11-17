/**
 * Utilizes a rest controller to get the players profile picture based on their id
 */
document.addEventListener("DOMContentLoaded", function () {
    let profiles = document.getElementsByClassName('formation-player-pictures')
    for (let profile in profiles) {
        let userID = profiles[profile].id
        let user = userID.split("li")
        fetch(`profile-picture/${user.at(1)}`)
            .then(response => {
                if (response.ok) {
                    return response.text();
                } else {
                    throw new Error('Profile picture not found');
                }
            })
            .then(profilePictureSource => {
                profiles[profile].src = profilePictureSource;
                getUserNameByID()
            })
            .catch(error => {
                if (userID !== '') {
                    console.error(error);
                } else {
                    console.info("Back to default")
                }
            });
    }
});


/**
 * Utilizes a rest controller to get the players names based on their id
 */
function getUserNameByID() {
    let profiles = document.getElementsByClassName('formation-player-name text-center')
    for (let profile in profiles) {

        let userID = profiles[profile].id
        let user = userID.split("ln")
        fetch(`${getCorrectURL()}/player-name/${user.at(1)}`)
            .then(response => {
                if (response.ok) {
                    return response.text();
                } else {
                    throw new Error('Player name not found');
                }
            })
            .then(playerName => {
                profiles[profile].innerText = playerName;
            })
            .catch(error => {
                if (userID !== '') {
                    console.error(error);
                } else {
                    console.info("Back to default")
                }
            });
    }
}

/**
 * Utilizes a rest controller to get the players names based on their id
 */
function getActivityLineup() {

    let activityId = document.getElementById("activityId").value

    fetch(`/activity-lineup/${activityId}`)
        .then(response => {
            if (response.ok) {
                return response.text();
            } else {
                throw new Error('Player name not found');
            }
        })
        .then(lineup => {
            document.getElementById("lineupDB").value = lineup
            if (checkLineUp()) {
                updateLineUpHtml(lineup)
            }
        })
        .catch(error => {
            console.log(error)
        });
}

function checkFormation() {
    let formation = document.getElementById("changedFormation").value
    if (formation === "true") {
        switchDisplayLineUp()
    }
}

/**
 * Switches the content of the line-up (right most) container on the activity details page
 * Switches between Edit line-up and Display line-up
 */
function switchDisplayLineUp() {
    if (document.getElementById("editLineUp").style.display === "block") {
        getActivityLineup()
        document.getElementById("editLineUp").style.display = "none"
        document.getElementById("displayLineUp").style.display = "block"
        getUserNameByID()
        document.getElementById("currentLineUpTitle").style.display = "block"
        document.getElementById("displaySubstitution").style.display = "block"
    } else {
        document.getElementById("editLineUp").style.display = "block"
        document.getElementById("displayLineUp").style.display = "none"
        document.getElementById("currentLineUpTitle").style.display = "none"
        document.getElementById("displaySubstitution").style.display = "none"
    }
}

function checkLineUp() {
    return document.getElementById("lineupDB").value !== "";
}

function updateLineUpHtml(lineup) {
    let div = document.getElementById("currentLineUpGenerator")
    div.setAttribute("th:each", "${#strings.arraySplit('" + lineup + "', '-')}")
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