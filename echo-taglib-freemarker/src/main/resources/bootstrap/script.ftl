<#macro script entry="" jsMethod="" requireJs=true>
    <#if requireJs>
        <#if entry != "">
            <script type="text/javascript">
                window["app"] = {
                    contextPath: '${request.contextPath}',
                    rootPath: '${request.contextPath}/',
                    entry: '${entry}',
                    jsMethod: '${jsMethod}'
                };
            </script>
        </#if>
        <script type="text/javascript" data-main="${request.contextPath}/static/script/main"
                src="${request.contextPath}/static/script/require.js"></script>
        <script type="text/javascript">
            var debug =  ${RequestParameters["debug"]!"0"};
            var edition = 'v1.0.0';
            var v = edition;

            if (debug) {
                v = debug;
            }
            requirejs.config({
                urlArgs: "v=" + v
            });
        </script>
    <#else>
        <script src="https://cdn.bootcss.com/jquery/3.2.1/jquery.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>
        <script src="https://cdn.bootcss.com/popper.js/1.12.9/umd/popper.min.js" integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q" crossorigin="anonymous"></script>
        <script src="https://cdn.bootcss.com/bootstrap/4.0.0/js/bootstrap.min.js" integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl" crossorigin="anonymous"></script>
    </#if>
</#macro>

