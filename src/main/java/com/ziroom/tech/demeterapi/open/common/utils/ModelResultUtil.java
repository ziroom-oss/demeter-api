package com.ziroom.tech.demeterapi.open.common.utils;

import com.ziroom.tech.demeterapi.open.common.model.ModelResult;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.Map;

/**
 * 业务层ModelResult返回工具类
 * @author xuzeyu
 */
public class ModelResultUtil {

    /**
     * @Description: 生成成功时的通用实体对象
     */
    public static <T> ModelResult<T> success(T model) {
        ModelResult<T> modelResult = new ModelResult<>();
        modelResult.setResult(model);
        modelResult.setSuccess(true);
        return modelResult;
    }


    /**
     * 返回失败结构体
     *
     * @return ModelResult<T>   统一响应结构体
     */
    public static <T> ModelResult<T> error(String code, String errorMessage) {
        ModelResult<T> modelResult = new ModelResult<T>();
        modelResult.setResultCode(code);
        modelResult.setResultMessage(errorMessage);
        modelResult.setSuccess(false);
        return modelResult;
    }

    /**
     * @param modelResult
     * @return
     * @Description: 判断服务是否成功</ br>
     * 支持对类型为ModelResult<T>， 的对象进行非空、不包含错误信息、boolean型结果、非空集合、非空实体的判断
     */
    public static <T> boolean isSuccess(ModelResult<T> modelResult) {
        if (StringUtils.isNoneBlank(modelResult.getResultMessage())) {
            return false;
        }

        if (modelResult.isSuccess() && modelResult.getResult() != null) {
            if (modelResult.getResult() instanceof Boolean) {
                return (Boolean) modelResult.getResult();
            } else if (modelResult.getResult() instanceof Collection<?>) {
                return !((Collection<?>) modelResult.getResult()).isEmpty();
            } else if (modelResult.getResult() instanceof Map<?, ?>) {
                return !((Map<?, ?>) modelResult.getResult()).isEmpty();
            } else if (modelResult.getResult() instanceof Object[]) {
                return ((Object[]) modelResult.getResult()).length != 0;
            }
            return true;
        }
        return false;
    }
}
