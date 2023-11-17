let sports = new Set();

/**
 * Checks to see if the users age is valid
 * @return boolean, true if it's a valid age, false otherwise
 */
function checkAge() {
    let dob = document.getElementById("DOB").value.split("-");
    let date = new Date(dob[0], parseInt(dob[1]) - 1, dob[2]);
    let today = new Date();
    let input = document.getElementById("DOB");
    if (((today.getTime() - date.getTime()) / 31536000000) < 13.01) {
        input.style.outlineColor = "red";
        input.style.borderColor = "red";
    } else {
        input.style.outlineColor = "black";
        input.style.borderColor = "black";
    }
}

// Listen for page to be loaded
document.addEventListener('DOMContentLoaded', () => {


    // select the buttons and input fields
    const editBtns = document.querySelectorAll('.editBtn');
    const inputFields = document.querySelectorAll('.form-control');
    const submitBtns = document.querySelectorAll('.submitBtn');
    const resetBtn = document.getElementById("resetBtn");
    const submitPFPBtn = document.getElementById("submitPFPBtn");
    const submitDetailsBtn = document.getElementById("submitDetailsBtn");
    const profilePicture = document.getElementById("profilePicture");

    // add click event listener to each edit button
    editBtns.forEach((btn) => {
        btn.addEventListener('click', () => {
            // toggle the readonly attribute on the corresponding input field
            resetFields();
            if (btn.innerText === "Cancel") {
                btn.innerText = "Edit";
                btn.style.backgroundColor = "#4CAF50";
            } else {
                btn.innerText = "Cancel";
                btn.style.backgroundColor = "#B90E0A";
            }
            const inputField2 = inputFields[1];
            const inputField3 = inputFields[2];
            const inputField4 = inputFields[3];
            const inputField5 = inputFields[4];

            if (resetBtn.style.visibility === "hidden") {
                resetBtn.style.visibility = "visible";
            } else {
                resetBtn.style.visibility = "hidden"
            }
            if (inputField2.disabled === true) {
                inputField2.disabled = false;
                inputField3.disabled = false;
                inputField4.disabled = false;
                inputField5.disabled = false;
                submitPFPBtn.disabled = true;
                profilePicture.disabled = true;
                submitBtns[1].disabled = false;
                submitDetailsBtn.style.display = "block";
            } else {
                inputField2.disabled = true;
                inputField3.disabled = true;
                inputField4.disabled = true;
                inputField5.disabled = true;
                submitPFPBtn.disabled = false;
                profilePicture.disabled = false;
                submitBtns[1].disabled = true;
                submitDetailsBtn.style.display = "none";
            }
        });
    });
});

/**
 * Checks to see if the user has uploaded a valid image type
 * @param event the file upload event
 */
async function handleChange(event) {
    let pfp = document.getElementById("profilePicture");
    let allowed = ["image/png", "image/jpeg", "image/svg"];
    const file = event.target.files[0];
    const imgSize = file.size;
    if (!allowed.includes(file.type)) {
        let toast = document.getElementById("imageIsWrongType")
        let bootstrapToast = new bootstrap.Toast(toast)
        bootstrapToast.show()
        pfp.setCustomValidity("Invalid format. Must be one of jpeg, png, svg.");
        pfp.checkValidity();
    } else if (imgSize > 10000000) {
        let toast = document.getElementById("imageTooLarge")
        let bootstrapToast = new bootstrap.Toast(toast)
        bootstrapToast.show()
        pfp.setCustomValidity("File size is too big");
        pfp.checkValidity();
    } else {
        pfp.setCustomValidity("");
        pfp.checkValidity();
    }
}


/**
 * Changes the favourite sport row to what is in the sports set
 */
function changeFavouriteSportRow() {
    let fav = document.getElementById("favouriteSport");
    let string = "";
    sports.forEach(element => {
        if (string === "") {
            string += element;
        } else {
            string += ", " + element;
        }
    })
    fav.value = string;
}

/**
 * Adds or deletes the value of the input in the sports set
 * @param element representing the input the user has typed in
 */
function changeSport(element) {
    if (element.checked) {
        sports.add(element.value);
    } else {
        sports.delete(element.value);
    }
    changeFavouriteSportRow()
}

/**
 * Called on profilePage page
 * Changes the active tab based on validation errors
 */
