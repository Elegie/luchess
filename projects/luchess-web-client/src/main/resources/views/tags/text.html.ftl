<#macro text label name="" id="" title="" default="" value="" accessKey="">
<div class="input">
    <#if name!="">
        <label for="<#if id!="">${id}<#else>${name}</#if>">${msg(label)}</label>
        <input
            <#if title!="">title="${msg(title)}"</#if>
            <#if accessKey!="">accessKey="${accessKey}"</#if>
            type="text"
            id="<#if id!="">${id}<#else>${name}</#if>"
            name="${name}"
            value="${value}" />
    <#else>
        <span>${msg(label)}</span>
        <span<#if id!=""> id="${id}"</#if>><#if value!="">${value}<#else>-</#if></span>
    </#if>
</div>
</#macro> 
