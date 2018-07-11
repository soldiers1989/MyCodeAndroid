package com.dm.utils;

/**
 * 富文本Html处理
 * Created by jiaohongyun on 2016/4/13.
 */
public class HtmlUtil {
    private StringBuilder head;
    private StringBuilder css;
    private StringBuilder javascript;
    private StringBuilder body;

    public HtmlUtil() {
        head = new StringBuilder();
        css = new StringBuilder();
        javascript = new StringBuilder();
        body = new StringBuilder();
    }
    /**
     * 默认的初始化
     * @return
     */
    public HtmlUtil init(){
        head.append("<meta charset='UTF-8'>");
        head.append("<meta name='viewport' content='width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=no,minimal-ui'>");
        head.append("<link rel='stylesheet' href='file:///android_asset/css/common.css' type='text/css'/>");
        return this;
    }

    /**
     * 默认的初始化
     * @param baseUrl
     * @return
     */
    public HtmlUtil init(String baseUrl){
        init();
        head.append("<base href='");
        head.append(baseUrl);
        head.append("'>");
        return this;
    }

    /**
     * 创建Html字符串
     * @return
     */
    public String CreateHtml() {
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html><html><head>");
        html.append(head);
        html.append("<style>");
        html.append(css);
        html.append("</style><script>");
        html.append(javascript);
        html.append("</script>");
        html.append("<body>");
        html.append(body);
        html.append("</body>");
        html.append("</html>");
        return html.toString();
    }

    public StringBuilder getHead() {
        return head;
    }

    public void setHead(StringBuilder head) {
        this.head = head;
    }

    public StringBuilder getCss() {
        return css;
    }

    public void setCss(StringBuilder css) {
        this.css = css;
    }

    public StringBuilder getJavascript() {
        return javascript;
    }

    public void setJavascript(StringBuilder javascript) {
        this.javascript = javascript;
    }

    public StringBuilder getBody() {
        return body;
    }

    public void setBody(StringBuilder body) {
        this.body = body;
    }
}
