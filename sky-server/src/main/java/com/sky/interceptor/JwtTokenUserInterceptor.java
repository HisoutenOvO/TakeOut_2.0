package com.sky.interceptor;

import com.sky.constant.JwtClaimsConstant;
import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.exception.UserNotLoginException;
import com.sky.properties.JwtProperties;
import com.sky.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtTokenUserInterceptor implements HandlerInterceptor {
    private final JwtProperties jwtProperties;

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,Object handler) throws Exception{
        //判断当前拦截到的是Controller方法还是其他资源，如果不是Controller方法则直接放行
        if(!(handler instanceof HandlerMethod)){
            return true;
        }
        //1.从请求头中获取令牌
        String token = request.getHeader(jwtProperties.getUserTokenName());
        //2.校验令牌
        try{
            log.info("jwt校验:{}",token);
            //调用jwt工具类进行解析,第一个参数是传入的用于解析的密钥，第二个参数是传入的令牌。解析成功返回Claims对象，失败则抛出异常
            Claims claims = JwtUtil.parseJWT(jwtProperties.getUserSecretKey(),token);
            //从解析完jwt后得到的对象claims中获取当前登录的用户id，基于Long的转换方法，将从claims中得到的userid转换为字符串，再把字符串转化为long，这样做的目的是防止转换类型错误
            Long userId = Long.valueOf(claims.get(JwtClaimsConstant.USER_ID).toString());
            log.info("当前用户的id:{}",userId);
            //保存当前登录用户的id到当前线程的存储空间ThreadLocal
            BaseContext.setCurrentId(userId);
            //放行
            return true;
        }catch (Exception e){
            log.error("校验失败");
            throw new UserNotLoginException(MessageConstant.USER_NOT_LOGIN);
        }
    }
}
