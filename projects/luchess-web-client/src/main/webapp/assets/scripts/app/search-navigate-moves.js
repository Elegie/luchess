Events.addListener(Elements.get(Elements.MOVES), "onkeyup", function (evt) {
    var LEFT_ARROW = 37;
    var RIGHT_ARROW = 39;
    var DOWN_ARROW = 40;
    
    if (Dom.contains(Elements.get(Elements.MOVES), Events.getTarget(evt))) {
        var index = +Elements.get(Elements.CURRENT).value;
        if (index) {
            var actions = {};
            actions[LEFT_ARROW] = Elements.PREVIOUS;
            actions[RIGHT_ARROW] = Elements.NEXT;
            actions[DOWN_ARROW] = Elements.DELETE;
            
            var target = actions[evt.keyCode];
            if (target) {
                Elements.get(target).click();
            }
        }
    }
});