<#include "page.html.ftl">

<@page>
<#include "board.html.ftl">
<#include "criteria.html.ftl">
<#if query??>
    <#include "results.html.ftl">    
</#if>
</@page>