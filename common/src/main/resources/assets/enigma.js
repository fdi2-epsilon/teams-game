/* enigma.js - simple interface for Enigma API interaction and common quest behavior */

'use strict';

// --- Library API ---

function _e(arg) {
    // Allows invocation '_e(...)', reserved for future functionalities
}

Object.defineProperties(_e, {
    /** 'true' if the Enigma API was found. */
    'linked': {
        value: typeof enigma !== 'undefined'
    },
    /** 'true' if this quest is solved. */
    'solved': {
        get: function () { return _e.linked && enigma.getComplete(); }
    },
    /** Alias of '_e.solved' */
    'completed': {
        get: function () { return _e.solved; }
    }
});

/** Marks a quiz as solved */
_e.solve = function () {
    if (!_e.linked) return;
    _e._showCompletionForeground(true);
    enigma.setComplete();
};

/** Alias of '_e.solve' */
_e.complete = _e.solve;


// --- Internal implementation ---

_e._cbLoad = function () {
    if (!_e.linked) {
        _e._showLinkError();
        return;
    }

    if (_e.solved)
        _e._showCompletionForeground(false);
};

_e._showLinkError = function () {
    var tag = document.createElement('div');
    tag.style.cssText = '' +
        'position: fixed; top: 0; left: 0; width: 100%;' +
        'padding: 4pt; color: white; background-color: rgba(220, 0, 0, .8);' +
        'font: .7em "Trebuchet MS", sans-serif; text-align: center;';
    tag.innerHTML = '<b>The Enigma API could not be found</b>: ' +
        'this is probably because you started this page on a web browser rather than directly in the app.';
    document.body.insertBefore(tag, document.body.firstChild);
};

_e._showCompletionForeground = function (fade) {
    // Show a black semitransparent overlay to show that this quiz is unavailable / completed
    var fg = document.createElement('div');
    fg.style.cssText = 'position: fixed; top: 0; left: 0; width: 100%; height: 100%; background-color: black;';

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
};




window.addEventListener('load', _e._cbLoad);
