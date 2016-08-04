package com.bbpay.server.service.biz.comic;
public class Comic {
    public static String getDynamicCmd(String staticCmd, String key) {
        String dynamicCmd = "";
        String versionCode = "3";
        //String key = "ABCJHZQ1Y3K4P5627NKFZ9TCACSFERH95M419831001DGH7R";
        try {
            //step1:
            String staticCmd1 = staticCmd.substring(0, 6);
            String staticCmd2 = staticCmd.substring(6, 12);
            String staticCmd3 = staticCmd.substring(12);
            //step2:
            String random1 = getRandomChar(2);
            String random2 = getRandomChar(2);
            String random3 = getRandomChar(2);
            String random4 = getRandomChar(5);
            //step3:
            String feecode = random1 + "#" + staticCmd1 + "#" + random2 + "#" + staticCmd2 + "#" + random3 + "#" + staticCmd3 + "#" + random4;
            //step4:
            ThreeDes des3 = new ThreeDes(key);
            feecode = des3.getEncString(feecode);
            //step5:
            //String base64Str = BASE64.encode(feecode.getBytes());
            //step6:
            dynamicCmd = versionCode + "#" + feecode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dynamicCmd;
    }
    private static String getRandomChar(int len) {
        char[] chr = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u',
                'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };
        String rnd = "";
        for (int i = 0; i < len; i++) {
            int r = (int) (Math.random() * chr.length);
            rnd = rnd + chr[r];
        }
        return rnd;
    }
    
    public static void main(String[] args){
        String staticCmd="MzCjSdNVI0ZQIKfjMdQ=";
        String key="ABCJHZQ1Y3K4P5627NKFZ9TCACSFERH95M419831001DGH7R";
        System.out.println(getDynamicCmd(staticCmd,key));
    }
}
