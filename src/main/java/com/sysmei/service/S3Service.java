package com.sysmei.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.net.URI;

public interface S3Service {

	public URI uploadFile(MultipartFile multipartfile);

	public URI uploadFile(InputStream is, String fileName, String contentType);

	public void deleteFile(final String keyName);

}
