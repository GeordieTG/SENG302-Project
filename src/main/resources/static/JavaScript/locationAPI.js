/**
 * Request the automapping of the location
 * @type {*[]}
 */

let address = []

/**
 * Get the location API from the open route service
 * Display the Address list when the user input the address
 *
 * @type {*[]}
 */
async function get_input_mapping(input_location) {

    let options = '';
    let url = window.location.href;
    let result = "";
    if (url.includes("//localhost")) {
        result = "";
    } else if (url.includes("csse-s302g4.canterbury.ac.nz/test")) {
        result = "/test";
    } else if (url.includes("csse-s302g4.canterbury.ac.nz/prod")) {
        result = "/prod";
    }

    const addresses = await fetch(result + "/address?locations=" + input_location)
        .then((response) => response.json());
    address = addresses;
    const size = addresses.length
    for (let i = 0; i < size; i++) {
        options += '<option value="' + addresses[i].properties.label + '" />';
    }
    document.getElementById("address_list").innerHTML = options;
    // onInput();
}

/**
 * Auto fill the country, city  location and street
 * when user select one of the address form the drop down list
 */
function onInput() {
    let val = document.getElementById("address1").value;
    let opts = document.getElementById('address_list').childNodes;
    for (let i = 0; i < opts.length; i++) {
        if (opts[i].value === val) {
            if (address[i].properties.name !== undefined) {
                document.getElementById("address1").value = address[i].properties.name;
                document.getElementById("address2").value = "";
            } else {
                document.getElementById("address1").value = "";
            }
            if (address[i].properties.locality !== undefined) {
                document.getElementById("suburb").value = address[i].properties.locality;
            } else {
                document.getElementById("suburb").value = "";
            }
            if (address[i].properties.localadmin !== undefined) {
                document.getElementById("city").value = address[i].properties.localadmin;
            } else {
                document.getElementById("city").value = "";
            }
            if (address[i].properties.country !== undefined) {
                document.getElementById("country").value = address[i].properties.country;
            } else {
                document.getElementById("country").value = "";
            }
            if (address[i].properties.postalcode !== undefined) {
                document.getElementById("postcode").value = address[i].properties.postalcode;
            } else {
                document.getElementById("postcode").value = "";
            }


        }
    }
}

/**
 * Timer counter start on key up dor the address text box
 * stimulate the api call after the time up
 */
function timerStartOnKeyUpForAddress() {
    let input = document.getElementById('address1');
// Init a timeout variable to be used below
    let timeout = null;
// Listen for keystroke events
    input.addEventListener('keyup', function (e) {
        // Clear the timeout if it has already been set.
        // This will prevent the previous task from executing
        // if it has been less than <MILLISECONDS>
        clearTimeout(timeout);
        // Make a new timeout set to go off in 1000ms (1 second)
        timeout = setTimeout(function () {
            get_input_mapping(document.getElementById('address1').value);
        }, 500);
    });
}

/**
 * Timer counter start on key up dor the city text box
 * stimulate the api call after the time up
 */
function timerStartOnKeyUpForCity() {
    let cityInput = document.getElementById('city');
    // Init a timeout variable to be used below
    let countryTimeout = null;
    // Listen for keystroke events
    countryInput.addEventListener('keyup', function (e) {
        // Clear the timeout if it has already been set.
        // This will prevent the previous task from executing
        // if it has been less than <MILLISECONDS>
        clearTimeout(cityTimeout);

        // Make a new timeout set to go off in 1000ms (1 second)
        let cityTimeout = setTimeout(function () {
            get_city_input_mapping(document.getElementById('city').value);
        }, 500);
    });
}

/**
 * Timer counter start on key up dor the country text box
 * stimulate the api call after the time up
 */
function timeStartOnKeyUpForCountry() {
    let countryInput = document.getElementById('country');
    // Init a timeout variable to be used below
    let countryTimeout = null;
    // Listen for keystroke events
    countryInput.addEventListener('keyup', function (e) {
        // Clear the timeout if it has already been set.
        // This will prevent the previous task from executing
        // if it has been less than <MILLISECONDS>
        clearTimeout(countryTimeout);

        // Make a new timeout set to go off in 1000ms (1 second)
        countryTimeout = setTimeout(function () {
            get_country_input_mapping(document.getElementById('country').value);
        }, 300);
    });
}

/**
 * Get auto mapping for the country chat box when the user input
 * @param input_location the input text from the country text box
 */
async function get_country_input_mapping(input_location) {

    let options = '';
    const addresses = await fetch("/country?locations=" + input_location)
        .then((response) => response.json());
    address = addresses;
    const size = addresses.length
    for (let i = 0; i < size; i++) {
        options += '<option value="' + addresses[i].properties.label + '" />';
    }
    document.getElementById("country_list").innerHTML = options;
}

/**
 * Get auto mapping for the city input text box upon the user input
 * @param input_location the input text from the country text box
 */
async function get_city_input_mapping(input_location) {

    let options = '';
    const addresses = await fetch("/city?locations=" + input_location)
        .then((response) => response.json());
    address = addresses;
    const size = addresses.length
    for (let i = 0; i < size; i++) {
        options += '<option value="' + addresses[i].properties.label + '" />';
    }
    document.getElementById("city_list").innerHTML = options;
}
