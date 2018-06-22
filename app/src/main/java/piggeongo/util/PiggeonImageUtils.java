package piggeongo.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;

import com.app.tanapoom.piggeongo.MainActivity;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.HashMap;

/**
 * Created by Poom on 3/2/2018.
 */

public class PiggeonImageUtils {
    public static final int SQUARE = -1;
    public static final int TRIANGLE = -2;

    private static HashMap<Integer, Integer> textureIDCache = new HashMap<>();
    private static HashMap<Integer, Integer> textureIDCacheFromBitmap = new HashMap<>();
    private static HashMap<Integer, ImageInfo> imageInfoCache = new HashMap<>();
    private static HashMap<Integer, ImageInfo> imageInfoCacheFromBitmap = new HashMap<>();

    public static int loadTexture(Bitmap bitmap, int resourceId) {
        if (!textureIDCache.containsKey(resourceId)) {
            final int[] textureHandle = new int[1];

            GLES20.glGenTextures(1, textureHandle, 0);

            if (textureHandle[0] != 0) {

                // Bind to the texture in OpenGL
                GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle[0]);

                // Set filtering
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);

                // Load the bitmap into the bound texture.
                GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
            }

            if (textureHandle[0] == 0) {
                throw new RuntimeException("Error loading texture.");
            }

            textureIDCache.put(resourceId, textureHandle[0]);

