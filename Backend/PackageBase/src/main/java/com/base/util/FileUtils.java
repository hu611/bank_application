package com.base.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {
    public static boolean create_folder(String folder_loc) {
        // Replace with your desired folder path
        File folder = new File(folder_loc);
        System.out.println(folder_loc);
        if (!folder.exists()) {
            boolean success = folder.mkdirs();
            System.out.println("create folder: " + success);
            return success;
        } else {
            return true;
        }
    }

    public static void inputstream_to_outputstream(int bufferSize, InputStream inputStream, OutputStream outputStream) throws Exception{
        byte[] buffer = new byte[bufferSize];
        int len = 0;
        while((len = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer,0,len);
        }
    }

    public static void save_image(String location, MultipartFile... files) throws Exception {
        int i = 0;
        for(MultipartFile multipartFile: files) {
            File file = new File(location + multipartFile.getOriginalFilename());
            if(file.exists()) {
                continue;
            }
            if(!file.createNewFile()) {
                throw new RuntimeException("Error creating file");
            }
            OutputStream outputStream = new FileOutputStream(file);
            InputStream inputStream = multipartFile.getInputStream();
            inputstream_to_outputstream(1024,inputStream, outputStream);
            inputStream.close();
            outputStream.close();
            System.out.println("Save Image successful" + location + multipartFile.getOriginalFilename());
            i++;
        }
    }

    public static List<String> imageNameUnderFolder(String folderName) {
        File folder = new File(folderName);
        List<String> ret= new ArrayList<>();
        if(folder.exists() && folder.isDirectory()) {
            File[] files = folder.listFiles();
            for(File file: files) {
                ret.add(file.getName());
            }
        }
        return ret;

    }
}
