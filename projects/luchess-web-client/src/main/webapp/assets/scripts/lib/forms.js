var Forms = {};

Forms.FIELD_DEFAULT_VALUE = "data-default-value";

Forms.clear = function (form, restoreDefault) {
    if (!form) {
        return;
    }
    var elem = form.elements;
    for (var ii = 0; ii < elem.length; ii++) {
        var ctrl = elem[ii];
        var type = ctrl.type;
        if (type) {
            type = type.toLowerCase();
            var defaultValue = ctrl.getAttribute(Forms.FIELD_DEFAULT_VALUE);
            switch (type) {
                case "text":
                    if (defaultValue == null || typeof defaultValue == "undefined") {
                        defaultValue = "";
                    }
                    ctrl.value = restoreDefault ? defaultValue : "";
                    break;
                case "checkbox":
                    ctrl.checked = restoreDefault ? toBoolean(defaultValue) : false;
                    break;
            }
        }
    }
    
    function toBoolean(booleanString) {
        if (booleanString) {
            booleanString = booleanString.toLowerCase();
        }
        return booleanString == "true" || booleanString == "1" || booleanString == "y" || booleanString == "yes";
    }
};

Forms.isEmpty = function(control) {
    return /^\s*$/.test(control.value);
};