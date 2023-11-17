
const badSvg = `<svg class="bi bi-exclamation-diamond-fill"
                                                     fill="#D9534F"
                                                     height="48" viewBox="0 0 16 16"
                                                     width="48"
                                                     xmlns="http://www.w3.org/2000/svg">
                                                    <path d="M9.05.435c-.58-.58-1.52-.58-2.1 0L.436 6.95c-.58.58-.58 1.519 0 2.098l6.516 6.516c.58.58 1.519.58 2.098 0l6.516-6.516c.58-.58.58-1.519 0-2.098L9.05.435zM8 4c.535 0 .954.462.9.995l-.35 3.507a.552.552 0 0 1-1.1 0L7.1 4.995A.905.905 0 0 1 8 4zm.002 6a1 1 0 1 1 0 2 1 1 0 0 1 0-2z"/>
                                                </svg>`
// JavaScript dictionary (object) with category as the key and icon as the value
let categoryIcons = {
    "CYCLING": '<svg fill="currentColor" xmlns="http://www.w3.org/2000/svg" height="48" viewBox="0 0 640 512"><!--! Font Awesome Free' +
        ' 6.4.2 by @fontawesome - https://fontawesome.com License - https://fontawesome.com/license (Commercial License) Copyright 2023 Fonticons, Inc. --><path d="M400 96a48 48 0 1 0 0-96 48 48 0 1 0 0 96zm27.2 64l-61.8-48.8c-17.3-13.6-41.7-13.8-59.1-.3l-83.1 64.2c-30.7 23.8-28.5 70.8 4.3 91.6L288 305.1V416c0 17.7 14.3 32 32 32s32-14.3 32-32V288c0-10.7-5.3-20.7-14.2-26.6L295 232.9l60.3-48.5L396 217c5.7 4.5 12.7 7 20 7h64c17.7 0 32-14.3 32-32s-14.3-32-32-32H427.2zM56 384a72 72 0 1 1 144 0A72 72 0 1 1 56 384zm200 0A128 128 0 1 0 0 384a128 128 0 1 0 256 0zm184 0a72 72 0 1 1 144 0 72 72 0 1 1 -144 0zm200 0a128 128 0 1 0 -256 0 128 128 0 1 0 256 0z"/></svg>',
    "RUNNING": '<svg fill="currentColor" xmlns="http://www.w3.org/2000/svg" height="48" viewBox="0 0 448 512"><!--! Font Awesome Free' +
        ' 6.4.2 by @fontawesome - https://fontawesome.com License - https://fontawesome.com/license (Commercial License) Copyright 2023 Fonticons, Inc. --><path d="M320 48a48 48 0 1 0 -96 0 48 48 0 1 0 96 0zM125.7 175.5c9.9-9.9 23.4-15.5 37.5-15.5c1.9 0 3.8 .1 5.6 .3L137.6 254c-9.3 28 1.7 58.8 26.8 74.5l86.2 53.9-25.4 88.8c-4.9 17 5 34.7 22 39.6s34.7-5 39.6-22l28.7-100.4c5.9-20.6-2.6-42.6-20.7-53.9L238 299l30.9-82.4 5.1 12.3C289 264.7 323.9 288 362.7 288H384c17.7 0 32-14.3 32-32s-14.3-32-32-32H362.7c-12.9 0-24.6-7.8-29.5-19.7l-6.3-15c-14.6-35.1-44.1-61.9-80.5-73.1l-48.7-15c-11.1-3.4-22.7-5.2-34.4-5.2c-31 0-60.8 12.3-82.7 34.3L57.4 153.4c-12.5 12.5-12.5 32.8 0 45.3s32.8 12.5 45.3 0l23.1-23.1zM91.2 352H32c-17.7 0-32 14.3-32 32s14.3 32 32 32h69.6c19 0 36.2-11.2 43.9-28.5L157 361.6l-9.5-6c-17.5-10.9-30.5-26.8-37.9-44.9L91.2 352z"/></svg>',
    "FITNESS EQUIPMENT": '<svg fill="currentColor" xmlns="http://www.w3.org/2000/svg" height="48" viewBox="0 0 640 512"><!--! FontAwesome' +
        ' Free 6.4.2 by @fontawesome - https://fontawesome.com License - https://fontawesome.com/license (Commercial License) Copyright 2023 Fonticons, Inc. --><path d="M96 64c0-17.7 14.3-32 32-32h32c17.7 0 32 14.3 32 32V224v64V448c0 17.7-14.3 32-32 32H128c-17.7 0-32-14.3-32-32V384H64c-17.7 0-32-14.3-32-32V288c-17.7 0-32-14.3-32-32s14.3-32 32-32V160c0-17.7 14.3-32 32-32H96V64zm448 0v64h32c17.7 0 32 14.3 32 32v64c17.7 0 32 14.3 32 32s-14.3 32-32 32v64c0 17.7-14.3 32-32 32H544v64c0 17.7-14.3 32-32 32H480c-17.7 0-32-14.3-32-32V288 224 64c0-17.7 14.3-32 32-32h32c17.7 0 32 14.3 32 32zM416 224v64H224V224H416z"/></svg>',
    "SWIMMING": '<svg fill="currentColor" xmlns="http://www.w3.org/2000/svg" height="48" viewBox="0 0 576 512"><!--! Font Awesome Free' +
        ' 6.4.2 by @fontawesome - https://fontawesome.com License - https://fontawesome.com/license (Commercial License) Copyright 2023 Fonticons, Inc. --><path d="M309.5 178.4L447.9 297.1c-1.6 .9-3.2 2-4.8 3c-18 12.4-40.1 20.3-59.2 20.3c-19.6 0-40.8-7.7-59.2-20.3c-22.1-15.5-51.6-15.5-73.7 0c-17.1 11.8-38 20.3-59.2 20.3c-10.1 0-21.1-2.2-31.9-6.2C163.1 193.2 262.2 96 384 96h64c17.7 0 32 14.3 32 32s-14.3 32-32 32H384c-26.9 0-52.3 6.6-74.5 18.4zM160 160A64 64 0 1 1 32 160a64 64 0 1 1 128 0zM306.5 325.9C329 341.4 356.5 352 384 352c26.9 0 55.4-10.8 77.4-26.1l0 0c11.9-8.5 28.1-7.8 39.2 1.7c14.4 11.9 32.5 21 50.6 25.2c17.2 4 27.9 21.2 23.9 38.4s-21.2 27.9-38.4 23.9c-24.5-5.7-44.9-16.5-58.2-25C449.5 405.7 417 416 384 416c-31.9 0-60.6-9.9-80.4-18.9c-5.8-2.7-11.1-5.3-15.6-7.7c-4.5 2.4-9.7 5.1-15.6 7.7c-19.8 9-48.5 18.9-80.4 18.9c-33 0-65.5-10.3-94.5-25.8c-13.4 8.4-33.7 19.3-58.2 25c-17.2 4-34.4-6.7-38.4-23.9s6.7-34.4 23.9-38.4c18.1-4.2 36.2-13.3 50.6-25.2c11.1-9.4 27.3-10.1 39.2-1.7l0 0C136.7 341.2 165.1 352 192 352c27.5 0 55-10.6 77.5-26.1c11.1-7.9 25.9-7.9 37 0z"/></svg>',
    "WALKING": '<svg fill="currentColor" xmlns="http://www.w3.org/2000/svg" height="48" viewBox="0 0 320 512"><!--! Font Awesome Free' +
        ' 6.4.2 by @fontawesome - https://fontawesome.com License - https://fontawesome.com/license (Commercial License) Copyright 2023 Fonticons, Inc. --><path d="M160 48a48 48 0 1 1 96 0 48 48 0 1 1 -96 0zM126.5 199.3c-1 .4-1.9 .8-2.9 1.2l-8 3.5c-16.4 7.3-29 21.2-34.7 38.2l-2.6 7.8c-5.6 16.8-23.7 25.8-40.5 20.2s-25.8-23.7-20.2-40.5l2.6-7.8c11.4-34.1 36.6-61.9 69.4-76.5l8-3.5c20.8-9.2 43.3-14 66.1-14c44.6 0 84.8 26.8 101.9 67.9L281 232.7l21.4 10.7c15.8 7.9 22.2 27.1 14.3 42.9s-27.1 22.2-42.9 14.3L247 287.3c-10.3-5.2-18.4-13.8-22.8-24.5l-9.6-23-19.3 65.5 49.5 54c5.4 5.9 9.2 13 11.2 20.8l23 92.1c4.3 17.1-6.1 34.5-23.3 38.8s-34.5-6.1-38.8-23.3l-22-88.1-70.7-77.1c-14.8-16.1-20.3-38.6-14.7-59.7l16.9-63.5zM68.7 398l25-62.4c2.1 3 4.5 5.8 7 8.6l40.7 44.4-14.5 36.2c-2.4 6-6 11.5-10.6 16.1L54.6 502.6c-12.5 12.5-32.8 12.5-45.3 0s-12.5-32.8 0-45.3L68.7 398z"/></svg>',
    "HIKING": '<svg fill="currentColor" xmlns="http://www.w3.org/2000/svg" height="48" viewBox="0 0 384 512"><!--! Font Awesome Free' +
        ' 6.4.2 by @fontawesome - https://fontawesome.com License - https://fontawesome.com/license (Commercial License) Copyright 2023 Fonticons, Inc. --><path d="M192 48a48 48 0 1 1 96 0 48 48 0 1 1 -96 0zm51.3 182.7L224.2 307l49.7 49.7c9 9 14.1 21.2 14.1 33.9V480c0 17.7-14.3 32-32 32s-32-14.3-32-32V397.3l-73.9-73.9c-15.8-15.8-22.2-38.6-16.9-60.3l20.4-84c8.3-34.1 42.7-54.9 76.7-46.4c19 4.8 35.6 16.4 46.4 32.7L305.1 208H336V184c0-13.3 10.7-24 24-24s24 10.7 24 24v55.8c0 .1 0 .2 0 .2s0 .2 0 .2V488c0 13.3-10.7 24-24 24s-24-10.7-24-24V272H296.6c-16 0-31-8-39.9-21.4l-13.3-20zM81.1 471.9L117.3 334c3 4.2 6.4 8.2 10.1 11.9l41.9 41.9L142.9 488.1c-4.5 17.1-22 27.3-39.1 22.8s-27.3-22-22.8-39.1zm55.5-346L101.4 266.5c-3 12.1-14.9 19.9-27.2 17.9l-47.9-8c-14-2.3-22.9-16.3-19.2-30L31.9 155c9.5-34.8 41.1-59 77.2-59h4.2c15.6 0 27.1 14.7 23.3 29.8z"/></svg>',
    "TEAM SPORTS": '<svg fill="currentColor" xmlns="http://www.w3.org/2000/svg" height="48" viewBox="0 0 640 512"><!--! Font Awesome Free' +
        ' 6.4.2 by @fontawesome - https://fontawesome.com License - https://fontawesome.com/license (Commercial License) Copyright 2023 Fonticons, Inc. --><path d="M72 88a56 56 0 1 1 112 0A56 56 0 1 1 72 88zM64 245.7C54 256.9 48 271.8 48 288s6 31.1 16 42.3V245.7zm144.4-49.3C178.7 222.7 160 261.2 160 304c0 34.3 12 65.8 32 90.5V416c0 17.7-14.3 32-32 32H96c-17.7 0-32-14.3-32-32V389.2C26.2 371.2 0 332.7 0 288c0-61.9 50.1-112 112-112h32c24 0 46.2 7.5 64.4 20.3zM448 416V394.5c20-24.7 32-56.2 32-90.5c0-42.8-18.7-81.3-48.4-107.7C449.8 183.5 472 176 496 176h32c61.9 0 112 50.1 112 112c0 44.7-26.2 83.2-64 101.2V416c0 17.7-14.3 32-32 32H480c-17.7 0-32-14.3-32-32zm8-328a56 56 0 1 1 112 0A56 56 0 1 1 456 88zM576 245.7v84.7c10-11.3 16-26.1 16-42.3s-6-31.1-16-42.3zM320 32a64 64 0 1 1 0 128 64 64 0 1 1 0-128zM240 304c0 16.2 6 31 16 42.3V261.7c-10 11.3-16 26.1-16 42.3zm144-42.3v84.7c10-11.3 16-26.1 16-42.3s-6-31.1-16-42.3zM448 304c0 44.7-26.2 83.2-64 101.2V448c0 17.7-14.3 32-32 32H288c-17.7 0-32-14.3-32-32V405.2c-37.8-18-64-56.5-64-101.2c0-61.9 50.1-112 112-112h32c61.9 0 112 50.1 112 112z"/></svg>',
    "RACKET SPORTS": '<svg fill="currentColor" height="48" width="48" id="Capa_1"' +
        ' xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" viewBox="0 0 412.425 412.425" xml:space="preserve"><path d="M412.425,108.933c0-30.529-10.941-58.18-30.808-77.86C361.776,11.418,333.91,0.593,303.153,0.593  c-41.3,0-83.913,18.749-116.913,51.438c-30.319,30.034-48.754,68.115-51.573,105.858c-0.845,5.398-1.634,11.13-2.462,17.188  c-4.744,34.686-10.603,77.415-34.049,104.503c-2.06,0.333-3.981,1.295-5.476,2.789L7.603,367.447  c-10.137,10.138-10.137,26.632,0,36.77c4.911,4.911,11.44,7.615,18.385,7.615s13.474-2.705,18.386-7.617l85.06-85.095  c1.535-1.536,2.457-3.448,2.784-5.438c27.087-23.461,69.829-29.322,104.524-34.068c6.549-0.896,12.734-1.741,18.508-2.666  c1.434-0.23,2.743-0.76,3.885-1.507c36.253-4.047,72.464-21.972,101.325-50.562C393.485,192.166,412.425,149.905,412.425,108.933z   M145.476,218.349c4.984,10.244,11.564,19.521,19.608,27.49c8.514,8.434,18.51,15.237,29.576,20.262  c-25.846,5.238-52.769,13.823-73.415,30.692l-6.216-6.216C131.639,270.246,140.217,243.831,145.476,218.349z M30.23,390.075  c-1.133,1.133-2.64,1.757-4.242,1.757c-1.603,0-3.109-0.624-4.243-1.757c-2.339-2.339-2.339-6.146,0-8.485l78.006-78.007  l8.469,8.469L30.23,390.075z M243.559,256.318c-0.002,0-0.008,0-0.011,0c-25.822-0.003-48.087-8.54-64.389-24.688  c-16.279-16.126-24.883-38.136-24.883-63.652c0-2.596,0.1-5.201,0.276-7.808c0.023-0.143,0.045-0.295,0.068-0.438  c0.11-0.685,0.147-1.364,0.117-2.031c2.87-32.422,19.121-65.253,45.579-91.461c29.284-29.009,66.767-45.646,102.837-45.646  c25.819,0,48.085,8.537,64.389,24.689c16.279,16.126,24.883,38.136,24.883,63.651c-0.001,35.672-16.781,72.755-46.04,101.739  C317.1,239.682,279.624,256.319,243.559,256.318z"/></svg>',
    "OTHER": '<svg width="48" height="48" viewBox="0 0 60 60" xmlns="http://www.w3.org/2000/svg"><rect' +
    ' fill="currentColor" fill-opacity="0%" height="60" rx="10" width="60"/><path d="M30,8A22,22,0,1,0,52,30,22,22,0,0,0,30,8Zm0,38A16,16,0,1,1,46,30,16,16,0,0,1,30,46Z" fill="#f2c4bb"/><path d="M30,53.5A23.5,23.5,0,1,1,53.5,30,23.527,23.527,0,0,1,30,53.5Zm0-44A20.5,20.5,0,1,0,50.5,30,20.523,20.523,0,0,0,30,9.5Zm0,38A17.5,17.5,0,1,1,47.5,30,17.521,17.521,0,0,1,30,47.5Zm0-32A14.5,14.5,0,1,0,44.5,30,14.517,14.517,0,0,0,30,15.5Z" fill="#f28080"/><path d="M30,15.5a1.5,1.5,0,0,1,0-3,1.5,1.5,0,0,0,0-3,1.5,1.5,0,0,1,0-3,4.5,4.5,0,0,1,0,9Z" fill="#ffafc5"/><path d="M30,14A16,16,0,1,0,46,30,16,16,0,0,0,30,14Zm0,26A10,10,0,1,1,40,30,10,10,0,0,1,30,40Z" fill="#eff28d"/><path d="M30,47.5A17.5,17.5,0,1,1,47.5,30,17.521,17.521,0,0,1,30,47.5Zm0-32A14.5,14.5,0,1,0,44.5,30,14.517,14.517,0,0,0,30,15.5Zm0,26A11.5,11.5,0,1,1,41.5,30,11.513,11.513,0,0,1,30,41.5Zm0-20A8.5,8.5,0,1,0,38.5,30,8.51,8.51,0,0,0,30,21.5Z" fill="#f2bf80"/><path d="M30,21.5a1.5,1.5,0,0,1,0-3,1.5,1.5,0,0,0,0-3,1.5,1.5,0,0,1,0-3,4.5,4.5,0,0,1,0,9Z" fill="#f2bf80"/><path d="M30,20A10,10,0,1,0,40,30,10,10,0,0,0,30,20Zm0,14a4,4,0,1,1,4-4A4,4,0,0,1,30,34Z" fill="#c1f7fd"/><path d="M30,41.5A11.5,11.5,0,1,1,41.5,30,11.513,11.513,0,0,1,30,41.5Zm0-20A8.5,8.5,0,1,0,38.5,30,8.51,8.51,0,0,0,30,21.5Zm0,14A5.5,5.5,0,1,1,35.5,30,5.506,5.506,0,0,1,30,35.5Zm0-8A2.5,2.5,0,1,0,32.5,30,2.5,2.5,0,0,0,30,27.5Z" fill="#7bcdd1"/><path d="M30,27.5a1.5,1.5,0,0,1,0-3,1.5,1.5,0,0,0,0-3,1.5,1.5,0,0,1,0-3,4.5,4.5,0,0,1,0,9Z" fill="#7bcdd1"/><path d="M8,31.5A1.5,1.5,0,0,1,6.5,30,23.527,23.527,0,0,1,30,6.5a1.5,1.5,0,0,1,0,3A20.523,20.523,0,0,0,9.5,30,1.5,1.5,0,0,1,8,31.5Z" fill="#ffafc5"/></svg>',
    // Add more categories and icons here
    "WINTER SPORTS" : `<svg fill="currentColor" id="Capa_1" xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink"
                           width="48" height="48" viewBox="0 0 473.805 473.805"
                           xml:space="preserve">
<g>
	<g>
		<path d="M103.007,148.388l-71.996-22.386c-12.603-3.93-26,3.107-29.931,15.73c-3.911,12.604,3.127,26.01,15.74,29.921
			l81.31,25.283c2.314,0.727,4.705,1.081,7.096,1.081c3.758,0,7.497-0.89,10.901-2.63l45.221-23.17l22.51,77.771
			c-2.438,0.832-4.781,1.951-6.942,3.414l-45.125,30.801c-12.766,8.712-16.333,25.972-8.071,39.034l45.776,72.312l-50.873,7.774
			c-37.131,5.671-62.596,23.495-56.83,39.79c5.738,16.294,40.517,25.044,77.667,19.507l262.701-39.015
			c37.149-5.527,69.165-24.595,71.508-42.601c2.343-17.997-25.856-27.99-62.998-22.319l-49.342,7.545l-51.848-102.128
			c-0.316-0.611-0.708-1.186-1.062-1.778l-27.923-110.438l64.106-7.038c5.432-0.593,10.51-3.041,14.354-6.933l63.237-63.744
			c9.304-9.371,9.247-24.508-0.134-33.803c-9.362-9.304-24.499-9.247-33.804,0.134l-57.289,57.738l-64.681,7.105
			c-1.683,0.182-3.299,0.555-4.839,1.052l-107.989,18.58c-3.318,0.153-6.636,0.947-9.772,2.563L103.007,148.388z M201.932,305.796
			h64.346l35.324,69.567l-70.208,10.729l-44.389-70.122L201.932,305.796z"/>
        <circle cx="194.721" cy="57.344" r="47.822"/>
	</g>
</g>
</svg>`,
    "WATER SPORTS" : `<svg fill="#000000" height="48" width="48" id="Layer_1" xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink"
                         viewBox="0 0 256 256" xml:space="preserve">
<g id="XMLID_8_">
	<path id="XMLID_12_" d="M253.4,141.2c-6.4-19.6-31.4-20.8-54.5-10.8c-4,1.7-9.2,3.2-15.2,4.3l-4.7-18.2c3.8-1.9,6.2-5.9,6-10.4
		c-0.3-6.1-5.4-10.9-11.5-10.6l-3.4-13.4c3.4-2.6,5.2-6.9,4.2-11.3c-1.4-6-7.3-9.8-13.3-8.4c0,0-48.7,0.4-64,4.5
		c-15.2,4.1-20.4,29.7-20.4,29.7L63.3,140H0v20h168.3C187.9,160,239.3,154.8,253.4,141.2z M124.5,85.9l37.8-1.6l3,11.6
		c-10.2,0.5-28.4,1.4-44.6,2.4L124.5,85.9z M114,120.5l57-2.6l4.7,18.2c-23.3,3.4-53.4,3.8-67.7,3.9L114,120.5z"/>
    <path id="XMLID_14_" d="M220.4,206c-5.7,0-11.2,1.3-16,3.6c-0.4,0.2-0.8,0.3-1.1,0.4l-4.8-18.6c0.9-0.3,1.7-0.6,2.6-1
		c6.1-2.8,12.9-4.3,19.5-4.3c0,0,34.5-0.4,34.5-34.2c0-1.3-0.1-2.8-0.3-4.1c-21.5,15.6-79.1,18.2-86.4,18.2H0v27
		c4,0,8.2-0.9,11.9-2.6c6.1-2.8,12.8-4.3,19.5-4.3c6.6,0,13.3,1.5,19.3,4.3c4,1.8,8,2.6,12.2,2.6c4.2,0,8.3-0.9,12-2.6
		c6.1-2.8,12.9-4.3,19.5-4.3c6.7,0,13.3,1.4,19.4,4.3c3.8,1.8,8,2.6,12.2,2.6c4.1,0,8.2-0.9,12-2.6c6.1-2.8,12.9-4.3,19.4-4.3
		c6.8,0,13.3,1.4,19.4,4.3c3.9,1.8,8.1,2.6,12.2,2.6c0.5,0,1-0.1,1.5-0.1l5,19.3c-2.2,0.4-4.3,0.6-6.6,0.6c-5.5,0-10.8-1.2-15.5-3.3
		c-4.8-2.3-10.3-3.6-16-3.6s-11.2,1.3-16,3.6c-4.7,2.1-10,3.3-15.5,3.3c-5.5,0-10.8-1.2-15.5-3.3c-4.8-2.3-10.3-3.6-16-3.6
		c-5.7,0-11.2,1.3-16,3.6c-4.7,2.1-10,3.3-15.5,3.3c-5.5,0-10.8-1.2-15.5-3.3c-4.8-2.3-10.3-3.6-16-3.6c-5.6,0-11,1.3-15.9,3.6
		c-4.8,2.1-10.5,3.3-15.5,3.3v23.3c5,0,10.7-1.2,15.5-3.3c4.8-2.3,10.2-3.6,15.9-3.6c5.7,0,11.1,1.3,16,3.6c4.7,2.1,10,3.3,15.5,3.3
		c5.5,0,10.8-1.2,15.5-3.3c4.8-2.3,10.3-3.6,16-3.6c5.7,0,11.2,1.3,16,3.6c4.7,2.1,10,3.3,15.5,3.3c5.5,0,10.8-1.2,15.5-3.3
		c4.8-2.3,10.3-3.6,16-3.6s11.2,1.3,16,3.6c4.7,2.1,10,3.3,15.5,3.3c5.5,0,10.8-1.2,15.5-3.3c4.8-2.3,10.3-3.6,16-3.6
		c5.7,0,11.2,1.3,16,3.6c4.7,2.1,9.6,3.3,15.6,3.3v-23.3c-6,0-10.9-1.2-15.6-3.3C231.6,207.3,226.1,206,220.4,206z"/>
    <circle id="XMLID_15_" cx="106.8" cy="39.6" r="21.6"/>
</g>
</svg>`,

};




