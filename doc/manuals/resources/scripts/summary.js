window.onload = function(evt) {
    if (!document.body.childNodes.length == 1) {
        throw "The summary can only be populated if the body contains one element (the content container).";
    }
    
    var SUMMARY_ID = "summary";
    var HEADER_PREFIX_LCASE = "h";
    var HEADER_MAX_LEVEL = 6;
    var INDENT_LENGTH = 5;
    
    positionSummary(buildSummary(document));
    
    // ------------------------------------------------------------------------
    
    function buildSummary(root) {
        var summary = document.createElement("div");
        var headers = createHeadersMap();
        var elements = root.getElementsByTagName("*");
        for(var ii=0; ii<elements.length; ii++) {
            var element = elements[ii];
            var elementName = element.nodeName.toLowerCase();
            if (headers[elementName]) {
                var level = +elementName.replace(/\D/g, "") || 0;
                var link = document.createElement("a");
                link.href = "#" + element.id;
                link.appendChild(document.createTextNode(Dom.getText(element)));
                link.style.display = "block";
                link.style.textIndent = (INDENT_LENGTH * level) + "px";
                summary.appendChild(link);
            }
        }
        summary.id = SUMMARY_ID;
        return summary;
    }
    
    function createHeadersMap() {
        var headers = {};
        for(var ii=0; ii<HEADER_MAX_LEVEL; ii++) {
            headers[HEADER_PREFIX_LCASE + ii] = true;
        }
        return headers;
    }
    
    function positionSummary(summary) {
        var SUMMARY_RIGHT_MARGIN = 10;
        var content = getContentContainer();
        document.body.insertBefore(summary, content);
        summary.style.position = "fixed";
        content.style.position = "absolute";
        content.style.left = (Dom.getLeft(summary) + Dom.getWidth(summary) + SUMMARY_RIGHT_MARGIN) + "px";
    }
    
    function getContentContainer() {
        var contents = document.getElementsByTagName("div");
        if (!contents || contents.length < 1) {
            throw "No content container found.";
        }
        return contents[0];
    }
};