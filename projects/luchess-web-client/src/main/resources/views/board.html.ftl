<#include "tags/text.html.ftl">

<#assign cols = ["a", "b", "c", "d", "e", "f", "g", "h"]>
<table id="board">
    <#list 8..1 as row>
    <tr>
        <th>${row}</th>
        <#list cols as col>
            <#assign piece = board.getPiece(col, row)!"">
            <td><#if piece!=""><img src="${url("/assets/images/pieces/" + piece + ".png")}" alt="${piece}"></#if></td>
        </#list>
    </tr>
    </#list>
    <tr>
        <th>&nbsp;</th>
        <#list cols as col>
            <th>${col}</th>
        </#list>
    </tr>
</table>
<script type="text/javascript" src="${url("/assets/scripts/app/search-play-move.js")}"></script>
<script type="text/javascript">
    searchPlayMove.init("${url(nav.go("move"))}", "${url("/assets/images/pieces/%%.png")}");
</script>