const activityTypes = {
    "RUNNING": [
        "INDOOR RUNNING",
        "OBSTACLE RUN",
        "STREET RUNNING",
        "TRACK RUNNING",
        "TRAIL RUNNING",
        "TREADMILL RUNNING",
        "ULTRA RUN",
        "VIRTUAL RUN",
        "BIKE TO RUN TRANSITION",
        "BIKE TO RUN TRANSITION V2",
    ],
    "CYCLING": [
        "BMX",
        "CYCLOCROSS",
        "DOWNHILL BIKING",
        "E BIKE FITNESS",
        "E BIKE MOUNTAIN",
        "GRAVEL CYCLING",
        "INDOOR CYCLING",
        "MOUNTAIN BIKING",
        "RECUMBENT CYCLING",
        "ROAD BIKING",
        "TRACK CYCLING",
        "VIRTUAL RIDE",
        "RUN TO BIKE TRANSITION",
        "RUN TO BIKE TRANSITION V2",
        "SWIM TO BIKE TRANSITION",
        "SWIM TO BIKE TRANSITION V2"

    ],
    "FITNESS EQUIPMENT": [
        "BOULDERING",
        "ELLIPTICAL",
        "INDOOR CARDIO",
        "HIIT",
        "INDOOR CLIMBING",
        "INDOOR ROWING",
        "PILATES",
        "STAIR CLIMBING",
        "STRENGTH TRAINING",
        "YOGA"

    ],
    "SWIMMING": [
        "LAP SWIMMING",
        "OPEN WATER SWIMMING"
    ],
    "WALKING": [
        "CASUAL WALKING",
        "SPEED WALKING",
        "WALKING"
    ],
    "HIKING": [
        "HIKING"
    ],
    "WINTER SPORTS": [
        "BACKCOUNTRY SNOWBOARDING",
        "BACKCOUNTRY SKIING",
        "CROSS COUNTRY SKIING WS",
        "RESORT SKIING SNOWBOARDING WS",
        "SKATE SKIING WS",
        "SKATING WS",
        "SNOW SHOE WS",
        "SNOWMOBILING WS"

    ],
    "WATER SPORTS": [
        "BOATING",
        "BOATING V2",
        "FISHING",
        "FISHING V2",
        "KAYAKING",
        "KAYAKING V2",
        "KITEBOARDING",
        "KITEBOARDING V2",
        "OFFSHORE GRINDING",
        "OFFSHORE GRINDING V2",
        "ONSHORE GRINDING",
        "ONSHORE GRINDING V2",
        "PADDLING",
        "PADDLING V2",
        "ROWING",
        "ROWING V2",
        "SAILING",
        "SAILING V2",
        "STAND UP PADDLEBOARDING",
        "STAND UP PADDLEBOARDING V2",
        "SURFING",
        "SURFING V2",
        "WAKEBOARDING",
        "WAKEBOARDING V2",
        "WATERSKIING",
        "WHITEWATER RAFTING",
        "WHITEWATER RAFTING V2",
        "WINDSURFING",
        "WINDSURFING V2"
    ],
    "TEAM SPORTS": [
        "AMERICAN FOOTBALL",
        "BASEBALL",
        "BASKETBALL",
        "CRICKET",
        "FIELD HOCKEY",
        "ICE HOCKEY",
        "LACROSSE",
        "RUGBY",
        "SOCCER",
        "SOFTBALL",
        "ULTIMATE DISC",
        "VOLLEYBALL"

    ],
    "RACKET SPORTS": [
        "BADMINTON",
        "PADEL",
        "PICKLEBALL",
        "PLATFORM TENNIS",
        "RACQUETBALL",
        "SQUASH",
        "TABLE TENNIS",
        "TENNIS",
        "TENNIS V2"

    ],
    "OTHER": [
        "BOXING",
        "BREATHWORK",
        "DISC GOLF",
        "FLOOR CLIMBING",
        "GOLF",
        "INLINE SKATING",
        "MIXED MARTIAL ARTS",
        "MOUNTAINEERING",
        "ROCK CLIMBING",
        "STOP WATCH"
    ]
};


