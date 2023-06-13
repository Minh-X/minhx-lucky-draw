package cn.minhx.common.handler;

import cn.minhx.config.exception.LdException;
import cn.minhx.config.vo.FailInfoAbstract;
import com.mysql.cj.jdbc.exceptions.MysqlDataTruncation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

/**
 * @author permission
 */
@Slf4j
@RestControllerAdvice
public class SysExceptionHandler {

    /**
     * 最大的兜底错误处理
     *
     * @param ex ex
     * @return {@link FailInfoAbstract}
     */
    @ExceptionHandler(value = Exception.class)
    public FailInfoAbstract exception(Exception ex) {
        log.error("Exception_info:{}", ex.getMessage());
        log.error("Exception_info:", ex);
        return FailInfoAbstract.builder().exception(ex.getMessage()).build();
    }

    /**
     * 参数绑定错误
     *
     * @param ex ex
     * @return  {@link FailInfoAbstract}
     */
    @ExceptionHandler(value = BindException.class)
    public FailInfoAbstract exception(BindException ex) {
        String defaultMessage = Objects.requireNonNull(ex.getBindingResult().getFieldError()).getDefaultMessage();
        log.error("Exception_info:{}", defaultMessage);
        log.error("Exception_info:", ex);
        return FailInfoAbstract.builder().exception(defaultMessage).build();
    }


    @ExceptionHandler(value = LdException.class)
    public FailInfoAbstract sysException(Exception ex) {
        log.error("Exception_info:{}", ex.getMessage());
        log.error("Exception_info:", ex);
        return FailInfoAbstract.builder().exception(ex.getMessage()).build();
    }

    @ExceptionHandler(value = MysqlDataTruncation.class)
    public FailInfoAbstract mysqlDataTruncation(Exception ex) {
        log.error("Exception_info:{}", ex.getMessage());
        log.error("Exception_info:", ex);
        return new FailInfoAbstract(500, ex.getMessage());
    }

    @ExceptionHandler(value = DataIntegrityViolationException.class)
    public FailInfoAbstract dataIntegrityViolationException(Exception ex) {
        log.error("Exception_info:{}", ex.getMessage());
        log.error("Exception_info:", ex);
        String message = ex.getMessage();
        String[] split = message.split("\r\n###");
        for (String str : split) {
            if (str.trim().isBlank() || str.trim().contains("Error")) {
                continue;
            }
            String[] split1 = str.split(":");
            if (split1.length > 0) {
                message = split1[split1.length - 1].trim();
            }
        }
        return new FailInfoAbstract(500, message);
    }

}
