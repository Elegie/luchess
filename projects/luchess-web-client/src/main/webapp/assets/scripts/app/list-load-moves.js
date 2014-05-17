function loadMoves(url) {
    XHR.readText(url, queryCallback);

    function queryCallback(content) {
        var result = eval("(" + content + ")");
        if (result && result.moves) {
            var inputs = document.getElementsByName(Elements.MOVES);
            for (var ii = 0; ii < inputs.length; ii++) {
                inputs[ii].value = "";
            }
            for (ii = 0; ii < result.moves.length && ii < inputs.length; ii++) {
                inputs[ii].value = result.moves[ii];
            }
            Elements.get(Elements.CURRENT).value = "1";
            inputs[0].focus();
        }
    }
}