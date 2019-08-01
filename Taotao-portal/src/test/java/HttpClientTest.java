import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

public class HttpClientTest {

	@Test
	public void httpClentGet() throws ClientProtocolException, IOException {
		// 创建httpclient
		CloseableHttpClient httpClient = HttpClients.createDefault();
		// 创建一个get
		// get里面包含url
		HttpGet get = new HttpGet("http://www.baidu.com");
		// 执行请求
		CloseableHttpResponse response = httpClient.execute(get);
		// 取响应的结果
		response.getHeaders("content-type");
		// EntityUtils.toString(response.getEntity()) (取返回结果)
		// response.getStatusLine().getStatusCode() (取状态码)
		if (response.getStatusLine().getStatusCode() == 200) {
			System.out.println("状态码：" + response.getStatusLine().getStatusCode() + "\n"
					+ EntityUtils.toString(response.getEntity(), "utf-8"));
		}

		httpClient.close();

	}

	// @Test
	// public void httpClientPost() throws ClientProtocolException, IOException {
	// // 创建httpclient
	// CloseableHttpClient httpClient = HttpClients.createDefault();
	// // 创建post请求
	// HttpPost post = new HttpPost("http://localhost:8080/httpclient/popst.html");
	// // 创建一个entity，模拟一个表单
	// List<NameValuePair> lvlistList = new ArrayList<>();
	// // lvlistList.addAll((Collection<? extends NameValuePair>) new
	// // BasicNameValuePair("username", "123456"));
	// // // 包装成一个Entry对象
	// // StringEntity entity = new UrlEncodedFormEntity(lvlistList);
	// // // 设置请求的内容
	// // post.setEntity(entity);
	// // 执行请求
	// CloseableHttpResponse response = httpClient.execute(post);
	// if (response.getStatusLine().getStatusCode() == 200) {
	// System.out.println(
	// "状态码：" + response.getStatusLine().getStatusCode() + "\n" +
	// response.getEntity().toString());
	// }
	// // 关闭httpclient
	//
	// }

}
