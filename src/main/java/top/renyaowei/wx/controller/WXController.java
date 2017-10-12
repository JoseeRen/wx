package top.renyaowei.wx.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import top.renyaowei.wx.entity.WxData;
import top.renyaowei.wx.utils.Constant;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * Created by Mr_Shadow on 2017/9/16.
 */
@Controller
@RequestMapping("/wx")
public class WXController {
    @RequestMapping("/")
    @ResponseBody
    public String tokenCheck(WxData data, HttpServletRequest request) {
        try {
            request.setCharacterEncoding("UTF-8");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        if (data.getEchostr() == null || data.getEchostr().length() <= 0) {
            return "error   nullpoint  >>>>>>>";
        }
        String[] arr = {Constant.token, data.getTimestamp(), data.getNonce()};

        Arrays.sort(arr);
        StringBuilder content = new StringBuilder();


        for (int i = 0; i < arr.length; i++) {
            content.append(arr[i]);


        }
        MessageDigest md = null;
        String tmpStr = null;
        try {
            md = MessageDigest.getInstance("SHA-1");
            //将三个参数字符串拼接成一个字符串进行sha1加密
            byte[] digest = md.digest(content.toString().getBytes());
            tmpStr = byteToStr(digest);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        content = null;
        if (tmpStr != null && tmpStr.equals(data.getSignature().toUpperCase())) {
            System.out.println("check ok >>>>>");

            return data.getEchostr();
        } else {
            System.out.println("check failure");

            return "";
        }
    }

    /**
     * 将字节数组转换为十六进制字符串
     *
     * @param byteArray
     * @return
     */
    private static String byteToStr(byte[] byteArray) {
        String strDigest = "";
        for (int i = 0; i < byteArray.length; i++) {
            strDigest += byteToHexStr(byteArray[i]);
        }
        return strDigest;
    }

    /**
     * 将字节转换为十六进制字符串
     *
     * @param mByte
     * @return
     */
    private static String byteToHexStr(byte mByte) {
        char[] Digit = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A',
                'B', 'C', 'D', 'E', 'F'};
        char[] tempArr = new char[2];
        tempArr[0] = Digit[(mByte >>> 4) & 0X0F];
        tempArr[1] = Digit[mByte & 0X0F];

        String s = new String(tempArr);
        return s;
    }
}
