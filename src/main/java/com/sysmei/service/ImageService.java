package com.sysmei.service;

import java.awt.image.BufferedImage;
import java.io.InputStream;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService {

	public BufferedImage getJpgImageFromFile(MultipartFile uploadedFile);

	public BufferedImage pngToJpg(BufferedImage img);

	public InputStream getInputStream(BufferedImage img, String extenString);

}
