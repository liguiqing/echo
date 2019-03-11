<#macro html entryjs="no" class="bg1" jsMethod="" title="" css=[] scroll="false" application="">
<!DOCTYPE html>
<html lang="zh-cn">
<head>
    <title>${title!""}</title>
    <#import "meta.ftl" as meta>
    <@meta.meta  css=css/>
</head>
<#compress >
    <body class="${class}" entry="${entryjs}" jsMethod="${jsMethod}" rootPath="${request.contextPath}/">
    <#nested >
    </body>
    <#import "script.ftl" as script>
    <@script.script entry="${entryjs}" jsMethod="${jsMethod}" requireJs=requireJs!true/>
</#compress>
</html>
</#macro>