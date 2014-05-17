var Events = {};

Events.addListener = function (target, evt, listener) {
    evt = evt.replace(/^on/i, "");
    if (target.addEventListener) {
        target.addEventListener(evt, listener, false);
    } else if (target.attachEvent) {
        target.attachEvent("on" + evt, function (evt) {
            return listener.call(target, evt || window.event);
        });
    } else {
        if (target[evt]) {
            target[evt] = (function (oldListener) {
                return function (evt) {
                    oldListener.call(this, evt || window.event);
                    return listener.call(this, evt || window.event);
                };
            })(target[evt]);
        } else {
            target[evt] = listener;
        }
    }
};

Events.getTarget = function (evt) {
    return evt && (evt.target || evt.srcElement);
};

Events.stopPropagation = function (evt) {
    if (evt.stopPropagation) {
        evt.stopPropagation();
    } else if (typeof evt.cancelBubble != "undefined") {
        evt.cancelBubble = true;
    }
};

Events.preventDefaultBehavior = function (evt) {
    if (window.event) {
        window.event.returnValue = false;
    }
    if (evt.preventDefault) {
        evt.preventDefault();
    }
};
