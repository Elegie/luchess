<#include "tags/stat.html.ftl">
<#include "tags/result.html.ftl">

<p>
    <#assign total=query.totalWhiteWins + query.totalDraws + query.totalBlackWins + query.totalUnfinished>
    ${total?string(',###')}
    <#if total lte 1>${msg("searchFoundInSingular")}<#else>${msg("searchFoundInPlural")}</#if>
    ${query.duration} ms
    <@stat result=query />
</p>

<#if query.games?size gt 0>
    <table id="games">
        <tr>
            <th>${msg("listWhiteName")}</th>
            <th>${msg("listWhiteElo")}</th>
            <th>${msg("listBlackName")}</th>
            <th>${msg("listBlackElo")}</th>
            <th>${msg("listResult")}</th>
            <th>${msg("listActions")}</th>
        </tr>
        <#list query.games as game>
            <tr>
                <td>${(game.white)!"-"}</td>
                <td><#if game.whiteElo gt 0>${game.whiteElo}<#else>-</#if></td>
                <td>${(game.black)!"-"}</td>
                <td><#if game.blackElo gt 0>${game.blackElo}<#else>-</#if></td>
                <td><@result value=game.result.intValue /></td>
                <td>
                    <a href="${url(nav.go("save"))+"?id="+game.id}"><img
                        src="${url("/assets/images/app/save.png")}"
                        title="${msg("listSave")}"
                        alt="${msg("listSave")}" /></a>
                    <a href="${url(nav.go("load"))+"?id="+game.id}"><img
                        src="${url("/assets/images/app/load.png")}"
                        title="${msg("listLoad")}"
                        alt="${msg("listLoad")}"/></a>
                </td>
            </tr>
        </#list>
    </table>
    <script type="text/javascript" src="${url("/assets/scripts/app/list-load-moves.js")}"></script>    
</#if>


<#if query.continuations?size gt 0>
    <table id="continuations">
        <tr>
            <th>${msg("listMove")}</th>
            <th>${msg("listCount")}</th>
            <th>${msg("listResult")}</th>
        </tr>
        <#list query.continuations as continuation>
            <tr>
                <td>${(continuation.move.value)!"-"}</td>
                <td>${(continuation.totalWhiteWins + continuation.totalDraws + continuation.totalBlackWins + continuation.totalUnfinished)?string(',###')}</td>
                <td><@stat result=continuation /></td>
            </tr>
        </#list>
    </table>
    <script type="text/javascript" src="${url("/assets/scripts/app/list-enable-continuations.js")}"></script>
</#if>

<div class="float-separator"></div>