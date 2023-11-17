const DEFAULT_PORTION_SIZE_WEIGHT = 1
const DEFAULT_PORTION_SIZE_QUANTITY = 100
const ADD_FOOD_SUCCESSFUL = 1
const ADD_FOOD_UNSUCCESSFUL = 2
const PROTEIN = '203'
const ENERGY = '208'
const SUGAR = '269'
const FAT = '204'
const CARBOHYDRATES = '205'

let APIResponse = null;
let foodDict = {}
let currentMealIndex = -1
let nutritionDoughnutChart
let doughnutLabel
let currentPortionGramWeight = DEFAULT_PORTION_SIZE_WEIGHT
let currentFoodDescription = ""


const plugin = {
    id: 'customCanvasBackgroundColor',
    beforeDraw: (chart, args, options) => {
        const {ctx} = chart;
        ctx.save();
        ctx.globalCompositeOperation = 'destination-over';
        ctx.fillStyle = options.color || '#99ffff';
        ctx.fillRect(0, 0, chart.width, chart.height);
        ctx.restore();
    }
};

/**
 * Gets the spring security token and saves it as a global variable to be used in the post requests
 * @param csrfToken
 */
function createGlobal(csrfToken) {
    TOKEN = csrfToken
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
 * Uses a rest controller endpoint to query food from api
 * Delete when fully redundant
 */
async function searchFoods(query) {
    if (query.length >= 3 && !(foodList.hasOwnProperty(query))) {
        document.getElementById("loading").style.visibility = "visible"
         fetch(`${getCorrectURL()}/search-foods/${query}`)
            .then(response => {
                let hold = response.text()
                hold.then(async result => {
                    APIResponse = JSON.parse(result);
                    if (APIResponse.foods.length === 0) {
                        document.getElementById("noFoodResultLabel").style.display = "block"
                        document.getElementById("loading").style.visibility = "hidden"

                    } else {
                        document.getElementById("noFoodResultLabel").style.display = "none"
                    }
                    let fdcIds = APIResponse.foods.map(obj => obj.fdcId);
                    await getFoodDetails(fdcIds)
                    // let foods = APIResponse["foods"]
                    // for (let i = 0; i < foods.length; i++) {
                    //     foodList[foods[i]["description"]] = extractNutrients(foods[i])
                    // }
                })
            })
            .catch(error => {
                console.log(error);
            });
    }
}

/**
 * Queries API database with the user's search query through search-foods endpoint.
 * @param query search query retrieved from the html, string.
 * @return JSON string | print error message on console
 */
async function searchFoodDirect(query) {
    return await fetch(`${getCorrectURL()}/search-foods/${query}`)
        .then(res => {
            return res.json();
        })
        .catch(error => {
            console.log(error.toString());
        });
}

/**
 * Handles given search-food query results and sends information to
 * the correct sub-handler
 * @param queryResult
 */
function handleQueryResults(queryResult) {
    //if there is result
    document.getElementById("noFoodResultLabel").style.display = "none";
    //update dropdown

    for (let i = 0; i < queryResult.foods.length; i++) {
        foodDict[queryResult.foods[i].fdcId] = queryResult.foods[i]
    }
    foodSearchDropdown(queryResult);

}

/**
* Clears food search text field on click.
*/
function clearFoodSearch() {
    document.getElementById("foodSearch").value = "";
}

/**
 * Uses a rest controller endpoint to query food based on fdc ids
 */
async function getFoodDetails(fdcIds) {
        let encodedFdcIds = fdcIds.join(",")

        fetch(`${getCorrectURL()}/get-foods/${encodedFdcIds}`)
            .then(response => {
                let hold = response.text()
                hold.then(result => {
                    APIResponse = JSON.parse(result);
                    foodList = APIResponse
                    foodSearchDropdown(foodList)
                    currentMealIndex = getCurrentFoodIndex(currentFoodDescription)
                })
            })
            .catch(error => {
                console.log(error);
            });
}


/**
 * Uses a rest controller endpoint to query food based on fdc ids
 */
async function getFoodDetailsDirect(fdcIds) {
    let encodedFdcIds = fdcIds.join(",");
    let responseString = "";

    return await fetch(`${getCorrectURL()}/get-foods/${fdcIds}`)
        .then(response => {
            return response.json()
        })
        .catch(error => {
            console.log(error);
        });
}

/**
 * Extracts all the required nutrients from the APIResponse
 */
function extractNutrients(food) {
    let desiredNutrients = ["Protein", "Carbohydrate, by difference", "Energy", "Total lipid"
                            + " (fat)", "Sugars, total including NLEA"]
    return extractType(desiredNutrients, food)
}

/**
 * Extracts the protein from the APIResponse
 */
function extractType(desiredNutrients, food) {
    let dict = {};
    let nutrients = food["foodNutrients"]

    for (let i = 0; i < nutrients.length; i++) {
        dict["fdcId"] = food["fdcId"]
        if (desiredNutrients.includes(nutrients[i]["nutrientName"])) {
            dict[nutrients[i]["nutrientName"]] = nutrients[i]
        }
    }
    return dict
}

/**
 * Creates dropdown options retrieved from the given foodList
 * @param foodList
 */
function foodSearchDropdown(foodList) {
    const foods = foodList["foods"];
    let foodListLength = foods.length
    let options = '';

    for (let i = 0; i < foodListLength; i++) {
        let description = foods[i].description
        let fdcId = String(foods[i].fdcId).match(/\d+/)
        if (foods[i].description.includes('\'')) {
            options += `<option value="${description}"
                    id="${fdcId}" />`;
        } else {
            options += `<option value='${description}'
                    id="${fdcId}" />`;
        }
    }

    document.getElementById("loading").style.visibility = "hidden"
    document.getElementById("food_list").innerHTML = options;
}

/**
 * Updates food serving size options according to the given servingSizes param
 * @param servingSizes
 */
async function foodServingSizeSelect(servingSizes) {

    currentPortionGramWeight = DEFAULT_PORTION_SIZE_WEIGHT
    let options = '<option selected value="1,0">Grams</option>';
    for (let i = 0; i < servingSizes.length; i++) {
        options += `<option value='${servingSizes[i].gramWeight + "," + servingSizes[i].id}'>${servingSizes[i].modifier + " (" + servingSizes[i].gramWeight + "g)"}</option> `;
    }
    document.getElementById("servingSize").innerHTML = options;
}

/** Gets the nutrient value for the given nutrientId and foodItem
 *
 * @param nutrientId The id of the nutrient to get the value for
 * @param foodItem The JSON of foods to get the nutrient value from
 * @returns {number} The nutrient value
 */
function getNutrientValue(nutrientId, foodItem) {
    let nutrientIndex = foodItem["foodNutrients"].findIndex(obj => obj["nutrientNumber"] ===
    nutrientId);
    try {
        return foodItem["foodNutrients"][nutrientIndex]["value"] / 100;
    } catch (e) {
        return 0;
    }
}
/** Gets the nutrient value for the given nutrientId, foodList and foodIndex
 *
 * @param nutrientId The id of the nutrient to get the value for
 * @param foodList The list of foods to get the nutrient value from
 * @param foodIndex The index of the food in the foodList to get the nutrient value from
 * @returns {number} The nutrient value
 */
function getNutrientValueWithIndex(nutrientId, foodList, foodIndex) {
    let nutrientIndex = foodList[foodIndex].foodNutrients.findIndex(obj => obj.nutrient.number === nutrientId);
    try {
        return foodList[foodIndex].foodNutrients[nutrientIndex].amount / 100;
    } catch (e) {
        return 0;
    }
}



/** Gets the index of the food in the foodList with the given description
 *
 * @param description The description of the food to get the index of
 * @returns {number} The index of the food in the foodList
 */
function getCurrentFoodIndex(description) {
    return foodList.findIndex(obj => obj.description === description);
}

/** Sets the current meal to the meal selected in the food search input
 *
 * @param e The input element
 */
function setCurrentMeal(e)  {
    currentMealIndex = foodList.findIndex(obj => obj.description === e.value);
    currentFoodDescription = e.value
    const fdcIdInput = document.getElementById("fdcIdInput");
    if (fdcIdInput) {
        fdcIdInput.value = foodList[currentMealIndex].fdcId;
    }
    foodServingSizeSelect(foodList[currentMealIndex].foodPortions)
    document.getElementById("portionSizeNumber").value = 0
    displayNutrition();
}
/**
 * handles the change event for the food search input, updates the current meal
 * if the value of the input is in the foodList
 *
 * @param e The input element
 */
async function handleFoodSearchChange(e) {

    const currentList = document.getElementById("food_list").children;

    if (currentList.length === 0) {
        if (e.value.length >= 3) {
            document.getElementById("noFoodResultLabel").style.display = "none";
            document.getElementById("loading").style.visibility = "visible";
            let response = await searchFoodDirect(e.value);
            if (response["totalHits"] >= 1){
                handleQueryResults(response);
            } else {
                document.getElementById("noFoodResultLabel").style.display = "block";
                document.getElementById("loading").style.visibility = "hidden";
            }
        } else {
            document.getElementById("noFoodResultLabel").style.display = "block";
            document.getElementById("loading").style.visibility = "hidden";
        }
    } else {
        for (let i = 0; i < currentList.length; i++) {
            if (e.value === currentList[i].value) {
                let food = await getFoodDetailsDirect([(currentList[i].id)])
                await foodServingSizeSelect(food[0].foodPortions);
                document.getElementById("fdcIdInput").value = String(currentList[i].id)
                displayNutrition(currentList[i].id);
                break;
            } else if (i === (currentList.length - 1)) {
                hideAll();
                document.getElementById("noFoodResultLabel").style.display = "none";
                document.getElementById("loading").style.visibility = "visible";
                handleQueryResults(await searchFoodDirect(e.value));
            }
        }
    }
}

/** Sets the current portion gram weight to the portion selected in the input element
 *
 * @param e The input element
 */
function setCurrentPortionGramWeight(e) {
    weightPortionNumberPair = e.value.split(',')
    currentPortionGramWeight = weightPortionNumberPair[0]
    let portionSizeNumberInput = document.getElementById("portionSizeNumber");
    if (portionSizeNumberInput) {
        portionSizeNumberInput.value = weightPortionNumberPair[1]
    }
    quantityInput = document.getElementById("mealQuantity");
    if (currentPortionGramWeight[1] == 0) {
        quantityInput.value = DEFAULT_PORTION_SIZE_WEIGHT
    } else {
        quantityInput.value = 1
    }
    displayNutrition(document.getElementById("fdcIdInput").value)
}

/** Displays the nutrition doughnut chart and the nutrition labels
 */
function displayNutrition(selectedFood) {
    let foodName = document.getElementById(selectedFood).value;
    let currFood = foodDict[selectedFood];
    let protein;
    let calories;
    let carbohydrates;
    let sugar;
    let fat;
    let portionQuantity

    document.getElementById("foodNutritionOverview").style.display = "block"
    document.getElementById("nutritionLabels").style.visibility = "visible"
    document.getElementById("graphContainer").style.visibility = "visible"
    document.getElementById("loading").style.visibility = "hidden"
    document.getElementById("foodName").style.visibility = "visible"

    portionQuantity = document.getElementById("mealQuantity").value
    protein = currentPortionGramWeight * portionQuantity * getNutrientValue(PROTEIN, currFood) ;
    carbohydrates = currentPortionGramWeight * portionQuantity * getNutrientValue(CARBOHYDRATES, currFood) ;
    calories = currentPortionGramWeight * portionQuantity * getNutrientValue(ENERGY, currFood) ;
    sugar = currentPortionGramWeight * portionQuantity * getNutrientValue(SUGAR, currFood) ;
    fat = currentPortionGramWeight * portionQuantity * getNutrientValue(FAT, currFood);

    document.getElementById("foodName").innerHTML = foodName
    document.getElementById("protein").innerHTML = protein.toFixed(2) + 'g';
    document.getElementById("carbohydrates").innerHTML = carbohydrates.toFixed(2) + 'g';
    document.getElementById("sugar").innerHTML = sugar.toFixed(2) + 'g';
    document.getElementById("fat").innerHTML = fat.toFixed(2) + 'g';
    document.getElementById("addMealBtn").style.display = "block"
    document.getElementById("mealQuantity").style.display = "block"
    document.getElementById("mealQuantityLabel").style.display = "block"
    document.getElementById("servingSizeLabel").style.display = "block"
    document.getElementById("servingSize").style.display = "block"

    displayFoodNutritionChart(protein, calories, sugar, fat, carbohydrates)
}

/** Prevents the user from submitting the form by pressing enter
 *
 */
function preventEnterBtn() {
    const form = document.getElementById('mealForm');
    form.addEventListener('keypress', function(e) {
        if (e.keyCode === 13) {
            e.preventDefault();
        }
    });
}

function getThemeColour() {
    let themeCookie = readCookie('sel_theme')
    if (themeCookie === 'dark') {
        return `rgba(255,255,255,1)`
    } else {
        return `rgba(0,0,0,1)`
    }
}

/** Updates the details in the food nutrition chart
 *
 * @param protein The new protein value
 * @param calories The new calories value
 * @param sugar The new sugar value
 * @param fat The new fat value
 * @param carbohydrates The new carbohydrates value
 */
function displayFoodNutritionChart(protein, calories, sugar, fat, carbohydrates) {

    // Update the data
    nutritionDoughnutChart.data.datasets[0].data = [protein, sugar, fat, carbohydrates];

// Update the text
    if (doughnutLabel) {
        doughnutLabel.beforeDatasetsDraw = function(chart, args, pluginOptions) {
            // You are drawing logic with the updated text
            // For example:
            const {ctx, data} = chart;
            ctx.save();
            const xCoor = chart.getDatasetMeta(0).data[0].x;
            const yCoor = chart.getDatasetMeta(0).data[0].y;
            ctx.font = 'bold 20px sans-serif';
            ctx.fillStyle = getThemeColour();
            ctx.textAlign = 'center';
            ctx.textBaseline = 'middle';
            ctx.fontSize = '100';
            ctx.fillText(calories.toFixed(2), xCoor, yCoor - 5);
            ctx.font = '16px sans-serif';
            ctx.fillText("cal", xCoor, yCoor + 15);
        };
    }

    nutritionDoughnutChart.update();
}

/** Creates the doughnut chart for the displaying the food nutrition
 *
 */
function createFoodNutritionChart() {

    let ctx = document.getElementById('nutritionDoughnutChart').getContext('2d');

    const data = {
        datasets: [{
            data: [0, 0, 0, 0],
            backgroundColor: ['#FFCE56', '#451ebb', '#3fb4a4', '#FF0000'],
        }]
    };


    doughnutLabel = {
        id:'doughnutLabel',
        beforeDatasetsDraw(chart, args, pluginOptions) {
            const {ctx, data} = chart;
            ctx.save();
            const xCoor = chart.getDatasetMeta(0).data[0].x;
            const yCoor = chart.getDatasetMeta(0).data[0].y;
            ctx.font = 'bold 20px sans-serif';
            ctx.fillStyle = 'rgba(255,255,255,1';
            ctx.textAlign = 'center';
            ctx.textBaseline = 'middle';
            ctx.fontSize = '100';
            ctx.fillText(200, xCoor, yCoor - 5)
            ctx.font = '16px sans-serif';
            ctx.fillText("cal", xCoor, yCoor + 15)

        }
    }
    nutritionDoughnutChart = new Chart(ctx, {
        type: 'doughnut',
        data: data,
        options: {
            borderWidth: 1,
            radius: "75%",
            responsive: true,
            maintainAspectRatio: false,
            cutoutPercentage: 80, // Adjust this value for the size of the cutout
            legend: {
                display: false
            },
        },
        plugins: [doughnutLabel]
    });
}

/**
 * Shows the food added toast
 * @param showFoodAdded A number which determines which toast to
 show
 */
function showFoodAddedToast(showFoodAdded) {
    if (showFoodAdded === ADD_FOOD_SUCCESSFUL) {
        let toast = document.getElementById("foodAdded")
        let bootstrapToast = new bootstrap.Toast(toast)
        bootstrapToast.show()
    } else if (showFoodAdded === ADD_FOOD_UNSUCCESSFUL) {
        let toast = document.getElementById("foodAddedFailure")
        let bootstrapToast = new bootstrap.Toast(toast)
        bootstrapToast.show()
    }
}

/**
 * Gets the users nutritional data for the last 7 days
 * @param userId
 * @returns {Promise<void>}
 */
async function handle7DaysObject(userId) {
    let last7Days = await fetch(`${getCorrectURL()}/get-foods/last-7-days/${userId}`)
        .then(response => {
            if (response.ok) {
                return response.json();
            } else {
                throw new Error('Team not found');
            }
        });
    await renderTrendChart(last7Days)
    await renderEnergyUsage(last7Days)
}

/**
 * Used to set the x labels of the nutritional graphs to the last 7 days from today
 * @returns a list of the labels to be used in order
 */
function last7Days() {
    let result = [];
    let daysOfWeek = ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday'];

    for (let i = 0; i < 7; i++) {
        let d = new Date();
        d.setDate(d.getDate() - i);
        result.push(daysOfWeek[d.getDay()]);
    }

    return result.reverse();
}

/**
 * Render the calendar to display the weekly trend of food intake
 * @param last7days
 */
function renderTrendChart(last7days) {
    const labels = last7Days()
    const protein = last7days.map(x => x[PROTEIN]).reverse();
    const carb = last7days.map(x => x[CARBOHYDRATES]).reverse();
    const fat = last7days.map(x => x[FAT]).reverse();

    const proteinGraph = {
        labels: labels,
        datasets: [{
            label: 'Protein',
            data: protein,
            fill: false,
            borderColor: '#FFCE56',
            tension: 0.1
        },
            {
                label: 'Carbohydrate',
                data: carb,
                fill: false,
                borderColor: '#FF0000',
                tension: 0.1
            },
            {
                label: 'Fat',
                data: fat,
                fill: false,
                borderColor: '#3fb4a4',
                tension: 0.1
            }]
    };


    // Get a reference to the canvas element
    const ctx = document.getElementById('weeklyNutrition').getContext('2d');
    const existingChart = Chart.getChart(ctx)
    if (existingChart) {
        existingChart.destroy();
    }
    // Define chart options (optional)
    const options = {
        responsive: true, // Make the chart responsive
        maintainAspectRatio: false, // Don't maintain aspect ratio
    };

    // Create the line chart
    const myLineChart = new Chart(ctx, {
        type: 'line',
        data: proteinGraph,
        options: options,
    });
    myLineChart.update()
}

/**
 * Render the graph to display the weekly trend of energy intake
 * @param data the data to be displayed on the graph
 */
function renderEnergyUsage(data){
    const energy = data.map(x => x[ENERGY]).reverse();
    const sugar = data.map(x => x[SUGAR]).reverse();
    const labels = last7Days()
    const energyGraph = {
        labels: labels,
        datasets: [{
            label: 'Energy',
            data: energy,
            fill: false,
            cubicInterpolationMode: 'monotone',
            borderColor: 'rgb(9,101,4)',
            tension: 0.4
        },
            {
                label: 'Sugar',
                data: sugar,
                fill: false,
                cubicInterpolationMode: 'monotone',
                borderColor: '#451ebb',
                tension: 0.4
            }]
    };
    const ctx = document.getElementById('energy').getContext('2d');
    const existingChart = Chart.getChart(ctx)
    if (existingChart) {
        existingChart.destroy();
    }

    const options = {
        responsive: true, // Make the chart responsive
        maintainAspectRatio: false, // Don't maintain aspect ratio
    };

    // Create the line chart
    const myLineChart = new Chart(ctx, {
        type: 'line',
        data: energyGraph,
        options: options,
    });
    myLineChart.render()
}


/**
 * Creates recently added meals on the page
 * @param recentFoods JSON string
 */
async function loadRecent(recentFoods) {

    //check if recent foods do contain information
    if (recentFoods === "[]") {
        return null;
    }

    const recentJson = JSON.parse(recentFoods);

    const fdcIds = [];

    for (let i = 0; i < recentJson.length; i++) {
        fdcIds.push(recentJson[i].fdcId);
    }
    let recentFoodDiv = document.getElementById("recentFoodsDiv");
    while (recentFoodDiv.firstChild) {
        recentFoodDiv.removeChild(recentFoodDiv.firstChild);
    }
    //retrieve from api and create
    await getFoodDetailsDirect(fdcIds)
        .then((result) => {
            for (let j = 0; j < result.length; j++) {

                const colDesc = document.createElement("a");
                colDesc.setAttribute("class", "col-6 text-center meal");
                colDesc.setAttribute("href", "#label-" +j);
                colDesc.setAttribute("data-bs-toggle", "collapse");
                colDesc.setAttribute("id","recent-" + j);
                colDesc.innerHTML = result[j]["description"] + " - " + (getWeight(recentJson[j]["portionSizeNumber"], result[j], recentJson[j]["quantity"] )+ " g");

                const foodLabel = createLabels([result[j]], recentJson[j]["quantity"],
                    0, recentJson[j]["portionSizeNumber"], j)
                recentFoodDiv.appendChild(colDesc);
                recentFoodDiv.appendChild(foodLabel);
            }
        });
}
/**
 * Get the weight in grams of a specified food portion.
 *
 * @param {number} portionSizeNum - The ID of the food portion.
 * @param {object} food - The food object containing an array of food portions.
 * @param {number} quantity - The default quantity in grams.
 * @returns {number} The weight in grams of the specified food portion. Returns the default quantity if the portion is not found.
 */
function getWeight(portionSizeNum, food, quantity) {
    if (portionSizeNum !== 0) {
        for (let portionType = 0; portionType < food["foodPortions"].length; portionType++) {
            if (portionSizeNum === food["foodPortions"][portionType]["id"]) {
                quantity = quantity * food["foodPortions"][portionType]["gramWeight"];
            }
        }
    }
    return quantity
}

/**
 * Create a label element for displaying nutrient information of a food item.
 *
 * @param {object} food - The food object containing nutrient information.
 * @param {number} quantity - The quantity of the food item.
 * @param {number} currentMealIndex - The index of the current meal.
 * @param {number} portionSizeNum - The ID of the selected portion size.
 * @param {number} labelIndex - The label identifier index.
 * @returns {HTMLElement} A dynamically created HTML label element with nutrient information.
 */
function createLabels(food, quantity, currentMealIndex, portionSizeNum, labelIndex) {
    quantity = getWeight(portionSizeNum, food[0], quantity);
    let protein = Math.round(quantity * getNutrientValueWithIndex(PROTEIN, food, currentMealIndex)*100)/100;
    let carbohydrates = Math.round(quantity * getNutrientValueWithIndex(CARBOHYDRATES, food, currentMealIndex)*100)/100;
    let calories = Math.round(quantity * getNutrientValueWithIndex(ENERGY, food, currentMealIndex)*100)/100;
    let sugar = Math.round( quantity * getNutrientValueWithIndex(SUGAR, food, currentMealIndex)*100)/100;
    let fat = Math.round(quantity * getNutrientValueWithIndex(FAT, food, currentMealIndex)*100)/100;
    const foodLabel = document.createElement("div");
    foodLabel.setAttribute("class", "collapse row");
    foodLabel.setAttribute("id", "label-" + labelIndex);

    const labels = ["Protein", "Sugar", "Fat", "Carbs", "Calories"];
    const colors = ["#FFCE56", "#451ebb", "#3fb4a4", "#FF0000", "#ED6B86"];
    const values = [protein, sugar, fat, carbohydrates, calories];

    let html = "";

    for (let i = 0; i < labels.length; i++) {
        html += `
                <div class="col-2">
                  <div class="d-flex flex-column">
                    <p style="color: ${colors[i]}" class="mb-0">${labels[i]}</p>
                    <p>${values[i]}</p>
                  </div>
                </div>
              `;
    }
    foodLabel.innerHTML = html;
    return foodLabel;
}

/**
 * Hides all labels and such
 */
function hideAll() {
    document.getElementById("foodNutritionOverview").style.display = "none"
    document.getElementById("nutritionLabels").style.visibility = "hidden"
    document.getElementById("graphContainer").style.visibility = "hidden"
    document.getElementById("addMealBtn").style.display = "none"
    document.getElementById("mealQuantity").style.display = "none"
    document.getElementById("mealQuantityLabel").style.display = "none"
    document.getElementById("servingSizeLabel").style.display = "none"
    document.getElementById("servingSize").style.display = "none"
    document.getElementById("foodName").style.visibility = "hidden"
}

/**
 * Submits the food item
 * @param event submission event to prevent default
 */
async function submitFood(event) {
    event.preventDefault();
    const formData = new FormData()
    formData.append('quantity', document.getElementById("mealQuantity").value)
    formData.append('fdcId', document.getElementById("fdcIdInput").value)
    formData.append('portionSizeNumber', document.getElementById("portionSizeNumber").value)
    await fetch("addFood", {
        method: 'POST',
        headers: {
            'X-CSRF-TOKEN': TOKEN
        },
        body: formData

    }).then(async response => {
        if (response.status === 200) {
            loadRecent(await response.text())
            handle7DaysObject(userId);
            showFoodAddedToast(ADD_FOOD_SUCCESSFUL)
        } else {
            showFoodAddedToast(ADD_FOOD_UNSUCCESSFUL)
        }});
    let addFoodModal = document.getElementById('addFoodModal');
    let modal = bootstrap.Modal.getInstance(addFoodModal)
    modal.hide();
}
