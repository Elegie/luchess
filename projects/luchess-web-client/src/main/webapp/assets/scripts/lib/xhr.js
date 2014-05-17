/**
 * Based on work by <a href="http://jiberring.com/">Jim Ley</a>
 */
var XHR = (function () {
    var getXHRObject = function () {
        var xmlhttp = false;
        /* @cc_on @ */
        /*
         * @if (@_jscript_version >= 5) try { xmlhttp = new
         * ActiveXObject("Msxml2.XMLHTTP"); } catch (e) { try { xmlhttp = new
         * ActiveXObject("Microsoft.XMLHTTP"); } catch (E) { xmlhttp = false; } }
         * @end @
         */
        if (!xmlhttp && typeof XMLHttpRequest != "undefined") {
            try {
                xmlhttp = new XMLHttpRequest();
            } catch (e) {
                xmlhttp = false;
            }
        }
        if (!xmlhttp && window.createRequest) {
            try {
                xmlhttp = window.createRequest();
            } catch (e) {
                xmlhttp = false;
            }
        }
        return xmlhttp;
    };

    function readResponse(prop, url, callback) {
        var XHRObj = getXHRObject();
        XHRObj.open("GET", url, true);
        XHRObj.onreadystatechange = function () {
            if (XHRObj.readyState == 4) {
                callback(XHRObj[prop]);
            }
        };
        XHRObj.send(null);
    }

    return {
        canRun: function () {
            return !!getXHRObject();
        },
        readText: function (url, callback) {
            readResponse("responseText", url, callback);
        },
        readXML: function (url, callback) {
            readResponse("responseXML", url, callback);
        }
    };
})();
