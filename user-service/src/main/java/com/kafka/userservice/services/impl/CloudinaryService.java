package com.kafka.userservice.services.impl;

import com.cloudinary.Cloudinary;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@Service
public class CloudinaryService {

    private final Cloudinary cloudinary;

    public CloudinaryService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    public String uploadFile(MultipartFile photo, String email){
        try{
            HashMap<Object, Object> options = new HashMap<>();
            options.put("folder", "users_photos_campusAlert");
            options.put("public_id", email+ "_" + System.currentTimeMillis());
            Map uploadResult =  cloudinary.uploader().upload(photo.getBytes(), options);
            String publicId = (String) uploadResult.get("public_id");

            return cloudinary.url().secure(true).generate(publicId);
        }catch (Exception e){
            throw new RuntimeException("Error al subir el archivo: " + e.getMessage());
        }
    }
}
