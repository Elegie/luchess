<#assign strCurrent=""><#if ((search.current)!0) gt 0><#assign strCurrent=(search.current?c)!""></#if>
<#assign strElo=""><#if ((search.elo)!0) gt 0><#assign strElo=(search.elo?c)!""></#if>
<#assign strPageStart=""><#if ((search.pageStart)!0) gt 0><#assign strPageStart=(search.pageStart?c)!""></#if>
<#assign strPageCount=""><#if ((search.pageCount)!0) gt 0><#assign strPageCount=(search.pageCount?c)!""></#if>
<form action="${url(nav.go("list"))}" method="POST">
    <input type="submit" name="exec" value="${msg("searchExec")}" style="position:absolute; left: -100%" />
    <div class="search-col">
        <fieldset id="moves">
            <legend>${msg("searchMoves")}</legend>
            <ol>
                <#list 0..99 as i>
                    <li>
                        <input name="moves" value="${(search.moves[2*i])!""}" type="text"/>
                        <input name="moves" value="${(search.moves[2*i+1])!""}" type="text"/>
                    </li>
                </#list>
            </ol>
        </fieldset>
        <div class="movesNav">
            <input type="submit" id="previous" name="previous" value="&#x25C0;"/>
            <input type="text" id="current" name="current" value="${strCurrent}"/>
            <input type="submit" id="refresh" name="refresh" value="Go" />
            <script type="text/javascript">
                document.write("<input id=\"delete\" name=\"delete\" type=\"button\" value=\"&#x25BC;\"\ />");
            </script>
            <script type="text/javascript" src="${url("/assets/scripts/app/search-delete-moves.js")}"></script>
            <input type="submit" id="next" name="next" value="&#x25B6;"/>
        </div>
        <script type="text/javascript" src="${url("/assets/scripts/app/search-navigate-moves.js")}"></script>
    </div>    
    <div class="search-col">
        <fieldset>
            <legend>${msg("searchParameters")}</legend>
            <@text label="searchName" value="${(search.name)!\"\"}" name="name" title="searchNameHelp" accessKey="${msg(\"searchNameKey\")}" />
            <@text label="searchElo" value="${strElo}" name="elo" title="searchEloHelp" accessKey="${msg(\"searchEloKey\")}" />
            <@text label="searchPageStart" value="${strPageStart}" name="pageStart" title="searchPageStartHelp" accessKey="${msg(\"searchPageStartKey\")}" />
            <@text label="searchPageCount" value="${strPageCount}" name="pageCount" title="searchPageCountHelp" accessKey="${msg(\"searchPageCountKey\")}" />
        </fieldset>
        <script type="text/javascript">
            document.write("<input type=\"button\" value=\"${msg("searchClear")}\" onclick=\"Forms.clear(this.form, true)\"/>");
        </script>
        <input type="submit" id="exec" name="exec" value="${msg("searchExec")}"/>
    </div>
</form>
<div class="float-separator "></div>
<script type="text/javascript" src="${url("/assets/scripts/app/search-init-cursor.js")}"></script>
<script type="text/javascript" src="${url("/assets/scripts/app/search-refresh-move.js")}"></script>