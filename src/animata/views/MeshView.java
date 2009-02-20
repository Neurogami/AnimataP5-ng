package animata.views;

import processing.core.PApplet;
import animata.ViewBase;
import animata.model.Layer;
import animata.model.Mesh.Face;
import animata.model.Mesh.Vertex;

public class MeshView extends ViewBase {

	private final Layer layer;
	//private PFont font;

	public MeshView(PApplet applet, Layer layer) {
		super(applet);
		//if(font == null) font = applet.loadFont("Helvetica.vlw");
		this.layer = layer;
	}
	public void draw() {
		applet.noStroke();
		drawFaces(layer.mesh.faces);
//		applet.textFont(font);
//		applet.fill(0);
//		applet.text(layer.name,0,0,100,40,1);
	}
	private void drawFaces(Face[] faces) {
		for (int i = 0; i < faces.length; i++) {
			Face face = faces[i];
			drawFace(face.vertices);
		}
	}
	private void drawFace(Vertex[] vertices) {
		applet.beginShape();
		applet.texture(layer.texture.getImage(applet));
		for (int i = 0; i < vertices.length; i++) {
			Vertex vertex = vertices[i];
			applet.vertex(vertex.x,vertex.y,vertex.u, vertex.v);
		}
		applet.endShape();
	}
	/*
	 * 	glColor3f(1.f, 1.f, 1.f);
		glEnable(GL_TEXTURE_2D);
		glBindTexture(GL_TEXTURE_2D, attachedTexture->getGlResource());

		for (unsigned int i = 0; i < faces->size(); i++)
		{
			Face *face = (*faces)[i];

			glColor4f(1.f, 1.f, 1.f, textureAlpha);
			glBegin(GL_TRIANGLES);
				glTexCoord2f(face->v[0]->texCoord.x, face->v[0]->texCoord.y);
				glVertex2f(face->v[0]->view.x, face->v[0]->view.y);
				glTexCoord2f(face->v[1]->texCoord.x, face->v[1]->texCoord.y);
				glVertex2f(face->v[1]->view.x, face->v[1]->view.y);
				glTexCoord2f(face->v[2]->texCoord.x, face->v[2]->texCoord.y);
				glVertex2f(face->v[2]->view.x, face->v[2]->view.y);
			glEnd();
			glColor3f(1.f, 1.f, 1.f);
		}

		glDisable(GL_TEXTURE_2D);
	 */

}
