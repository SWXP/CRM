package com.yjxxt.crm.exceptions;

import com.alibaba.fastjson.JSON;
import com.yjxxt.crm.base.ResultInfo;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class GlobalExceptionResolver implements HandlerExceptionResolver {


    @Override
    public ModelAndView resolveException(HttpServletRequest req, HttpServletResponse resp, Object handler, Exception e) {

        //设置默认异常信息
        ModelAndView mav = new ModelAndView("");
        mav.addObject("code",400);
        mav.addObject("msg","系统异常,请稍后再试...");

        //判断HandlerMethod
        if(handler instanceof HandlerMethod){
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            //获取方法上的ResponseBody注解
            ResponseBody responseBody = handlerMethod.getMethod().getDeclaredAnnotation(ResponseBody.class);

            //判断方法上是否存在ResponseBody注解
            if(responseBody == null){
                //方法返回视图
                if(e instanceof ParamsException){
                    ParamsException ps = (ParamsException) e;
                    mav.addObject("code",ps.getCode());
                    mav.addObject("msg",ps.getMsg());
                }
                return mav;
            }else{
                //方法返回JSON
                ResultInfo resultInfo = new ResultInfo();
                resultInfo.setCode(300);
                resultInfo.setMsg("系统异常，请重试！");
                if(e instanceof ParamsException){
                    ParamsException ps = (ParamsException) e;
                    //这边需要对resultInfo进行设置,因为和视图无关
                    resultInfo.setCode(ps.getCode());
                    resultInfo.setMsg(ps.getMsg());
                }
                //设置相应类型和编码格式
                resp.setContentType("application/json;charset=utf-8");
                PrintWriter out = null;
                try {
                    out = resp.getWriter();
                    out.write(JSON.toJSONString(resultInfo));
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }finally {
                    out.flush();
                    if(out!=null){
                        out.close();
                    }
                }
                return null;
            }
        }




        //判断是否是未登录异常
        if(e instanceof NoLoginException){
            mav = new ModelAndView("redirect:/index");
            //mav = new ModelAndView("redirect:"+req.getContextPath()+"/index");
            //System.out.println(req.getContextPath());
            return mav;
        }



        return mav;
    }
}