/**
 * Gets a category icon based on its activity type
 * @param activityType the activity type we are looking for
 * @returns {*|string} represents an svg to place in the innerHTML of the div
 */
function getActivityGroup(activityType) {
    for (let group in activityTypes) {
        // Check if the input activityType is in the group's list
        if (activityTypes[group].includes(activityType)) {
            return categoryIcons[group];
        }
    }
    // If the activity type is not found, return 'Other' or handle it as needed
    return categoryIcons['OTHER'];
}

/**
 * Gets all the divs for the garmin activities to place a svg
 */
function getIconSvg() {
    let garminDivs = document.getElementsByClassName("garmin-activity-icon")
    for (let i = 0; i < garminDivs.length; i++) {
        let div = garminDivs[i];
        let value = div.getAttribute("value")
        div.innerHTML = getActivityGroup(value)
    }
}

/**
 * Gets all the cards by certain class
 * @param input specific element to add a class to the grandparents class.
 */
function changeBackground(input) {

    let cards = document.getElementsByClassName("highlighted")
    if (cards.length > 0) {
        cards[0].classList.remove("highlighted")
    }
    input.parentElement.parentElement.classList.add("highlighted");
}

/**
 * Displays toast message for saving an activity
 * success toast if response is successful
 * danger toast if response is unsuccessful
 * @param errors the errors from the response
 */
