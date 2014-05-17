<span id="messages">
    <#if info??>
        <span class="msgInfo">${msg(info)}</span>
    <#elseif warning??>
        <span class="msgWarning">${msg(warning)}</span>
    <#elseif error??>
        <span class="msgError">${msg(error)}</span>
    <#elseif fatal??>
        <span class="msgFatal">${msg(fatal)}</span>
    </#if>
</span>

<script type="text/javascript">
    Messages.animate();
</script>