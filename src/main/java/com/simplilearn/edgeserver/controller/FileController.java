package com.simplilearn.edgeserver.controller;

import java.io.File;
import java.io.FileInputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.simplilearn.edgeserver.exception.StorageException;
import com.simplilearn.edgeserver.service.StorageService;

@Controller
public class FileController {
	
	@Autowired
	StorageService storageService;
	
	@RequestMapping(value="/", method=RequestMethod.GET)
	public String index() {
		return "index.html";
	}
	
	@RequestMapping(value="/upload", method=RequestMethod.POST, consumes= {"multipart/form-data"})
	public String upload(@RequestParam MultipartFile file) {
		// logic to upload file.
		storageService.uploadFile(file);
		return "redirect:/success.html";
	}
	
	@RequestMapping(value="/download", method=RequestMethod.GET)
	public ResponseEntity<InputStreamResource> download(@RequestParam(value="filename") String filename) {
		try {
			// read File
			File file = new File(storageService.getFile(filename));
			// download file
			InputStreamResource  resource= new InputStreamResource(new FileInputStream(file));
			
			HttpHeaders headers = new HttpHeaders();
			headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"", file.getName()));
			
			return ResponseEntity.ok().headers(headers).contentLength(file.length())
					.contentType(MediaType.parseMediaType("application/text")).body(resource);
		} catch (Exception e) {
			throw new StorageException("Falied to download a file !");
		}
	}
	
	
	@ExceptionHandler(StorageException.class)
	public String handStorageException() {
		return "redirect:/failure.html";
	}
}
