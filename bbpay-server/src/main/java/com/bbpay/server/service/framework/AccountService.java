package com.bbpay.server.service.framework;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.security.utils.Digests;
import org.springside.modules.utils.DateProvider;
import org.springside.modules.utils.Encodes;
import com.bbpay.server.entity.UserEntity;
import com.bbpay.server.repository.UserRepository;
import com.bbpay.server.service.ServiceException;
import com.bbpay.server.service.framework.ShiroDbRealm.ShiroUser;

/**
 * 用户管理类.
 *
 * @author calvin
 */
// Spring Service Bean的标识.
@Component
@Transactional(readOnly = true)
public class AccountService {
    public static void main(String[] args) {
        AccountService account = new AccountService();
        UserEntity user = new UserEntity();
        //user.setPlainPassword("li-hellopass");   //李松
        user.setPlainPassword("hellopass-ty");   //滕月
        account.entryptPassword(user);
        System.out.println(user.getPassword());
        System.out.println(user.getSalt());
    }
    public static final String HASH_ALGORITHM = "SHA-1";
    public static final int HASH_INTERATIONS = 1024;
    private static final int SALT_SIZE = 8;
    private static Logger logger = LoggerFactory.getLogger(AccountService.class);
    private UserRepository userDao;
    private DateProvider dateProvider = DateProvider.DEFAULT;
    @Transactional(readOnly = false)
    public void deleteUser(Long id) {
        if (isSupervisor(id)) {
            logger.warn("操作员{}尝试删除超级管理员用户", getCurrentUserName());
            throw new ServiceException("不能删除超级管理员用户");
        }
        userDao.delete(id);
    }
    /**
     * 设定安全的密码，生成随机的salt并经过1024次 sha-1 hash
     */
    private void entryptPassword(UserEntity user) {
        byte[] salt = Digests.generateSalt(SALT_SIZE);
        user.setSalt(Encodes.encodeHex(salt));
        byte[] hashPassword = Digests.sha1(user.getPlainPassword().getBytes(), salt, HASH_INTERATIONS);
        user.setPassword(Encodes.encodeHex(hashPassword));
    }
    public UserEntity findUserByLoginName(String loginName) {
        return userDao.findByLoginName(loginName);
    }
    public List<UserEntity> getAllUser() {
        return (List<UserEntity>) userDao.findAll();
    }
    /**
     * 取出Shiro中的当前用户LoginName.
     */
    private String getCurrentUserName() {
        ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
        return user.loginName;
    }
    public UserEntity getUser(Long id) {
        return userDao.findOne(id);
    }
    /**
     * 判断是否超级管理员.
     */
    private boolean isSupervisor(Long id) {
        return id == 1;
    }
    @Transactional(readOnly = false)
    public void registerUser(UserEntity user) {
        entryptPassword(user);
        user.setRoles("user");
        user.setRegisterDate(dateProvider.getDate());
        userDao.save(user);
    }
    public void setDateProvider(DateProvider dateProvider) {
        this.dateProvider = dateProvider;
    }
    @Autowired
    public void setUserDao(UserRepository userDao) {
        this.userDao = userDao;
    }
    @Transactional(readOnly = false)
    public void updateUser(UserEntity user) {
        if (StringUtils.isNotBlank(user.getPlainPassword())) {
            entryptPassword(user);
        }
        userDao.save(user);
    }
}
