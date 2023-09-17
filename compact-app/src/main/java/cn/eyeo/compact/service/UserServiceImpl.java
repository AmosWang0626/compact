package cn.eyeo.compact.service;

import cn.eyeo.compact.api.IUserService;
import cn.eyeo.compact.dto.UserModifyCmd;
import cn.eyeo.compact.dto.UserRegisterCmd;
import cn.eyeo.compact.dto.data.UserVO;
import cn.eyeo.compact.dto.query.UserListByParamQuery;
import cn.eyeo.compact.dto.query.UserLoginQuery;
import cn.eyeo.compact.user.command.UserModifyCmdExe;
import cn.eyeo.compact.user.command.UserRegisterCmdExe;
import cn.eyeo.compact.user.command.query.UserInfoQueryExe;
import com.alibaba.cola.catchlog.CatchAndLog;
import com.alibaba.cola.dto.MultiResponse;
import com.alibaba.cola.dto.SingleResponse;
import cn.eyeo.compact.user.command.query.UserListByParamQueryExe;
import cn.eyeo.compact.user.command.query.UserLoginQueryExe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用户相关
 *
 * @author <a href="mailto:daoyuan0626@gmail.com">amos.wang</a>
 * @date 2021/1/8
 */
@Service
@CatchAndLog
public class UserServiceImpl implements IUserService {

    /**
     * xxxExe 避免 Service 膨胀利器
     */
    @Autowired
    private UserRegisterCmdExe userRegisterCmdExe;
    @Autowired
    private UserModifyCmdExe userModifyCmdExe;
    @Autowired
    private UserLoginQueryExe userLoginQueryExe;
    @Autowired
    private UserInfoQueryExe userInfoQueryExe;
    @Autowired
    private UserListByParamQueryExe userListByParamQueryExe;

    @Override
    public UserVO register(UserRegisterCmd cmd) {
        return userRegisterCmdExe.execute(cmd);
    }

    @Override
    public UserVO modify(UserModifyCmd cmd) {
        return userModifyCmdExe.execute(cmd);
    }

    @Override
    public void login(UserLoginQuery query) {
        userLoginQueryExe.execute(query);
    }

    @Override
    public SingleResponse<UserVO> getUserInfo(Long id) {
        return userInfoQueryExe.execute(id);
    }

    @Override
    public MultiResponse<UserVO> listByName(UserListByParamQuery query) {
        return userListByParamQueryExe.execute(query);
    }

}
