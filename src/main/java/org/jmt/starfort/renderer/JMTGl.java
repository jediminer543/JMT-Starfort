package org.jmt.starfort.renderer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
//import static org.lwjgl.opengl.GL30.*;

import java.io.IOException;
import java.io.InputStream;
import java.nio.FloatBuffer;
import java.util.Scanner;

import org.joml.Matrix4f;
import org.joml.MatrixStack;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;
//import org.lwjgl.stb.STBTruetype;

/**
 * A wrapper for OpenGL, implemented during conversion between GL11 and
 * GL30. GL30 is being used as spec since anyone with Mesa3d (i.e. my
 * dev laptop) can run it and it is the base for intel graphics, but
 * means no GLSL >1.3 and no GL>30 :( May be changed later.
 * 
 * @author Jediminer543
 *
 */
public class JMTGl {
	
	//OpenGL Begin
	
	/**
	 * Our matrix stack, emulating GL11 style code.
	 */
	public static MatrixStack matModelviewStack = new MatrixStack(16);
	public static MatrixStack matProjectionStack = new MatrixStack(16);
	public static MatrixStack curMatStack = matModelviewStack;
	public static Vector4f color = new Vector4f(1f, 1f, 1f, 1f);
	
	/**
	 * Mapped straight from glApi
	 */
	public static final int JGL_PROJECTION = 0x1701;
	public static final int JGL_MODELVIEW = 0x1700;
	
	/**
	 * Equivelent of GLMatrixMode; 
	 * @param mode One of JGL_PROJECTION or JGL MODELVIEW
	 */
	public static void jglMatrixMode(int mode) {
		if (mode == JGL_PROJECTION) {
			curMatStack = matProjectionStack;
		} else if (mode == JGL_MODELVIEW) {
			curMatStack = matModelviewStack;
		} else {
			//NYI
		}
	}
	
	/**
	 * Pushes matrix to current stack; just like glPushMatrix
	 */
	public static void jglPushMatrix() {
		curMatStack.pushMatrix();
	}
	
	/**
	 * Pops matrix from current stack just like glPopMatrix
	 */
	public static void jglPopMatrix() {
		curMatStack.popMatrix();
	}
	
	/**
	 * Creates an orthographic projection matrix
	 * @param l left
	 * @param r right
	 * @param b bottom
	 * @param t top
	 * @param n near-z
	 * @param f far-z
	 */
	public static void jglOrtho(double l, double r, double b, double t, double n, double f) {
		curMatStack.ortho((float) l, (float) r, (float) b, (float) t, (float) n, (float) f);
	}
	
	/**
	 * Loads identity of current matrix
	 */
	public static void jglLoadIdentity() {
		curMatStack.loadIdentity();
	}
	
	/**
	 * Translates matrix by vector
	 * @param translate vector to translate by
	 */
	public static void jglTranslatef(Vector3f translate) {
		curMatStack.translate(translate);
	}
	
	/**
	 * Translates current matrix by values passed
	 * @param x x translation
	 * @param y y translation
	 * @param z z translation
	 */
	public static void jglTranslatef(float x, float y, float z) {
		curMatStack.translate(x, y, z);
	}
	
	/**
	 * Translates current matrix by values passed
	 * @param x x translation
	 * @param y y translation
	 * @param z z translation
	 */
	public static void jglTranslated(double x, double y, double z) {
		curMatStack.translate((float)x, (float)y, (float)z);
	}
	
	/**
	 * Translates current colour; which is auto-passed to shaders when they are loaded
	 * @param red
	 * @param green
	 * @param blue
	 * @param alpha
	 */
	public static void jglColor4f(float red, float green, float blue, float alpha) {
		color.x = red;
		color.y = green;
		color.z = blue;
		color.w = alpha;
	}
	
	//OpenGL END
	
	//CUSTOM GL BEGIN
	
	//VAO ADDR BEGIN
	
	//NYI TODO
	public static final class JGLVAOPreGen {
		//VertDat
		int topx;
		int topy;
		int botx;
		int boty;
		
		//TexData
		int texWidth;
		int texHeight;
		int texX;
		int texY;
		
		//OGL
		int VAOID;
		int VBOID;
	}
	
	// NYI TODO
	public static int jglGenVAO() {
		return 0;
	}
	//VAO ADDR END
	
	//SHADER BEGIN
	
