package org.apache.rocketmq.example.batch;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.*;

public class FileUtils {
    public static void main(String[] args) throws Exception {
        File file = new File("/Users/lxq/Desktop/服务单元告警等级统计");
        InputStreamReader reader = new InputStreamReader(
                new FileInputStream(file)); // 建立一个输入流对象reader
        BufferedReader br = new BufferedReader(reader); // 建立一个对象，它把文件内容转成计算机能读懂的语言
        String line = "";

        Map<String, NodeTotal> map = new HashMap<>();
        int count = 0;
        while (true) {
            line = br.readLine(); // 一次读入一行数据

            if (StringUtils.isEmpty(line)) {
                break;
            }

            String[] split = line.split(",");

            if (split[0].equals("meituan.hotel.biz.ebooking.prod")) {
                System.out.println("===============");
            }

            if (map.get(split[0]) == null) {
                NodeTotal total = new NodeTotal();
                total.setAppkey(split[0]);
                total.setReportNum(Integer.valueOf(split[1]));
                total.setTotalNum(Integer.valueOf(split[2]));
                total.setSmsNum(Integer.valueOf(split[3]));
                total.setXmNum(Integer.valueOf(split[4]));
                total.setMailNum(Integer.valueOf(split[5]));
                total.setNocNum(Integer.valueOf(split[6]));

                map.put(split[0], total);
            } else {
                NodeTotal total = map.get(split[0]);
                total.setReportNum(total.getReportNum() + Integer.valueOf(split[1]));
                total.setTotalNum(total.getTotalNum() + Integer.valueOf(split[2]));
                total.setSmsNum(total.getSmsNum() + Integer.valueOf(split[3]));
                total.setXmNum(total.getXmNum() + Integer.valueOf(split[4]));
                total.setMailNum(total.getMailNum() + Integer.valueOf(split[5]));
                total.setNocNum(total.getNocNum() + Integer.valueOf(split[6]));
            }

            count++;
            System.out.println(line);
        }
        System.out.println(count);
        System.out.println(JSONObject.toJSON(map));

        ArrayList<NodeTotal> nodeTotals = new ArrayList<>(map.values());
        Collections.sort(nodeTotals, new Comparator() {

            @Override
            public int compare(Object o1, Object o2) {
                NodeTotal nodeTotal1 = (NodeTotal) o1;
                NodeTotal nodeTotal2 = (NodeTotal) o2;

                if (((NodeTotal) o1).getSmsNum() > ((NodeTotal) o2).getSmsNum()) {
                    return -1;
                }

                if (((NodeTotal) o1).getSmsNum() < ((NodeTotal) o2).getSmsNum()) {
                    return 1;
                }

                return 0;
            }
        });

        System.out.println(JSONObject.toJSON(nodeTotals));
    }
}

class NodeTotal {
    private String appkey;

    private int reportNum;

    private int totalNum;

    private int smsNum;

    private int xmNum;

    private int mailNum;

    private int nocNum;

    public String getAppkey() {
        return appkey;
    }

    public void setAppkey(String appkey) {
        this.appkey = appkey;
    }

    public int getReportNum() {
        return reportNum;
    }

    public void setReportNum(int reportNum) {
        this.reportNum = reportNum;
    }

    public int getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }

    public int getSmsNum() {
        return smsNum;
    }

    public void setSmsNum(int smsNum) {
        this.smsNum = smsNum;
    }

    public int getXmNum() {
        return xmNum;
    }

    public void setXmNum(int xmNum) {
        this.xmNum = xmNum;
    }

    public int getMailNum() {
        return mailNum;
    }

    public void setMailNum(int mailNum) {
        this.mailNum = mailNum;
    }

    public int getNocNum() {
        return nocNum;
    }

    public void setNocNum(int nocNum) {
        this.nocNum = nocNum;
    }
}
