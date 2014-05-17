Events.addListener(Elements.get(Elements.DELETE), "onclick", function(evt) {
    var inputs = document.getElementsByName(Elements.MOVES);
    var fromIndex = +Elements.get(Elements.CURRENT).value;
    if (isNaN(fromIndex) || fromIndex < 0) {
        fromIndex = 0;
    }
    for(var ii=fromIndex; ii<inputs.length; ii++) {
        inputs[ii].value = "";
    }
});