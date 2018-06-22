/* 
 * Copyright (c) 2017, Tanapoom Sermchaiwong
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are 
 * met:
 * 
 * * Redistributions of source code must retain the above copyright 
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name 'Piggeon' nor the names of 
 *   its contributors may be used to endorse or promote products derived 
 *   from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR 
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, 
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, 
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR 
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING 
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package piggeongo.util;

/**
 * FloatMatrixUtils is used to facilitate matrix manipulations
 */
public class FloatMatrixUtils
{
    /**
     * creates an identy 4 by 4 matrix
     * @return  identity 4x4 matrix
     */
    public static float[] createIdentity4x4()
    {
        return new float[]{1f,0f,0f,0f,
                           0f,1f,0f,0f,
                           0f,0f,1f,0f,
                           0f,0f,0f,1f};
    }

    /**
     * multiplies two square matrices (same width and height)
     * @param mat1          first matrix
     * @param mat2          second matrix
     * @param sidelength    side lengths of both matrix
     * @return              resulting matrix of multiplication
     */
    public static float[] multiplySquares(float[] mat1, float[] mat2, int sidelength)
    {
        float[] newMat = new float[sidelength*sidelength];
        for(int r = 0; r < sidelength*sidelength; r+=sidelength)
        {
            for(int c = 0; c < sidelength; c++)
            {
                float sum = 0;
                for (int x = 0; x < sidelength; x++)
                {
                    sum += mat1[r + x] * mat2[c + x * sidelength];
                }
                newMat[r + c] = sum;
            }
        }
        return newMat;
    }

    /**
     * creates a new 4 by 4 matrix for the scale provided
     * @param scaleX    horizontal scale
     * @param scaleY    vertical scale
     * @return          scaling matrix
     */
    public static float[] scaleTransformMatrix(float scaleX, float scaleY)
    {
        return new float[]{scaleX,  0f    ,     0f,     0f,
                           0f    ,  scaleY,     0f,     0f,
                           0f    ,  0f    ,     1f,     0f,
                           0f    ,  0f    ,     0f,     1f,};
    }

    /**
     * creates a new 4x4 matrix for the translation provided
     * @param tX    translation in x axis
     * @param tY    translation in y axis
     * @return      translation matrix
     */
    public static float[] translateTransformMatrix(float tX, float tY)
    {
        return new float[]{1f    ,  0f    ,     0f,     tX,
                           0f    ,  1f    ,     0f,     tY,
                           0f    ,  0f    ,     1f,     0f,
                           0f    ,  0f    ,     0f,     1f,};
    }

    /*rounding precision of the rotation matrix since java FLOAT may
     *have rounding error*/
    private static final float ROUNDING_PRECISION = 10000000f;

    /**
     * creates a 4x4 rotation matrix which rotates around Z axis
     * (Z axis rotation is the only one applicable in 2D)
     * @param angleInDegrees    The angle in degrees measure
     * @return                  rotation matrix
     */
    public static float[] rotateTransformMatrix(float angleInDegrees)
    {
        float sinX = (float) Math.round(Math.sin(angleInDegrees / 180 * Math.PI)*ROUNDING_PRECISION)/ROUNDING_PRECISION;
        float cosX = (float) Math.round(Math.cos(angleInDegrees / 180 * Math.PI)*ROUNDING_PRECISION)/ROUNDING_PRECISION;

        return new float[]{  cosX, -sinX,    0f,    0f,
                             sinX,  cosX,    0f,    0f,
                             0f  ,    0f,    1f,    0f,
                             0f  ,    0f,    0f,    1f,};
    }

    public static boolean compareMat4fv(float[] mat1, float[] mat2){
        if(mat1 == null || mat2 == null){
            return false;
        }

        boolean same = true;

        for(int x = 0; x < 16; x++){
            same = same && (mat1[x] == mat2[x]);
        }

        return same;
    }
}
