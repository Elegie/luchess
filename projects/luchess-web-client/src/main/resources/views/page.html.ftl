<#macro page>
<!DOCTYPE html>
<html>
<head>
    <title>${msg("luchess")}</title>
    <meta charset="UTF-8"/>
    <link rel="stylesheet" type="text/css" media="screen" href="${url("/assets/styles/core.css")}"/>
    <link rel="stylesheet" type="text/css" media="screen" href="${url("/assets/styles/layout.css")}"/>
    <link rel="stylesheet" type="text/css" media="screen" href="${url("/assets/styles/index.css")}"/>
    <link rel="stylesheet" type="text/css" media="screen" href="${url("/assets/styles/search.css")}"/>
    <link rel="shortcut icon" href="${url("/assets/images/favicon/favicon.ico")}"/>    
    <script type="text/javascript" src="${url("/assets/scripts/lib/dom.js")}"></script>
    <script type="text/javascript" src="${url("/assets/scripts/lib/events.js")}"></script>
    <script type="text/javascript" src="${url("/assets/scripts/lib/forms.js")}"></script>
    <script type="text/javascript" src="${url("/assets/scripts/lib/xhr.js")}"></script>
    <script type="text/javascript" src="${url("/assets/scripts/lib/popins.js")}"></script>
    <script type="text/javascript" src="${url("/assets/scripts/lib/text-effects.js")}"></script>
    <script type="text/javascript" src="${url("/assets/scripts/app/common.js")}"></script>
    <script type="text/javascript" src="${url("/assets/scripts/app/messages.js")}"></script>
</head>
<body>
    <div id="page-container">
        <div id="page-navigation">
            <a href="${url(nav.go("index"))}" class="navItem">${msg("navIndex")}</a>
            <a href="${url(nav.go("search"))}" class="navItem">${msg("navSearch")}</a>
            <#include "messages.html.ftl">
        </div>
        <div id="page-content"><#nested></div>
    </div>
</body>
</html>
</#macro>
