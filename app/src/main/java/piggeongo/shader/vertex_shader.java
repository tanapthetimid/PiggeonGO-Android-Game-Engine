package piggeongo.shader;

/**
 * Created by Poom on 3/2/2018.
 */

public class vertex_shader {

    public static String defaultGLES20 =
                    "uniform mat4 uMVPMatrix;" +
                    "attribute vec4 vPosition;" +
                    "attribute vec2 a_texCoord;" +
                    "varying vec2 v_texCoord;" +
                    "void main() {" +
                    "  gl_Position = vPosition * uMVPMatrix;" +
                    "  v_texCoord = a_texCoord;" +
                    "}";

    public static String noTextureShader =
            "uniform mat4 uMVPMatrix;" +
            "attribute vec4 vPosition;" +
                    "void main() {" +
                    "  gl_Position = vPosition * uMVPMatrix;" +
                    "}";

    public static String instancingVertex =
                    "attribute mat4 uMVPMatrix;" +
                    "attribute vec4 vPosition;" +
                    "attribute vec2 a_texCoord;" +
                    "varying vec2 v_texCoord;" +
                    "void main() {" +
                    "  gl_Position = vPosition * uMVPMatrix;" +
                    "  v_texCoord = a_texCoord;" +
                    "}";















    public static String vertexShaderCode =
            "attribute vec4 vPosition;" +
                    "void main() {" +
                    "  gl_Position = vPosition;" +
                    "}";

    public static String defaultShader = "layout(location = 0) in vec2 position;\n" +
            "layout(location = 1) in vec2 tex_coords;\n" +
            "\n" +
            "out vec2 pass_tex_coords;\n" +
            "\n" +
            "void main()\n" +
            "{\n" +
            "\tpass_tex_coords = tex_coords;\n" +
            "\tgl_Position = vec4(position, 0.0, 1.0);\n" +
            "}";

    public static String backup = "layout(location = 0) in vec2 position;\n" +
            "layout(location = 1) in vec2 tex_coords;\n" +
            "\n" +
            "uniform mat4 transformation;\n" +
            "\n" +
            "out vec2 pass_tex_coords;\n" +
            "\n" +
            "void main()\n" +
            "{\n" +
            "\tpass_tex_coords = tex_coords;\n" +
            "\tgl_Position = vec4(position, 0.0, 1.0) * transformation;\n" +
            "}";
}
