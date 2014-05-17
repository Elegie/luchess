var Messages = (function () {
    function display(cssClass, textContent) {
        Elements.get(Elements.MESSAGES).innerHTML = "<span class=\"" + cssClass + "\">" + textContent + "<\/span>";
        animate();
    }
    
    function animate() {
        var smoothEffect = TextEffects.createSmoothEffect(null, 10);
        var progressiveEffect = TextEffects.createProgressiveEffect(smoothEffect, 2);
        progressiveEffect.applyOnTarget(Elements.get(Elements.MESSAGES));
    }
    
    return {
        animate: animate,
        clear: function() {this.info("")},
        info: function (msg) { display(Styles.MSG_INFO, msg); },
        error: function (msg) { display(Styles.MSG_ERROR, msg); },
        warning: function (msg) { display(Styles.MSG_WARNING, msg); },
        fatal: function (msg) { display(Styles.MSG_FATAL, msg); }
    };
})();