package cn.com.taiji.redis.test.controller;

import cn.com.taiji.redis.test.model.User;
import cn.com.taiji.redis.test.service.UserService;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

	@Autowired
	private StringRedisTemplate redisTemplate;

	@Autowired
	private UserService userService;

	@RequestMapping("/getUser")
	public User getUser(String id) {
		User user = userService.getUser(id);
		return user;
	}

	@RequestMapping("/deleteUser")
	public String deleteUser(String id) {
		userService.deleteUser(id);
		return "执行删除";
	}

	@RequestMapping("/putUser")
	public User putUser(String id) {
		User user = userService.putUser(id);
		return user;
	}

	@RequestMapping("/getUserNokey")
	public User getUserNokey(String id) {
		User user = userService.getUserNokey(id);
		return user;
	}

	// stringRedisTemplate.opsForValue().set("test","100",60*10,TimeUnit.SECONDS);//向redis里存入数据和设置缓存时间
	// stringRedisTemplate.boundValueOps("test").increment(-1);//val做-1操作
	// stringRedisTemplate.opsForValue().get("test")//根据key获取缓存中的val
	// stringRedisTemplate.boundValueOps("test").increment(1);//val +1
	// stringRedisTemplate.getExpire("test")//根据key获取过期时间
	// stringRedisTemplate.getExpire("test",TimeUnit.SECONDS)//根据key获取过期时间并换算成指定单位
	// stringRedisTemplate.delete("test");//根据key删除缓存
	// stringRedisTemplate.hasKey("546545");//检查key是否存在，返回boolean值
	// stringRedisTemplate.opsForSet().add("red_123", "1","2","3");//向指定key中存放set集合
	// stringRedisTemplate.expire("red_123",1000 , TimeUnit.MILLISECONDS);//设置过期时间
	// stringRedisTemplate.opsForSet().isMember("red_123", "1")//根据key查看集合中是否存在指定数据
	// stringRedisTemplate.opsForSet().members("red_123");//根据key获取set集合

	// 获取当前时间戳
	// //方法 一
	// System.currentTimeMillis();
	// //方法 二
	// Calendar.getInstance().getTimeInMillis();
	// //方法 三
	// new Date().getTime();

	@RequestMapping("/sendMessage")
	public String sendMessage() {
		String startTime = Long.toString(System.currentTimeMillis());
		int count;
		String MobileMsssage = "看短信";
		redisTemplate.opsForValue().set("start", startTime, 30 * 60L, TimeUnit.SECONDS);
		if (!redisTemplate.hasKey(MobileMsssage)) {
			count = 1;
			redisTemplate.opsForValue().set(MobileMsssage, String.valueOf(count), 60L, TimeUnit.SECONDS);
			String result = MobileMsssage + "已发送" + redisTemplate.opsForValue().get(MobileMsssage) + "条";
			return result;
		}
		String times = redisTemplate.opsForValue().get(MobileMsssage);
		long start = Long.parseLong(redisTemplate.opsForValue().get("start"));
		count = Integer.parseInt(times);
		count++;
		//三次或者时间小于30
		if (count > 3 || (System.currentTimeMillis() - start) < 30) {
			return "操作频繁";
		} else {
			redisTemplate.opsForValue().set(MobileMsssage, String.valueOf(count), 60L, TimeUnit.SECONDS);
			String result = MobileMsssage + "已发送" + redisTemplate.opsForValue().get(MobileMsssage) + "条";
			return result;
		}

	}
}