	/**
	 * Loads and compiles a shader, so you don't have to; uses input streams so you can use ClassLoader.getResourceAsStream() to load
	 * files from classpath
	 * 
	 * @param vertShaderStream vert buffer stream - compiles to vert shader
	 * @param fragShaderStream frag buffer stream - compiles to frag shader
	 * @return OpenGL shader program ID
	 * @throws IOException When something goes wrong while reading files
	 */
	public static int jglLoadShader(InputStream vertShaderStream, InputStream fragShaderStream) throws IOException {
		String vertShaderSrc = readFile(vertShaderStream);
		String fragShaderSrc = readFile(fragShaderStream);
		
		int shader = glCreateProgram();
		int vertShader = glCreateShader(GL_VERTEX_SHADER);
		int fragShader = glCreateShader(GL_FRAGMENT_SHADER);
		
		glShaderSource(vertShader, vertShaderSrc);
		glShaderSource(fragShader, fragShaderSrc);
		
		glCompileShader(vertShader);
		System.out.println("Vert Shader Compile status: " + (glGetShaderi(vertShader, GL_COMPILE_STATUS) == GL_TRUE));
		System.out.println(glGetShaderInfoLog(vertShader));
		
		glCompileShader(fragShader);
		System.out.println("Frag Shader Compile status: " + (glGetShaderi(fragShader, GL_COMPILE_STATUS) == GL_TRUE));
		System.out.println(glGetShaderInfoLog(fragShader));
		
		
		glAttachShader(shader, vertShader);
		//assert(GL_NO_ERROR == glGetError());
		glAttachShader(shader, fragShader);
		//assert(GL_NO_ERROR == glGetError());
		
		//glBindAttribLocation(glslprog, 13, "texCoord");
		
		glLinkProgram(shader);
		//assert(GL_NO_ERROR == glGetError());
		
		return shader;
		
	}
	
	/**
	 * A wrapper for glUseProgram, which also maps the
	 * matrices to the shader.
	 * 
	 * Shaders using this functionality should use: 
	 * 	u_projection and u_modelview uniforms for the two matricese
	 * 	OR u_matrix for a single matrix (u_modelview * u_modelview);
	 * 
	 * @param program Program to bind
	 */
	public static void jglUseProgram(int program) {
		glUseProgram(program);
		
		int projLoc = glGetUniformLocation(program, "u_projection");
		//System.out.println("u_projection:" + projLoc);
		if (projLoc >=0 ) {
			FloatBuffer buf = BufferUtils.createFloatBuffer(16);
			matProjectionStack.get(buf);
			glUniformMatrix4fv(projLoc, false, buf);
		}
		
		int modlLoc = glGetUniformLocation(program, "u_modelview");
		//System.out.println("u_modelview:" + modlLoc);
		if (modlLoc >=0 ) {
			FloatBuffer buf = BufferUtils.createFloatBuffer(16);
			matModelviewStack.get(buf);
			glUniformMatrix4fv(modlLoc, false, buf);
		}
		
		// If shaders use single matrix instead of two
		int matLoc = glGetUniformLocation(program, "u_matrix");
		//System.out.println("u_matrix:" + matLoc);
		if (matLoc >=0 ) {
			FloatBuffer buf = BufferUtils.createFloatBuffer(16);
			matModelviewStack.get(new Matrix4f()).mul(matProjectionStack.getDirect()).get(buf);
			glUniformMatrix4fv(matLoc, false, buf);
		}
		
		//ColorMapping
		int colLoc = glGetUniformLocation(program, "u_col");
		//System.out.println("u_col:" + colLoc);
		if (colLoc >=0 ) {
			FloatBuffer buf = BufferUtils.createFloatBuffer(4);
			color.get(buf);
			glUniform4fv(colLoc, buf);;
		}
		
		//TextureMapping
		int texLoc = glGetUniformLocation(program, "u_tex");
		//System.out.println("u_tex:" + texLoc);
		if (texLoc >=0 ) {
			glUniform1i(texLoc, 0);;
		}
	}
	
	/**
	 * Gets the location of a uniform in the current program
	 * glGetUniformLocation(glGetInteger(GL_CURRENT_PROGRAM), name);
	 * 
	 * @param name
	 * @return
	 */
	public static int jglGetUniformLocation(String name) {
		return glGetUniformLocation(glGetInteger(GL_CURRENT_PROGRAM), name);
	}
	
	/**
	 * Gets the location of a uniform in the current program
	 * glGetAttribLocation(glGetInteger(GL_CURRENT_PROGRAM), name);
	 * 
	 * @param name
	 * @return
	 */
	public static int jglGetAttribLocation(String name) {
		return glGetAttribLocation(glGetInteger(GL_CURRENT_PROGRAM), name);
	}
	
	private static String readFile(InputStream file) throws IOException {
		Scanner reader = new Scanner(file);
		reader.useDelimiter("\n");
	    StringBuilder  stringBuilder = new StringBuilder();
	    String         ls = System.getProperty("line.separator");

	    try {
	        while (reader.hasNext()) {
	            stringBuilder.append(reader.next());
	            stringBuilder.append(ls);
	        }
	        return stringBuilder.toString();
	    } finally {
	        reader.close();
	        file.close();
	    }
	}
	
	//SHADER END
	
	//FONT STUFFS BEGIN
	
	//TODO
	
	//FONTS STUFFS END
}
