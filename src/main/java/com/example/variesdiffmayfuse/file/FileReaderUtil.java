package com.example.variesdiffmayfuse.file;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

import java.io.File;
import java.io.IOException;

/**
 * @author liuyuan
 * @createdAt: 2022/9/2 19:32
 * @since: 1.0
 * @describe
 */
public class FileReaderUtil {

 public static void main(String[] args) throws IOException {
  LineIterator lineIterator = FileUtils.lineIterator(new File("d:\\opt2\\real\\10658508.txt"));
  while (lineIterator.hasNext()) {
   String line = lineIterator.nextLine();
   System.out.println(line);
   //do other thing

  }

 }
}
