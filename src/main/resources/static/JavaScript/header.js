const lightBulb = "M2 6a6 6 0 1 1 10.174 4.31c-.203.196-.359.4-.453.619l-.762 1.769A.5.5 0 0 1 10.5 13a.5.5 0 0 1 0 1 .5.5 0 0 1 0 1l-.224.447a1 1 0 0 1-.894.553H6.618a1 1 0 0 1-.894-.553L5.5 15a.5.5 0 0 1 0-1 .5.5 0 0 1 0-1 .5.5 0 0 1-.46-.302l-.761-1.77a1.964 1.964 0 0 0-.453-.618A5.984 5.984 0 0 1 2 6zm6-5a5 5 0 0 0-3.479 8.592c.263.254.514.564.676.941L5.83 12h4.342l.632-1.467c.162-.377.413-.687.676-.941A5 5 0 0 0 8 1z"
const lightBulbFilled = "M2 6a6 6 0 1 1 10.174 4.31c-.203.196-.359.4-.453.619l-.762 1.769A.5.5 0 0 1 10.5 13h-5a.5.5 0 0 1-.46-.302l-.761-1.77a1.964 1.964 0 0 0-.453-.618A5.984 5.984 0 0 1 2 6zm3 8.5a.5.5 0 0 1 .5-.5h5a.5.5 0 0 1 0 1l-.224.447a1 1 0 0 1-.894.553H6.618a1 1 0 0 1-.894-.553L5.5 15a.5.5 0 0 1-.5-.5z"

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
 * checks the theme cookie to set the page theme
 */
function checkThemeCookie() {
    let themeSwitch = document.getElementById("flexSwitchCheckDefault")
    let themeCookie = readCookie('sel_theme')
    themeSwitch.checked = themeCookie === 'dark'
    setPageTheme()

}

/**
 * Sets the calendar theme to the users theme
 * @param calendar calendar to set the theme
 * @param colour colour to set
 */
function setCalendarTheme(calendar, colour) {

    calendar.setTheme({
        common: {
            backgroundColor: colour,
        },
    });
    calendar.render()
}

/**
 * Sets the pages theme
 */
function setPageTheme() {
    let themeSwitch = document.getElementById("flexSwitchCheckDefault")


    if (themeSwitch.checked) {
        setTheme('dark')
        if (typeof calendar !== 'undefined') {
            setCalendarTheme(calendar, '#c6c9cc')
        }
        document.documentElement.setAttribute('data-bs-theme', 'dark')
    } else {
        setTheme('light')
        if (typeof calendar !== 'undefined') {
            setCalendarTheme(calendar, '#e9ecef')
        }
        document.documentElement.setAttribute('data-bs-theme', 'light')
    }

}

/**
 * sets the themes cookie
 * @param name of the theme
 */
function setTheme(name) {
    document.cookie = 'sel_theme=' + name + ';';
}

/**
 * Reads a cookie
 * @param name of the cookie
 * @returns {null|string} value of the cookie if found else null
 */
function readCookie(name) {
    var nameEQ = name + '='
    var ca = document.cookie.split(';')

    for (var i = 0; i < ca.length; i++) {
        var c = ca[i]
        while (c.charAt(0) === ' ') {
            c = c.substring(1, c.length)
        }
        if (c.indexOf(nameEQ) === 0) {
            return c.substring(nameEQ.length, c.length)
        }
    }

    return null
}