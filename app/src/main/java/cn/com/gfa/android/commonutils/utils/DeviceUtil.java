package cn.com.gfa.android.commonutils.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.PowerManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Method;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

public class DeviceUtil {
    /**
     * 获得手机名称
     * @return 手机名称
     */
    public static String getMobileName(){
        return  Build.MANUFACTURER+" "+ Build.MODEL;
    }

    /**
     * 获得手机类型
     * @return 手机类型
     */
    public static String getPhoneType(){
        String phoneType=Build.MODEL;
        return phoneType;
    }

    /**
     * 获得系统版本号
     * @return 系统版本号
     */
    public static int getOSVersion(){
        return Build.VERSION.SDK_INT;
    }

    /**
     * 获取手机号，几乎获取不到
     * @param context 上下文
     * @return 手机号，几乎获取不到
     */
    public static String getPhoneNumber(Context context) {
        TelephonyManager mTelephonyMgr = (TelephonyManager) context
                .getApplicationContext().getSystemService(
                        Context.TELEPHONY_SERVICE);
        @SuppressLint("MissingPermission") String phoneNum = mTelephonyMgr.getLine1Number();
        return TextUtils.isEmpty(phoneNum) ? "" : phoneNum;
    }

    /**
     * 获得IMEI号(国际移动设备身份码)
     * @param context 上下文
     * @return IMEI号
     */
    public static String getPhoneIMEI(Context context){
        TelephonyManager mTelephonyMgr = (TelephonyManager) context
                .getApplicationContext().getSystemService(
                        Context.TELEPHONY_SERVICE);
        @SuppressLint("MissingPermission") String phoneImei = mTelephonyMgr.getDeviceId();
        return TextUtils.isEmpty(phoneImei) ? "" : phoneImei;
    }

    /**
     * 获取imsi号(国际移动用户识别码)
     * @param context 上下文
     * @return IMSI号
     */
    public static String getPhoneIMSI(Context context) {
        TelephonyManager mTelephonyMgr = (TelephonyManager) context
                .getApplicationContext().getSystemService(
                        Context.TELEPHONY_SERVICE);
        @SuppressLint("MissingPermission") String phoneImsi = mTelephonyMgr.getSubscriberId();

        return TextUtils.isEmpty(phoneImsi) ? "" : phoneImsi;
    }

