package com.simplilearn.edgeserver.service;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.simplilearn.edgeserver.exception.StorageException;

@Service
public class StorageService {

	@Value("${upload.filepath}")
	private String filepath;
	
	public void uploadFile(MultipartFile file) {
		// verify file is empty
		if(file.isEmpty()) {
			throw new StorageException("Falied to upload a file { file is empty !} ");
		}
		String filename = file.getOriginalFilename();
		try {
			InputStream source = file.getInputStream();
			Files.copy(source, Paths.get(filepath+filename), StandardCopyOption.REPLACE_EXISTING);
		} catch (Exception e) {
			throw new StorageException("Falied to upload a file { file is empty !} ");
		}
	}
	
	// get file path
	public String getFile(String filename) {
		return filepath+filename;
	}
	
}
