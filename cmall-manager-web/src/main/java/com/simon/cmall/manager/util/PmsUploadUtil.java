package com.simon.cmall.manager.util;


import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.springframework.web.multipart.MultipartFile;


public class PmsUploadUtil {


    public static String uploadImage(MultipartFile multipartFile) {
        String imgUrl = "http://192.168.146.11";
        //    上传图片到服务器
        //    配置fdfs的全局链接地址

        // 上传图片到服务器
        // 配置fdfs的全局链接地址
        String tracker = PmsUploadUtil.class.getResource("/tracker.conf").getPath();// 获得配置文件的路径
        try {
            ClientGlobal.init(tracker);
        } catch (Exception e) {
            e.printStackTrace();
        }
        TrackerClient trackerClient = new TrackerClient();
//        获得一个trackerServer的实例
        TrackerServer trackerServer = null;
        try {
            trackerServer = trackerClient.getTrackerServer();
        }catch (Exception e){
            e.printStackTrace();
        }
        // 通过tracker获得一个Storage链接客户端
        StorageClient storageClient = new StorageClient(trackerServer,null);

        try{
//            获取二进制文件
            byte[] bytes = multipartFile.getBytes();
//            获得后缀名
            String originalFilename = multipartFile.getOriginalFilename();
            int i = originalFilename.lastIndexOf(".");
            String extName = originalFilename.substring(i+1);
//            上传
            String[] uploadInfos = storageClient.upload_file(bytes, extName, null);
//            凑合地址
            for (String uploadInfo : uploadInfos) {
                imgUrl += "/"+uploadInfo;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return imgUrl;
    }


}
