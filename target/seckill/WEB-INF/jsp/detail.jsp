<%@page import="com.fasterxml.jackson.annotation.JsonInclude.Include"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>秒杀详情页</title>
    <!-- 静态包含导入头文件 -->
    <%@include file="common/head.jsp"%>
</head>
<body>
    <!-- 页面显示部分 -->
    <div class="container">
        <div class="panel panel-default text-center">
            <div class="pannel-heading">
                <h1>${seckill.name}</h1>
            </div>

            <div class="panel-body">
                <h2 class="text-danger">
                    <%-- 显示time图标 --%>
                    <span class="glyphicon glyphicon-time"></span>
                    <%-- node 展示倒计时 --%>
                    <span class="glyphicon" id="seckill-box"></span>
                </h2>
            </div>
        </div>
    </div>

    <%-- 登录弹出层 输入电话 --%>
    <div id="killPhoneModal" class="modal fade">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h3 class="modal-title text-center">
                        <span class="glyphicon glyphicon-phone"> </span>秒杀电话:
                    </h3>
                </div>
                <div class="modal-body">
                    <div class="row">
                        <div class="col-xs-8 col-xs-offset-2">
                            <input type="text" name="killPhone" id="killPhoneKey" placeholder="填写手机号^o^"
                                   class="form-control">
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <%-- 验证信息 --%>
                    <span id="killPhoneMessage" class="glyphicon"> </span>
                    <button type="button" id="killPhoneBtn" class="btn btn-success">
                        <span class="glyphicon glyphicon-phone"></span>
                        Submit
                    </button>
                </div>
            </div>
        </div>
    </div>
</body>

<!-- jQuery文件。务必在bootstrap.min.js 之前引入 -->
<script src="https://cdn.bootcss.com/jquery/2.1.1/jquery.min.js"></script>
<!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
<script src="https://cdn.bootcss.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<!-- 使用CDN获取公共js http://cdn.bootcss.com/ -->
<%--jQuery Cookie操作插件--%>
<script type="text/javascript" src="https://cdn.bootcss.com/jquery-cookie/1.4.1/jquery.cookie.min.js "></script>
<script src="https://cdn.bootcss.com/jquery.countdown/2.2.0/jquery.countdown.min.js"></script>
<!-- 引入编写好的交互逻辑
        bug1: 浏览器只支持<script></script>
                 不能解析<script/>

 -->
<script src="/resources/script/seckill.js" type="text/javascript"></script>
<script type="text/javascript">
    $(function(){
        //使用El表达式传入json格式参数
        //startTime.time startTime为Date，startTime.timeEL表达式获取毫秒值,便于js解析
        seckill.detail.init({
            seckillId : ${seckill.seckillId},
            startTime : ${seckill.startTime.time},
            endTime : ${seckill.endTime.time} //毫秒
        });
     //bug: )};写错了
     });
</script>
</html>
