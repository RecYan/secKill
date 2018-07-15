//存放主要交互逻辑js代码
//模块化编写业务逻辑，防止出错混乱 ==> json模拟java的模块化
var seckill ={
    //封装秒杀相关的ajax的url
    URL : {
        now : function(){
            return '/seckill/time/now';
        },
        exposer : function(seckillId) {
            return '/seckill/'+seckillId+'/exposer';
        },
        execution: function (seckillId, md5) {
            return '/seckill/' + seckillId + "/" + md5 + '/execution';
        }
    },
    //1.4.3 秒杀开始
    handlerSeckill : function(seckillId,node) {
        //获取秒杀地址,控制显示逻辑,执行秒杀
        node.hide()
            .html('<button class="btn btn-primary btn-lg" id="killBtn">开始秒杀</button>');
        $.post(seckill.URL.exposer(seckillId), {}, function (result) {
            //在回掉函数中执行交互逻辑
            if (result && result['success']) {
                //获取expose对象
                var exposer = result['data'];
                //判断是否开启秒杀
                if(exposer['exposed']){
                    //开启秒杀
                    //1 获取秒杀地址
                    var md5 = exposer['md5'];
                    //跳转处理
                    var killUrl = seckill.URL.execution(seckillId, md5);
                    console.log('killUrl: '+killUrl);

                    //绑定一次点击 开始秒杀事件
                    $('#killBtn').one('click', function () {
                        //绑定执行秒杀请求操作
                        //1 禁用按钮
                        $(this).addClass('disabled');//,$(this)==$('#killBtn')
                        //2 发送秒杀的请求执行秒杀
                        $.post(killUrl, {}, function (result) {
                            //3 分析
                            if (result && result['success']) {
                                var killResult = result['data'];
                                var state = killResult['state'];
                                console.log('state: '+state);
                                var stateInfo = killResult['stateInfo'];
                                //4 显示秒杀结果
                                node.html('<span class="label label-success">' + stateInfo + '</span>');
                            }
                        });
                    });
                    node.show();
                }else{
                    //未开启秒杀,获取当前时间
                    var now = exposer['now'];
                    var start = exposer['start'];
                    var end = exposer['end'];
                    //重新倒计时
                    seckill.countDown(seckillId, now, start, end);
                }

            } else {
                //为获取exposer对象
                console.log('result: '+result);
            }


        });
    },
    //验证手机号--封装成函数方便复用
    validatePhone : function(phone){
        //isNaN(phone) 手机号码为非数字时为真
        if(phone && phone.length == 11 && !isNaN(phone)){
            //号码正确且长度为11且为数字
            return true;
        } else{
            return false;
        }
    },
    //1.4.2 计时
    countDown : function(seckillId, nowTime, startTime, endTime) {
        var seckillBox = $('#seckill-box');
        //1.4.2.1 时间判断
        if (nowTime > endTime) {
            //秒杀结束
            seckillBox.html('秒杀结束');
        } else if (nowTime < startTime) {
            //秒杀未开始,开始计时绑定事件 jQuery的countdown插件
            var killTime = new Date(startTime + 1000); //防止时间偏移

            //function(event) 时间变化会回调这个函数
            seckillBox.countdown(killTime, function (event) {
                //控制时间格式
                var format = event.strftime('秒杀倒计时: %D天 %H时 %M分 %S秒 ');
                seckillBox.html(format);
                //时间完成后回调事件
            }).on('finish.countdown', function () {
                //获取秒杀地址,控制显示逻辑,执行秒杀
                console.log(seckillId);
                seckill.handlerSeckill(seckillId, seckillBox);
            });
        } else {
            //1.4.3 秒杀开始
            seckill.handlerSeckill(seckillId, seckillBox);
        }

    },
    //详情页秒杀逻辑
    detail : {
        //[1] 详情页初始化
        init : function(params){
            //1 手机验证和登录，计时交互
            //1.1 从cookie中查询手机号
            var killPhone = $.cookie('killPhone');
            //1.2 js获取json数据方式
            var startTime = params['startTime'];
            var endTime = params['endTime'];
            var seckillId = params['seckillId'];
            //1.3 验证手机号
            if(!seckill.validatePhone(killPhone)){
                //1.3.1 手机号空，绑定手机号 $('#killPhoneModal') jQuery的id选择器选中节点
                var killPhoneModal = $('#killPhoneModal');
                //1.3.2 显示弹出层
                killPhoneModal.modal({
                    show: true,//显示弹出层
                    backdrop: 'static', //禁止位置关闭
                    keyboard: false//关闭键盘事件
                });
                //1.3.3 绑定弹出层按钮
                $('#killPhoneBtn').click(function () {
                    //val()取killPhoneKey的值 ****bug: 浏览器缓存忘记清理，出现异常 ==> ctrl+shift+r 清除当前页面缓存
                    var inputPhone = $('#killPhoneKey').val();
                    if (seckill.validatePhone(inputPhone)) {
                        //电话写入cookie (/seckill模块下7天内有效--优化)
                        $.cookie('killPhone', inputPhone, {expires: 7, path: '/seckill'});
                        //刷新页面
                        window.location.reload();
                    } else {
                        $('#killPhoneMessage').hide().html('<label class="label label-danger">手机号错误!</label>').show(300);
                    }
                });
            }
            /**1.4 成功登陆,进入计时交互
             *     $.get() 方法使用 HTTP GET 请求从服务器加载数据。
             *     $.get(URL,data,function(data,status,xhr),dataType)
             *          URL: 规定需要请求的URL
             *          data: 规定连同请求发送到服务器的数据
             *          function(data,status,xhr): 规定当请求成功时运行的函数
             *               data - 包含来自请求的结果数据
             *               status - 包含请求的状态（"success"、"notmodified"、"error"、"timeout"、"parsererror"）
             *               xhr - 包含 XMLHttpRequest 对象
             *          dataType: 规定预期的服务器响应的数据类型
             */
            $.get(seckill.URL.now(), {}, function (result) {
                //1.4.1 result: SeckillResult对象
                if (result && result['success']) {
                    var nowTime = result['data'];
                    console.log(seckillId);
                    //1.4.2 时间判断 计时交互  --bug: 缺少json依赖
                    seckill.countDown(seckillId, nowTime, startTime, endTime);
                } else {
                    //alert('result: ' + result);
                }
            });
        }

    }
}
