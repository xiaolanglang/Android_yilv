package test;

import java.io.File;

import org.junit.Test;

import com.itravel.http.FormFile;
import com.itravel.http.SocketHttpRequester;

public class TestSocket {

	@Test
	public void test() {
		String path = "localhost:8080/yilv/travel";
		String filePath = "C:\\Users\\Administrator\\Desktop\\新建文本文档.txt";
		FormFile formFile = new FormFile("test", new File(filePath), "file", null);
		try {
			SocketHttpRequester.post(path, null, formFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
