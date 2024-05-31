package com.sysmei.service;

import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.io.InputStream;

public interface ImageService {

  public BufferedImage getJpgImageFromFile(MultipartFile uploadedFile);

  public BufferedImage pngToJpg(BufferedImage img);

  public InputStream getInputStream(BufferedImage img, String extenString);

}
