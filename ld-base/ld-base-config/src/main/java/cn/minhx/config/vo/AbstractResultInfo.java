package cn.minhx.config.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

import java.io.Serializable;

/**
 * 统一返回结果
 * @author permission
 */
@Getter
public abstract class AbstractResultInfo implements Serializable {
    /**
     * true or false
     */
    protected Boolean result;
    protected Integer code;
    /**
     * @JsonInclude(JsonInclude.Include.NON_NULL) 表示为 null 时不参与序列化，也就是说响应中不会出现此字段
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    protected String message;

    protected AbstractResultInfo(Boolean result, Integer code, String message) {
        this.result = result;
        this.code = code;
        this.message = message;
    }

}