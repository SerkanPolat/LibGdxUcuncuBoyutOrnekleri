package com.serkanp.libgdx;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.loaders.ModelLoader;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.PointLight;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;

public class UcuncuBoyutYuzey extends ApplicationAdapter{
	
	//Rastgele Yuksekliklerde Kareler Ile Uc Boyutlu Bir Yuzey Olusturuldu.
	//Blender Programi Ile Yapilan Cisim Programa Dahil Edilecek Yuzey Uzerinde Hareket Etmesi Saglandi.
	
	
	
	PerspectiveCamera kamera;
	Environment cevre;
	ModelBatch modelBatch;
	float KameraX,KameraY,KameraZ,KameraAci;
	ModelBuilder modelBuilder;
	MeshPartBuilder meshPartBuilder;
	Model modelPlan1,modelKup,modelDisaridanGelen,modelDunya;
	ModelInstance modelOrnek1,modelYeryuzu,modelKupOrnek,modelInstanceDisaridanGelen,modelInstanceDunya;
	PointLight NoktasalIsik1;
	ModelInstance modelIsikKaynak;
	Model ModelIsik;
	float [][] YuzeyGrid;
	int gridX,gridZ;
	float dGrid = .7f;
	float GridYukseklikMax;
	float YeryuzuMerkezlemeX;
	float YeryuzuMerkezlemeZ;
	Vector3 KupPozisyon,KupYon;
	
