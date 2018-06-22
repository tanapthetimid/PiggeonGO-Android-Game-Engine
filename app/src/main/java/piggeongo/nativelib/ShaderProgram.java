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

package piggeongo.nativelib;

import static android.opengl.GLES20.*;

public class ShaderProgram
{
	private int programID;
	private int fragmentShaderID = -1;
	private int vertexShaderID;

	/**
	 * Create a piggeon.shader program
	 */
	public ShaderProgram()
	{
		programID = glCreateProgram();
	}

	/**
	 * Attach a Vertex Shader to this program
	 *
	 * @param shader shader source
	 */
	public void attachVertexShader(String shader)
	{
		//load source
		String vertexShaderSource = shader;

		//create piggeon.shader and set source
		vertexShaderID = glCreateShader(GL_VERTEX_SHADER);
		glShaderSource(vertexShaderID, vertexShaderSource);

		//compile piggeon.shader
		glCompileShader(vertexShaderID);

		//attach piggeon.shader
		glAttachShader(programID, vertexShaderID);
	}

	/**
	 * Attach a Fragment Shader to this program
	 *
	 * @param shader shader source
	 */
	public void attachFragmentShader(String shader)
	{
		if(fragmentShaderID != -1){
			glDetachShader(programID, fragmentShaderID);
			glDeleteShader(fragmentShaderID);
		}

		//load source
		String fragmentShaderSource = shader;

		//create piggeon.shader and set source
		fragmentShaderID = glCreateShader(GL_FRAGMENT_SHADER);
		glShaderSource(fragmentShaderID, fragmentShaderSource);

		//compile piggeon.shader
		glCompileShader(fragmentShaderID);

		//attach piggeon.shader
		glAttachShader(programID, fragmentShaderID);
	}

	/**
	 * link this program to use it
	 */
	public void link()
	{
		//link this program
		glLinkProgram(programID);

	}

	public void bind()
	{
		glUseProgram(programID);
	}

	public void dispose()
	{
		//detach shaders
		glDetachShader(programID, vertexShaderID);
		glDetachShader(programID, fragmentShaderID);

		//delete piggeon.shader
		glDeleteShader(vertexShaderID);
		glDeleteShader(fragmentShaderID);
		//delete program
		glDeleteProgram(programID);
	}

	public int getID()
	{
		return programID;
	}
}