    /**
     * 获得ipv4类型的ip地址
     * @return ip地址
     */
    public static String getLocalIPAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()&& inetAddress instanceof Inet4Address) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 获得Mac物理地址
     * @return Mac物理地址
     */
    public static String getLocalMacAddress() {
        String Mac = null;
        try {
            String path = "sys/class/net/wlan0/address";
            if ((new File(path)).exists()) {
                FileInputStream fis = new FileInputStream(path);
                byte[] buffer = new byte[8192];
                int byteCount = fis.read(buffer);
                if (byteCount > 0) {
                    Mac = new String(buffer, 0, byteCount, "utf-8");
                }
                fis.close();
            }

            if (Mac == null || Mac.length() == 0) {
                path = "sys/class/net/eth0/address";
                FileInputStream fis = new FileInputStream(path);
                byte[] buffer_name = new byte[8192];
                int byteCount_name = fis.read(buffer_name);
                if (byteCount_name > 0) {
                    Mac = new String(buffer_name, 0, byteCount_name, "utf-8");
                }
                fis.close();
            }

            if (Mac == null || Mac.length() == 0) {
                return "";
            } else if (Mac.endsWith("\n")) {
                Mac = Mac.substring(0, Mac.length() - 1);
            }
        } catch (Exception io) {
        }

        return TextUtils.isEmpty(Mac) ? "" : Mac;
    }

    /**
     * 获得序列号
     * @return 序列号
     */
    public static String getSerialNum() {
        String serialNum = null;
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("get", String.class, String.class);
            serialNum = (String) (get.invoke(c, "ro.serialno", "unknown"));
        } catch (Exception ignored) {
        }

        return serialNum;
    }


    /**
     * 判断设备是否 root
     *
     * @return the boolean{@code true}: 是<br>{@code false}: 否
     */
    public static boolean isDeviceRooted() {
        String su = "su";
        String[] locations = {"/system/bin/", "/system/xbin/", "/sbin/", "/system/sd/xbin/", "/system/bin/failsafe/",
                "/data/local/xbin/", "/data/local/bin/", "/data/local/"};
        for (String location : locations) {
            if (new File(location + su).exists()) {
                return true;
            }
        }
        return false;
    }



    /**
     * 获取设备 AndroidID
     *
     * @return AndroidID
     */
    @SuppressLint("HardwareIds")
    public static String getAndroidID() {
        return Settings.Secure.getString(ApplicationUtil.getApp().getContentResolver(), Settings.Secure.ANDROID_ID);
    }


    /**
     * 获取设备 MAC 地址
     * <p>需添加权限 {@code <uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>}</p>
     *
     * @return MAC 地址
     */
    @SuppressLint("HardwareIds")
    private static String getMacAddressByWifiInfo() {
        try {
            @SuppressLint("WifiManagerLeak")
            WifiManager wifi = (WifiManager) ApplicationUtil.getApp().getSystemService(Context.WIFI_SERVICE);
            if (wifi != null) {
                WifiInfo info = wifi.getConnectionInfo();
                if (info != null) return info.getMacAddress();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "02:00:00:00:00:00";
    }

    /**
     * 获取设备 MAC 地址
     * <p>需添加权限 {@code <uses-permission android:name="android.permission.INTERNET"/>}</p>
     *
     * @return MAC 地址
     */
    private static String getMacAddressByNetworkInterface() {
        try {
            List<NetworkInterface> nis = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface ni : nis) {
                if (!ni.getName().equalsIgnoreCase("wlan0")) continue;
                byte[] macBytes = ni.getHardwareAddress();
                if (macBytes != null && macBytes.length > 0) {
                    StringBuilder res1 = new StringBuilder();
                    for (byte b : macBytes) {
                        res1.append(String.format("%02x:", b));
                    }
                    return res1.deleteCharAt(res1.length() - 1).toString();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "02:00:00:00:00:00";
    }

    /**
     * 获取设备 MAC 地址
     *
     * @return MAC 地址
     */
    private static String getMacAddressByFile() {
        ShellUtil.CommandResult result = ShellUtil.execCmd("getprop wifi.interface", false);
        if (result.result == 0) {
            String name = result.successMsg;
            if (name != null) {
                result = ShellUtil.execCmd("cat /sys/class/net/" + name + "/address", false);
                if (result.result == 0) {
                    if (result.successMsg != null) {
                        return result.successMsg;
                    }
                }
            }
        }
        return "02:00:00:00:00:00";
    }

    /**
     * 获取设备厂商
     * <p>如 Xiaomi</p>
     *
     * @return 设备厂商
     */

    public static String getManufacturer() {
        return Build.MANUFACTURER;
    }

    /**
     * 获取设备型号
     * <p>如 MI2SC</p>
     *
     * @return 设备型号
     */
    public static String getModel() {
        String model = Build.MODEL;
        if (model != null) {
            model = model.trim().replaceAll("\\s*", "");
        } else {
            model = "";
        }
        return model;
    }

    /**
     * 关机
     * <p>需要 root 权限或者系统权限 {@code <android:sharedUserId="android.uid.system"/>}</p>
     */
    public static void shutdown() {
        ShellUtil.execCmd("reboot -p", true);
        Intent intent = new Intent("android.intent.action.ACTION_REQUEST_SHUTDOWN");
        intent.putExtra("android.intent.extra.KEY_CONFIRM", false);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ApplicationUtil.getApp().startActivity(intent);
    }

    /**
     * 重启
     * <p>需要 root 权限或者系统权限 {@code <android:sharedUserId="android.uid.system"/>}</p>
     */
    public static void reboot() {
        ShellUtil.execCmd("reboot", true);
        Intent intent = new Intent(Intent.ACTION_REBOOT);
        intent.putExtra("nowait", 1);
        intent.putExtra("interval", 1);
        intent.putExtra("window", 0);
        ApplicationUtil.getApp().sendBroadcast(intent);
    }

    /**
     * 重启
     * <p>需系统权限 {@code <android:sharedUserId="android.uid.system"/>}</p>
     *
     * @param reason 传递给内核来请求特殊的引导模式，如"recovery"
     */
    public static void reboot(final String reason) {
        PowerManager mPowerManager = (PowerManager) ApplicationUtil.getApp().getSystemService(Context.POWER_SERVICE);
        try {
            mPowerManager.reboot(reason);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 重启到 recovery
     * <p>需要 root 权限</p>
     */
    public static void reboot2Recovery() {
        ShellUtil.execCmd("reboot recovery", true);
    }

    /**
     * 重启到 bootloader
     * <p>需要 root 权限</p>
     */
    public static void reboot2Bootloader() {
        ShellUtil.execCmd("reboot bootloader", true);
    }
}