	ModelLoader modelLoader; //Disaridan Model Eklemeye Yardimci Class
	
	
	@Override
	public void create() {
		KupYon = new Vector3(0.02f,0,0);
		KupPozisyon = new Vector3(0,1,0);
		GridYukseklikMax = dGrid * .85f;
		gridX = 20;
		gridZ = 20;
		cevre = new Environment();
		modelBuilder  = new ModelBuilder();
		modelLoader = new ObjLoader(); //Olusturulan Cisim Obj Turunde Oldugundan Obj Loader Kullaniliyor.
		modelDisaridanGelen = modelLoader.loadModel(Gdx.files.internal("tahtakup.obj")); //Obj dosyasi modelDisaridanGelene Aktariliyor.
		modelInstanceDisaridanGelen = new ModelInstance(modelDisaridanGelen);
		modelDunya = modelLoader.loadModel(Gdx.files.internal("dunya.obj"));
		modelInstanceDunya = new ModelInstance(modelDunya);
		
		modelBatch = new ModelBatch();
		KameraX = 0F; 
		KameraY = 2F;
		KameraZ = 5f; //Kamera Z Ekseninde 10F e konumlanacak.
		KameraAci=60;
		kamera = new PerspectiveCamera(/*Kameranin Gorecegi Aci*/KameraAci,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		kamera.position.set(KameraX,KameraY,KameraZ);
		kamera.lookAt(0,0,0);
		kamera.near = 0.1f;
		kamera.far = 100f;
		modelKup =  modelBuilder.createBox(.2f, .2f, .2f, new Material(ColorAttribute.createDiffuse(Color.BLUE)), 
				VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
		
		cevre.set(new ColorAttribute(
				ColorAttribute.AmbientLight,
				0.3f,0.3f,0.3f,1f));
		NoktasalIsik1 = new PointLight();
		NoktasalIsik1.set(1, 1, 1, 0, 3, 0, 5);
		cevre.add(NoktasalIsik1);
		ModelIsik = modelBuilder.createSphere(0.1F, 0.1F, 0.1F, 15, 15, new Material(ColorAttribute.createDiffuse(Color.WHITE)), 
				VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal); //ModelBuilder ile Model Planlari Olusturuyor.
		modelIsikKaynak = new ModelInstance(ModelIsik,1f,1f,1f);
		YuzeyGrid = new float[gridX][gridZ];
		for(int i = 0;i<gridX;i++) {
			
			for(int j = 0;j<gridZ;j++) {
				YuzeyGrid[i][j] = (float) (Math.random()*GridYukseklikMax); //Her Bir Kareye Rastgele Yukseklik Atamasi Yapiliyor.
			}
		}
		modelYeryuzu = new ModelInstance(Yeryuzu());
		modelKupOrnek = new ModelInstance(modelKup,KupPozisyon.x,KupPozisyon.y,KupPozisyon.z);
		modelInstanceDisaridanGelen.transform.scale(.2f,.2f , .2f);
		modelInstanceDisaridanGelen.transform.setTranslation(1, 1, 1);
		modelInstanceDunya.transform.translate(2, 2, 2);
		modelInstanceDunya.transform.rotate(Vector3.X,170);
		modelInstanceDunya.transform.scale(.25f, .25f, .25f);
	}
	@Override
	public void render() {
		Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT); //3D deki derinlik bufferi temizleniyor.
		modelInstanceDisaridanGelen.transform.setTranslation(KupPozisyon);
		modelInstanceDunya.transform.rotate(Vector3.Y, 0.4f); //Dunya Yavas Yavas Donuyor :)
		kamera.update();
		modelBatch.begin(kamera);
		modelBatch.render(modelInstanceDisaridanGelen,cevre);
		modelBatch.render(modelYeryuzu,cevre);
		modelBatch.render(modelKupOrnek,cevre);
		modelBatch.render(modelIsikKaynak);
		modelBatch.render(modelInstanceDunya,cevre);
		GirisIsle();
		modelBatch.end();
		
		
	}
	private Model Yeryuzu() {
		//MeshPartBuilder Tanimlanmis (Varsayilan)
		//Grid Yeryuzunun Olusacak Kare Sayisi
		//dGrid Her Bir Gridin Uzunlugu
		//Yukseklik Degerleri YuzeyGridden Aliniyor
		//Kopukluk Olmamasi Icin Vectorler Arasi YuzeyGrid Degerleri Uyumlu Veriliyor.
		//Yeryuzu Merkezleme Yeryuzunu Ortalamak Icin Kullaniliyor.Aksi Halde 0,0 Kordinatlarindan Basliyor.
		modelBuilder.begin();
		meshPartBuilder = modelBuilder.part("Ucgen", GL20.GL_TRIANGLES, Usage.Position | Usage.Normal,new Material(ColorAttribute.createDiffuse(Color.ORANGE)));

		YeryuzuMerkezlemeX = gridX * dGrid/2f;
		YeryuzuMerkezlemeZ = gridZ * dGrid/2f;
		for(int i = 0;i<gridX-1;i++) {	
			for(int j = 0;j<gridZ-1;j++) {
				
				DortgenCiz(meshPartBuilder,new Vector3(i*dGrid-YeryuzuMerkezlemeX,YuzeyGrid[i][j],j*dGrid-YeryuzuMerkezlemeZ),
						   new Vector3(i*dGrid-YeryuzuMerkezlemeX,YuzeyGrid[i][j+1],(j+1)*dGrid-YeryuzuMerkezlemeZ),
						   new Vector3((i+1)*dGrid-YeryuzuMerkezlemeX,YuzeyGrid[i+1][j+1],(j+1)*dGrid-YeryuzuMerkezlemeZ),
						   new Vector3((i+1)*dGrid-YeryuzuMerkezlemeX,YuzeyGrid[i+1][j],j*dGrid-YeryuzuMerkezlemeZ));

			}
		}
		return modelBuilder.end();
	}
	
	private void DortgenCiz(MeshPartBuilder meshPartBuilder,Vector3 a,Vector3 b,Vector3 c,Vector3 d) {
		
	//	meshPartBuilder.triangle(a, b, c);
	//	meshPartBuilder.triangle(a, c, d);
		meshPartBuilder.rect(a, b, c, d, 
							a.cpy().sub(b.cpy()).crs(b.cpy().sub(c.cpy())).nor()  );
		
	}
	private void GirisIsle() {
		
		if(Gdx.input.isKeyPressed(Input.Keys.Z)){
			
			kamera.fieldOfView = KameraAci--/2;
		}
		if(Gdx.input.isKeyPressed(Input.Keys.W)){
			
			NoktasalIsik1.position.y += 0.1f;
			
			modelIsikKaynak.transform.setTranslation(NoktasalIsik1.position); //IsikKaynakModeli NoktasalIsik Kaynaginin Matrisine Esitleniyor.
		}
		if(Gdx.input.isKeyPressed(Input.Keys.A)){
			NoktasalIsik1.position.x -= 0.1f;
			modelIsikKaynak.transform.setTranslation(NoktasalIsik1.position);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.S)){
			NoktasalIsik1.position.y -= 0.1f;
			modelIsikKaynak.transform.setTranslation(NoktasalIsik1.position);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.D)){
			NoktasalIsik1.position.x += 0.1f;
			modelIsikKaynak.transform.setTranslation(NoktasalIsik1.position);
		}

