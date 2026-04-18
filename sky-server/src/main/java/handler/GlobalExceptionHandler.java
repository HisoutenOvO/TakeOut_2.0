package handler;

import com.sky.constant.MessageConstant;
import com.sky.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

@RestControllerAdvice
@Slf4j
/**
 * 全局异常处理器类，负责处理异常
 */
public class GlobalExceptionHandler {
    /**
     * 负责捕获业务异常
     * @param e
     * @return
     */
    @ExceptionHandler
    public Result baseExceptionHandler(Exception e){
        //通过getMessage方法获取异常信息内容
        log.error("异常信息：{}",e.getMessage());
        //返回异常信息
        return Result.error(e.getMessage());
    }

    /**
     * 捕获sql异常
     * @param e
     * @return
     */
    public Result sqlExceptionHandler(SQLIntegrityConstraintViolationException e){
        //sql中报错重复的字段时会显示 Duplicate entry 'xxx' for key 'xxx'
        String message =e.getMessage();
        //用string的contains方法判断是否是sql异常
        if(message.contains("Duplicate entry")){
            //通过用空格作分隔符依次将字符串Duplicate entry 'xxx' for key 'xxx'获取到split里
            String[] split = message.split(" ");
            //调用split[2]，即'xxx'和常量异常信息拼凑成异常信息
            String errMessage = split[2] + MessageConstant.ALREADY_EXISTS;
            return Result.error(errMessage);
        }else{
            //不是这个错误则返回未知错误
            return Result.error(MessageConstant.UNKNOWN_ERROR);
        }
    }
}
