let selectedMeals = new Set()
let mealIds = []
let mealQuantities = []
/**
 * Adds event listeners for recommended foods
 */
document.addEventListener('DOMContentLoaded', function() {

    /**
     * Adds event listeners to turn flip the chevrons when a meal card is expanded
     */
    function setupCardListeners(cardId, chevronId) {
        let card = document.getElementById(cardId);
        let chevron = document.getElementById(chevronId);

        card.addEventListener('show.bs.collapse', function() {
            chevron.innerHTML = `
                <svg xmlns="http://www.w3.org/2000/svg" class="bi bi-chevron-up" viewBox="0 0 16 16">
                    <path fill-rule="evenodd" d="M7.646 4.646a.5.5 0 0 1 .708 0l6 6a.5.5 0 0 1-.708.708L8 5.707l-5.646 5.647a.5.5 0 0 1-.708-.708l6-6z"/>
                </svg>
                `;
        });

        card.addEventListener('hide.bs.collapse', function() {
            chevron.innerHTML = `
                <svg style="width: 18px; height: 18px;" xmlns="http://www.w3.org/2000/svg" id="chevvy" class="bi bi-chevron-down" fill="currentColor" viewBox="0 0 16 16">
                    <path fill-rule="evenodd" d="M1.646 4.646a.5.5 0 0 1 .708 0L8 10.293l5.646-5.647a.5.5 0 0 1 .708.708l-6 6a.5.5 0 0 1-.708 0l-6-6a.5.5 0 0 1 0-.708z"/>
                </svg>
            `;
        });
    }

    /**
     * Adds event listeners to turn the plus button into a tick when a meal is added
     */
    function setupTickListeners(tickId) {
        let tick = document.getElementById(tickId);
        let card = document.getElementById("meal-card-" + tickId.slice(tickId.length - 1))

        tick.addEventListener('click', function() {
            let classList = tick.children[0].classList
            if (classList.contains("bi-check2")) {
                tick.innerHTML = `
                    <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-plus-lg" viewBox="0 0 16 16">
                        <path fill-rule="evenodd" d="M8 2a.5.5 0 0 1 .5.5v5h5a.5.5 0 0 1 0 1h-5v5a.5.5 0 0 1-1 0v-5h-5a.5.5 0 0 1 0-1h5v-5A.5.5 0 0 1 8 2Z"/>
                    </svg>
                `;
                card.style.boxShadow = "none"
                selectedMeals.delete(card)
                toggleConfirmBtn()
            } else {
                tick.innerHTML = `
                    <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-check2" viewBox="0 0 16 16">
                      <path d="M13.854 3.646a.5.5 0 0 1 0 .708l-7 7a.5.5 0 0 1-.708 0l-3.5-3.5a.5.5 0 1 1 .708-.708L6.5 10.293l6.646-6.647a.5.5 0 0 1 .708 0z" style="stroke-width: 2; stroke: forestgreen"/>
                    </svg>
                `;
                card.style.boxShadow = "0px 0px 5px 0.5px green"
                selectedMeals.add(card)
                toggleConfirmBtn()
            }
        });

    }

    setupTickListeners('tick0');
    setupTickListeners('tick1');
    setupTickListeners('tick2');

    setupCardListeners('mealOne', 'chevronmealOne');
    setupCardListeners('mealTwo', 'chevronmealTwo');
    setupCardListeners('mealThree', 'chevronmealThree');
});

function toProfilePage() {
    window.location.href = getCorrectURL() + "/profilePage";
}

function displaySuccessToast() {
    const toast = document.getElementById("addedMealsToast")
    const bootstrapToast = new bootstrap.Toast(toast)
    bootstrapToast.show()
}

function saveMeals() {

    let meals = []
    let mealQuantities = []
    let activityId = parseInt(document.getElementById("activityId").value)

    selectedMeals.forEach(meal => {
        meals.push(parseInt(meal.classList[2]))
        mealQuantities.push(parseInt(meal.classList[3]))
    })

    let mealData = {
        mealIds: meals,
        mealQuantities: mealQuantities,
        activityId: activityId
    }

    fetch(`save-meals`, {
        method: 'POST',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'X-CSRF-TOKEN': TOKEN
        },
        body: JSON.stringify(mealData)
    })
        .then(response => {
            if (response.ok) {
                displaySuccessToast()
                return response.json();

            } else {
                displayErrors(response.json())
                throw new Error('Club not created!');
            }
        })
    document.getElementById("closeRecommendedMealsModal").click();
    document.getElementById("recommendedMealsButton").setAttribute("disabled", "true");
    document.getElementById("recommendedMealsButton").textContent = "Meal Selected"

}

function toggleConfirmBtn() {

    let confirmBtn = document.getElementById("confirmMealBtn")
    selectedMeals.size === 0 ? confirmBtn.disabled = true : confirmBtn.disabled = false
}