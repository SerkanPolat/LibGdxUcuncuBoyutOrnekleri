package com.serkanp.libgdx;

import java.util.ArrayList;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.PointLight;
import com.badlogic.gdx.graphics.g3d.model.MeshPart;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;

public class UcuncuBoyut extends ApplicationAdapter implements InputProcessor{
	
	/* UcuncuBoyuta Giris Yapildi.
	 * Cevre ve Isik Kaynagina Giris Yapildi.
	 * MeshBuilder Kullanilarak Cizgiler ile Hypotrochoid Sekli Cizildi.
	 * 
	 */
	
	//Kordinat Duzlemi ve boyutlar matris formunda gosterilir.Buyutme Kucultme ve Hareket Islemlerinde Matriste Toplama Cikarma ve
	//Carpma Islemleri gerceklesmektedir.Kodlar Farkli Olabilir fakat her yerde matris seklinde bir kullanim vardir.
	
	float hkucukYaricap = 2,
	hbuyukYaricap = 4, //Hypotrochoid Degerleriyle oynamak icin olusturuldu.
	hCubukUzunluk = 5, 
	hnoktaSayisi = 2000;
	
	private PerspectiveCamera kamera; //UcuncuBoyutta Gozden Bakis Acisi Icin Perspektif Kamera Kullanilir.
	ModelBatch modelBatch; //Ekrana Cizmeyi Saglar.
	ModelBuilder modelBuilder ; //Sekilleri Olusturmayi Saglar.Olusturduktan Sonra Sekiller ModelBatch'e gonderilir.
	Model model1,modelPlan1,ModelIsik; //Olusturulan Sekile Dair Planlari Ozellikleri Barindirir.
	Model modelCizgiler;
	ModelInstance modelInstance,modelOrnek1,modelOrnek2,modelOrnek3,modelOrnekCizgiler,modelUcgen; //Model Planina Gore Olusan Ekrana Gosterilebilecek Bir Nesne Instance:Ornek
	ModelInstance modelIsikKaynak; //Isiktan Etkilenmicek Isik Kaynagi gibi gosterilecek bir ornek
	ModelInstance modelKure;
	float KameraX,KameraY,KameraZ,KameraAci;
	Vector3 orta,eksen,eksen2;
	MeshPartBuilder meshPartBuilder; //Mesh Modelin Noktalarla Tanimlanmis Hali //MeshPartBuilder Modeli Parca Parca Olusturmayi Saglayan Sinif
	Environment cevre; //Isik Kaynagi Kullanmak Icin Cevre Olusturulmasi Gerekiyor.
	PointLight NoktasalIsik1;
	@Override
	public void create() {
		cevre = new Environment();
		Gdx.input.setInputProcessor(this);
		orta = new Vector3(0f,0f,0f);
		eksen = new Vector3(0f,1f,0f);
		eksen2 = new Vector3(1f,0f,0f);
		KameraX = 0F;
		KameraY = 0F;
		KameraZ = 10f; //Kamera Z Ekseninde 10F e konumlanacak.
		KameraAci=60;
		kamera = new PerspectiveCamera(/*Kameranin Gorecegi Aci*/KameraAci,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		
		kamera.position.set(KameraX,KameraY,KameraZ);
		kamera.lookAt(0,0,0); //Kameranin Belirlenen Yere Bakmasini Saglar
		kamera.near = 0.1F; //Kameraya bu nuzunluktan Daha Yakindaysa Goruntulenmeyecek
		kamera.far = 100f; //Bu Degerden Daha Uzak Olan Cisimler Goruntulenmeyecek;
		modelBatch = new ModelBatch();
		modelBuilder = new ModelBuilder();
		ModelIsik = modelBuilder.createSphere(0.1F, 0.1F, 0.1F, 15, 15, new Material(ColorAttribute.createDiffuse(Color.WHITE)), 
				VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal); //ModelBuilder ile Model Planlari Olusturuyor.
		model1 = modelBuilder.createBox(1F, 1F, 1F, new Material(ColorAttribute.createDiffuse(Color.BLUE)), 
																	VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
		
		modelUcgen = new ModelInstance(UcgenCiz(modelBuilder));
	
		
		modelPlan1 = modelBuilder.createSphere(1F, 1F, 1F, 15, 15, new Material(ColorAttribute.createDiffuse(Color.GOLD)), 
				VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
		
		modelIsikKaynak = new ModelInstance(ModelIsik,1f,1f,1f);
		modelInstance = new ModelInstance(model1,-3f,0f,0f); //Olusturulan Model Planlari Kordinatlara Yerlestirilerek Cizilebilecek Cisimler Oluyor.
		modelOrnek1 = new ModelInstance(model1,2f,2f,2f);
		modelOrnek2 = new ModelInstance(model1,-2f,-2f,2f);
		modelOrnek3 = new ModelInstance(modelPlan1,-2f,-2f,-2f);
		
		modelKure = new ModelInstance(Kure(modelBuilder, 3, 30, 30));
		
		
		NoktasalIsik1 = new PointLight();
		NoktasalIsik1.set(1f, 1f, 1f, 1f, 1f, 1f, 5f);
		cevre.add(NoktasalIsik1);
		
		cevre.set(new ColorAttribute(
				ColorAttribute.AmbientLight,
				0.3f,0.3f,0.3f,1f)); //Isik Almayan Yuzeylerin Ne Kadar hangi renk ile aydinlacagi belirleniyor.
		
	}
	private Model Kure(ModelBuilder modelBuilder,float yaricap,float EnlemParcaSayisi,float BoylamParcaSayisi) {
		modelBuilder.begin();
		meshPartBuilder = modelBuilder.part("Kure", GL20.GL_TRIANGLES, 3, new Material());
		meshPartBuilder.setColor(Color.LIME);
		Vector3 a, b, c, d;
		float EnlemAciDeger = (float) (2* Math.PI / EnlemParcaSayisi);
		float BoylamAciDeger = (float) (Math.PI / BoylamParcaSayisi);
		float x,y,z;
		for(float EnlemAci = 0;EnlemAci < Math.PI*2-EnlemAciDeger;EnlemAci +=EnlemAciDeger) {
			for(float BoylamAci = 0;BoylamAci <Math.PI;BoylamAci +=BoylamAciDeger) {
			a = new Vector3(	
					x = (float) (yaricap*Math.cos(EnlemAci)*Math.sin(BoylamAci)),
					y = (float) (yaricap * Math.sin(EnlemAci) * Math.sin(BoylamAci)),
					z = (float) (yaricap * Math.cos(BoylamAci)));
			b = new Vector3(	
					x = (float) (yaricap*Math.cos(EnlemAci+EnlemAciDeger)*Math.sin(BoylamAci)),
					y = (float) (yaricap * Math.sin(EnlemAci+EnlemAciDeger) * Math.sin(BoylamAci)),
					z = (float) (yaricap * Math.cos(BoylamAci)));
			c = new Vector3(	
					x = (float) (yaricap*Math.cos(EnlemAci)*Math.sin(BoylamAci+BoylamAciDeger)),
					y = (float) (yaricap * Math.sin(EnlemAci) * Math.sin(BoylamAci+BoylamAciDeger)),
					z = (float) (yaricap * Math.cos(BoylamAci+BoylamAciDeger)));
			d = new Vector3(	
					x = (float) (yaricap*Math.cos(EnlemAci+EnlemAciDeger)*Math.sin(BoylamAci+BoylamAciDeger)),
					y = (float) (yaricap * Math.sin(EnlemAci+EnlemAciDeger) * Math.sin(BoylamAci+BoylamAciDeger)),
					z = (float) (yaricap * Math.cos(BoylamAci+BoylamAciDeger)));
					
				
				DortgenCiz(meshPartBuilder,a,b,d,c);
				
			}
		}
		
		
		return modelBuilder.end();
	}
	
	
	private Model UcgenCiz(ModelBuilder modelBuilder) {
		modelBuilder.begin();
		meshPartBuilder = modelBuilder.part("Ucgen", GL20.GL_TRIANGLES, 3, new Material());
		meshPartBuilder.setColor(Color.GOLD);
		DortgenCiz(meshPartBuilder,0,0,0); //Ucgenler ile Dortgen Cizimi
		return modelBuilder.end();
	}
	private void DortgenCiz(MeshPartBuilder meshPartBuilder,Vector3 a,Vector3 b,Vector3 c,Vector3 d) {
		meshPartBuilder.setColor((float)Math.random(),(float)Math.random(),(float)Math.random(),1f);
		meshPartBuilder.triangle(a, b, c);
		meshPartBuilder.triangle(a, c, d);
	}
	
	private void DortgenCiz(MeshPartBuilder meshPartBuilder ,float x,float y,float z) {
		meshPartBuilder.triangle(new Vector3(x-2,y-2,z+0),
				 				new Vector3(x-2,y+2,z+0),
								 new Vector3(x+2,y-2,z+0));
		
		meshPartBuilder.triangle(new Vector3(x-2,y+2,z+0), 
				 new Vector3(x+2,y+2,z+0),new Vector3(x+2,y-2,z+0));
		
		meshPartBuilder.triangle(new Vector3(x-2,y-2,z+4),
				 new Vector3(x+2,y-2,z+4), 
				 new Vector3(x-2,y+2,z+4));

		meshPartBuilder.triangle(new Vector3(x-2,y+2,z+4),
				 new Vector3(x+2,y-2,z+4), 
				 new Vector3(x+2,y+2,z+4));
		
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT); //3D deki derinlik bufferi temizleniyor.
	//	modelOrnekCizgiler = new ModelInstance(NoktalardanCizgilere(modelBuilder, hypotrochoid(hkucukYaricap,hbuyukYaricap,hCubukUzunluk,hnoktaSayisi)));
		
		kamera.update();
		modelBatch.begin(kamera);
		
	/*	modelBatch.render(modelInstance,cevre); //Isik Kaynaginin Oldugu Cevreye Objeler Ekleniyor.
		modelBatch.render(modelOrnek1,cevre);
		modelBatch.render(modelOrnek2,cevre);*/
		modelBatch.render(modelOrnek3,cevre);
		modelBatch.render(modelIsikKaynak);
		
		modelBatch.render(modelKure,cevre);
	//	modelBatch.render(modelUcgen,cevre);
		
	//	modelBatch.render(modelOrnekCizgiler);
		
		
		
		modelBatch.end();
		
	//	modelOrnek1.transform.rotate(1f,0f,1f,1f);
		
		GirisIsle();
			
	}
	
	float x=0;
	
	private void GirisIsle() {
		if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
			
			kamera.rotateAround(orta, eksen, 1F);
			//kamera.rotate(0.3F);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
			
			kamera.rotateAround(orta, eksen, -1F);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.UP)){
			
			kamera.rotateAround(orta, eksen2, -1F);
			//kamera.rotate(0.3F);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.DOWN)){
			
			kamera.rotateAround(orta, eksen2, 1F);
		}if(Gdx.input.isKeyPressed(Input.Keys.X)){
			kamera.fieldOfView = KameraAci++/2;
		}
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
		if(Gdx.input.isKeyPressed(Input.Keys.NUM_1)){
			hkucukYaricap++;
		}
		if(Gdx.input.isKeyPressed(Input.Keys.NUM_2)){
			hkucukYaricap--;
		}
		if(Gdx.input.isKeyPressed(Input.Keys.NUM_4)){
			hbuyukYaricap++;
		}
		if(Gdx.input.isKeyPressed(Input.Keys.NUM_5)){
			hbuyukYaricap--;
		}
		if(Gdx.input.isKeyPressed(Input.Keys.NUM_7)){
			hCubukUzunluk++;
		}
		if(Gdx.input.isKeyPressed(Input.Keys.NUM_8)){
			hCubukUzunluk--;
		}
	}

