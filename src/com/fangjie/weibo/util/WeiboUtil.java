package com.fangjie.weibo.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.fangjie.weibo.bean.User;
import com.fangjie.weibo.bean.Weibo;
public class WeiboUtil {

	public List<Weibo> getWeiboList(String token, long i) {
		List<Weibo> weibos=new ArrayList<Weibo>();
		String url="https://api.weibo.com/2/statuses/home_timeline.json";
		String params="access_token="+token+"&max_id="+i;
		String result=HttpRequest(1, url, params);
		try {
			JSONObject json_res=new JSONObject(result);
			JSONArray json_weibos=json_res.getJSONArray("statuses");
			for(int j=0;j<json_weibos.length();j++)
			{
				Weibo weibo=new Weibo();
				JSONObject json_weibo=json_weibos.getJSONObject(j);
				weibo.setComments_count(json_weibo.getInt("comments_count"));
				weibo.setContent(json_weibo.getString("text"));
				weibo.setFrom(json_weibo.getString("source"));
				weibo.setReposts_count(json_weibo.getInt("reposts_count"));
				weibo.setTime(json_weibo.getString("created_at"));
				weibo.setWid(json_weibo.getLong("id"));
				
				User user=new User();
				JSONObject json_user=json_weibo.getJSONObject("user");
				user.setUid(json_user.getLong("id"));
				user.setName(json_user.getString("screen_name"));
				user.setProfile_image_url(json_user.getString("profile_image_url"));
				user.setIsv(json_user.getBoolean("verified"));
				weibo.setUser(user);
				
				if(!json_weibo.isNull("thumbnail_pic"))
					weibo.setBmiddle_pic(json_weibo.getString("thumbnail_pic"));
				else
					weibo.setBmiddle_pic("");
				
				if(!json_weibo.isNull("retweeted_status"))
				{
					Weibo zweibo=new Weibo();
					
					JSONObject json_zweibo=json_weibo.getJSONObject("retweeted_status");
					zweibo.setComments_count(json_zweibo.getInt("comments_count"));
					zweibo.setContent(json_zweibo.getString("text"));
					zweibo.setFrom(json_zweibo.getString("source"));
					zweibo.setReposts_count(json_zweibo.getInt("reposts_count"));
					zweibo.setTime(json_zweibo.getString("created_at"));
					zweibo.setWid(json_zweibo.getLong("id"));
					
					User zuser=new User();
					JSONObject json_zuser=json_zweibo.getJSONObject("user");
					zuser.setUid(json_zuser.getLong("id"));
					zuser.setName(json_zuser.getString("screen_name"));
					zuser.setProfile_image_url(json_zuser.getString("profile_image_url"));
					zuser.setIsv(json_zuser.getBoolean("verified"));
					zweibo.setUser(zuser);
					
					if(!json_zweibo.isNull("thumbnail_pic"))
						zweibo.setBmiddle_pic(json_zweibo.getString("thumbnail_pic"));
					else
						zweibo.setBmiddle_pic("");
					
					weibo.setWeibo(zweibo);
				}
				else
				{
					weibo.setWeibo(null);
				}
				weibos.add(weibo);
				System.out.println(weibo.content);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return weibos;
	}

	public int update(String token, String text) {
		return 0;
	}

	public int guanzhuMe(String token) {
		// TODO Auto-generated method stub
		return 0;
	}

	//HTTP请求   way-0 Post   way-1  Get
	public static String HttpRequest(int way,String url,String params)
	{
		String result = "";
        BufferedReader in = null;
        PrintWriter out = null;
        try {
            String urlNameString = url + "?" + params;
    		System.out.println(urlNameString);
            URL realUrl = new URL(urlNameString);
            URLConnection connection;
            if(way==0)
            {
                // 打开和URL之间的连接
            	connection= realUrl.openConnection();
                // 发送POST请求必须设置如下两行
                connection.setDoOutput(true);
                connection.setDoInput(true);
                // 获取URLConnection对象对应的输出流
                out = new PrintWriter(connection.getOutputStream());
                // 发送请求参数
                out.print(params);
                // flush输出流的缓冲
                out.flush();            	
            }
            else
            {
                // 打开和URL之间的连接
                connection = realUrl.openConnection();
                // 设置通用的请求属性
                connection.setRequestProperty("accept", "*/*");
                connection.setRequestProperty("connection", "Keep-Alive");
                connection.setRequestProperty("user-agent",
                        "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
                // 建立实际的连接
                //connection.connect();
            }
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
	}
}
