package com.jixiang.chat.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * UBB编码格式转换成HTML格式
 * 
 * @author Wangda
 */
public class UBB {
	/**
	 * 转换UBB
	 * 
	 * @param text
	 * @return
	 */
	public static String UbbDecode(String text) {
		text = replace(text, "tr(.*?)", "tr", "<tr>", "</tr>");

		text = replace(text, "td(.*?)", "td", "<td>", "</td>");

		text = replace(text, "table(.*?)", "table", "<table>", "</table>");

		text = replace(text, "align=(.*?)", "align", "<p align=$2>", "</align>");

		if (text.contains("[url=")) {
			text = replace(text, "url=(.*?)", "url", "<a href='$2'>", "</a>");
		} else if(text.contains("[url]")){
			String content = text.substring(text.indexOf("[url]") + 5, text.indexOf("[/url]"));
			text = replace(text, "url", "<a href='" + content + "'>", "</a>");
		}

		text = replace(text, "img", "img", "<img border=0 src=", "></img>");

		text = replace(text, "img=(.+?),(.+?)", "img", "<img width=\"$2\" hight=\"$3\" border=0 src=", "></img>");

		text = replace(text, "size=(.+?)", "size", "<font size=$2>", "</font>");

		text = replace(text, "font=(.+?)", "font", "<font face=$2>", "</font>");

		text = replace(text, "color=(.+?)", "color", "<font color=$2>", "</font>");

		text = replace(text, "email=(.+?)", "email", "<a href='mailto:$2'>", "</a>");

		text = replace(text, "u", "<u>", "</u>");
		text = replace(text, "i", "<i>", "</i>");
		text = replace(text, "li", "<li>", "</li>");
		text = replace(text, "list=(.*?)", "<ol>", "</ol>");
		text = replace(text, "list(.*?)", "<ul>", "</ul>");
		text = replace(text, "\\*", "<li>", "</li>");
		text = replace(text, "ol", "<ol>", "</ol>");
		text = replace(text, "ul", "<ul>", "</ul>");
		text = replace(text, "b", "<b>", "</b>");
		text = replace(text, "h1", "<h1>", "</h1>");
		text = replace(text, "h2", "<h2>", "</h2>");
		text = replace(text, "h3", "<h3>", "</h3>");
		text = replace(text, "h4", "<h4>", "</h4>");
		text = replace(text, "h5", "<h5>", "</h5>");
		text = replace(text, "h6", "<h6>", "</h6>");
		text = replace(text, "sub", "<sub>", "</sub>");
		text = replace(text, "sup", "<sup>", "</sup>");

		return text;
	}

	public static String replace(String text, String reg, String replaceStr1, String replaceStr2) {
		return replace(text, reg, reg, replaceStr1, replaceStr2);
	}

	public static String replace(String text, String reg, String regEnd, String replaceStr1, String replaceStr2) {
		Matcher m = null;

		m = Pattern.compile("(\\[" + reg + "\\])", Pattern.DOTALL | Pattern.CASE_INSENSITIVE | Pattern.MULTILINE)
				.matcher(text);
		text = m.replaceAll(replaceStr1);

		m = Pattern.compile("(\\[/" + regEnd + "\\])", Pattern.DOTALL | Pattern.CASE_INSENSITIVE | Pattern.MULTILINE)
				.matcher(text);
		text = m.replaceAll(replaceStr2);

		return text;
	}
}
