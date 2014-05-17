<#compress>
    <#if game??>
        <#assign white = game.white!"">
        <#assign black = game.black!"">
        <#assign whiteElo = game.whiteElo!"">
        <#assign blackElo = game.blackElo!"">
        <#assign result = (game.result.stringValue)!"">
        
        <#if white != "">[White "${white}"]</#if>
        <#if black != "">[Black "${black}"]</#if>
        <#if whiteElo != 0>[WhiteElo "${whiteElo?c}"]</#if>
        <#if blackElo != 0>[BlackElo "${blackElo?c}"]</#if>
        <#if result != "">[Result "${result}"]</#if>
    </#if>
</#compress>


<#compress>
    <#if moves??>
        <#assign index = 1>
        <#list moves as move><#if (move_index % 2) == 0>${index?c}.<#assign index = (index + 1)></#if>${move} <#if !move_has_next>${result}</#if></#list>
    <#else>
        ${msg("errorSaveNoGame")}
    </#if>
</#compress>
