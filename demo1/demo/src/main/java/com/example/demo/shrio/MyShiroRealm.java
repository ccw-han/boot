package com.example.demo.shrio;

import com.example.demo.entity.UserInfo;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;


public class MyShiroRealm extends AuthorizingRealm {

    /**
     * 方面用于加密 参数：AuthenticationToken是从表单穿过来封装好的对象
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        System.out.println("doGetAuthenticationInfo:" + token);

        // 将AuthenticationToken强转为AuthenticationToken对象
        UsernamePasswordToken upToken = (UsernamePasswordToken) token;

        // 获得从表单传过来的用户名
        String username = upToken.getUsername();

        // 从数据库查看是否存在用户
//        UserService userService = new UserService();

        // 如果用户不存在，抛此异常
//        if (!userService.selectUsername(username)) {
//            throw new UnknownAccountException("无此用户名！");
//        }

        // 认证的实体信息，可以是username，也可以是用户的实体类对象，这里用的用户名
        Object principal = username;
        // 从数据库中查询的密码
//        Object credentials = userService.selectPassword(username);
        // 颜值加密的颜，可以用用户名
//        ByteSource credentialsSalt = ByteSource.Util.bytes(username);
        // 当前realm对象的名称，调用分类的getName()
        String realmName = this.getName();

        // 创建SimpleAuthenticationInfo对象，并且把username和password等信息封装到里面
        // 用户密码的比对是Shiro帮我们完成的
//        SimpleAuthenticationInfo info = null;
//        info = new SimpleAuthenticationInfo(principal, credentials, credentialsSalt, realmName);
        return null;
    }

    // 用于授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {

        System.out.println("MyShiroRealm的doGetAuthorizationInfo授权方法执行");

        // User user=(User)
        // principals.fromRealm(this.getClass().getName()).iterator().next();//获取session中的用户
        // System.out.println("在MyShiroRealm中AuthorizationInfo（授权）方法中从session中获取的user对象:"+user);

        // 从PrincipalCollection中获得用户信息
//        Object principal = principals.getPrimaryPrincipal();
//        System.out.println("ShiroRealm  AuthorizationInfo:" + principal.toString());

        // 根据用户名来查询数据库赋予用户角色,权限（查数据库）
//        Set<String> roles = new HashSet<>();
//        Set<String> permissions = new HashSet<>();
//		2018.09.14更新
        //		给用户添加user权限 (没有进行判断、对所有的用户给user权限)
//        if ("user".equals(principal)) {
//            roles.add("user");
//            permissions.add("user:query");
//        }
////		当用户名为admin时 为用户添加权限admin  两个admin可以理解为连个字段
//        if ("admin".equals(principal)) {
//            roles.add("admin");
//            permissions.add("admin:query");
//        }
////		为用户添加visit游客权限，在url中没有为visit权限，所以，所有的操作都没权限
//        if ("visit".equals(principal)) {
//            roles.add("visit");
//            permissions.add("visit:query");
//        }
//              更新以上代码
//        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo(roles);
//        //添加权限
//        info.setStringPermissions(permissions);
//          info.addRole(role.getRole());
        return null;
        // return null;
        /*
         * filterChainDefinitionMap.put(“/add”, “perms[权限添加]”);就说明访问/add这个链接必须要有“权限添加”这个权限才可以访问，如果在shiro配置文件中添加了filterChainDefinitionMap.put(“/add”, “roles[100002]，perms[权限添加]”);
         * 就说明访问/add这个链接必须要有“权限添加”这个权限和具有“100002”这个角色才可以访问。
         *
         * */
        /*
        * controller
        * // 属于user角色@RequiresRoles("user")
	// 必须同时属于user和admin角@RequiresRoles({ "user", "admin" })
	// 属于user或者admin之一;修改logical为OR 即可@RequiresRoles(value = { "user", "admin"},
	// logical = Logical.OR)


	@RequestMapping("/showUserHtml.action")
	@RequiresRoles(value = { "user", "admin"},logical = Logical.OR)
	@RequiresPermissions("user:query")
	* https://www.e-learn.cn/content/qita/2325552 require各种东西
	public String userHtml() {
		return "/user";
	}
login
*
*
* @RequestMapping("/login.action")
	public String login(String username, String password, Map<String, Object> map, HttpSession session) {
		System.out.println(username + "---" + password);
		// 获得当前Subject
		Subject currentUser = SecurityUtils.getSubject();
		// 验证用户是否验证，即是否登录
		if (!currentUser.isAuthenticated()) {
			String msg = "";
			// 把用户名和密码封装为 UsernamePasswordToken 对象
			UsernamePasswordToken token = new UsernamePasswordToken(username, password);

			// remembermMe记住密码
			token.setRememberMe(true);
			try {
				// 执行登录.
				currentUser.login(token);

				// 登录成功...
				return "redirect:/LoginSuccess.action";
			} catch (IncorrectCredentialsException e) {
				msg = "登录密码错误";
				System.out.println("登录密码错误!!!" + e);
			} catch (ExcessiveAttemptsException e) {
				msg = "登录失败次数过多";
				System.out.println("登录失败次数过多!!!" + e);
			} catch (LockedAccountException e) {
				msg = "帐号已被锁定";
				System.out.println("帐号已被锁定!!!" + e);
			} catch (DisabledAccountException e) {
				msg = "帐号已被禁用";
				System.out.println("帐号已被禁用!!!" + e);
			} catch (ExpiredCredentialsException e) {
				msg = "帐号已过期";
				System.out.println("帐号已过期!!!" + e);
			} catch (UnknownAccountException e) {
				msg = "帐号不存在";
				System.out.println("帐号不存在!!!" + e);
			} catch (UnauthorizedException e) {
				msg = "您没有得到相应的授权！";
				System.out.println("您没有得到相应的授权！" + e);
			} catch (Exception e) {
				System.out.println("出错！！！" + e);
			}
			map.put("msg", msg);
			return "/index";
		}

		// 登录成功，重定向到LoginSuccess.action
		return "redirect:/LoginSuccess.action";

	}

* 加密算法
* //盐值用的用的是对用户名的加密（测试用的"lisi"）
                ByteSource credentialsSalt01 = ByteSource.Util.bytes("lisi");
		Object salt = null;//盐值
		Object credential = "123456";//密码
		String hashAlgorithmName = "MD5";//加密方式
                //1024指的是加密的次数
		Object simpleHash = new SimpleHash(hashAlgorithmName, credential,
				credentialsSalt01, 1024);
		System.out.println("加密后的值----->" + simpleHash);

        *
        * */

    }


}