function displayErrorsToast(errors) {
    const toast = document.getElementById("garminActivityToast")
    const toastContentErrors = document.getElementById("garminToastErrors")
    toastContentErrors.innerHTML = ""
    for (let i = 0; i < errors.length; i++) {
        let error = errors[i]
        let errorDiv = document.createElement("li")
        errorDiv.classList.add("toast-danger", "h5", "m-0")
        errorDiv.innerHTML = error
        toastContentErrors.appendChild(errorDiv)
    }
    let bootstrapToast = new bootstrap.Toast(toast)
    bootstrapToast.show()
}
/**
 * Pops not connected toast
 */
function popNotConnectedToast() {
    let toast = document.getElementById("notConnectedToast");
    let errorToast = new bootstrap.Toast(toast);
    errorToast.show();
}

/**
 * Function to call rest controller when user hits save on the modal
 */
async function saveGarminActivity() {
    const selectedActivity = document.getElementsByClassName("highlighted")[0];
    const activityId = document.getElementById("activityId").value;
    const inputElements = selectedActivity.querySelectorAll('input');
    let data = {}
    // Loop through the input elements to access their values
    inputElements.forEach(function(inputElement) {
        const name = inputElement.getAttribute('name');
        data[name] = inputElement.value
        // Now you can work with each input element's name and value
    });
    await fetch(`${getCorrectURL()}/saveGarminActivity/${activityId}`, {
        method: 'POST',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'X-CSRF-TOKEN': TOKEN
        },
        body: JSON.stringify(data)
    })
        .then(response => {

            if (response.status === 200) {
                window.location.reload();
            } else {
                response.json().then(json => {
                    displayErrorsToast(json);
                })
            }
        })
        .catch(error => {
            console.error('Error saving Garmin activity:', error);
        });
}

