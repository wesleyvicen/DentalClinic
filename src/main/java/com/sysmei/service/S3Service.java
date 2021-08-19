package com.sysmei.service;

import java.io.InputStream;
import java.net.URI;

import org.springframework.web.multipart.MultipartFile;

public interface S3Service {

	public URI uploadFile(MultipartFile multipartfile);

	public URI uploadFile(InputStream is, String fileName, String contentType);

	public void deleteFile(final String keyName);

}