function changeDisplayTab(invalidSecondTab, invalidThirdTab) {

    let profileTab = document.getElementById("nav-profile-tab")
    let detailsTab = document.getElementById("detailsTab")
    let locationTab = document.getElementById("locationTab")

    let navProfile = document.getElementById("nav-profile")
    let navDetails = document.getElementById("nav-details")
    let navLocation = document.getElementById("nav-location")

    if (invalidSecondTab) {

        profileTab.classList.remove("active")
        navProfile.classList.remove("active")
        navProfile.classList.remove("show")

        detailsTab.classList.add("active")
        navDetails.classList.add("show")
        navDetails.classList.add("active")

        locationTab.classList.remove("active")
        navLocation.classList.remove("show")
        navLocation.classList.remove("active")

    } else if (invalidThirdTab) {

        profileTab.classList.remove("active")
        navProfile.classList.remove("active")
        navProfile.classList.remove("show")

        detailsTab.classList.remove("active")
        navDetails.classList.remove("show")
        navDetails.classList.remove("active")

        locationTab.classList.add("active")
        navLocation.classList.add("show")
        navLocation.classList.add("active")

    }

}

/**
 * Set the checkboxs to see if they should be ticked or not depending
 */
function setCheckboxes() {

    setKnownSports()

    let fav = document.getElementById("favouriteSport");

    let favourites = fav.value.split(", ");
    if (fav.value !== "") {
        favourites.forEach(element => {
            let box = document.getElementById(element);
            sports.add(element);
            box.checked = true;
        })
    }
    changeFavouriteSportRow()
}

/**
 * Displays the dropdown list if button is hit
 * if the dropdown list is showing then stops displaying
 */
function changeKnownSportView() {
    let div = document.getElementById("knownSports");
    if (div.style.display === "flex") {
        div.style.display = "none";
    } else {
        div.style.display = "flex";
    }
}

/**
 * Automatically closes the dropdown list
 */
function closeDropdown() {
    let div = document.getElementById("knownSports");
    div.style.display = "none"
}

async function alertOnErrorConnectToGarminWatch() {
    let div = document.getElementById("connectState")
    let result = getCorrectURL()
    if (div.outerHTML.includes("FAIL")) {

        const reset = await fetch(result + "/resetConnectState?reset=True")
            .then((response) => response.ok);
        if (reset) {
            alert("Fail to connect to garmin watch due to authentication error")
        }

    }
}

/**
 * Parent function for validating the exercise goals, called from profilePage HTML
 */
function validateGoals() {

    let steps = document.getElementById("steps");
    let caloriesBurnt = document.getElementById("caloriesBurnt");
    let distanceTravelled = document.getElementById("distanceTravelled");
    let totalActivityTime = document.getElementById("totalActivityTime");

    steps.style.borderColor = "#dee2e6";
    caloriesBurnt.style.borderColor = "#dee2e6";
    distanceTravelled.style.borderColor = "#dee2e6";
    totalActivityTime.style.borderColor = "#dee2e6";

    resetErrorMessages()

    let valid = true;
    if (!validateGoal("steps", steps.value)) {
        steps.style.borderColor = "red";
        document.getElementById("stepsError").style.display = "block";
        valid = false;
    }
    if (!validateGoal("calories", caloriesBurnt.value)) {
        caloriesBurnt.style.borderColor = "red";
        document.getElementById("caloriesBurntError").style.display = "block";
        valid = false;
    }
    if (!validateGoal("distance", distanceTravelled.value)) {
        distanceTravelled.style.borderColor = "red";
        document.getElementById("distanceTravelledError").style.display = "block";
        valid = false;
    }
    if (!validateGoal("time", totalActivityTime.value)) {
        totalActivityTime.style.borderColor = "red";
        document.getElementById("totalActivityTimeError").style.display = "block";
        valid = false;
    }

    if (valid) {
        document.getElementById("goalsForm").submit();
    }
}

/**
 * Validates the goals value
 * @param goal The type of goal (step, calorie, etc)
 * @param value The value to validate
 * @returns {boolean} True if valid, false otherwise
 */
function validateGoal(goal, value) {
    let intAndFloatRegex = new RegExp("^(?:[1-9]\\d*|0)?(?:\\.\\d+)?$")
    switch(goal) {
        case "steps":
            return value === "" || (intAndFloatRegex.test(value) && value <= 150000 && value >= 1 && !value.includes("."));
        case "calories":
            return value === "" || (intAndFloatRegex.test(value) && value <= 50000 && value >= 1);
        case "distance":
            return value === "" || (intAndFloatRegex.test(value) && value <= 500 && value >= 0.001);
        case "time":
            return value === "" || (intAndFloatRegex.test(value) && value <= 1440 && value >= 1);
    }
}

/**
 * Resets the error messages
 */
function resetErrorMessages() {
    document.getElementById("stepsError").style.display = "none";
    document.getElementById("caloriesBurntError").style.display = "none";
    document.getElementById("distanceTravelledError").style.display = "none";
    document.getElementById("totalActivityTimeError").style.display = "none";
}