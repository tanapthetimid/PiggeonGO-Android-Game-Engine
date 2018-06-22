package piggeongo.nativelib;

import static android.opengl.GLES20.*;

import android.opengl.GLES20;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;

import java.nio.FloatBuffer;
import java.util.ArrayList;

import piggeongo.piggeon.GameObject;
import piggeongo.piggeon.Node;
import piggeongo.piggeon.Stage;
import piggeongo.piggeon.Updatable;
import piggeongo.shader.fragment_shader;
import piggeongo.shader.vertex_shader;
import piggeongo.util.FloatMatrixUtils;
import piggeongo.util.ImageInfo;
import piggeongo.util.PiggeonImageUtils;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static piggeongo.util.FloatMatrixUtils.multiplySquares;


/**
 * Created by Poom on 3/2/2018.
 */

public class PiggeonGLRenderer implements GLSurfaceView.Renderer {

    private ShaderProgram shaderProgramTexture;
    private ShaderProgram shaderProgramNoTexture;
    private int activeShader;
    private Stage stage;
    private int mPositionHandleNoTexture;
    private int mColorHandleNoTexture;
    private int mtrxhandleNoTexture;
    private int mPositionHandleTexture;
    private int mTexCoordLoc;
    private int mtrxhandleTexture;
    private int mSamplerLoc;
    private int activeTexture;

    private Stage newStage;

    private FloatBuffer currentVertices = null;

    public PiggeonGLRenderer(Stage stage){
        this.stage = stage;
    }

    public void changeStage(Stage newStage){
        this.newStage = newStage;
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        shaderProgramTexture = new ShaderProgram();
        shaderProgramTexture.attachVertexShader(vertex_shader.defaultGLES20);
        shaderProgramTexture.attachFragmentShader(fragment_shader.defaultGLES20);
        shaderProgramTexture.link();

        shaderProgramNoTexture = new ShaderProgram();
        shaderProgramNoTexture.attachVertexShader(vertex_shader.noTextureShader);
        shaderProgramNoTexture.attachFragmentShader(fragment_shader.noTextureShader);
        shaderProgramNoTexture.link();

        stage.loadStage();
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int i, int i1) {
        glClear(GL_COLOR_BUFFER_BIT);
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        processUpdates();
        render();

        if(newStage != null){
            stage = newStage;
            stage.loadStage();
            newStage = null;
        }
    }

    private void processUpdates()
    {
        //updates all updatable game objects
        Updatable[] updateObjects = stage.getUpdateListAsArray();
        for (Updatable updatable : updateObjects)
        {
            updatable.update(stage);
        }

        //updates stage's camera
        stage.getCamera().update(stage);
    }

    private void render(){
        glClear(GL_COLOR_BUFFER_BIT);

        //enable blend (transparency)
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        GLES20.glEnable(GLES20.GL_BLEND);

        //setup NoTexture render stuff
        // get handle to vertex shader's vPosition member
        mPositionHandleNoTexture = GLES20.glGetAttribLocation(shaderProgramNoTexture.getID(), "vPosition");
        // Enable a handle to the triangle vertices
        GLES20.glEnableVertexAttribArray(mPositionHandleNoTexture);
        // get handle to fragment shader's vColor member
        mColorHandleNoTexture = GLES20.glGetUniformLocation(shaderProgramNoTexture.getID(), "vColor");
        //transformmatrix no texture
        // Get handle to shape's transformation matrix
        mtrxhandleNoTexture = glGetUniformLocation(shaderProgramTexture.getID(),"uMVPMatrix");

        //setup textured stuff
        // get handle to vertex shader's vPosition member
        mPositionHandleTexture = glGetAttribLocation(shaderProgramTexture.getID(), "vPosition");
        // Enable generic vertex attribute array
        glEnableVertexAttribArray(mPositionHandleTexture);
        // Get handle to texture coordinates location
        mTexCoordLoc = glGetAttribLocation(shaderProgramTexture.getID(),"a_texCoord" );
        // Enable generic vertex attribute array
        glEnableVertexAttribArray ( mTexCoordLoc );
        // Get handle to shape's transformation matrix
        mtrxhandleTexture = glGetUniformLocation(shaderProgramTexture.getID(),"uMVPMatrix");
        // Get handle to textures locations
        mSamplerLoc = glGetUniformLocation (shaderProgramTexture.getID(), "s_texture" );
        // Set the sampler texture unit to 0, where we have saved the texture.
        glUniform1i ( mSamplerLoc, 0);
        // Prepare the texturecoordinates
        glVertexAttribPointer ( mTexCoordLoc,
                2,
                GL_FLOAT,
                false,
                0,
                PiggeonImageUtils.getStandardTextureCoordinates());


        /*normalizing transformation, which is applied LAST by property
         *of matrices multiplication.*/
        float[] scale = FloatMatrixUtils
                .scaleTransformMatrix(2f/PiggeonGLSurfaceView.canvasWidth, 2f/PiggeonGLSurfaceView.canvasHeight);
        float[] translate = FloatMatrixUtils
                .translateTransformMatrix(-1f, -1f);


        //starts rendering from root node
        renderNode(stage.getRootNode(), multiplySquares(translate, scale,4));
    }

