package com.zeno.pojo;

import java.util.List;

/**
 * @program: wx-official-account
 * @description:
 * @author: Zeno
 * @create: 2019-12-24 10:54
 **/
public class NewsMessage extends BaseMessage{
    private int ArticleCount;
    private List<News> Articles;
    public int getArticleCount() {
        return ArticleCount;
    }
    public void setArticleCount(int articleCount) {
        ArticleCount = articleCount;
    }
    public List<News> getArticles() {
        return Articles;
    }
    public void setArticles(List<News> articles) {
        Articles = articles;
    }
}