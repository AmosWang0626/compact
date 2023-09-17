package cn.eyeo.compact.user.command.query;

import cn.eyeo.compact.common.exception.CompactBizException;
import cn.eyeo.compact.domain.gateway.UserGateway;
import cn.eyeo.compact.domain.user.UserEntity;
import cn.eyeo.compact.dto.data.ErrorCode;
import cn.eyeo.compact.dto.query.UserLoginQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * UserListByNameQueryExe
 *
 * @author <a href="mailto:daoyuan0626@gmail.com">amos.wang</a>
 * @date 2021/1/10
 */
@Component
public class UserLoginQueryExe {

    @Autowired
    private UserGateway userGateway;

    public void execute(UserLoginQuery query) {
        UserEntity userEntity = userGateway.findPasswordInfo(query.getUsername());
        if (Objects.isNull(userEntity)) {
            throw new CompactBizException(ErrorCode.B_USER_PASSWORD_ERROR);
        }

        // 校验密码是否正确
        if (!userEntity.getPassword().isCorrect(query.getPassword())) {
            throw new CompactBizException(ErrorCode.B_USER_PASSWORD_ERROR);
        }
    }

}
