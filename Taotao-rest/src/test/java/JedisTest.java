import java.util.HashSet;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

public class JedisTest {

	@Test
	public void test() {
		// 创建一个jedis对象
		Jedis jedis = new Jedis("192.168.244.128", 6379);
		// 调用jedis对象的方法，方法名称和redis的命名一致
		jedis.set("key1", "1");
		System.out.println(jedis.get("key1"));
		// 关闭jedis对象
		jedis.close();

	}

	/**
	 * 使用连接池
	 */
	@Test
	public void testJedisPool() {
		// 创建jedis连接池
		JedisPool pool = new JedisPool("192.168.244.128", 6379);
		// 从连接池中获取对象
		Jedis jedis = pool.getResource();
		System.out.println(jedis.get("key1"));
	}

	/**
	 * 测试集群版（很快呦）
	 */
	@Test
	public void jiqunTest() {
		// 创建一个集合
		HashSet<HostAndPort> nodes = new HashSet<>();
		nodes.add(new HostAndPort("192.168.244.128", 7001));
		nodes.add(new HostAndPort("192.168.244.128", 7002));
		nodes.add(new HostAndPort("192.168.244.128", 7003));
		nodes.add(new HostAndPort("192.168.244.128", 7004));
		nodes.add(new HostAndPort("192.168.244.128", 7005));
		nodes.add(new HostAndPort("192.168.244.128", 7006));
		// 新建JedisCluster对象
		JedisCluster cluser = new JedisCluster(nodes);
		cluser.set("k1", "4545");
		System.out.println(cluser.get("k1"));
	}

	/**
	 * spring和jedis整合的测试(集群版)
	 */
	@Test
	public void jedisAndSpring() {
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
				"classpath:spring/applicationContext-*.xml");
		JedisCluster jedisCluster = (JedisCluster) applicationContext.getBean("redisClient");
		jedisCluster.set("xh", "1");
		System.out.println(jedisCluster.get("xh"));
		jedisCluster.close();
	}
}
