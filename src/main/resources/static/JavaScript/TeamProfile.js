window.addEventListener("resize", calculateHeight);

function calculateHeight() {
    let column = document.getElementById('column-1').clientHeight
    column -= 98
    if (window.innerWidth <= 1200) {
        column = column / 2
    }

    document.getElementById('tab-members').style.height = column + "px";

}

function changeTeamSlide(teamSlide, index) {
    let buttons = document.querySelectorAll('#team_' + teamSlide + '_activities_pagination button')
    buttons.forEach(function (button) {
        button.classList.remove('active');
    });
    // Add the 'active' class to the clicked pagination button
    let clickedButton = document.getElementById('team_' + teamSlide + '_button_' + (index + 1));
    clickedButton.classList.add('active');
}

function changePersonalSlide(index) {
    // Remove the 'active' class from all pagination buttons
    let buttons = document.querySelectorAll('#personalActivitiesPagination button');
    buttons.forEach(function (button) {
        button.classList.remove('active');
    });

    // Add the 'active' class to the clicked pagination button

    let clickedButton = document.getElementById('button_' + (index + 1));
    clickedButton.classList.add('active');
}

/**
 * Called on editTeamProfile and viewTeam page load
 * Changes the active tab based on the previous tab or if there are any errors
 */
function changeDisplayTeam(invalidFirstTab, invalidSecondTab, tab) {

    if (invalidFirstTab || invalidSecondTab) {
        if (invalidFirstTab) {
            setProfileTabActive()
        } else if (invalidSecondTab) {
            setLocationTabActive()
        }
    } else {
        switchNormalTab(tab)
    }
}

/**
 * Called when there are no errors. Switches the active tab based on the previously active tab
 */
function switchNormalTab(tab) {

    switch (tab) {
        case 'Profile':
            setProfileTabActive()
            break;
        case 'Location':
            setLocationTabActive()
            break;
        case 'Members':
            setMemberTabActive()
            break;
        case 'Invite':
            setInviteTabActive()
            break;
    }
}

/**
 * Called when a tab on the viewTeam page gets clicked.
 * Updates the edit button link to contain the correct active tab variable
 */
function changeEditHref(tab, id) {

    switch (tab) {
        case 'Profile':
            document.getElementById("editTeamMembersBtn").href = `editTeamProfile?id=${id}&tab=Profile`
            break;
        case 'Location':
            document.getElementById("editTeamMembersBtn").href = `editTeamProfile?id=${id}&tab=Location`
            break;
        case 'Members':
            document.getElementById("editTeamMembersBtn").href = `editTeamProfile?id=${id}&tab=Members`
            break;
    }
}

/**
 * Called when a tab on the editTeamProfile page gets clicked.
 * Updates the go back button link to contain the correct active tab variable
 */
function changeViewHref(tab, id) {

    switch (tab) {
        case 'Profile':
            document.getElementById("editBackBtnProfile").href = `viewTeam?id=${id}&showAllTeams=0&tab=Profile`
            break;
        case 'Location':
            document.getElementById("editBackBtnLocation").href = `viewTeam?id=${id}&showAllTeams=0&tab=Location`
            break;
        case 'Members':
            document.getElementById("editBackBtnMembers").href = `viewTeam?id=${id}&showAllTeams=0&tab=Members`
            break;
    }
}

/**
 * Sets the profile tab to be the active tab
 */
function setProfileTabActive() {

    switchProfileTabStyling('add')
    switchLocationTabStyling('remove')
    switchMemberTabStyling('remove')
    switchInviteTabStyling('remove')

}

/**
 * Sets the loation tab to be the active tab
 */
function setLocationTabActive() {

    switchProfileTabStyling('remove')
    switchLocationTabStyling('add')
    switchMemberTabStyling('remove')
    switchInviteTabStyling('remove')

}

/**
 * Sets the member roles tab to be the active tab
 */
function setMemberTabActive() {

    switchProfileTabStyling('remove')
    switchLocationTabStyling('remove')
    switchMemberTabStyling('add')
    switchInviteTabStyling('remove')

}

/**
 * Sets the invite tab to be the active tab
 */
function setInviteTabActive() {

    switchProfileTabStyling('remove')
    switchLocationTabStyling('remove')
    switchMemberTabStyling('remove')
    switchInviteTabStyling('add')

}

/**
 * Switches the styling for the profile tab
 */
function switchProfileTabStyling(styling) {

    let profileTab = document.getElementById("nav-profile-tab")
    let navProfile = document.getElementById("nav-profile")

    switch (styling) {
        case "add":
            profileTab.classList.add("active")
            navProfile.classList.add("active")
            navProfile.classList.add("show")
            break;
        case "remove":
            profileTab.classList.remove("active")
            navProfile.classList.remove("active")
            navProfile.classList.remove("show")
            break;
    }
}

/**
 * Switches the styling for the location tab
 */
function switchLocationTabStyling(styling) {

    let locationTab = document.getElementById("locationTab")
    let navLocation = document.getElementById("nav-location")

    switch (styling) {
        case "add":
            locationTab.classList.add("active")
            navLocation.classList.add("show")
            navLocation.classList.add("active")
            break;
        case "remove":
            locationTab.classList.remove("active")
            navLocation.classList.remove("show")
            navLocation.classList.remove("active")
            break;
    }
}

/**
 * Switches the styling for the invite tab
 */
function switchInviteTabStyling(styling) {

    let invitesTab = document.getElementById("nav-invites-tab")
    let navInvites = document.getElementById("nav-invites")

    switch (styling) {
        case "add":
            invitesTab.classList.add("active")
            navInvites.classList.add("active")
            navInvites.classList.add("show")
            break;
        case "remove":
            invitesTab.classList.remove("active")
            navInvites.classList.remove("active")
            navInvites.classList.remove("show")
            break;
    }
}

/**
 * Switches the styling for the member roles tab
 */
function switchMemberTabStyling(styling) {

    let membersTab = document.getElementById("nav-member-tab")
    let navMember = document.getElementById("nav-members")

    switch (styling) {
        case "add":
            membersTab.classList.add("active")
            navMember.classList.add("active")
            navMember.classList.add("show")
            break;
        case "remove":
            membersTab.classList.remove("active")
            navMember.classList.remove("active")
            navMember.classList.remove("show")
            break;
    }
}