/**
 * Calculates the amount of a food in grams or kilograms
 * @param foodAmount the amount of food in grams
 * @returns {string} the amount of food in grams or kilograms
 */
function calculateFoodAmountUnit(foodAmount) {
    if (foodAmount >= 1000) {
        return `${(foodAmount / 1000).toFixed(1)}kg`
    } else {
        return `${foodAmount.toFixed(1)}g`
    }
}

/**
 * Generates a string of food descriptions for a meal
 * @param mealIndex the index of the meal
 * @param newFoods the foods in the meal
 * @param quantity the quantity of the meal
 * @returns {string} the string of food descriptions
 */
function generateFoodDescription(mealIndex, newFoods, quantity) {
    let foodDescriptionList = []
    for (let i = 0; i < newFoods.length; i++) {
        const newPortion = newFoods[i].portion * quantity;
        const foodDescription = `${newFoods[i].name} ${calculateFoodAmountUnit(newPortion)}`
        foodDescriptionList.push(foodDescription)
    }
    return foodDescriptionList.join(", ")

}

/**
 * Gets the recommended meals for the user
 * @returns {Promise<any>}
 */
async function getRecommendedMeals() {
    return await fetch(`${getCorrectURL()}/get-recommended-meals/${caloriesBurned}`)
        .then(async response => {
            return response.json();
        });
}

