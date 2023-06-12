package cn.minhx.config.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class SuccessInfo extends ResultInfo {

    protected static final Integer DEFAULT_CODE = 0;
    protected static final String DEFAULT_MESSAGE = "操作成功";

    @JsonInclude(JsonInclude.Include.NON_NULL)
    protected Object data;


    protected SuccessInfo(Object data) {
        super(true, DEFAULT_CODE, DEFAULT_MESSAGE);
        this.data = data;
    }
}