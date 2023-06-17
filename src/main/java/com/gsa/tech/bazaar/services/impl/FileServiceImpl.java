package com.gsa.tech.bazaar.services.impl;


import com.gsa.tech.bazaar.exceptions.BadApiRequestException;
import com.gsa.tech.bazaar.services.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);
    @Override
    public String uploadFile(MultipartFile file, String path) throws IOException {
         // abc.png  to id.png from id that is generated via UUID
        String originalFilename = file.getOriginalFilename();

        logger.info("File Name : {}",originalFilename);

        String filename = UUID.randomUUID().toString();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileNameWithExtension =filename+extension;
        String fullPathWithFileName = path+fileNameWithExtension;

        if(extension.equalsIgnoreCase(".png") || extension.equalsIgnoreCase(".jpeg") || extension.equalsIgnoreCase(".jpg")  ){
            // File Save
            File folder = new File(path);
            if(!folder.exists()){
                // Create folder  WE use MKDIRS if we use Different levels
                folder.mkdirs();
            }
                //Upload
                Files.copy(file.getInputStream(), Paths.get(fullPathWithFileName));

                return fileNameWithExtension;

        }else {
            throw new BadApiRequestException("File with this " + extension + " not allowed");
        }

      //  return null;
    }

    @Override
    public InputStream getResource(String path, String name) throws FileNotFoundException {
        String fullPath = path + File.separator + name ;
        InputStream inputStream=new FileInputStream(fullPath);
        return inputStream;
    }
}
