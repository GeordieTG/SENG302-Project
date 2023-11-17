/**
 * Set to hold all current filters
 * @type {Set<any>}
 */
let sports = new Set();
/**
 * Boolean whether the checkboxes have changed.
 * @type {boolean}
 */
let altered = false;
/**
 * Set to hold all current filters
 * @type {Set<any>}
 */
let cities = new Set();

/**
 * Changes CSV upon check-box checking.
 * @returns {Promise<void>} Not used.
 */
async function changeFilterQuerySport() {
    let fav = document.getElementById("hiddenFilterQuerySport");
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
 * Changes CSV upon check-box checking.
 * @returns {Promise<void>} Not used.
 */
async function changeFilterQueryCity() {
    let fav = document.getElementById("hiddenFilterQueryCity");
    let string = "";
    cities.forEach(element => {
        let box = document.getElementById(element);
        if (box !== null) {
            if (string === "") {
                string += element;
            } else {
                string += ", " + element;
            }
        }
    })
    fav.value = string;
}

/**
 * Adds or removes the sports from the known filters.
 * @param element Checkbox item.
 * @returns {Promise<void>} Not used.
 */
async function changeFilterSport(element) {
    if (element.checked) {
        sports.add(element.value);
        altered = true;
    } else {
        sports.delete(element.value);
        altered = true;
    }
    await changeFilterQuerySport()
}

/**
 * Adds or removes the sports from the known filters.
 * @param element Checkbox item.
 * @returns {Promise<void>} Not used.
 */
async function changeFilterCity(element) {
    if (element.checked) {
        cities.add(element.value);
        altered = true;
    } else {
        cities.delete(element.value);
        altered = true;
    }
    await changeFilterQueryCity()
}

/**
 * Displays filter params.
 */
function changeKnownSportView() {
    let city = document.getElementById("knownCities");
    let sport = document.getElementById("knownSports");
    if (sport.style.display === "flex") {
        sport.style.display = "none";
        city.style.display = "none";
    } else {
        sport.style.display = "flex";
        city.style.display = "none";
    }
}

/**
 * Displays filter params.
 */
function changeKnownCityView() {
    let city = document.getElementById("knownCities");
    let sport = document.getElementById("knownSports");

    if (city.style.display === "flex") {
        city.style.display = "none";
        sport.style.display = "none";
    } else {
        city.style.display = "flex";
        sport.style.display = "none";
    }
}

/**
 * Submits the filters when mouse leaves drop down.
 */
function applyFilter() {
    if (altered) {
        document.getElementById("filterForm").submit();
    }
}

/**
 * Sets the current checkboxes to be ticked if in hiddenFilterQuery on page load.
 */
function setCheckboxes() {
    setSearchInput();
    setSportCheckBoxes();
    setCityCheckBoxes();
}

function setSportCheckBoxes() {
    let fav = document.getElementById("hiddenFilterQuerySport");
    let favourites = fav.value.split(", ");
    if (fav.value !== "none") {
        favourites.forEach(element => {
            let box = document.getElementById(element);
            sports.add(element);
            box.checked = true;
        })
    }
    changeFilterQuerySport()
}

function setSearchInput() {
    let input = document.getElementById("searchQuery");
    if (input.value === "none") {
        input.value = "";
    }

}

function setCityCheckBoxes() {
    let fav = document.getElementById("hiddenFilterQueryCity");
    let favourites = fav.value.split(", ");
    if (fav.value !== "none") {
        favourites.forEach(element => {
            let box = document.getElementById(element);
            if (box !== null) {
                cities.add(element);
                box.checked = true;
            }
        })
    }
    let filterOptionCities = document.getElementById("knownCities").getElementsByTagName("li").length;
    if (filterOptionCities < 6) {
        document.getElementById("knownCities").style.height = "auto";
    }
    changeFilterQueryCity()
}