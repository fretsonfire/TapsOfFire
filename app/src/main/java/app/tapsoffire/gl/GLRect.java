package app.tapsoffire.gl;

public class GLRect {

    public float x;
    public float y;
    public float width;
    public float height;

    public GLRect() {
        this(0, 0, 0, 0);
    }

    public GLRect(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public GLRect(GLRect other) {
        this.x = other.x;
        this.y = other.y;
        this.width = other.width;
        this.height = other.height;
    }

    public float centerX() {
        return this.x + this.width/2;
    }

    public float centerY() {
        return this.y + this.height/2;
    }

}
