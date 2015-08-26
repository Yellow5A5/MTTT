package com.meitu.util;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class SaxTool {

  public SaxTool() {
    // TODO Auto-generated constructor stub
  }

  public static List<HashMap<String, String>> readXML(
      InputStream inputStream, String nodeName) {
    try {
      // 创建一个解析xml的工厂对象
      SAXParserFactory spf = SAXParserFactory.newInstance();
      SAXParser parser = spf.newSAXParser();// 解析xml
      SaxHandler handler = new SaxHandler(nodeName);
      parser.parse(inputStream, handler);
      inputStream.close();
      return handler.getList();
    } catch (Exception e) {
      // TODO: handle exception
    }
    return null;
  }
}
