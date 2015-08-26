package com.meitu.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class SaxHandler extends DefaultHandler {

  private HashMap<String , String> map = null;
  private List<HashMap<String , String>> list = null;
  private String currentTag = null;
  private String currentValue = null;
  private String nodeName = null;
  
  public SaxHandler(String nodeName) {
    this.nodeName = nodeName;
  }
  
  public List<HashMap<String, String>> getList() {
    return list;
  }

  public void setList(List<HashMap<String, String>> list) {
    this.list = list;
  }

  @Override
  public void startDocument() throws SAXException {
    list = new ArrayList<HashMap<String,String>>();
  }
  
  @Override
  public void startElement(String uri, String localName, String qName,
      Attributes attributes) throws SAXException {
    if (qName.equals(nodeName)) {
      map = new HashMap<String, String>();
    }
    if (attributes != null && map != null) {
      for (int i = 0; i < attributes.getLength(); i++) {
        map.put(attributes.getQName(i), attributes.getValue(i));
      }
    }
    currentTag = qName;
  }
  
  @Override
  public void characters(char[] ch, int start, int length) throws SAXException {
    //用于处理XML文件所读取到的内容.
    if (currentTag != null && map != null) {
      currentValue = new String(ch, start, length);
      if (currentValue != null && !currentValue.trim().equals("") && !currentValue.trim().equals("")) {
        map.put(currentTag , currentValue);
      }
    }
    currentTag = null;
    currentValue = null;
  }
  
  @Override
  public void endElement(String uri, String localName, String qName)
      throws SAXException {
    if (qName.equals(nodeName)) {
      list.add(map);
      map = null;
    }
    super.endElement(uri, localName, qName);
  }
}
