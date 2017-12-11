package chargnn.space.model;

import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL14.*;

import org.lwjglx.BufferUtils;

public class Texture {

	private int textureID;
	private int width;
	private int height;

	private ByteBuffer pixels;
	
	public Texture(String file, boolean mipmap)
	{
		BufferedImage bi;
		
		try {
			bi = ImageIO.read(new File(file));
			width = bi.getWidth();
			height = bi.getHeight();
			
			int[] pixels_raw = new int[width * height * 4];
			pixels_raw = bi.getRGB(0, 0, width, height, null, 0, width);
			
			pixels = BufferUtils.createByteBuffer(width * height * 4);
			
			for (int i = 0; i < width; i++) {
				for (int j = 0; j < height; j++) {
					int pixel = pixels_raw[i * width + j];
					pixels.put((byte) ((pixel >> 16) & 0xFF));
					pixels.put((byte) ((pixel >> 8) & 0xFF));
					pixels.put((byte) ((pixel) & 0xFF));
					pixels.put((byte) ((pixel >> 24) & 0xFF));
				}
			}
			
			pixels.flip();
			
			textureID = glGenTextures();
			
			glBindTexture(GL_TEXTURE_2D, textureID);

			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, pixels);
			
			if(mipmap)
			{
				glGenerateMipmap(GL_TEXTURE_2D);
				glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
			    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
			    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);	
			    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_LOD_BIAS, -1);
			}
			} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int getTextureID() {
		return textureID;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public ByteBuffer getPixels() {
		return pixels;
	}	
}
