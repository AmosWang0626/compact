package cn.eyeo.compact.user.command.query;

import cn.eyeo.compact.domain.user.UserEntity;
import cn.eyeo.compact.dto.data.ErrorCode;
import cn.eyeo.compact.dto.data.UserVO;
import com.alibaba.cola.dto.SingleResponse;
import cn.eyeo.compact.common.exception.CompactBizException;
import cn.eyeo.compact.domain.gateway.UserGateway;
import cn.eyeo.compact.user.assembler.UserAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * 用户信息查询
 *
 * @author daoyuan
 * @date 2021/2/14 23:27
 */
@Component
public class UserInfoQueryExe {

    @Autowired
    private UserGateway userGateway;

    public SingleResponse<UserVO> execute(Long id) {
        UserEntity userEntity = userGateway.findById(id);
        if (Objects.isNull(userEntity)) {
            throw new CompactBizException(ErrorCode.B_USER_UNDEFINED);
        }

        return SingleResponse.of(UserAssembler.toValueObject(userEntity));
    }

}
