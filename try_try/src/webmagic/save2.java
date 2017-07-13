package webmagic;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.elasticsearch.xpack.client.PreBuiltXPackTransportClient;

import webmagic.StringToJSON;

public class save2 extends Thread {
	public save2(TransportClient client) {
		this.client = client;
	}

	public void ess(List<String> data) throws IOException {
		System.out.println("save_start");
		
		IndexResponse response2 = client.prepareIndex("articles_spider", "article", String.valueOf(weixin_new_nerchange.id))
				.setSource(jsonBuilder().startObject().field("content", data.get(2)).field("title", data.get(1))
						.field("url", data.get(4)).field("date", data.get(3)).field("tags", data.get(5))
						.field("official_account", data.get(0)).endObject())
				.get();
		System.out.println("状态" + response2.getResult());
		// shutdown
		// client.close();
	}

	public void run() {
		for(int i=0;i<7;i++)
			try {
				weixin_new_nerchange.id++;
				if (weixin_new_nerchange.all_ner.size() != 0) {
					List<String> data = new ArrayList(weixin_new_nerchange.all_ner.remove(0));
					webmagic.StringToJSON.writeFile(webmagic.StringToJSON.testMapToJSON(data.get(0), data.get(1),
							data.get(2), data.get(3), data.get(4), data.get(5),
							String.valueOf(weixin_new_nerchange.id)));
					ess(data);
				} else
					try {
						Thread.sleep(100);
					} catch (InterruptedException ie) {
					}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
	}

	public static void main(String[] args) throws IOException {

		for (int i = 10; i < 16; i++) {
			List<String> s = new ArrayList<String>();
			for (int iw = 0; iw < 6; iw++)
				s.add("sd");
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");// 设置日期格式
			System.out.println();// new Date()为获取当前系统时间
			s.set(3, df.format(new Date()).toString());
			weixin_new_nerchange.all_ner.add(s);
		}
		System.out.println(weixin_new_nerchange.all_ner);

		Settings settings = Settings.builder().put("cluster.name", "DeepSearch&")
				.put("xpack.security.user", "elastic:YangZC*#03").build();
		TransportClient client = new PreBuiltXPackTransportClient(settings)
				.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("124.127.117.108"), 9300));
		save2 save_new = new save2(client);
		weixin_new_nerchange.id=10;
		save_new.run();
		client.close();
	}
	private TransportClient client;
}
