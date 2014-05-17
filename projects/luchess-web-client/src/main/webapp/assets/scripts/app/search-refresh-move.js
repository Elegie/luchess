(function () {
	var currentMove = Elements.get(Elements.CURRENT);

	Events.addListener(currentMove, "onkeydown", refreshMove);
	Events.addListener(currentMove, "onkeypress", refreshMove);

	function refreshMove(evt) {
		var ENTER_KEYCODE = 13;
		if (evt.keyCode == ENTER_KEYCODE) {
			Events.preventDefaultBehavior(evt);
			Elements.get(Elements.REFRESH).click();
		}
	}
})();