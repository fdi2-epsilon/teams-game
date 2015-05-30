/* JavaScript here, don't worry: it's only text, random text */

'use strict';

// Function called after page load, so we can look at the full DOM.
window.addEventListener('load', function () {
    var elements = document.querySelectorAll('#answers li');
    for (var i = 0; i < elements.length; i++) {
        if (elements[i].hasAttribute('correct'))
            elements[i].onclick = cbSuccess;
        else
            elements[i].onclick = cbFailure;
    }
});

// Function called on exact answer
function cbSuccess() {
    _e.solve();
    this.classList.add('success');
}

// Epic fail handling (opposite as previous function)
function cbFailure() {
    this.classList.add('failure');
}