/**
 * Updates the recommended meals
 * @returns {Promise<void>}
 */
document.addEventListener("DOMContentLoaded", async function () {
    await updateRecommendedMeals()
});

/**
 * Updates the recommended meals
 * @returns {Promise<void>}
 */
async function updateRecommendedMeals() {
    const newRecommendedMeals = await getRecommendedMeals();

    for (let i = 0; i < 3; i++) {
        let tick = document.getElementById(`tick${i}`);
        let classList = tick.children[0].classList
        if (!classList.contains("bi-check2")) {

            //Grab current meal information
            const currentMealName = document.getElementById(`meal-name-${i}`);
            const currentMealImage = document.getElementById(`meal-icon-${i}`);
            const currentMealCalories = document.getElementById(`meal-calories-${i}`);
            const currentMealFat = document.getElementById(`meal-fat-${i}`);
            const currentMealProtein = document.getElementById(`meal-protein-${i}`);
            const currentMealSugar = document.getElementById(`meal-sugar-${i}`);
            const currentMealFoodDescription = document.getElementById(`meal-food-description-${i}`);


            document.getElementById(`meal-card-${i}`).className = "card mb-3"
            document.getElementById(`meal-card-${i}`).classList.add(newRecommendedMeals[i].mealId)
            document.getElementById(`meal-card-${i}`).classList.add(newRecommendedMeals[i].quantity)

            //Update meal information

            currentMealName.textContent = newRecommendedMeals[i].name;
            currentMealFat.textContent = (newRecommendedMeals[i].fat * newRecommendedMeals[i].quantity).toFixed(1) + "g";
            currentMealProtein.textContent = (newRecommendedMeals[i].protein * newRecommendedMeals[i].quantity).toFixed(1) + "g";
            currentMealSugar.textContent = (newRecommendedMeals[i].sugar * newRecommendedMeals[i].quantity).toFixed(1) + "g";
            currentMealCalories.textContent = (newRecommendedMeals[i].calories * newRecommendedMeals[i].quantity).toFixed(1);
            currentMealFoodDescription.textContent = generateFoodDescription(i, newRecommendedMeals[i].foods, newRecommendedMeals[i].quantity);
            currentMealImage.textContent = icons[newRecommendedMeals[i].icon];
        }
    }
}
