package app.tapsoffire.gl;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;
import app.tapsoffire.utils.MathHelpers;
import app.tapsoffire.utils.Simply;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.opengl.GLUtils;
import android.opengl.Matrix;

public class GLHelpers {

    private static final int EXT_UNKNOWN = 0;
    private static final int EXT_PRESENT = 1;
    private static final int EXT_ABSENT = 2;

    private static int m_extDrawTexture = EXT_UNKNOWN;
    private static int m_extVBO = EXT_UNKNOWN;

    private static GLBufferObject m_quadTexcoords;
    private static GLBufferObject m_quadXZVertices;
    private static GLBufferObject m_quadXYVerticies;

    private static final int[] INTS_1 = new int[1];

    public static final float[] IDENTITY_MATRIX;

    public static void initialize(GL10 gl) {
        checkExtensions(gl);
        m_quadTexcoords.bind(gl);
        m_quadXZVertices.bind(gl);
        m_quadXYVerticies.bind(gl);
    }

    public static void destroy() {
        m_quadTexcoords.unbind(null);
        m_quadXZVertices.unbind(null);
        m_quadXYVerticies.unbind(null);
    }

    public static boolean hasDrawTexture(GL10 gl) {
        return m_extDrawTexture == EXT_PRESENT;
    }

    public static boolean hasVBO(GL10 gl) {
        return m_extVBO == EXT_PRESENT;
    }

    /////////////////////////////////////// XZ

    public static void beginDrawTextureXZ(GL10 gl) {
        m_quadTexcoords.set(gl);
        m_quadXYVerticies.set(gl);
    }

