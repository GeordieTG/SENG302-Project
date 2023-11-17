window.addEventListener("load", () => {
    updateCalendar(events, teamEvents, [])
    // Use a MutationObserver to call the modifyStyle function when the element appears
    const observer = new MutationObserver((records) => {
        records.forEach((record) => {
            if (record.addedNodes.forEach((node) => {
                if (node == document.querySelector('.toastui-calendar-see-more-container')) {
                    node.style.top = '400px';
                }
            }));
        });
    });

// Define the target node and options
    const config = { childList: true, subtree: true };

// Start observing the target node
    observer.observe(document.body, config);
})
/**
 * Creates the calendar element
 * @param events List<String> Information regarding the users personal activities
 * @param teamEvents List<String> Information regarding the activities the users teams
 * @param sports List<String> of all the selected sports
 */
let calendar;
let allEvents = [];
function modifyStyle() {
    const element = document.querySelector('.toastui-calendar-see-more-container');
    if (element) {
        element.style.top = '0px';
        element.style.left = '0px';
    }
}



function renderCalendar(events, teamEvents, sports) {
    // Initialize the calendar if it's not already initialized
    if (!calendar) {
        calendar = new tui.Calendar(document.getElementById('calendar'), {
            defaultView: 'month',
            scheduleView: true,
            template: {
                time: function (event) {
                    const dynamicColor = event.raw[0] === "true" ? 'green' : (event.raw[2] ? "black" : 'red');
                    const svg = switchSVG(dynamicColor, event.raw);
                    return svg + event.title;
                }
            }
        });

        // Set calendar theme if needed
        setCalendarTheme(calendar);

        // Add pagination controls and other UI elements
        addBackPagination(calendar);
        addNextPagination(calendar);
        addTodayLink(calendar);
        addMonthTitle(calendar);
        addEventLink(calendar);
    }

    // Clear existing events from the calendar
    calendar.clear();

    // Update the events data
    allEvents = extractAllEvents(events, teamEvents, sports);

    // Create and add events to the calendar
    calendar.createEvents(allEvents);

    // Render the calendar
    calendar.render();
}

// You can call this function whenever you need to update the calendar with new data
function updateCalendar(events, teamEvents, sports) {
    // Update the events data
    allEvents = extractAllEvents(events, teamEvents, sports);

    // Clear existing events from the calendar
    calendar.clear();

    // Create and add events to the calendar
    calendar.createEvents(allEvents);

    // Render the calendar
    calendar.render();
}
/**
 * Gets the selected sports from the checkboxes for the calendar to display
 * @returns {List<String>} selected sports
 */
function getSelectedSports() {
    const selectedSports = [];
    const checkboxes = document.querySelectorAll('input[type="checkbox"]');

    checkboxes.forEach((checkbox) => {
        if (checkbox.checked) {
            selectedSports.push(checkbox.value);
        }
    });

    return selectedSports;
}

/**
 * Selects or deselects all checkboxes
 * @param checkbox the select all value to be assigned to (checked or not checked)
 */
function handleSelectAll(checkbox) {
    const checkboxes = document.querySelectorAll('.form-check-input[type="checkbox"]');
    checkboxes.forEach((chkbox) => {
        chkbox.checked = checkbox.checked;
    });
    renderCalendar(events, teamEvents, getSelectedSports());
}

/**
 * Switches the SVG displayed on an event between an unlocked and locked lock
 * @param dynamicColor The color of the lock
 * @param unlockedSVG List of event properties, one of which contains whether an unlocked svg is needed
 * @returns {string} The svg to be added to the event
 */
function switchSVG(dynamicColor, unlockedSVG) {
    if (unlockedSVG[0] === "true") {
        return '<svg xmlns="http://www.w3.org/2000/svg" width="16px" style="color:' + dynamicColor + '"' +
            ' height="16px" fill="currentColor" class="bi-unlock-fill" viewBox="0 0 16 16">\n' +
            '  <path d="M11 1a2 2 0 0 0-2 2v4a2 2 0 0 1 2 2v5a2 2 0 0 1-2 2H3a2 2 0 0 1-2-2V9a2 2 0 0 1 ' +
            '2-2h5V3a3 3 0 0 1 6 0v4a.5.5 0 0 1-1 0V3a2 2 0 0 0-2-2z"/>\n' +
            '</svg>';
    } else {
        return '<svg xmlns="http://www.w3.org/2000/svg" width="16px" style="color:' + dynamicColor + '"' +
            ' height="16px" fill="currentColor" class="bi-lock-fill" viewBox="0 0 16 16">\n' +
            '<path d="M8 1a2 2 0 0 1 2 2v4H6V3a2 2 0 0 1 2-2zm3 6V3a3 3 0 0 0-6 0v4a2 2 0 0 0-2 2v5a2 ' +
            '2 0 0 0 2 2h6a2 2 0 0 0 2-2V9a2 2 0 0 0-2-2z"/>\n' +
            '</svg>';
    }
}

/**
 * A list of all the months
 * Used to update the calendar date title via indexing
 * @type {string[]} List<String> The months
 */