	@Override
	public void dispose() {
		
	}

	@Override
	public boolean keyDown(int keycode) {
		
		
		
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}
	float EkranX;
	float EkranY;
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		EkranX = screenX;
		EkranY = screenY;
		
		return false;
	}
	private void KameraKonumuDegistir(int screenX, int screenY) {
	
		kamera.translate((screenX - EkranX)/10, -(screenY - EkranY)/10, 0F);
		EkranX = screenX;
		EkranY = screenY;
	
		
	}
	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		KameraKonumuDegistir( screenX, screenY);
		return false;
	}
	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

	private ArrayList<Nokta> hypotrochoid(float kucukYaricap,float buyukYaricap,float CubukUzunluk,float noktaSayisi){
		
		//http://mathworld.wolfram.com/Hypotrochoid.html  Yapilan Cizim
		
		ArrayList<Nokta> Noktalar = new ArrayList<Nokta>();
		float x=0;
		float y=0;
		float theta = 0;
		float dtheta = (float) (2*Math.PI / noktaSayisi);
		for(int i =0;i<noktaSayisi;i++) {
			
			x = (float) ((buyukYaricap - kucukYaricap)*Math.cos(theta) + 
													CubukUzunluk*Math.cos((buyukYaricap - kucukYaricap)/kucukYaricap*theta));
			y = (float) ((buyukYaricap - kucukYaricap)*Math.sin(theta) - 
													CubukUzunluk*Math.sin((buyukYaricap - kucukYaricap)/kucukYaricap*theta));
			
			Noktalar.add(new Nokta(x,y));
			theta +=dtheta;
		}
		
		
		return Noktalar;
	}
	private Model NoktalardanCizgilere(ModelBuilder modelBuilder,ArrayList<Nokta> Noktalar) {
		
		//Hypotrochoid Cizilirken belirlenen noktalari cizgilerle birlestiriyoruz.
		
		modelBuilder.begin(); //Yeni Bir Model Insa Etmek Icin modelBuilder Baslatiliyor.
		meshPartBuilder = 
				modelBuilder.part("Cizgi",/*Olusturulan Parcanin Ekrana Cizilme Tipi */ GL20.GL_LINES , 3, 
																								new Material());
		meshPartBuilder.setColor(Color.BLUE);
		for(int i=0 ;i<Noktalar.size()-1;i++) {
			meshPartBuilder.line(Noktalar.get(i).x,Noktalar.get(i).y,Noktalar.get(i).z,
											Noktalar.get(i+1).x,Noktalar.get(i+1).y,Noktalar.get(i+1).z);
		}
		meshPartBuilder.line(Noktalar.get(Noktalar.size()-1).x,Noktalar.get(Noktalar.size()-1).y,Noktalar.get(Noktalar.size()-1).z,
				Noktalar.get(0).x,Noktalar.get(0).y,Noktalar.get(0).z);
		return modelBuilder.end();  //Ara Satirlarda Olusturulan Model Modele Aktariliyor.
	}
}