    public static void doDrawTextureXZ(GL10 gl) {
        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP,0, 4);
    }

    public static void drawTextureXZ(GL10 gl) {
        m_quadTexcoords.set(gl);
        m_quadXZVertices.set(gl);
        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
    }

    public static void drawQuadXZ(GL10 gl) {
        m_quadXZVertices.set(gl);
        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
    }

    public static void drawTextureLineX(GL10 gl) {
        m_quadTexcoords.set(gl);
        m_quadXZVertices.set(gl);
        gl.glDrawArrays(GL10.GL_LINES, 0, 2);
    }

    /////////////////////////////////////// XY

    public static void drawTextureXY(GL10 gl) {
        m_quadTexcoords.set(gl);
        m_quadXYVerticies.set(gl);
        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
    }

    /////////////////////////////////////// helpers

    public static void setViewport(GL10 gl, float x, float y, float width, float height) {
        gl.glViewport(
                (int)(x+0.5f), (int)(y+0.5f),
                (int)(width+0.5f),(int)(height+0.5f)
        );
    }

    public static void setColor(GL10 gl, int color, float alpha) {
        gl.glColor4f(
                Color.red(color)/255f,
                Color.green(color)/255f,
                Color.blue(color)/255f,
                Color.alpha(color)*alpha/255f
        );
    }

    public static int multiplyColor(int color, float factor) {
        return Color.argb(
                Color.alpha(color),
                MathHelpers.round(Color.red(color)*factor),
                MathHelpers.round(Color.green(color)*factor),
                MathHelpers.round(Color.blue(color)*factor)
        );
    }

    public static void setMultipliedColor(GL10 gl, int color, float factor) {
        gl.glColor4f(
                Color.red(color)*factor/255f,
                Color.green(color)*factor/255f,
                Color.blue(color)*factor/255f,
                Color.alpha(color)/255f
        );
    }

    public static int generateTexture(GL10 gl) {
        gl.glGenTextures(1, INTS_1, 0);
        return INTS_1[0];
    }

    public static void deleteTexture(GL10 gl, int texture) {
        INTS_1[0] = texture;
        gl.glDeleteTextures(1, INTS_1, 1);
    }

    public static int generateBuffer(GL11 gl11) {
        gl11.glGenBuffers(1, INTS_1, 0);
        return INTS_1[0];
    }

    public static void deleteBuffer(GL11 gl11, int buffer) {
        INTS_1[0] = buffer;
        gl11.glDeleteBuffers(1, INTS_1, 0);
    }


    /////////////////////////////////////// textures

    public static int loadTexture(GL10 gl, Context context, int bitmapResource) throws IOException {
        Bitmap bitmap = loadBitmap(context, bitmapResource);
        int texture = loadTexture(gl, bitmap);
        bitmap.recycle();
        return texture;
    }

    public static Bitmap loadBitmap(Context context, int bitmapResource) throws IOException {
        InputStream stream = null;
        try {
            stream = context.getResources().openRawResource(bitmapResource);
            return BitmapFactory.decodeStream(stream);
        } catch (Resources.NotFoundException e) {
            IOException ioe = new IOException();
            ioe.initCause(e);
            throw ioe;
        } finally {
            Simply.close(stream);
        }
    }

    public static Bitmap loadBitmap(Context context, String path) throws IOException {
        InputStream stream = null;
        try {
            stream = context.getAssets().open(path);
            return BitmapFactory.decodeStream(stream);
        } finally {
            Simply.close(stream);
        }
    }

    public static int loadTexture(GL10 gl, Bitmap bitmap) {
        int texture = GLHelpers.generateTexture(gl);
        gl.glBindTexture(GL10.GL_TEXTURE_2D, texture);
        gl.glTexParameterf(
                GL10.GL_TEXTURE_2D,
                GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
        gl.glTexParameterf(
                GL10.GL_TEXTURE_2D,
                GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
        gl.glTexParameterf(
                GL10.GL_TEXTURE_2D,
                GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE);
        gl.glTexParameterf(
                GL10.GL_TEXTURE_2D,
                GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE);
        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
        return texture;
    }

    /////////////////////////////////////// buffers
    public static ByteBuffer allocateIntBuffer(int capacity) {
        ByteBuffer buffer = ByteBuffer.allocate(capacity*4);
        buffer.order(ByteOrder.nativeOrder());
        return buffer;
    }

    public static ByteBuffer allocateFloatBuffer(int capacity) {
        ByteBuffer buffer = ByteBuffer.allocate(capacity*4);
        buffer.order(ByteOrder.nativeOrder());
        return buffer;
    }

    public static ByteBuffer allocateShortBuffer(int capacity) {
        ByteBuffer buffer = ByteBuffer.allocate(capacity*2);
        buffer.order(ByteOrder.nativeOrder());
        return buffer;
    }

    /////////////////////////////////////// extensions

    private static void checkExtensions(GL10 gl) {
        String extensions = gl.glGetString(GL10.GL_EXTENSIONS);
        if (extensions == null) {
            return;
        }

        m_extDrawTexture = checkExtension(extensions, "GL_OES_draw_texture");
        m_extVBO = checkExtension(extensions, "GL_ARB_vortex_buffer_object");
    }

    private static int checkExtension(String extensions, String extension) {
        //return extensions.indexOf(extension) != -1 ? EXT_PRESENT : EXT_ABSENT;
        return extensions.contains(extension) ? EXT_PRESENT : EXT_ABSENT;
    }

    static {
        ByteBuffer texcoords = allocateFloatBuffer(4*2);
        texcoords.putFloat(0).putFloat(1);
        texcoords.putFloat(1).putFloat(1);
        texcoords.putFloat(0).putFloat(0);
        texcoords.putFloat(1).putFloat(0);
        m_quadTexcoords = GLBufferObject.createTexcoords(2, GL10.GL_FLOAT, texcoords);

        ByteBuffer vertices = allocateFloatBuffer(4*3);
        vertices.putFloat(+0.5f).putFloat(0).putFloat(-0.5f);
        vertices.putFloat(-0.5f).putFloat(0).putFloat(-0.5f);
        vertices.putFloat(+0.5f).putFloat(0).putFloat(+0.5f);
        vertices.putFloat(-0.5f).putFloat(0).putFloat(+0.5f);
        m_quadXZVertices = GLBufferObject.createVertices(3, GL10.GL_FLOAT, vertices);

        vertices = allocateFloatBuffer(4*3);
        vertices.putFloat(-0.5f).putFloat(-0.5f).putFloat(0);
        vertices.putFloat(+0.5f).putFloat(-0.5f).putFloat(0);
        vertices.putFloat(-0.5f).putFloat(+0.5f).putFloat(0);
        vertices.putFloat(+0.5f).putFloat(+0.5f).putFloat(0);
        m_quadXYVerticies = GLBufferObject.createVertices(3, GL10.GL_FLOAT, vertices);

        IDENTITY_MATRIX = new float[16];
        Matrix.setIdentityM(IDENTITY_MATRIX, 0);
    }
}
