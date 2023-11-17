const MAX_FILE_SIZE = 10 * 1000 * 1000; // 10MB
let TOKEN = null;

const ALLOWED_MIME_TYPES = [
    "image/png",
    "image/jpeg",
    "image/jpg",
    "image/svg",
    "image/svg+xml",
    "image/gif"
];

function handleUpload(event) {

    // raith told me to fabian
    const file = event.target.files[0];
    const reader = new FileReader();
    // Check if file is an image
    if (!file.type.startsWith("image/")) {
        alert("File must be an image");
        event.target.value = "";
        return;
    }

    // Check if file is an allowed type
    if (!ALLOWED_MIME_TYPES.includes(file.type)) {
        alert("Image of wrong type, needs to be svg, png, jpeg");
        event.target.value = "";
        return;
    }

    // Check if file is not too large
    if (file.size > MAX_FILE_SIZE) {
        alert("Image size cannot exceed 10MB. Please reselect a smaller file");
        event.target.value = "";
        return;
    }
    reader.readAsDataURL(file);
    reader.onloadend = () => {
        document.getElementById("picture-id").src = reader.result
    };
    changeSVGS()
}

async function uploadImage(id, typeString) {
    const formData = new FormData()
    formData.append('image', document.getElementById("profilePicture").files[0])
    formData.append('id', id)
    formData.append('type', typeString)
    await fetch("uploadImage", {
        method: 'POST',
        headers: {
            'X-CSRF-TOKEN': TOKEN
        },
        body: formData
    })
    changeSVGS()
}

function changeSVGS() {
    let tick = document.getElementById("profilePictureLabelTick");
    if (tick) {
        let plus = document.getElementById("profilePictureLabelPlus")
        if (plus.style.display === "inline-block") {
            plus.style.display = "none"
            tick.style.display = "inline-block"
        } else {
            plus.style.display = "inline-block"
            tick.style.display = "none"
        }
    }
}
