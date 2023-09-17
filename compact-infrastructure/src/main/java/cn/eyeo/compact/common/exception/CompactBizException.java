package cn.eyeo.compact.common.exception;

import cn.eyeo.compact.dto.data.ErrorCode;
import com.alibaba.cola.exception.BizException;

/**
 * 业务异常
 *
 * @author <a href="mailto:daoyuan0626@gmail.com">amos.wang</a>
 * @date 2021/11/27
 */
public class CompactBizException extends BizException {

    private static final long serialVersionUID = -2776433598172531409L;

    public CompactBizException(ErrorCode errorCode) {
        super(errorCode.getErrCode(), errorCode.getErrDesc());
    }
}
