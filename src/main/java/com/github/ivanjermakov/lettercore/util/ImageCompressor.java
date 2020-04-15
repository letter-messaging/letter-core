package com.github.ivanjermakov.lettercore.util;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ImageCompressor {

	/**
	 * Code from here https://memorynotfound.com/compress-images-java-example/
	 *
	 * @param targetPath
	 * @param destinationPath
	 * @param qualityFactor
	 * @throws IOException
	 */
	//	TODO: compress not by factor but by maximum size
	public static void compress(String targetPath, String destinationPath, float qualityFactor) throws IOException {
		File input = new File(targetPath);
		BufferedImage image = ImageIO.read(input);

		File output = new File(destinationPath);
		OutputStream out = new FileOutputStream(output);

		ImageWriter writer = ImageIO.getImageWritersByFormatName("jpg").next();
		ImageOutputStream ios = ImageIO.createImageOutputStream(out);
		writer.setOutput(ios);

		ImageWriteParam param = writer.getDefaultWriteParam();
		if (param.canWriteCompressed()) {
			param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
			param.setCompressionQuality(qualityFactor);
		}

		writer.write(null, new IIOImage(image, null, null), param);

		out.close();
		ios.close();
		writer.dispose();
	}

}
