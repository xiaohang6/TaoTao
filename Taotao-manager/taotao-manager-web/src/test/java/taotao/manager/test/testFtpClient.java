package taotao.manager.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.SocketException;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.junit.Test;

import com.taotao.utils.FtpUtil;

public class testFtpClient {

	@Test
	public void test() throws SocketException, IOException {
		// 创建一个FtpClient对象
		FTPClient ftpClient = new FTPClient();
		// 创建ftp连接。默认是21端口
		ftpClient.connect("192.168.244.128", 21);
		// 登录ftp服务器，使用用户名和密码(注意用的是ftpuser的用户名和密码)
		ftpClient.login("ftpuser", "191037**");
		// 上传文件。
		// 读取本地文件
		FileInputStream inputStream = new FileInputStream(
				new File("C:\\Users\\12742\\Desktop\\简历工作区\\xh证件照\\照片03.jpg"));
		// 设置上传的路径
		ftpClient.changeWorkingDirectory("/home/ftpuser/www/images");
		// 修改上传文件的格式
		ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
		// 第一个参数：服务器端文档名
		// 第二个参数：上传文档的inputStream
		ftpClient.storeFile("xh03.jpg", inputStream);
		// 关闭连接
		ftpClient.logout();
	}

	@Test
	public void testFtpUtil() throws Exception {
		FileInputStream inputStream = new FileInputStream(
				new File("C:\\Users\\12742\\Desktop\\简历工作区\\xh证件照\\照片03.jpg"));
		FtpUtil.uploadFile("192.168.244.128", 21, "ftpuser", "191037**", "/home/ftpuser/www/images", "/2019/06/30",
				"xh03.jpg", inputStream);

	}
}
