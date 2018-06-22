package piggeongo.shader;

/**
 * Created by Poom on 3/2/2018.
 */

public class fragment_shader {

    public static String defaultGLES20 =
            "precision mediump float;" +
                    "varying vec2 v_texCoord;" +
                    "uniform sampler2D s_texture;" +
                    "void main() {" +
                    "  gl_FragColor = texture2D( s_texture, v_texCoord );" +
                    "}";


    public static String noTextureShader =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}";















    public static String fragmentShaderCode =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}";

    public static String defaultShader = "in vec2 pass_tex_coords;\n" +
            "\n" +
            "out vec4 fragColor;\n" +
            "\n" +
            "uniform sampler2D textureSampler;\n" +
            "\n" +
            "void main()\n" +
            "{\n" +
            "\tfragColor = vec4(1.0,1.0,0.0,1.0);\n" +
            "}";

    public static String backup = "in vec2 pass_tex_coords;\n" +
            "\n" +
            "out vec4 fragColor;\n" +
            "\n" +
            "uniform sampler2D textureSampler;\n" +
            "\n" +
            "void main()\n" +
            "{\n" +
            "\tfragColor = texture(textureSampler, pass_tex_coords);\n" +
            "}";

    public static String blackWhiteShader = "in vec2 pass_tex_coords;\n" +
            "\n" +
            "out vec4 fragColor;\n" +
            "\n" +
            "uniform sampler2D textureSampler;\n" +
            "\n" +
            "void main()\n" +
            "{\n" +
            "\tvec4 Color = texture(textureSampler, pass_tex_coords);\n" +
            "\tvec3 lum = vec3(0.299, 0.587, 0.114);\n" +
            "\tfragColor = vec4(vec3(dot(Color.rgb, lum)), Color.a);\n" +
            "}";
}
