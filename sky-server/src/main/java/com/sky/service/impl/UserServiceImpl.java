package com.sky.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sky.constant.JwtClaimsConstant;
import com.sky.constant.MessageConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.exception.LoginFailedException;
import com.sky.mapper.UserMapper;
import com.sky.properties.JwtProperties;
import com.sky.properties.WeChatProperties;
import com.sky.service.UserService;
import com.sky.utils.HttpClientUtil;
import com.sky.utils.JwtUtil;
import com.sky.vo.UserLoginVO;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    //定义微信登录的接口地址
    public static final String WX_LOGIN = "https://api.weixin.qq.com/sns/jscode2session";
    private final WeChatProperties weChatProperties;
    private final JwtProperties jwtProperties;
    private final ObjectMapper objectMapper;
    private final UserMapper userMapper;

    /**
     * 微信登录
     * @param userLoginDTO
     * @return
     */
    @SneakyThrows//伪装异常处理
    @Override
    public UserLoginVO wxLogin(UserLoginDTO userLoginDTO){
        //调用微信服务接口，获得当前微信用户的openid
        String openid = getOpenid(userLoginDTO.getCode());
        //判断openid是否为空，若为空则登录失败，抛业务异常
            if(openid == null){
                throw new LoginFailedException(MessageConstant.LOGIN_FAILED);
            }
            //判断是否是新用户，若为新用户自动注册
            User user = userMapper.getByOpenid(openid);
            if(user == null){
                user = User.builder().openid(openid).createTime(LocalDateTime.now()).build();
                userMapper.insertUser(user);
            }
        //生成jwt令牌，封装成VO对象返回
        Map<String,Object> claim = new HashMap<>();
            claim.put(JwtClaimsConstant.USER_ID,user.getId());
        String token = JwtUtil.createJWT(jwtProperties.getUserSecretKey(), jwtProperties.getUserTtl(), claim);
        return UserLoginVO.builder().id(user.getId()).openid(openid).token(token).build();
    }

    /**
     * 获取微信用户的openid
     * @param code
     * @return
     * @throws JsonProcessingException
     */
    private String getOpenid(String code) throws JsonProcessingException{
        //调用微信服务接口，获得当前微信用户的openid
        Map<String,String> map = new HashMap<>();
        map.put("appId",weChatProperties.getAppid());
        map.put("secret",weChatProperties.getSecret());
        map.put("js_code",code);
        map.put("grant_type","authorization_code");
        String json = HttpClientUtil.doGet(UserServiceImpl.WX_LOGIN, map);

        JsonNode jsonObject = objectMapper.readTree(json);
        String openid = jsonObject.get("openid").asText();

        return openid;
    }
}
