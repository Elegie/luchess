<#macro stat result>
<span class="stat">
    <#assign total=result.totalWhiteWins + result.totalDraws + result.totalBlackWins + result.totalUnfinished>
    <#assign white=0>
    <#assign draws=0>
    <#assign black=0>
    <#assign unfinished=0>
    <#if total gt 0>
        <#assign white=(result.totalWhiteWins/total*100)>
        <#assign draws=(result.totalDraws/total*100)>
        <#assign black=(result.totalBlackWins/total*100)>
        <#assign unfinished=(result.totalUnfinished/total*100)>
    </#if>
    <span
     title="${msg("searchStatWhite")}"><#if white!=0>${white?string(",###")}%<#else>-</#if></span><span
     title="${msg("searchStatDraws")}"><#if draws!=0>${draws?string(",###")}%<#else>-</#if></span><span
     title="${msg("searchStatBlack")}"><#if black!=0>${black?string(",###")}%<#else>-</#if></span><span
     title="${msg("searchStatUnfinished")}"><#if unfinished!=0>${unfinished?string(",###")}%<#else>-</#if></span>
</span>
</#macro> 