    //recursively render game objects and nodes
    private void renderNode(Node node, float[] transformMat4fv)
    {
        //converts transformations into matrices
        float[] scale = FloatMatrixUtils.scaleTransformMatrix(node.getScaleX(), node.getScaleY());
        float[] rotate = FloatMatrixUtils.rotateTransformMatrix(node.getRotationAngle());
        float[] translate = FloatMatrixUtils.translateTransformMatrix(node.getX(), node.getY());

        //create new transformation matrix
        float[] mat4fvNew = multiplySquares(transformMat4fv,
                multiplySquares(translate,
                        multiplySquares(rotate, scale, 4), 4), 4);

        if (node instanceof GameObject)
        {
            renderGameObject((GameObject) node, mat4fvNew);
        }

        if(!node.isInstanced()) {
            for (Node child : node.getChildren()) {
                renderNode(child, mat4fvNew);
            }
        }else{
            renderInstancedNode(node, mat4fvNew);
        }
    }

    private void renderInstancedNode(Node parent, float[] transformMat4fv){

    }

    private void renderGameObject(GameObject gameObject, float[] transformMat4fv){
        ImageInfo imageInfo = gameObject.getImageInfo();
        if(imageInfo.textureID >= 0){
            renderGameObjectWithTexture(imageInfo, transformMat4fv);
        }else{
            renderGameObjectNoTexture(imageInfo, transformMat4fv);
        }
    }



    private final void renderGameObjectNoTexture(ImageInfo imageInfo, float[] transformMat4fv){
        if(activeShader != shaderProgramNoTexture.getID()) {
            shaderProgramNoTexture.bind();
            activeShader = shaderProgramNoTexture.getID();
        }

        if(!imageInfo.vertices.equals(currentVertices)) {
            glVertexAttribPointer(mPositionHandleNoTexture,
                    2,
                    GL_FLOAT,
                    false,
                    0,
                    imageInfo.vertices);

            currentVertices = imageInfo.vertices;
        }

        // Set color for drawing the triangle
        GLES20.glUniform4fv(mColorHandleNoTexture, 1, imageInfo.color, 0);

        glUniformMatrix4fv(mtrxhandleNoTexture, 1, false, transformMat4fv, 0);

        // Draw the triangle
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 6);
    }

    private final void renderGameObjectWithTexture(ImageInfo imageInfo, float[] transformMat4fv){
        //bind piggeon.shader program
        if(activeShader != shaderProgramTexture.getID()) {
            shaderProgramTexture.bind();
            activeShader = shaderProgramTexture.getID();
        }

        if(imageInfo.textureID != activeTexture) {
            glBindTexture(GL_TEXTURE_2D, imageInfo.textureID);
            activeTexture = imageInfo.textureID;
        }

        if(!imageInfo.vertices.equals(currentVertices)) {
            // Prepare the triangle coordinate data
            glVertexAttribPointer(mPositionHandleTexture,
                    2,
                    GL_FLOAT,
                    false,
                    0,
                    imageInfo.vertices);

            currentVertices = imageInfo.vertices;
        }

        // Apply the projection and view transformation
        glUniformMatrix4fv(mtrxhandleTexture, 1, false, transformMat4fv, 0);

        // Draw the triangle
        glDrawArrays(GL_TRIANGLES,0, 6);
    }

}
