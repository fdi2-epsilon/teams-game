/* JavaScript here, don't worry: it's only text, random text */

'use strict';

window.addEventListener('load', function () {
    // Add overlay if quiz is complete
    if (typeof enigma !== 'undefined' && enigma.getComplete())
        showCompletedForeground(false);
});

// The function called on page load, will be registered later at the end of the script.
function loadProc() {

    var elements = document.getElementById("answers").getElementsByTagName("li");
    for (var i = 0; i < elements.length; i++) {
        if (elements[i].hasAttribute("correct"))
            elements[i].onclick = cbSuccess;
        else
            elements[i].onclick = cbFailure;
    }
}

// Function called on exact answer
function cbSuccess() {
    showCompletedForeground(true);

    if (typeof enigma === 'undefined') {
        // We are not running inside the app
        var title = document.querySelector('h1');
        title.innerHTML = '- Wrong API Error -';
        title.style.color = 'red';
        return;
    }

    enigma.setComplete();
    this.classList.add('success');
}

// Epic fail handling (opposite as previous function)
function cbFailure() {
    this.classList.add('failure');
}

// Shows a black semitransparent overlay to show that this quiz is unavailable
function showCompletedForeground(fade) {
    var fg = document.createElement('div');
    fg.classList.add('fullscreen');
    fg.style.backgroundColor = 'black';

    if (fade) {
        fg.style.opacity = '0';
        fg.style.transition = 'opacity .5s linear';
        document.body.appendChild(fg);
        setTimeout(function () {
            fg.style.opacity = '.35';
        }, 50);
    } else {
        fg.style.opacity = '.35';
        document.body.appendChild(fg);
    }
}

// Exec 'loadProc' only when the document is fully loaded,
// so it can find its elements (e.g. #1st-elem, etc.).
window.addEventListener('load', loadProc);
