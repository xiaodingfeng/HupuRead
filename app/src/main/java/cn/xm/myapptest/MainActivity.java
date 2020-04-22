package cn.xm.myapptest;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<News> newsList;
    private NewsAdapter adapter;
    private Handler handler;
    private ListView lv;
    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        WebView wv_produce = (WebView) findViewById(R.id.webview);
//        //这里的文件路径是死定的，把html文件名改掉就可以了
//        wv_produce.loadUrl("https://www.cnblogs.com/xiaobaibuai/articles/12698746.html");
        newsList = new ArrayList<>();
        lv = (ListView) findViewById(R.id.news_lv);
        getNews();
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                TextView ed=(TextView) findViewById(R.id.textView);
                if(msg.what == 1){
                    ed.setText("加载成功！");
                    ed.setVisibility(View.GONE);
                    adapter = new NewsAdapter(MainActivity.this,newsList);
                    lv.setAdapter(adapter);
                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            News news = newsList.get(position);
                            Intent intent = new Intent(MainActivity.this, NewsDisplayActivity.class);
                            intent.putExtra("news_url",news.getNewsUrl());
                            startActivity(intent);
                        }
                    });
                }
                else {
                    ed.setText("加载中！");
                }
            }
        };
//        VideoView videoView = (VideoView) findViewById(R.id.videoView);
//
//        //加载指定的视频文件
//        String path = "http://www.nangua5.com/play/29488-1-1.html";
//        videoView.setVideoPath(path);
//
//        //创建MediaController对象
//        MediaController mediaController = new MediaController(this);
//
//        //VideoView与MediaController建立关联
//        videoView.setMediaController(mediaController);
//
//        //让VideoView获取焦点
//        videoView.requestFocus();

    }

    private void getNews(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    TextView ed=(TextView) findViewById(R.id.textView);
                    ed.setText("加载中！");
                    //获取虎扑新闻5页的数据，网址格式为：https://voice.hupu.com/nba/第几页
                    for(int i = 1;i<=5;i++) {
                        Document doc = Jsoup.connect("https://voice.hupu.com/nba/" + Integer.toString(i)).get();
                        Elements titleLinks = doc.select("div.list-hd");    //解析来获取每条新闻的标题与链接地址
//                        Elements descLinks = doc.select("div.list-content");//解析来获取每条新闻的简介
                        Elements timeLinks = doc.select("div.otherInfo");   //解析来获取每条新闻的时间与来源
                        //for循环遍历获取到每条新闻的四个数据并封装到News实体类中
                        for(int j = 0;j < titleLinks.size();j++){
                            try {
                                String title = titleLinks.get(j).select("a").text();
                                String uri = titleLinks.get(j).select("a").attr("href");
//                                String desc = descLinks.get(j).select("span").text();
                                String time = timeLinks.get(j).select("span.other-left").select("a").text();
                                News news = new News(title, uri, null, time);
//                            Log.i("mytag",title);
                                newsList.add(news);
                            }
                            catch (Exception e){
                                throw e;
                            }
                        }
                    }
                    Message msg = new Message();
                    msg.what = 1;
                    handler.sendMessage(msg);

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        new MenuInflater(this).inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("WrongConstant")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.contact_us:
//                Toast.makeText(this, "点击了亮度", 1).show();
                startActivity(new Intent(this, about.class));
                break;
            case R.id.abouts:
                Toast.makeText(this, "是一个废物产品！", 1).show();
                break;
        }
        return true;
    }
}
