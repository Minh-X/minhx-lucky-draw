package cn.minhx.common.handler;

import cn.minhx.common.annotation.ResponseResult;
import cn.minhx.config.vo.SuccessInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.annotation.Nonnull;
import java.util.Objects;

/**
 * 切面对响应增强，自动将响应结果封装为定义的统一响应
 */
@Slf4j
@ControllerAdvice
@AllArgsConstructor
public class ResponseResultHandler implements ResponseBodyAdvice<Object> {

    /**
     * 判断是否需要增强
     *      1.判断类上是否有 @ResponseResult 注解
     *      2.判断方法上是否有 @ResponseResult 注解
     *      3.判断 @ResponseResult 中 ignore 属性是否为false
     *
     * @param methodParameter methodParameter
     * @param aClass aClass
     * @return 返回 true 则进行增强，反之不进行增强
     */
    @Override
    public boolean supports(MethodParameter methodParameter,
                            @Nonnull Class<? extends HttpMessageConverter<?>> aClass) {
        final var method = methodParameter.getMethod();
        final var clazz = Objects.requireNonNull(method, "method is null").getDeclaringClass();

        // 只处理 ResponseResult 标注的类或方法
        // 先尝试获取类上注解，如果没获取到再获取方法上注解
        var annotation = clazz.getAnnotation(ResponseResult.class);
        if (Objects.isNull(annotation)) {
            annotation = method.getAnnotation(ResponseResult.class);
        }

        //如果是 FileSystemResource 则不拦截(文件类型不进行增强)
        if (method.getAnnotatedReturnType().getType().getTypeName()
                .equals(FileSystemResource.class.getTypeName())) {
            return false;
        }
        // 有@ResponseResult注解且注解中ignore值为false则拦截
        return annotation != null && !annotation.ignore();
    }

    /**
     * 返回结果增强
     * @param data data
     * @param methodParameter methodParameter
     * @param mediaType mediaType
     * @param aClass aClass
     * @param serverHttpRequest serverHttpRequest
     * @param serverHttpResponse serverHttpResponse
     * @return {@link SuccessInfo}
     */
    @SneakyThrows
    @Override
    public Object beforeBodyWrite(Object data,
                                  @Nonnull MethodParameter methodParameter,
                                  @Nonnull MediaType mediaType,
                                  @Nonnull Class<? extends HttpMessageConverter<?>> aClass,
                                  @Nonnull ServerHttpRequest serverHttpRequest,
                                  @Nonnull ServerHttpResponse serverHttpResponse) {
        var successInfo = SuccessInfo.builder()
                .data(data)
                .build();
        ObjectMapper om = new ObjectMapper();

        if ((data instanceof String) && !MediaType.APPLICATION_XML_VALUE.equals(mediaType.toString())) {
            serverHttpResponse.getHeaders().set("Content-Type", "application/json");
            return om.writeValueAsString(successInfo);
        }

        if (Objects.isNull(data) && MediaType.TEXT_HTML_VALUE.equals(mediaType.toString())) {
            serverHttpResponse.getHeaders().set("Content-Type", "application/json");
            return om.writeValueAsString(successInfo);
        }

        return successInfo;
    }
}