package org.seckill.web;

import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.dto.SeckillResult;
import org.seckill.entity.Seckill;
import org.seckill.enums.SeckillStatEnum;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.exception.SeckillException;
import org.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * 控制层开发
 *      接受参数，依据相关验证和判断，进行跳转控制，实现Restful接口，
 * Created by Yan_Jiang on 2018/7/14.
 */
@Controller
@RequestMapping("/seckill") //url: /模块/资源/{xxx}/其他
public class seckillController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SeckillService seckillService;

    //配置资源子模块 并指定请求方式为GET
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(Model model) {

        //获取列表页
        List<Seckill> list = seckillService.getSecKillList();
        model.addAttribute("list", list);
        return "list";
    }

    //{seckillId}：占位符 需要在下面方法中@PathVariable("seckillId")来说明
    //URL 中的 {xxx} 占位符可以通过@PathVariable(“xxx“) 绑定到操作方法的形参中
    @RequestMapping(value = "/{seckillId}/detail", method = RequestMethod.GET)
    public String detail(@PathVariable("seckillId") Long seckillId, Model model) {

        //秒杀商品id不存在是，跳转到list列表页
        if (seckillId == null) {
            return "redirect:/seckill/list";
        }
        Seckill seckill = seckillService.getById(seckillId);
        if (seckill == null) {
            return "forward:/seckill/list";
        }

        //秒杀商品存在，存入model对象中
        model.addAttribute("seckill", seckill);
        return "detail";
    }

    //ajax请求暴露秒杀接口
    @RequestMapping(value = "/{seckillId}/exposer",
            method = RequestMethod.POST,
            produces = {"application/json;charset=UTF-8"})
    //@ResponseBody：告诉springMVC 将SeckillResult<Exposer>封装json格式 需要在RequestMapping进行对应produces说明返回给浏览器信息
    @ResponseBody
    public SeckillResult<Exposer> exposerUrl(@PathVariable("seckillId") Long seckillId){

        SeckillResult<Exposer> result;
        try {
            //exposer：秒杀具体信息
            Exposer exposer = seckillService.exportSecKillUrl(seckillId);
            result = new SeckillResult<Exposer>(true, exposer);
        } catch (Exception e) {
            //出现异常
            logger.error(e.getMessage(), e);
            //打印错误信息
            result = new SeckillResult<Exposer>(false, e.getMessage());
        }
        return result;
    }


    @RequestMapping(value = "/{seckillId}/{md5}/execution",
            method = RequestMethod.POST,
            produces = {"application/json;charset=UTF-8"})
    //@CookieValue(value = "killPhone", required = false) Long phone
    //从浏览器Cookie中取值，当这个值不存在时，不报异常，交由程序处理
    @ResponseBody
    public SeckillResult<SeckillExecution> execution(@PathVariable("seckillId") Long seckillId,
                                                     @PathVariable("md5") String md5,
                                                     @CookieValue(value = "killPhone", required = false) Long phone) {

        //验证注册手机号
        if (phone == null) {
            new SeckillResult<SeckillExecution>(false, "手机号未注册");
        }

        SeckillResult<SeckillExecution> result;

        try {
            //**优化；调用存储过程
            SeckillExecution execution = seckillService.executeSecKillprocedure(seckillId, phone, md5);

            /** 优化前
             * SeckillExecution execution = seckillService.executeSecKill(seckillId, phone, md5);
             */
            return new SeckillResult<SeckillExecution>(true, execution);
        } catch (RepeatKillException e1) {
            //处理系统允许的异常(秒杀重复)
            SeckillExecution execution = new SeckillExecution(seckillId, SeckillStatEnum.REPEAT_KILL);
            return new SeckillResult<SeckillExecution>(true, execution);
        } catch (SeckillCloseException e2) {
            //处理系统允许的异常(秒杀结束)
            SeckillExecution execution = new SeckillExecution(seckillId, SeckillStatEnum.END);
            return new SeckillResult<SeckillExecution>(true, execution);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            //除了上述两个异常外的所有异常
            SeckillExecution execution = new SeckillExecution(seckillId, SeckillStatEnum.INNER_ERROR);
            return new SeckillResult<SeckillExecution>(true, execution);
        }
    }

    //获取系统时间
    @RequestMapping(value = "/time/now", method = RequestMethod.GET)
    @ResponseBody
    public SeckillResult<Long> time() {
        Date now = new Date();
        return new SeckillResult<Long>(true, now.getTime());
    }










}
