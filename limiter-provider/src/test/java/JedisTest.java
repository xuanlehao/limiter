import com.google.common.util.concurrent.RateLimiter;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @author Leo xuan
 * @date 2018/9/6
 */
public class JedisTest {

	@Test
	public void jedisTest() {
		ApplicationContext context = new ClassPathXmlApplicationContext("application-jedis.xml");
		JedisPool redis = (JedisPool)context.getBean("jedisPool");
		Jedis jedis = redis.getResource();
		jedis.set("xuan","123");
		System.out.println(jedis.get("xuan"));
		RateLimiter.create(100);
	}
}
