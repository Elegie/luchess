(function () {
    Events.addListener(window, "onload", addCursorRule);
    Events.addListener(Elements.get(Elements.CONTINUATIONS), "onclick", addUpdateMoveOnClick);
    
    function addCursorRule(evt) {
        Elements.get(Elements.CONTINUATIONS).className += " " + Styles.HIGHLIGHT_CONTINUATIONS;
    }
    
    function addUpdateMoveOnClick(evt) {
        var target = Events.getTarget(evt);
        if (target.nodeName.toLowerCase() == "td" && target.cellIndex == 0) {
            var moves = document.getElementsByName(Elements.MOVES);
            var form = null;
            for (var ii=0; ii<moves.length; ii++) {
                var input = moves[ii];
                if (Forms.isEmpty(input)) {
                    input.value = Dom.getText(target);
                    form = input.form;
                    Elements.get(Elements.CURRENT).value = "";
                    break;
                }
            }
            if (form){
                form.submit();
            }
        }
    }
})();

