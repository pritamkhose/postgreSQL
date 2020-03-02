package com.pritam.postgreSQL.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.pritam.postgreSQL.exception.CustomErrorResponse;
import com.pritam.postgreSQL.exception.ResourceNotFoundException;
import com.pritam.postgreSQL.model.FileObj;
import com.pritam.postgreSQL.repository.FileRepository;
import com.pritam.postgreSQL.storage.FileStorageService;
import com.pritam.postgreSQL.storage.UploadFileResponse;

// https://www.callicoder.com/spring-boot-file-upload-download-rest-api-example/

@RestController
@RequestMapping("/file/")
public class FileController {

	@Autowired(required=true)
	private FileStorageService fileStorageService;
	@Autowired
	private FileRepository fileRepository;

	@GetMapping("listall")
	public List<FileObj> getFiles() {
		return fileRepository.findAll();
	}


	@GetMapping("listlocal")
	public ArrayList<String> listFile() {
		return fileStorageService.listFiles();
	}

	@PostMapping("upload")
	public UploadFileResponse uploadFile(@RequestParam("file") MultipartFile file) {
		String fileName = fileStorageService.storeFile(file);

		String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
				.path("/file/get/")
				.path(fileName)
				.toUriString();

		FileObj fobj = new FileObj(); 
		fobj.setFilename(fileName);
		fobj.setFilepath("/file/get/"+fileName);
		fobj.setFilesize(String.valueOf(file.getSize()));
		fobj.setFiledatatype(file.getContentType());
		fileRepository.save(fobj);

		return new UploadFileResponse(fileName, fileDownloadUri,
				file.getContentType(), file.getSize());

	}


	@GetMapping("get/{fileId}")
	public ResponseEntity<?> getFileID(@PathVariable Long fileId) {
		return fileRepository.findById(fileId)
				.map(fobj -> {	
					Resource resource = fileStorageService.loadFileAsResource(fobj.getFilename());
					// Fallback to the default content type if type could not be determined
					String contentDispostion = "inline; fileName=\"" + resource.getFilename() + "\"";
					String contentType = fobj.getFiledatatype();
					if(contentType == null) {
						contentType = "application/octet-stream";
						contentDispostion = "attachment; fileName=\"" + resource.getFilename() + "\"";
					}

					return 
							ResponseEntity.ok()
							.contentType(MediaType.parseMediaType(contentType))
							.header(HttpHeaders.CONTENT_DISPOSITION, contentDispostion)
							.body(resource);

				}).orElseThrow(() -> new ResourceNotFoundException("File not found with id " + fileId));
	}



	@DeleteMapping("/delete/{fileId}")
	public ResponseEntity<?> deleteFileID(@PathVariable Long fileId) {
		return fileRepository.findById(fileId)
				.map(fobj -> {	
					File index = new File("../AppStorage/"+ fobj.getFilename());
					if (!index.exists()) {
						CustomErrorResponse  errors = new CustomErrorResponse ();
						errors.setError("File not found with fileName " + fobj.getFilename());
						errors.setStatus(HttpStatus.NOT_FOUND.value());
						return new ResponseEntity<>(errors, HttpStatus.NOT_FOUND);
					} else {
						index.delete();
					}
					fileRepository.delete(fobj);
					return ResponseEntity.ok().build();
				}).orElseThrow(() -> new ResourceNotFoundException("File not found with id " + fileId));
	}

	@DeleteMapping("deleteFileName/{fileName:.+}")
	public ResponseEntity<?> deleteFileName(@PathVariable String fileName, HttpServletRequest request) {
		File index = new File("../AppStorage/"+ fileName);
		if (!index.exists()) {
			CustomErrorResponse  errors = new CustomErrorResponse ();
			errors.setError("File not found with fileName " + fileName);
			errors.setStatus(HttpStatus.NOT_FOUND.value());
			return new ResponseEntity<>(errors, HttpStatus.NOT_FOUND);
		} else {
			index.delete();
			return ResponseEntity.ok().body(null);
		}
	}

	@PostMapping("uploadMultiple")
	public List<UploadFileResponse> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files) {
		return Arrays.asList(files)
				.stream()
				.map(file -> uploadFile(file))
				.collect(Collectors.toList());
	}

	@GetMapping("getFileName/{fileName:.+}")
	public ResponseEntity<Resource> getFileName(@PathVariable String fileName, HttpServletRequest request) {
		// Load file as Resource
		Resource resource = fileStorageService.loadFileAsResource(fileName);

		// Try to determine file's content type
		String contentType = null;
		try {
			contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
		} catch (IOException ex) {
			System.out.println("Could not determine file type.");
		}

		// Fallback to the default content type if type could not be determined
		String contentDispostion = "inline; fileName=\"" + resource.getFilename() + "\"";
		if(contentType == null) {
			contentType = "application/octet-stream";
			contentDispostion = "attachment; fileName=\"" + resource.getFilename() + "\"";
		}

		long len = 0;
		try {
			len = resource.contentLength();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return ResponseEntity.ok()
				.contentType(MediaType.parseMediaType(contentType))
				.contentLength(len)
				.header(HttpHeaders.CONTENT_DISPOSITION, contentDispostion)
				.body(resource);
	}


}
