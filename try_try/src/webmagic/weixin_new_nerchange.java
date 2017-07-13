package webmagic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.elasticsearch.xpack.client.PreBuiltXPackTransportClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import cn.edu.fudan.flow.NamedIdentityRecognizerStart;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.JsonFilePipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.scheduler.RedisScheduler;

public class weixin_new_nerchange implements PageProcessor {

	private Site site = Site.me().setRetryTimes(3).setSleepTime(100);

	@Override
	public void process(Page page) {
		System.out.println("get-start");
		bug++;
		// url筛选
		//page.addTargetRequests(page.getHtml().links().all());
		//System.out.println(page.getHtml().links().all());

		// 文本内容提取----1
		String officion_content = page.getHtml().xpath("//*[@id='img-content']/div[1]/span/text()").toString();
		String content = gettxt(page);
		String title = page.getHtml().xpath("//title/text()").toString();
		if ((title== null && officion_content == null)||content==""||content==null) {
			// skip this page
			System.out.println("yunxin" + page.getResultItems().get("title") + officion_content);
			page.setSkip(true);
		} else {
			// 文本内容提取---2
			
			
			String time = page.getHtml().xpath("//*[@id='post-date']/text()").toString();
			// String Ner = change(Ner_content(content));
			String url = page.getUrl().toString();
			// 放入list中,
			List<String> data = new ArrayList<String>();
			data.add(officion_content);
			data.add(title);
			data.add(content);
			data.add(time);
			data.add(url);
			all.add(data);

			// 保存json文件
			// page.putField("title", title);
			// page.putField("officion_content", officion_content);
			// page.putField("content", content);
			// page.putField("time", time);
			// page.putField("ner", Ner);
			// page.putField("url", url);

		}
	}

	public static String change(String[] Ner) {
		if (Ner.length > 0) {
			String ner = Ner[0];
			for (int i = 1; i < Ner.length; i++)
				ner = Ner[i] + ',' + ner;
			return ner;
		} else
			return "";
	}

	// 存储数据，es还没设计
	// public void save(String title, String officion_content, String content,
	// String time, String Ner, String url) {
	// System.out.println("time" + time);
	// System.out.println("title" + title);
	// System.out.println("officion_content" + officion_content);
	// System.out.println("content" + content);
	// System.out.println("url" + url);
	// System.out.println("ner" + Ner);
	// }

	public static String[] Ner_content(String content) {
		if(content==""||content==null){
			String[] Ner = {};
			return Ner;
		}
			else
		{
		System.out.println("content:" + content);
		// NamedIdentityRecognizerStart ner = new
		// NamedIdentityRecognizerStart();
		String[] Ner = ner.ner(content);
		return Ner;
		}
		
	}

	public String gettxt(Page page) {
		String txt = "";
		Document doc = Jsoup.parse(page.getHtml().toString());
		Elements p = doc.select("div.rich_media_content ").select("p");
		for (Element text : p)
			txt = txt + text.text();
		System.out.println(txt);
		return txt;
	}

	@Override
	public Site getSite() {
		return site;
	}

	public static String[] read(String path) {
		String[] s = new String[file_try.file_lines(path)];
		String url = "http://mp.weixin.qq.com/s?__biz=";
		// 假设内容小于十万条
		try {
			String encoding = "UTF-8";
			File file = new File(path);
			if (file.isFile() && file.exists()) { // 判断文件是否存在
				InputStreamReader read = new InputStreamReader(new FileInputStream(file), encoding);// 考虑到编码格式
				BufferedReader bufferedReader = new BufferedReader(read);
				String lineTxt = null;
				int n = 0;
				while ((lineTxt = bufferedReader.readLine()) != null) {
					String[] texts = lineTxt.split("\t");
					s[n] = url + texts[0];
					n++;
				}
				read.close();
			} else {
				System.out.println("找不到指定的文件");
			}
		} catch (Exception e) {
			System.out.println("读取文件内容出错");
			e.printStackTrace();
		}
		return s;
	}

	static NamedIdentityRecognizerStart ner = new NamedIdentityRecognizerStart();
	// 有静态值
	static List<List> all = Collections.synchronizedList(new ArrayList<List>());
	static List<List> all_ner = Collections.synchronizedList(new ArrayList<List>());
	static int id = 0;
	static int bug = 0;
	// json数据文件

	// es
	static TransportClient client = null;

	public static void main(String[] args) throws ParseException, IOException {
		// urls文件
		String[] file = file_try.getchild("/home/wsxcde1/code/urls");
		String[] s = {};
		for (String lines : file)
			s = file_try.getMergeArray(s, weixin_new_nerchange.read(lines));
		for (String ss : s)
			System.out.println(ss);

		// json数据文件

		// es链接
		Settings settings = Settings.builder().put("cluster.name", "DeepSearch&")
				.put("xpack.security.user", "elastic:YangZC*#03").build();
		client = new PreBuiltXPackTransportClient(settings)
				.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("124.127.117.108"), 9300));
		System.out.println("bug" + bug);
		// ner数据处理线程15
		if (bug == 0) {
			for (int i = 0; i < 7; i++) {
				ner h = new ner();
				h.start();
			}
		}
		System.out.println("bug:" + bug);
		// 数据保存
		if (bug == 0) {
			System.out.println("save");
			for (int i = 0; i < 4; i++) {
				save h2 = new save(client);
				h2.start();
			}

		}

		// 下载，提取线程
		Spider.create(new weixin_new_nerchange())
				.addUrl("http://mp.weixin.qq.com/s?__biz="
						+ "NzcwMjEwNDgx&idx=1&mid=2650632143&sn=64dbe8d00ae5a7c2d9dcff3767ec8128")
				.addUrl(s).addPipeline(new JsonFilePipeline("/home/wsxcde1/webmagic"))
				.setScheduler(new RedisScheduler("172.20.208.212")).run();
		System.out.println("start");
	}
}
