document.body.style.backgroundImage = 'url(' + screenBackground + ')';

var cnt = document.querySelector("#content");
cnt.style.backgroundImage = 'url(' + frame.background + ')';
cnt.style.width = frame.width + 'px';
cnt.style.height = frame.height + 'px';

for (var i = 0; i < items.length; ++i) {
    var itemTag = document.createElement('img');
    itemTag.src = items[i].img;
    itemTag.style.left = items[i].pos[0] + 'px';
    itemTag.style.top = items[i].pos[1] + 'px';

    updateRotation(itemTag, i);
    itemTag.addEventListener('click', itemClick);
    cnt.appendChild(itemTag);
}

checkSolved();

// Add overlay if quiz is complete
if (typeof enigma !== 'undefined' && enigma.getComplete())
    document.getElementById('fg').style.visibility = 'visible';

function updateRotation(elem, i) {
    elem.style.transform = 'rotate(' + 90 * items[i].c + 'deg)';
}

function itemClick() {
    var i = Array.prototype.indexOf.call(this.parentNode.childNodes, this) - 1;
    items[i].c++;
    updateRotation(this, i);
    checkSolved();
}

function checkSolved() {
    for (var i = 0; i < items.length; ++i)
        if (items[i].c % 4 != items[i].t) return;

    document.querySelector('#container h1').style.color = 'LimeGreen';
    if (typeof enigma !== 'undefined')
        enigma.setComplete();
}
