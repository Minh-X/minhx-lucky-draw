package cn.minhx.config.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * @author permission
 */
@Builder
@Getter
@ToString
public class SuccessInfoAbstract extends AbstractResultInfo {

    protected static final Integer DEFAULT_CODE = 0;
    protected static final String DEFAULT_MESSAGE = "操作成功";

    @JsonInclude(JsonInclude.Include.NON_NULL)
    protected Object data;


    protected SuccessInfoAbstract(Object data) {
        super(true, DEFAULT_CODE, DEFAULT_MESSAGE);
        this.data = data;
    }
}