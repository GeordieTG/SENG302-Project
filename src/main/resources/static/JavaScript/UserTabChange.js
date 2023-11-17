/**
 * Called on editProfile page load
 * Changes the active tab based on the previous tab or if there are any errors
 */
function changeDisplayProfile(invalidSecondTab, invalidThirdTab, tab) {

    if (invalidSecondTab || invalidThirdTab) {
        changeDisplayTab(invalidSecondTab, invalidThirdTab)
    } else {
        switchNormalTabProfile(tab)
    }
}

/**
 * Called when there are no errors. Switches the active tab based on the previously active tab
 */
function switchNormalTabProfile(tab) {

    switch (tab) {
        case 'Profile':
            setProfileTabActive()
            break;
        case 'Details':
            setDetailsTabActive()
            break;
        case 'Location':
            setLocationTabActive()
            break;

    }
}

/**
 * Called when a tab on the profilePage page gets clicked.
 * Updates the edit button link to contain the correct active tab variable
 */
function changeEditHrefProfile(tab) {

    switch (tab) {
        case 'Profile':
            document.getElementById("editProfileBtn").href = `editProfile?tab=Profile`
            break;
        case 'Details':
            document.getElementById("editProfileBtn").href = `editProfile?tab=Details`
            break;
        case 'Location':
            document.getElementById("editProfileBtn").href = `editProfile?tab=Location`
            break;
    }
}

/**
 * Called when a tab on the editProfile page gets clicked.
 * Updates the edit button link to contain the correct active tab variable
 */
function changeViewHrefProfile(tab) {

    switch (tab) {
        case 'Profile':
            document.getElementById("cancelEditBtn").href = `profilePage?tab=Profile`
            break;
        case 'Details':
            document.getElementById("cancelEditBtn").href = `profilePage?tab=Details`
            break;
        case 'Location':
            document.getElementById("cancelEditBtn").href = `profilePage?tab=Location`
            break;
    }
}

/**
 * Sets the profile tab to be the active tab
 */
function setProfileTabActive() {

    switchProfileTabStyling('add')
    switchDetailsTabStyling('remove')
    switchLocationTabStyling('remove')

}

/**
 * Sets the location tab to be the active tab
 */
function setLocationTabActive() {

    switchProfileTabStyling('remove')
    switchLocationTabStyling('add')
    switchDetailsTabStyling('remove')

}

/**
 * Sets the details tab to be the active tab
 */
function setDetailsTabActive() {

    switchProfileTabStyling('remove')
    switchLocationTabStyling('remove')
    switchDetailsTabStyling('add')

}

/**
 * Switches the styling for the details tab
 */
function switchDetailsTabStyling(styling) {

    let detailsTab = document.getElementById("detailsTab")
    let navDetails = document.getElementById("nav-details")

    switch (styling) {
        case "add":
            detailsTab.classList.add("active")
            navDetails.classList.add("active")
            navDetails.classList.add("show")
            break;
        case "remove":
            detailsTab.classList.remove("active")
            navDetails.classList.remove("active")
            navDetails.classList.remove("show")
            break;
    }
}
