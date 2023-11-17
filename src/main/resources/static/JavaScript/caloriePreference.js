/**
 * Uses a rest controller endpoint to update the calorie preference in the database
 */
async function updateCaloriePreference() {
    let caloriePreference = document.getElementById("caloriePreference").value

    let calorieData = {
        caloriePreference
    }

    const response = await fetch(`${getCorrectURL()}/update-calorie-preference`, {
        method: 'POST',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'X-CSRF-TOKEN': TOKEN
        },
        body: JSON.stringify(calorieData)
    })
        if (response.ok) {
            let toast = document.getElementById("calorieIntakeUpdateValid")
            let bootstrapToast = new bootstrap.Toast(toast)
            bootstrapToast.show()
            return response.json();
        } else {
            let toast = document.getElementById("calorieIntakeUpdateInvalid")
            let bootstrapToast = new bootstrap.Toast(toast)
            bootstrapToast.show()
            displayErrors(response.json())
            throw new Error('Calorie Intake Preference Not Updated');
        }
}