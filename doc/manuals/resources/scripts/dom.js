var Dom = {};

// --- Positioning ------------------------------------------------------------

Dom.getLeft = function (element) {
    if (element == null) {
        return 0;
    }
    return element.offsetLeft + arguments.callee(element.offsetParent);
};

Dom.getTop = function (element) {
    if (element == null) {
        return 0;
    }
    return element.offsetTop + arguments.callee(element.offsetParent);
};

Dom.getRight = function (element) {
    return Dom.getLeft(element) + Dom.getWidth(element);
};

Dom.getBottom = function (element) {
    return Dom.getTop(element) + Dom.getHeight(element);
};

Dom.getWidth = function (element) {
    if (element == null) {
        return 0;
    }
    return element.offsetWidth;
};

Dom.getHeight = function (element) {
    if (element == null) {
        return 0;
    }
    return element.offsetHeight;
};

Dom.setLeft = function (element, left) {
    element.style.left = left + "px";
};

Dom.setTop = function (element, top) {
    element.style.top = top + "px";
};

//--- Tree analysis -----------------------------------------------------------

Dom.findParent = function (element, type) {
    if (!element || !type) {
        return null;
    }
    if (element.nodeName.toLowerCase() == type.toLowerCase()) {
        return element;
    }
    return arguments.callee(element.parentNode, type);
};

Dom.contains = function (container, containee) {
    if (!container || !containee) {
        return false;
    }
    if (container == containee) {
        return true;
    }
    return arguments.callee(container, containee.parentNode);
};

Dom.getText = function (node) {
    if (!node) {
        return "";
    }
    if (node.nodeType == 3) {
        return node.nodeValue;
    } else if (node.nodeType == 1) {
        var children = node.childNodes;
        var text = "";
        for (var ii = 0; ii < children.length; ii++) {
            text += arguments.callee(children[ii]);
        }
        return text;
    } else {
        return "";
    }
};

// --- Styling ----------------------------------------------------------------

Dom.getStylePropertyValue = function (element, property) {
    if (element.currentStyle) {
        return element.currentStyle[property];
    } else if (document.defaultView && document.defaultView.getComputedStyle) {
        return document.defaultView.getComputedStyle(element, null).getPropertyValue(property);
    } else {
        return element.style[property];
    }
};

Dom.isVisible = function (element) {
    return element.style.visibility != "hidden";
};

Dom.setVisible = function (element, visible) {
    element.style.visibility = (visible ? "visible" : "hidden");
};

Dom.isDisplayed = function (element) {
    return element.style.display != "none";
};

Dom.setDisplayed = function (element, displayed) {
    element.style.display = (displayed ? "" : "none");
};

// --- Page -------------------------------------------------------------------

Dom.getPageSize = function() {
    var size = { width:0, height:0 };
    if (typeof window.innerWidth != "undefined") {
        size.width = window.innerWidth;
        size.height = window.innerHeight;
    } else {
        var docElement = document.documentElement;
        if (docElement && typeof docElement.clientWidth != "undefined" && docElement.clientWidth != 0) {
            size.width = docElement.clientWidth
            size.height = docElement.clientHeight
        } else {
            var body = document.body;
            if (body && typeof body.clientWidth != "undefined") {
                size.width = body.clientWidth
                size.height = body.clientHeight
            }
        }
    }
    return size;
};

Dom.getPageScroll = function () {
    var scroll = { x:0, y:0 };
    var docElement = document.documentElement;
    var body = document.body;
    if (docElement) {
        scroll.x = docElement.scrollLeft || 0;
        scroll.y = docElement.scrollTop || 0;
    }
    if (body) {
        if (scroll.x == 0) {
            scroll.x = body.scrollLeft || 0;
        }
        if (scroll.y == 0) {
            scroll.y = body.scrollTop || 0;
        }
    }
    return scroll;
};
