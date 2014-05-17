<#include "page.html.ftl">
<#include "tags/text.html.ftl">

<@page>
<div>
    ${msg("indexWelcomeBefore")}
    <span id="numGames" class="textHighlight">${numGames}</span>
    <#if numGames==1>
        ${msg("indexWelcomeAfterSingular")}
    <#else>
        ${msg("indexWelcomeAfterPlural")}
    </#if>
</div>


<#if monitor.status != "created">
    <div id="indexLast">
        <span class="textHighlight">${msg("indexLast")}</span>
        <ul>
            <li><@text label="indexLastNumGames" value="${monitor.numGames}" id="indexNumGames" /></li>
            <li><@text label="indexLastDuration" value="${dur(monitor.duration)}" id="indexDuration" /></li>
        </ul>
    </div>
</#if>

<div id="build" <#if monitor.status=="started"> style="display:none"</#if>>
    <form method="POST" action="${url(nav.go("build"))}">
        <#if hasPassword>
            <script type="text/javascript">
                document.write("<span onclick=\"toggleAdmin(this)\" class=\"indexingPasswordHandler\">${msg("indexMore")}<\/span>");
                function toggleAdmin(handler) {
                    var container = Elements.get(Elements.ADMIN);
                    var show = !Dom.isDisplayed(container);
                    Dom.setDisplayed(container, show);
                    handler.innerHTML = show ? "${msg("indexLess")}" : "${msg("indexMore")}";
                }
            </script>
            <div id="admin">
                ${msg("indexProposal")}
                <input type="password" placeholder="${msg("indexPassword")}" title="${msg("indexPassword")}" name="indexingPassword" />
                <input type="submit" value="${msg("indexBuild")}" />
            </div>
            <script type="text/javascript">
                Dom.setDisplayed(Elements.get(Elements.ADMIN), false);
            </script>
        <#else>
            ${msg("indexProposal")}
            <input type="submit" value="${msg("indexBuild")}" />
        </#if>    
    </form>
</div>
    
<#if monitor.status=="started">
    <script type="text/javascript" src="${url("/assets/scripts/app/build-update-info.js")}"></script>
    <script type="text/javascript">
        buildUpdateInfo.setSuccessListener(function() {
            Elements.get(Elements.SUCCESS).play();
            Elements.get(Elements.BUILD).style.display = "";
            XHR.readText("${url(nav.go("count"))}", function(count) {
                if (count) {
                    var objCount = eval("(" + count + ")");
                    Elements.get(Elements.NUM_GAMES).innerHTML = objCount.numGames;
                }
            });
        });
        buildUpdateInfo.start("${url(nav.go("progress"))}");
    </script>
    <script type="text/javascript">
        document.write(
            '<audio id="success">' +
                '<source src="${url("/assets/sounds/success.wav")}" type="audio/wav">' +
                '<source src="${url("/assets/sounds/success.mp3")}" type="audio/mpeg">' +
                '<source src="${url("/assets/sounds/success.ogg")}" type="audio/ogg">' +
            '<\/audio>'
        );
    </script>    
</#if>    
</@page>