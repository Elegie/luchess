var buildUpdateInfo = (function () {
    var QUERY_INTERVAL = 1000;
    var url;
    var listener;

    return {
        setSuccessListener: function (successListener) {
            listener = successListener;
        },
        start: function (statusUrl) {
            url = statusUrl;
            setTimeout(
                function () { retrieveInfo(); },
                QUERY_INTERVAL
            );
        }
    };

    function retrieveInfo() {
        XHR.readText(url, updateInfo);
    }

    function updateInfo(info) {
        if (info) {
            var objInfo = eval("(" + info + ")");
            if (objInfo) {
                var props = [ Elements.INDEX_NUM_GAMES, Elements.INDEX_DURATION ];
                for (var ii = 0; ii < props.length; ii++) {
                    Elements.get(props[ii]).innerHTML = objInfo[props[ii]];
                }
                if (objInfo.started) {
                    setTimeout(retrieveInfo, QUERY_INTERVAL);
                } else {
                    if (listener) {
                        listener();
                    }
                }
            }
        }
    }
})();