const months = [
    "January",
    "February",
    "March",
    "April",
    "May",
    "June",
    "July",
    "August",
    "September",
    "October",
    "November",
    "December"
];

/**
 * Turns the activity information into an array of dictionaries to be past into the calendar
 * @param events List<List<String>> Information for the users personal activities
 * @param teamEvents List<List<List<String>>> Information for the activities of the users teams
 * @param sports List<String> list of sports to filter by
 * @returns {*[]} A list of dictionaries
 */
function extractAllEvents(events, teamEvents, sports) {
    if (sports.length !== 0) {
        let allTeamEvents = extractEventsBySport(teamEvents, sports)
        let allPersonalEvents = extractPersonalEventsBySport(events, sports)
        return allPersonalEvents.concat(allTeamEvents)
    }
    let allTeamEvents = extractTeamEvents(teamEvents)
    let allPersonalEvents = extractEvents(events)
    return allPersonalEvents.concat(allTeamEvents)

}

/**
 * Turns the team activity information into an array of dictionaries to be past into the calendar
 * @param teamEvents List<List<List<String>>> Information regarding the team activities
 * @returns {*[]} A list of dictionaries
 */
function extractTeamEvents(teamEvents) {
    let teamEventsArray = []
    teamEvents.forEach(function (event) {
        teamEventsArray = teamEventsArray.concat(extractEvents(event));
    });
    return teamEventsArray
}

/**
 * Turns the team activity information into an array of dictionaries to be past into the calendar
 * @param teamEvents List<List<List<String>>> Information regarding the team activities
 * @param sports List<String> Sports in the Database
 * @returns {*[]} A list of dictionaries
 */
function extractEventsBySport(teamEvents, sports) {
    let sportEventsArray = []
    teamEvents.forEach(function (event) {
        if (sports.includes(event[0][8])) {
            sportEventsArray = sportEventsArray.concat(extractEvents(event));
        }
    });
    return sportEventsArray
}

/**
 * Turns the personal activity information into an array of dictionaries to be post into the
 * calendar
 * @param events List<List<List<String>>> Information regarding the personal activities
 * @param sports List<String> Sports in the Database
 * @returns {*[]} A list of dictionaries
 */
function extractPersonalEventsBySport(events, sports) {
    let sportEventsArray = []
    events.forEach(function (event) {
        if (sports.includes(event[8])) {
            sportEventsArray = sportEventsArray.concat(extractEvents(event));
        }
    });
    return sportEventsArray
}

/**
 * Turns the activity information into an array of dictionaries to be past into the calendar
 * @param events List<List<String>> Information regarding the activities
 * @returns {*[]} A list of dictionaries
 */
function extractEvents(events) {
    let eventsArray = [];
    if (events) {
        events.forEach(function (event) {

            let spansTwoDays = event[3].split("-")[2].split("T")[0] - event[2].split("-")[2].split("T")[0] !== 0
            let eventObject = {
                title: event[1] + " @ " + event[7],
                start: event[2],
                end: event[3],
                id: event[4],
                calendarId: 1,
                location: event[6],
                raw: [event[0], event[5], spansTwoDays],
                backgroundColor: event[0] === "true" ? 'green' : 'red'
            };
            eventsArray.push(eventObject);
        });
    }
    return eventsArray
}

/**
 * Adds an onClick handler to the "<-" button for the calendar
 * Changes the displayed month to the previous month
 * @param calendar the calendar element
 */
function addBackPagination(calendar) {
    // Previous month button event listener
    let prevMonthBtn = document.getElementById('prev-month-btn');
    prevMonthBtn.addEventListener('click', function () {
        calendar.prev();
        addMonthTitle(calendar)
    });
}

/**
 * Adds an onClick handler to the "->" button for the calendar
 * Changes the displayed month to the next month
 * @param calendar the calendar element
 */
function addNextPagination(calendar) {

    // Next month button event listener
    let nextMonthBtn = document.getElementById('next-month-btn');
    nextMonthBtn.addEventListener('click', function () {
        calendar.next();
        addMonthTitle(calendar)
    });
}

/**
 * Adds an onClick handler to the "Today" button for the calendar
 * Changes the displayed month to the current month
 * @param calendar the calendar element
 */
function addTodayLink(calendar) {

    // Next month button event listener
    let nextMonthBtn = document.getElementById('today-btn');
    nextMonthBtn.addEventListener('click', function () {
        calendar.setDate(new Date());
        addMonthTitle(calendar)
    });
}

/**
 * Adds a link to each event. Links to viewActivity page
 * @param calendar the calendar element
 */
function addEventLink(calendar) {

    calendar.on('clickEvent', function (event) {
        window.location.href = "viewActivity?id=" + event.event.id
    });
}

/**
 * Changes the Month and year header for the calendar
 * @param calendar the calendar element
 */
function addMonthTitle(calendar) {
    document.getElementById("currentMonth").textContent = months[calendar.getDate().getMonth()] + " " + calendar.getDate().getFullYear()
}