            return textureHandle[0];
        }
        return textureIDCache.get(resourceId);
    }

    public static int loadTextureBitmapID(Bitmap bitmap, int bitmapID) {
        if (!textureIDCacheFromBitmap.containsKey(bitmapID)) {
            final int[] textureHandle = new int[1];

            GLES20.glGenTextures(1, textureHandle, 0);

            if (textureHandle[0] != 0) {

                // Bind to the texture in OpenGL
                GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle[0]);

                // Set filtering
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);

                // Load the bitmap into the bound texture.
                GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, bitmap, 0);
            }

            if (textureHandle[0] == 0) {
                throw new RuntimeException("Error loading texture.");
            }

            textureIDCacheFromBitmap.put(bitmapID, textureHandle[0]);

            return textureHandle[0];
        }
        return textureIDCacheFromBitmap.get(bitmapID);
    }

    public static int loadTextureNoCache(Bitmap bitmap) {
        final int[] textureHandle = new int[1];

        GLES20.glGenTextures(1, textureHandle, 0);

        if (textureHandle[0] != 0) {

            // Bind to the texture in OpenGL
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle[0]);

            // Set filtering
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);

            // Load the bitmap into the bound texture.
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, bitmap, 0);
        }

        if (textureHandle[0] == 0) {
            throw new RuntimeException("Error loading texture.");
        }
        return textureHandle[0];
    }

    public static ImageInfo loadImage(final int resourceId) {
        if (!imageInfoCache.containsKey(resourceId)) {
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inScaled = false;   // No pre-scaling
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;

            // Read in the resource
            final Bitmap bitmap = BitmapFactory.decodeResource(MainActivity.context.getResources(), resourceId, options);
            int textureId = loadTexture(bitmap, resourceId);
            ImageInfo imageInfo = new ImageInfo(textureId, bitmap.getWidth(), bitmap.getHeight());

            float halfW = imageInfo.width / 2f;
            float halfH = imageInfo.height / 2f;

            //define vertices array
            float[] vertices = new float[]
                    {
                            -halfW, halfH,
                            -halfW, -halfH,
                            halfW, -halfH,
                            -halfW, halfH,
                            halfW, -halfH,
                            halfW, halfH
                    };

            // initialize vertex byte buffer for shape coordinates
            ByteBuffer bb = ByteBuffer.allocateDirect(vertices.length * 4);
            bb.order(ByteOrder.nativeOrder());
            imageInfo.vertices = bb.asFloatBuffer();
            imageInfo.vertices.put(vertices);
            imageInfo.vertices.position(0);

            //define texture coordinates
            float[] texCoords = new float[]
                    {
                            0.0f, 0.0f,
                            0.0f, 1.0f,
                            1.0f, 1.0f,
                            0.0f, 0.0f,
                            1.0f, 1.0f,
                            1.0f, 0.0f
                    };

            ByteBuffer bb2 = ByteBuffer.allocateDirect(texCoords.length * 4);
            bb2.order(ByteOrder.nativeOrder());
            imageInfo.texCoords = bb2.asFloatBuffer();
            imageInfo.texCoords.put(texCoords);
            imageInfo.texCoords.position(0);

            // Recycle the bitmap, since its data has been loaded into OpenGL.
            bitmap.recycle();

            //cache imageInfo
            imageInfoCache.put(resourceId, imageInfo);

            return imageInfo;

        } else {
            //retrieve from cache
            return imageInfoCache.get(resourceId);
        }
    }

    public static ImageInfo loadImageFromBitmap(Bitmap bitmap, int bitmapID) {
        if (!imageInfoCacheFromBitmap.containsKey(bitmapID)) {
            // Read in the resource
            int textureId = loadTexture(bitmap, bitmapID);
            ImageInfo imageInfo = new ImageInfo(textureId, bitmap.getWidth(), bitmap.getHeight());

            float halfW = imageInfo.width / 2f;
            float halfH = imageInfo.height / 2f;

            //define vertices array
            float[] vertices = new float[]
                    {
                            -halfW, halfH,
                            -halfW, -halfH,
                            halfW, -halfH,
                            -halfW, halfH,
                            halfW, -halfH,
                            halfW, halfH
                    };

            // initialize vertex byte buffer for shape coordinates
            ByteBuffer bb = ByteBuffer.allocateDirect(vertices.length * 4);
            bb.order(ByteOrder.nativeOrder());
            imageInfo.vertices = bb.asFloatBuffer();
            imageInfo.vertices.put(vertices);
            imageInfo.vertices.position(0);

            //define texture coordinates
            float[] texCoords = new float[]
                    {
                            0.0f, 0.0f,
                            0.0f, 1.0f,
                            1.0f, 1.0f,
                            0.0f, 0.0f,
                            1.0f, 1.0f,
                            1.0f, 0.0f
                    };

            ByteBuffer bb2 = ByteBuffer.allocateDirect(texCoords.length * 4);
            bb2.order(ByteOrder.nativeOrder());
            imageInfo.texCoords = bb2.asFloatBuffer();
            imageInfo.texCoords.put(texCoords);
            imageInfo.texCoords.position(0);

            // Recycle the bitmap, since its data has been loaded into OpenGL.
            bitmap.recycle();

            imageInfoCacheFromBitmap.put(bitmapID, imageInfo);

            return imageInfo;
        } else {
            return imageInfoCacheFromBitmap.get(bitmapID);
        }
    }

    public static ImageInfo loadImageFromBitmapNoCache(Bitmap bitmap) {
        // Read in the resource
        int textureId = loadTextureNoCache(bitmap);
        ImageInfo imageInfo = new ImageInfo(textureId, bitmap.getWidth(), bitmap.getHeight());

        float halfW = imageInfo.width / 2f;
        float halfH = imageInfo.height / 2f;

        //define vertices array
        float[] vertices = new float[]
                {
                        -halfW, halfH,
                        -halfW, -halfH,
                        halfW, -halfH,
                        -halfW, halfH,
                        halfW, -halfH,
                        halfW, halfH
                };

        // initialize vertex byte buffer for shape coordinates
        ByteBuffer bb = ByteBuffer.allocateDirect(vertices.length * 4);
        bb.order(ByteOrder.nativeOrder());
        imageInfo.vertices = bb.asFloatBuffer();
        imageInfo.vertices.put(vertices);
        imageInfo.vertices.position(0);

        //define texture coordinates
        float[] texCoords = new float[]
                {
                        0.0f, 0.0f,
                        0.0f, 1.0f,
                        1.0f, 1.0f,
                        0.0f, 0.0f,
                        1.0f, 1.0f,
                        1.0f, 0.0f
                };

        ByteBuffer bb2 = ByteBuffer.allocateDirect(texCoords.length * 4);
        bb2.order(ByteOrder.nativeOrder());
        imageInfo.texCoords = bb2.asFloatBuffer();
        imageInfo.texCoords.put(texCoords);
        imageInfo.texCoords.position(0);

        // Recycle the bitmap, since its data has been loaded into OpenGL.
        bitmap.recycle();
        return imageInfo;

    }

    public static ImageInfo createSquareImage(float width, float height, float[] color){
        // Read in the resource
        ImageInfo imageInfo = new ImageInfo(SQUARE, width, height);

        float halfW = imageInfo.width / 2f;
        float halfH = imageInfo.height / 2f;

        //define vertices array
        float[] vertices = new float[]
                {
                        -halfW, halfH,
                        -halfW, -halfH,
                        halfW, -halfH,
                        -halfW, halfH,
                        halfW, -halfH,
                        halfW, halfH
                };

        // initialize vertex byte buffer for shape coordinates
        ByteBuffer bb = ByteBuffer.allocateDirect(vertices.length * 4);
        bb.order(ByteOrder.nativeOrder());
        imageInfo.vertices = bb.asFloatBuffer();
        imageInfo.vertices.put(vertices);
        imageInfo.vertices.position(0);

        //define texture coordinates
        float[] texCoords = new float[]
                {
                        0.0f, 0.0f,
                        0.0f, 1.0f,
                        1.0f, 1.0f,
                        0.0f, 0.0f,
                        1.0f, 1.0f,
                        1.0f, 0.0f
                };

        ByteBuffer bb2 = ByteBuffer.allocateDirect(texCoords.length * 4);
        bb2.order(ByteOrder.nativeOrder());
        imageInfo.texCoords = bb2.asFloatBuffer();
        imageInfo.texCoords.put(texCoords);
        imageInfo.texCoords.position(0);

        imageInfo.color = color;

        // Recycle the bitmap, since its data has been loaded into OpenGL.
        return imageInfo;
    }

    public static void deleteTexture(int textureID) {
        GLES20.glDeleteTextures(1, new int[]{textureID}, 0);
    }

    public static void clearCache() {
        textureIDCache.clear();
        textureIDCacheFromBitmap.clear();
        imageInfoCache.clear();
        imageInfoCacheFromBitmap.clear();
    }

    public static FloatBuffer getStandardTextureCoordinates(){
        //define texture coordinates
        float[] texCoords = new float[]
                {
                        0.0f, 0.0f,
                        0.0f, 1.0f,
                        1.0f, 1.0f,
                        0.0f, 0.0f,
                        1.0f, 1.0f,
                        1.0f, 0.0f
                };

        ByteBuffer bb2 = ByteBuffer.allocateDirect(texCoords.length * 4);
        bb2.order(ByteOrder.nativeOrder());
        FloatBuffer texBuffer;
        texBuffer = bb2.asFloatBuffer();
        texBuffer.put(texCoords);
        texBuffer.position(0);

        return texBuffer;
    }
}
