<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no">
    <title>Echo</title>
    <link rel="stylesheet" href="https://www.tfkclass.com/wxweb/css/login.css">
</head>
<body>

<div class="container">
    <div class="body">
        <#if user.isAuthenticated()>
            Balala
        </#if>
        <div class="panel">
            <div class="login-type">
                <i class=""></i>
                <div class="login-tip">
                    <div class="poptip">
                        <div class="poptip-arrow"><em></em><span></span></div>
                        <div class="poptip-content">扫码登录</div>
                    </div>
                </div>
            </div>
            <div class="title">
                <img src="https://www.tfkclass.com/wxweb/img/logo.png" alt="logo">
            </div>
            <div class="dis_none" id="scan-type">
                <div class="scan-body">
                    <div>
                        <img src="https://www.tfkclass.com/wxweb/img/url_ysyp.png" alt="url">
                        <p><i class="icon-scan2"></i>扫一扫登录</p>
                    </div>
                </div>
            </div>
            <form id="passwd-type"  method="post" action="${request.contextPath}/index">
                <div class="from">
                    <div>
                        <label for="username"><i class="icon-user"></i></label><input type="text" name="username" id="username" name="username" placeholder="用户名">
                    </div>
                    <div>
                        <label for="passwd"><i class="icon-passwd"></i></label><input type="password" name="password" id="passwd" name="password" placeholder="密码">
                    </div>
                </div>
                <div class="t_c"><button class="Btn1" type="submit">登录</button></div>
            </form>
        </div>
    </div>
    <div class="footer">
        <p>Copyright©  &nbsp;&nbsp;&nbsp;Echo.ai</p>
    </div>
</div>
<!--微信二维码扫描登录 http://www.cnblogs.com/txw1958/p/scan-qrcode-login.html-->
<script src="http://res.wx.qq.com/connect/zh_CN/htmledition/js/wxLogin.js"></script>
<script src="https://cdn.bootcss.com/jquery/2.1.4/jquery.min.js"></script>
</body>
</html>