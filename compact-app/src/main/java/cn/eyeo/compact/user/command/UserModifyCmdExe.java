package cn.eyeo.compact.user.command;

import cn.eyeo.compact.domain.user.UserEntity;
import cn.eyeo.compact.dto.UserModifyCmd;
import cn.eyeo.compact.dto.data.ErrorCode;
import cn.eyeo.compact.dto.data.UserVO;
import cn.eyeo.compact.common.exception.CompactBizException;
import cn.eyeo.compact.domain.gateway.UserGateway;
import cn.eyeo.compact.user.assembler.UserAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * UserAddCmdExe
 *
 * @author <a href="mailto:daoyuan0626@gmail.com">amos.wang</a>
 * @date 2021/1/10
 */
@Component
public class UserModifyCmdExe {

    @Autowired
    private UserGateway userGateway;

    public UserVO execute(UserModifyCmd cmd) {
        // check 用户名是否重复
        if (userGateway.checkByUsername(cmd.getId(), cmd.getUsername())) {
            throw new CompactBizException(ErrorCode.B_USER_USERNAME_REPEAT);
        }

        UserEntity userEntity = userGateway.save(UserAssembler.toEntity(cmd));
        return UserAssembler.toValueObject(userEntity);
    }

}
