package com.simulations.test;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.content.Context;
import android.opengl.*;

//TODO: Implement per vertex lighting for better lighting performance

public final class GLShaders
{
    static final String texVertexShaderCode = 
    "uniform mat4 uMVPMatrix;" +
    "attribute vec4 aPosition;" +
	"attribute vec2 aTexCoordinate;" +
    "varying vec2 vTexCoordinate;"+
	"void main() {" +
	"  vTexCoordinate = aTexCoordinate;" +
	"  gl_Position = uMVPMatrix*aPosition;" +
	"}";
    
    static final String texFragmentShaderCode =
	"precision mediump float;" +
	"varying vec2 vTexCoordinate;" +
	"uniform sampler2D uTexture;" +
	"void main(void) {" +
	"  gl_FragColor = texture2D(uTexture, vTexCoordinate);" +
	"}";    
    
    static final String perFragmentLightingTex_VertexShaderCode = 
    "uniform mat4 uMVPMatrix;"+
    "uniform mat4 uMVMatrix;"+
    "attribute vec4 aPosition;"+
    "attribute vec3 aNormal;" +
    "attribute vec2 aTexCoordinate;"+
    "varying vec3 vPosition;"+
    "varying vec3 vNormal;"+
    "varying vec2 vTexCoordinate;"+
    "void main(){"+
    "	vPosition = vec3(uMVMatrix*aPosition);"+
    "	vTexCoordinate = aTexCoordinate;"+
    "	vNormal = vec3(uMVMatrix*vec4(aNormal, 1.0));"+
    "	gl_Position = uMVPMatrix*aPosition;}";
	//TODO: Implement the ability to have different type of light using 'uLighTypeNums'. Implement the ability to process multiple lights.
    static final String perFragmentLightingTex_FragmentShaderCode = 
	"precision mediump float;"+
	"uniform mat3 uLightPositions;"+
	"uniform vec3 uLightTypeNums;"+
	"uniform float uNumOfLights;"+
	"uniform sampler2D uTexture;"+
	"varying vec3 vPosition;"+
	"varying vec3 vNormal;"+
	"varying vec2 vTexCoordinate;"+
	"void main(){"+
	"	float distance = length(vec3(uLightPositions[0][0],uLightPositions[0][1],uLightPositions[0][2])-vPosition);"+
	"	vec3 lightVector = normalize(vec3(uLightPositions[0][0],uLightPositions[0][1],uLightPositions[0][2])-vPosition);"+
	//"	float distance = length(vec3(0.0,-2.5,0.0)-vPosition);"+
	//"	vec3 lightVector = normalize(vec3(0.0,-2.5,0.0)-vPosition);"+
	"	float diffuse = max(dot(vNormal,lightVector), 0.1);"+
	"	diffuse = diffuse*(8.0/(1.0+(0.25*distance*distance)));"+
	"	gl_FragColor = diffuse*texture2D(uTexture, vTexCoordinate);}";
    
    static final String perVertexLightingTex_VertexShaderCode = "";
	static final String perVertexLightingTex_FragmentShaderCode = "";
    ///////
    
	static final String colorVertexShaderCode =
	"uniform mat4 uMVPMatrix;" +
    "attribute vec4 aColor;"+
	"attribute vec4 aPosition;" +
    "varying vec4 vColor;"+
	"void main() {" +
    "  vColor = aColor;"+
	"  gl_Position = uMVPMatrix * aPosition;" +
	"}";
	
