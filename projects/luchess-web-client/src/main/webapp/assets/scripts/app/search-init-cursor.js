(function () {
    Events.addListener(window, "onload", function (evt) {
        addUpdateCurrentOnFocus(evt);
        focusFirstEmpty(evt);
    });
    
    function addUpdateCurrentOnFocus(evt) {
        var inputs = document.getElementsByName(Elements.MOVES);
        for(var ii=0; ii<inputs.length; ii++) {
            Events.addListener(inputs[ii], "onfocus", updateCurrent);
        }
    }
    
    function updateCurrent(evt) {
        var index = 0;
        var name = this.name;
        var elem = this.form.elements;
        for (var ii = 0; ii < elem.length; ii++) {
            if (elem[ii].name == name) {
                index++;
                if (elem[ii] == this) {
                    break;
                }
            }
        }
        if (index == 0) {
            index = "";
        }
        Elements.get(Elements.CURRENT).value = index;
    }
    
    function focusFirstEmpty(evt) {
        var inputs = document.getElementsByName(Elements.MOVES);
        var index = Elements.get(Elements.CURRENT).value - 1;
        for (var ii = 0; ii < inputs.length; ii++) {
            var input = inputs[ii];
            if (input.type.toLowerCase() == "text") {
                if (Forms.isEmpty(input) || index == ii) {
                    input.focus();
                    break;
                }
            }
        }
    }
})();