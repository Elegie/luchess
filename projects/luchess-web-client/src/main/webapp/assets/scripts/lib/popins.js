var Popins = (function () {
    Events.addListener(document, "onclick", closeAllPopins);
    
    var allPopins = [];
    
    function Popin(container) {
        this.container = container;
        this.container.style.position = "absolute";
    }
    
    Popin.prototype.show = function (evt) {
        closeAllPopins();
        var epsilon = 10;
        var pageScroll = Dom.getPageScroll();
        var pageSize = Dom.getPageSize();
        var x = evt.clientX;
        var y = evt.clientY;
        var w = Dom.getWidth(this.container);
        var h = Dom.getHeight(this.container);
        var pw = pageSize.width;
        var ph = pageSize.height;
        if (x + w + epsilon > pw) { x -= w; }
        if (y + h + epsilon > ph){ y -= h; }
        if (x < 0) { x = 0; }
        if (y < 0) { y = 0; }
        this.display(true, x + pageScroll.x, y + pageScroll.y);
        Events.stopPropagation(evt);
    };
    
    Popin.prototype.display = function (visible, x, y) {
        Dom.setVisible(this.container, visible);
        if (typeof x != "undefined") {
            Dom.setLeft(this.container, x);
        }
        if (typeof y != "undefined") {
            Dom.setTop(this.container, y);
        }
    };
    
    function createPopin(container) {
        var popin = new Popin(container);
        allPopins.push(popin);
        return popin;
    }
    
    function closeAllPopins(evt) {
        for (var ii=0; ii<allPopins.length; ii++) {
            var popin = allPopins[ii];
            var closePopin = true;
            if (evt) {
                closePopin = !Dom.contains(popin.container, Events.getTarget(evt));
            }
            popin.display(!closePopin);
        }
    }
    
    return {
        create: createPopin,
        closeAll: closeAllPopins
    };
})();