    static final String colorFragmentShaderCode =
	"precision mediump float;" +
	"varying vec4 vColor;" +
	"void main() {" +
	"  gl_FragColor = vColor;" +
	"}";
	//TODO: Implement the ability to have different type of light using 'uLighTypeNums'. Implement the ability to process multiple lights.
	static final String perFragmentLightingColor_VertexShaderCode = 
    "uniform mat4 uMVPMatrix;"+
    "uniform mat4 uMVMatrix;"+
    "attribute vec4 aPosition;"+
    "attribute vec3 aNormal;" +
    "attribute vec4 aColor;" +
    "varying vec3 vPosition;"+
    "varying vec3 vNormal;"+
    "varying vec4 vColor;"+
    "void main() {"+
    "	vPosition = vec3(uMVMatrix*aPosition);"+
    "	vColor = aColor;"+
    "	vNormal = vec3(uMVMatrix*vec4(aNormal, 1.0));"+
    "	gl_Position = uMVPMatrix*aPosition;}";
	static final String perFragmentLightingColor_FragmentShaderCode =
	"precision mediump float;"+
	"uniform mat3 uLightPositions;"+
	"uniform vec3 uLightTypeNums;"+
	"uniform float uNumOfLights;"+
	"varying vec3 vPosition;"+
	"varying vec4 vColor;"+
	"varying vec3 vNormal;"+
	"void main(){"+
	"	float distance = length(vec3(uLightPositions[0][0],uLightPositions[0][1],uLightPositions[0][2])-vPosition);"+
	"	vec3 lightVector = normalize(vec3(uLightPositions[0][0],uLightPositions[0][1],uLightPositions[0][2])-vPosition);"+
	//"	float distance = length(vec3(0.0,-2.5,0.0)-vPosition);"+
	//"	vec3 lightVector = normalize(vec3(0.0,-2.5,0.0)-vPosition);"+
	"	float diffuse = max(dot(vNormal,lightVector), 110.1);"+
	"	diffuse = diffuse*(8.0/(1.0+(0.25*distance*distance)));"+
	"	gl_FragColor = vColor*diffuse;}";

	static final String perVertexLightingColor_VertexShaderCode = "";
	static final String perVertexLightingColor_FragmentShaderCode = "";
    ///////
    
    static  String vertexShaderCode = "";
    static  String fragmentShaderCode = "";

	public static final int setShaderProgram(final Context context, final int vertexShadLocID, final int fragmentShadLocID, 
									   final boolean isTextured, final boolean isLighted, final LightManager.LightingQuality lightingQuality){
		//Selected or load appropriate shader information
		if( vertexShadLocID != -1 && fragmentShadLocID != -1){
			//Load shader information from files
			vertexShaderCode = loadShaderInfo(context, vertexShadLocID);
			fragmentShaderCode = loadShaderInfo(context, fragmentShadLocID);
		}
		else{
			if(isTextured){
				if(isLighted){
					switch (lightingQuality) {
					case PER_VERTEX:
						vertexShaderCode = perVertexLightingTex_VertexShaderCode;
						fragmentShaderCode = perVertexLightingTex_FragmentShaderCode;
						break;
					case PER_FRAGMENT:
						vertexShaderCode = perFragmentLightingTex_VertexShaderCode;
						fragmentShaderCode = perFragmentLightingTex_FragmentShaderCode;
						break;
					default:
						break;
					}
				}
				else{
					vertexShaderCode = texVertexShaderCode;
					fragmentShaderCode = texFragmentShaderCode;
				}
			}
			else{
				if(isLighted){
					switch (lightingQuality) {
					case PER_VERTEX:
						vertexShaderCode = perVertexLightingColor_VertexShaderCode;
						fragmentShaderCode = perVertexLightingColor_FragmentShaderCode;
						break;
					case PER_FRAGMENT:
						vertexShaderCode = perFragmentLightingColor_VertexShaderCode;
						fragmentShaderCode = perFragmentLightingColor_FragmentShaderCode;
						break;
					default:
						break;
					}
				}
				else{
					vertexShaderCode = colorVertexShaderCode;
					fragmentShaderCode = colorFragmentShaderCode;
				}
			}
		}
		
		// Prepare shaders and OpenGL program
        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER,
									  vertexShaderCode);
        int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER,
										fragmentShaderCode);

        int mProgram = GLES20.glCreateProgram();
        GLES20.glAttachShader(mProgram, vertexShader);   // add the vertex shader to program
        GLES20.glAttachShader(mProgram, fragmentShader); // add the fragment shader to program
        GLES20.glLinkProgram(mProgram);                  // create OpenGL program executables
        
        return mProgram;
	}  
	
	public static final int loadShader(int type, String shaderCode)
	{
        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
	}

	private static final String loadShaderInfo(final Context context, final int locationID){
		//Load resource binary input buffer using streams
		final InputStream inputStream = context.getResources().openRawResource(locationID);
		final InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
		final BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
		
		//Reader string line by line in binary buffer
		String nextLine;
		final StringBuilder body = new StringBuilder();
		
		try{
			while((nextLine = bufferedReader.readLine()) != null){
				body.append(nextLine);
				body.append('\n');
			}
		}
		catch(IOException e){
			return null;
		}
		 return body.toString();
	}
}
