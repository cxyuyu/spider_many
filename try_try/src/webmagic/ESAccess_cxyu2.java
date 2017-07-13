package webmagic; /**
						* Created by hujie on 14/02/2017.
						*/

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
//import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.elasticsearch.xpack.client.PreBuiltXPackTransportClient;

import static org.elasticsearch.common.xcontent.XContentFactory.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

public class ESAccess_cxyu2 {

	public static void main(String[] args) throws Exception {

		Settings settings = Settings.builder().put("cluster.name", "DeepSearch&")
				.put("xpack.security.user", "elastic:YangZC*#03").build();
		TransportClient client=new PreBuiltXPackTransportClient(settings).addTransportAddress(
				new InetSocketTransportAddress(InetAddress.getByName("124.127.117.108"), 9300));;

		
		SearchResponse sr = client.prepareSearch("articles").setSize(60).get();
		Iterator it = sr.getHits().iterator();
		while (it.hasNext()) {
			SearchHit sh = (SearchHit) it.next();
			System.out.println(sh.getScore());
		}
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");// 设置日期格式
		System.out.println(df.format(new Date()));// new Date()为获取当前系统时间
		
		IndexResponse response = client.prepareIndex("articles_spider", "article")
				.setSource(jsonBuilder().startObject().field("content", "kimc123hy").field("title", "asd")
						.field("url", "cxyu_asd_yes").field("date", df.format(new Date()))
						.field("tags", "kimc123123hy").field("official_account", "kimchy").endObject())
				.get();
		System.out.println(response.status());
		client.close();

	}
}
