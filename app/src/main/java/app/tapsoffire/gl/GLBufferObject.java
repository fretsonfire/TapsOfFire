package app.tapsoffire.gl;

import java.nio.ByteBuffer;
import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

public class GLBufferObject {

    private int m_type;
    private int m_dataType;
    private int m_pointSize;
    private ByteBuffer m_data;
    private int m_buffer;
    private boolean m_bound;

    private GLBufferObject(int type, int dataType, int pointSize, ByteBuffer data) {
        this.m_type = type;
        this.m_dataType = dataType;
        this.m_pointSize = pointSize;
        this.m_data = data;

        m_data.position(0);
    }

    public void bind(GL10 gl) {
        if (m_bound) {
            return;
        }

        m_bound = true;
        if (GLHelpers.hasVBO(gl)) {
            GL11 gl11 = (GL11) gl;
            m_buffer = GLHelpers.generateBuffer(gl11);
            gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, m_buffer);
            gl11.glBufferData(GL11.GL_ARRAY_BUFFER, m_data.capacity(), m_data, GL11.GL_STATIC_DRAW);
            gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, 0);
        }
    }

    public void unbind(GL10 gl) {
        if (!m_bound) {
            return;
        }

        m_bound = false;
        if (gl != null) {
            if (GLHelpers.hasVBO(gl)) {
                GL11 gl11 = (GL11) gl;
                GLHelpers.deleteBuffer(gl11, m_buffer);
            }
        }
    }

    public void set(GL10 gl) {
        bind(gl);
        if (GLHelpers.hasVBO(gl)) {
            GL11 gl11 = (GL11) gl;
            gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, m_buffer);
            switch (m_type) {
                case GL10.GL_VERTEX_ARRAY:
                    gl11.glVertexPointer(m_pointSize, m_dataType, 0, 0);
                    break;
                case GL10.GL_TEXTURE_COORD_ARRAY:
                    gl11.glTexCoordPointer(m_pointSize, m_dataType, 0, 0);
                    break;
                case GL10.GL_NORMAL_ARRAY:
                    gl11.glNormalPointer(m_dataType, 0, 0);
                    break;
            }
        } else {
            switch (m_type) {
                case GL10.GL_VERTEX_ARRAY:
                    gl.glVertexPointer(m_pointSize, m_dataType, 0, m_data);
                    break;
                case GL10.GL_TEXTURE_COORD_ARRAY:
                    gl.glTexCoordPointer(m_pointSize, m_dataType, 0, m_data);
                    break;
                case GL10.GL_NORMAL_ARRAY:
                    gl.glNormalPointer(m_dataType, 0, m_data);
            }
        }
    }

    public static GLBufferObject createVertices(int size, int type, ByteBuffer data) {
        return new GLBufferObject(GL10.GL_VERTEX_ARRAY, type, size, data);
    }

    public static GLBufferObject createTexcoords(int size, int type, ByteBuffer data) {
        return new GLBufferObject(GL10.GL_TEXTURE_COORD_ARRAY, type, size, data);
    }

    public static GLBufferObject createNormals(int type, ByteBuffer data) {
        return new GLBufferObject(GL10.GL_NORMAL_ARRAY, type, -1, data);
    }

}