		if(Gdx.input.isKeyPressed(Input.Keys.R)){
			NoktasalIsik1.position.z += 0.1f;
			modelIsikKaynak.transform.setTranslation(NoktasalIsik1.position);
		}

		if(Gdx.input.isKeyPressed(Input.Keys.F)){
			NoktasalIsik1.position.z -= 0.1f;
			modelIsikKaynak.transform.setTranslation(NoktasalIsik1.position);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.U)){

			KupPozisyon.add(KupYon);
			int i = (int) ((KupPozisyon.x+YeryuzuMerkezlemeX)/dGrid);
			int z = (int) ((KupPozisyon.z+YeryuzuMerkezlemeZ)/dGrid);
			if(i<19&&z<19&&i>0&&z>0) {
			KupPozisyon.y = ((YuzeyGrid[i][z]+YuzeyGrid[i++][z]+YuzeyGrid[i][z++]+YuzeyGrid[i][z++])/4+.1f);
			System.out.println(i+"   "+z);
			KameraZ += 0.02f;
			kamera.translate(KupYon);
			}else {
				KupPozisyon.sub(KupYon);
				
			}
		}
		else if(Gdx.input.isKeyPressed(Input.Keys.J)){
			
			KupPozisyon.sub(KupYon);
			int i = (int) ((KupPozisyon.x+YeryuzuMerkezlemeX)/dGrid);
			int z = (int) ((KupPozisyon.z+YeryuzuMerkezlemeZ)/dGrid);
			if(i<19&&z<19&&i>0&&z>0) {
			KupPozisyon.y = ((YuzeyGrid[i][z]+YuzeyGrid[i++][z]+YuzeyGrid[i][z++]+YuzeyGrid[i][z++])/4+.1f);
			System.out.println(i+"   "+z);
			KameraZ += 0.02f;
			kamera.translate(-KupYon.x,-KupYon.y,-KupYon.z);
			}else {
				KupPozisyon.add(KupYon);
				
			}
		}
		if(Gdx.input.isKeyPressed(Input.Keys.H)){
			
			KupYon.rotate(Vector3.Y,1f);
			modelInstanceDisaridanGelen.transform.rotate(Vector3.Y, 1f);
			
		}
		else if(Gdx.input.isKeyPressed(Input.Keys.K)){
			KupYon.rotate(Vector3.Y,-1f);
			modelInstanceDisaridanGelen.transform.rotate(Vector3.Y, -1f);
		}

		if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
			
			kamera.rotateAround(Vector3.Zero, Vector3.Y, 1F);
			//kamera.rotate(0.3F);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
			
			kamera.rotateAround(Vector3.Zero, Vector3.Y, -1F);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.UP)){
			
			kamera.rotateAround(Vector3.Zero, Vector3.X, -1F);
			//kamera.rotate(0.3F);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.DOWN)){
			
			kamera.rotateAround(Vector3.Zero, Vector3.X, 1F);
		}if(Gdx.input.isKeyPressed(Input.Keys.X)){
			kamera.fieldOfView = KameraAci++/2;
		}
		if(Gdx.input.isKeyPressed(Input.Keys.Z)){
			
			kamera.fieldOfView = KameraAci--/2;
		}
	}
	@Override
	public void dispose() {
		
	}
	
	
}
