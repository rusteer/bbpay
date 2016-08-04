package com.bbpay.admin.service.build;
import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;

public class BuildService {
    private static final boolean isWin = System.getProperty("os.name").toLowerCase().contains("win");
    private static final String driverPrefix = isWin ? "E:" : "";
    private static final String projectRoot = driverPrefix + "/workspace/bbpay/code/bbpay-android";
    private static final String sdkPrefix = "bbpay";
    public static void main(String args[]) {
        //start("aaaa","com.yousdk.pay", "yousdk");
        start("hysdk.haowagame.com","com.hypay.android", "hypay");
        //start("bbpay.yousdk.com","com.flyplay.pp", "flyplay");
        //start("app.y9688.com","com.bbgames.util", "bbg");
    }
    public static void start(String hostName, String newPackageName, String sdkShortName) {
        try {
            File newProjectRoot = new File(driverPrefix + "/tmp/bbpay-android-build/" + sdkShortName);
            FileUtils.deleteDirectory(newProjectRoot);
            FileUtils.copyDirectory(new File(projectRoot), newProjectRoot);
            File settingFile = new File(newProjectRoot + "/src/com/bbpay/android/setting/Setting.java");
            FileUtils.write(settingFile, FileUtils.readFileToString(settingFile).replace("localhost", hostName));
            System.out.println(newProjectRoot);
            File projectConfigFile = new File(newProjectRoot + "/.project");
            System.out.println(projectConfigFile);
            FileUtils.write(projectConfigFile, FileUtils.readFileToString(projectConfigFile).replace("bbpay-android", "bbpay-android-build-" + sdkShortName));
            String newProejctPath = newProjectRoot.getAbsolutePath();
            FileUtils.moveDirectory(new File(newProejctPath + "/src/com/export"), new File(newProejctPath + "/src/" + newPackageName.replace('.', '/')));
            replaceKeyworks(newProejctPath, newPackageName, sdkShortName);
            replacePrefix(newProejctPath, newPackageName, sdkShortName);
            ConstantReplacer.replace(newProejctPath);
            generateApk(newProejctPath);
            generateSdk(newProejctPath, sdkShortName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static void replacePrefix(String root, String newPackageName, String newSdkPrefix) throws IOException {
        File file = new File(root);
        if (file.isDirectory()) {
            for (File child : file.listFiles()) {
                replacePrefix(child.getAbsolutePath(), newPackageName, newSdkPrefix);
            }
        } else {
            String name = file.getName();
            String parent = file.getParent();
            if (name.startsWith(sdkPrefix)) {
                String newPath = parent + "/" + name.replace(sdkPrefix, newSdkPrefix);
                try {
                    FileUtils.moveFile(file, new File(newPath));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private static void replaceKeyworks(String root, String newPackageName, String newSdkPrefix) throws IOException {
        File file = new File(root);
        if (file.isDirectory()) {
            for (File child : file.listFiles()) {
                replaceKeyworks(child.getAbsolutePath(), newPackageName, newSdkPrefix);
            }
        } else {
            String name = file.getName();
            if (name.endsWith(".java") || name.endsWith(".xml") || name.endsWith(".txt")) {
                String content = FileUtils.readFileToString(file)//
                        .replaceAll("com.export", newPackageName)//
                        //.replaceAll(sdkPackageName, newPackageName)//
                        .replace(sdkPrefix + "_", newSdkPrefix + "_");
                FileUtils.write(file, content);
            }
        }
    }
    private static String generateApk(String projectPath) throws IOException {
        if (!isWin) {
            String localPropertiesFile = projectPath + "/local.properties";
            FileUtils.write(new File(localPropertiesFile), "sdk.dir=/usr/local/android/sdk");
            CmdUtil.exeCmd("sh " + projectPath + "/build.sh");
        } else {
            CmdUtil.exeCmd(projectPath + "/build.bat");
            CmdUtil.exeCmd(projectPath + "/extract.bat");
            CmdUtil.exeCmd(projectPath + "/d2j.bat");
        }
        return null;
    }
    private static String generateSdk(String projectPath, String newSdkPrefix) throws  Exception {
        String folder = driverPrefix + "/workspace/bbpay/code/sdk-release/" + newSdkPrefix;
        FileUtils.copyFile(new File(projectPath + "/bin/classes-dex2jar.jar"), new File(folder + "/libs/" + newSdkPrefix + ".jar"));
        FileUtils.copyDirectory(new File(projectPath + "/res"), new File(folder + "/res"));
        FileUtils.copyDirectory(new File(projectPath + "/sdk-output"), new File(folder));
        System.out.println(new File(folder).getCanonicalPath());
        return null;
    }
}
