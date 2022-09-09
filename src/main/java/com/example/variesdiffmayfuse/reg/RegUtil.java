package com.example.variesdiffmayfuse.reg;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author liuyuan
 * @createdAt: 2022/8/31 9:18
 * @since: 1.0
 * @describe
 */
public class RegUtil {


 public static void main(String[] args) {
//  Pattern compile = Pattern.compile("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$");
//  String str = "/BOSS/BOSS云化/BOSS云化灰度/10.243.29.115(主机名：B02)，NTP服务未同步，2022-08-26 22:01:54。";
//  String ymdhms = "[0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2}";//yyyy-MM-dd HH:mm:ss
//  String yymmdd = "[0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2}";
  Pattern percentParten = Pattern.compile("(\\([0-9]\\d*/[0-9]\\d*\\))");
//  Matcher matcher = compile.matcher(str);
//  String s = matcher.replaceAll(" ");
//  System.out.println(s);
//  Pattern compile1 = Pattern.compile(ymdhms);
//  Matcher matcher1 = compile1.matcher(str);
//  if (matcher1.matches()) {
//   String s1 = matcher1.replaceAll(" ");
//   System.out.println(s1);
//  }

  Matcher group = percentParten.matcher("(22/22)ahdfdjkalf") ;
//  System.out.println(group.find());
//  System.out.println(group.group());
//  System.out.println(count(percentParten,"(22/22)ahdfdjkalf(222/222)"));
  String ss = "2022-08-25 00:00:00|^15156881980|^严重告警,/AIOps场景/管信:新模式异常告警:2022-08-25 08:33:50_2022-08-25 08:33:50检测到<time>\tINFO\tcom.mochasoft.todo.srv.PushTodoService.doPost\t[ NUM - NUM - NUM NUM NUM NUM NUM CST] <*> SystemOut O NUM - NUM - NUM NUM NUM NUM [INFO]-[com.mochasoft.todo.srv.PushTodoService.doPost(PushTodoService.java NUM )] todo TodoEntity [appActivityId NUM appActivityName <*> appItemId PMS- NUM - NUM appProcessId NUM collectAppId extend1 null extend2 null extend3 null extend4 null extend5 null insertTime NUM - NUM - NUM NUM NUM NUM . NUM isCollectFlag NUM itemAppId PMS@HQ.CMCC itemBatchId <*> <*> <*> <*> <*> <*> NUM <*> NUM <*> <*> NUM <*> NUM <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> NUM <*> NUM <*> NUM <*> NUM <*> NUM <*> NUM <*> NUM <*> NUM <*> NUM <*> NUM <*> NUM <*> NUM <*> NUM <*> NUM <*> NUM <*> NUM <*> NUM <*> NUM <*> NUM <*> NUM <*> NUM <*> NUM <*> NUM <*> NUM <*> NUM <*> NUM <*> NUM <*> NUM &parentProcessInstID NUM &appID PMS@HQ.CMCC&uniqueId <*> http%3A%2F%2F172. NUM . NUM . NUM %2Ftodo-web%2Ftodo%2FflushState&itemId PMS- NUM - NUM lastActivityName <*> lastUid <*> lastUidType NUM lastUpdateTime null name 涓？？界Щ？ㄩ？？涓？？？璁″？？寤鸿？绠＄？？绯荤？ processCreateTime NUM - NUM - NUM NUM NUM NUM . NUM processTypeID NUM processTypeName <*> receiverUid NUM @ah.cmcc receiverUidType NUM region null senderUid NUM @ah.cmcc senderUidType NUM tenementId HQ urgentLevel NUM ]1条[BOMC][创建时间：2022-08-25 09:01:34]|^1008601777";
  String[] split = split(ss);
  Arrays.stream(split).forEach(System.out::println);
//  String str = "(1/2)";
//  : sed ‘s/旧字串/新字串/g filename
//  删除文档中不是英文字母的(数字和特殊符号): sed 's/[^a-z]//g' 1.txt
//  -r, --regexp-extended  在脚本中使用扩展正则表达式； 加r选项的意思为后面不用再加脱意\
  //sed -i -r  's/[0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2}//g' BcmpAlarmData20220824153920.txt  用扩展正则将文本中的日期yyyy-mm-dd hh:mm:ss 全部删除
 }

 public static int count(Pattern pattern, CharSequence content) {
  if (null != pattern && null != content) {
   int count = 0;

   for(Matcher matcher = pattern.matcher(content); matcher.find(); ++count) {
   }

   return count;
  } else {
   return 0;
  }
 }

 private static String[] split(String s) {
  return  s.split("\\|\\^");

 }
}
