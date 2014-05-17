<#macro result value>
    <#if value == 1>1-0
    <#elseif value == 0>½-½
    <#elseif value == -1>0-1
    <#else>*</#if>
</#macro> 
