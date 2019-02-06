package com.serkanp.libgdx;

import java.util.ArrayList;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Plane;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveByAction;
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction;
import com.badlogic.gdx.scenes.scene2d.actions.RotateByAction;
import com.badlogic.gdx.scenes.scene2d.actions.RotateToAction;
import com.badlogic.gdx.scenes.scene2d.actions.ScaleByAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class LibGdx extends ApplicationAdapter implements InputProcessor{
	
	SpriteBatch batch;
	Texture img;
	GorselEleman g1;
	GorselEleman g2;
	OrthographicCamera kamera;
	Viewport viewport;
	float CozunurlukX = 640;
	float CozunurlukY = 480;
	Texture img2;
	float x,y,z;
	ShapeRenderer shapeRenderer;
	ArrayList<Nokta> Noktalar;
	
	
	
	@Override
	public void create () {
		Gdx.input.setInputProcessor(this);
		x=0;
		y=0;
		z=0;
		kamera = new OrthographicCamera();
		img2 = new Texture("img1-1024x500.jpg");
		viewport = new FitViewport(CozunurlukX, CozunurlukY);
		viewport.setCamera(kamera);
		viewport.apply();
		
		kamera.position.set(kamera.viewportWidth/2,kamera.viewportHeight/2,0);
		batch = new SpriteBatch();
		img = new Texture("Adsýz.png");
		shapeRenderer = new ShapeRenderer();
		g1 = new GorselEleman(img);
		g2 = new GorselEleman(img2);
		
		g1.setPosition(300,300);
		g2.setPosition(1500, 1500);
		

		Noktalar = new ArrayList<Nokta>();
		Noktalar.add(new Nokta(0,0));
		Noktalar.add(new Nokta(100,100));
		Noktalar.add(new Nokta(200,0));
		Noktalar.add(new Nokta(300,100));
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		viewport.update(width, height);
	}

	@Override
	public void render () {
		KameraIslemleri();
		
		

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		kamera.update();
		shapeRenderer.setProjectionMatrix(kamera.combined); //kamera ShapeRenderer a Ayarlaniyor.
		
		batch.setProjectionMatrix(kamera.combined);
		batch.begin();
		//batch.draw(img, 0, 0);
		//batch.draw(img2,0,0);
		g1.act(Gdx.graphics.getDeltaTime());
		g1.draw(batch, 0.1F);
		
	//	System.out.println(g1.hasActions()+"  "+g2.hasActions());
		g2.act(Gdx.graphics.getDeltaTime());
		g2.draw(batch, 0.1F);
		batch.end();
		shapeRenderer.begin(ShapeType.Filled); //ShapeRenderer Cizim Tipi Belirleniyor.
		//shapeRenderer.setColor(Color.RED);
	//	shapeRenderer.line(0, 0, 100, 100);
		//shapeRenderer.setColor(Color.BLUE);;
		//shapeRenderer.line(100,100,200,200);
		
		for(int i = 0; i<Noktalar.size()-1;i++) {
			shapeRenderer.line(Noktalar.get(i).x, Noktalar.get(i).y, Noktalar.get(i+1).x, Noktalar.get(i+1).y);
		}
		
		
		shapeRenderer.end();
		
	}
	
	private void KameraIslemleri() {
		if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
			kamera.zoom +=0.01;
			//kamera.rotate(0.3F);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
			kamera.zoom -=0.01;
		}
		if(Gdx.input.isKeyPressed(Input.Keys.T)){
			kamera.translate(0,-1);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.R)){
			x+=2;
			kamera.lookAt(x, y, z);

			System.out.println("X : "+x+"\nY : "+y+"\nZ : "+z);

			System.out.println("\n\n");
		}
		if(Gdx.input.isKeyPressed(Input.Keys.F)){
			y+=2;
			kamera.lookAt(x, y, z);

			System.out.println("X : "+x+"\nY : "+y+"\nZ : "+z);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.C)){
			z+=2;
			kamera.lookAt(x, y, z);

			System.out.println("X : "+x+"\nY : "+y+"\nZ : "+z);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.G)){
			
			ParallelikSiralamaFarkliActionlar();	
			
		}
	}

	private void ParallelikSiralamaFarkliActionlar() {
		RotateByAction rba = new RotateByAction();
		rba.setDuration(3);
		rba.setAmount(360);
		rba.setInterpolation(Interpolation.sineOut);
		g2.setOrigin(g2.getWidth()/2, g2.getHeight()/2);
		
		AlphaAction aa = new AlphaAction();
		aa.setDuration(2F);
		aa.setAlpha(0);
		aa.setInterpolation(Interpolation.exp5Out);
		
		
		
		ScaleByAction sba = new ScaleByAction();
		sba.setDuration(1);
		sba.setAmount(0.1F, 0.1F);
		sba.setInterpolation(Interpolation.bounceOut);
		g2.addAction(new SequenceAction(new ParallelAction(sba,rba), aa)); //Actionlari Sirali Calistirma 5 e Kadar Destekler 5den Fazla olursa Icine Sequence Yzailabilir
	//	g2.addAction(sba);
		//g2.addAction(rba);
	}

	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}

	@Override
	public boolean keyDown(int keycode) {
		
		if(keycode == Input.Keys.DOWN) {
			kamera.zoom -= 0.1;
			return true;
		}
		if(keycode == Input.Keys.UP) {
			kamera.zoom += 0.1;
			return true;
		}
		if(keycode == Input.Keys.V) {
			MoveByAction mta = new MoveByAction();
			mta.setDuration(4);
			//mta.setPosition(250,250);  MoveToAction //Verilen Saniye Boyunca Ulasilacak X ve Y Degerleri
			mta.setAmount(-200, -200); //MoveByAction Verilen Saniye Boyunca Kordinatlardan Cikarilacak Kordinatlar
			mta.setInterpolation(Interpolation.bounceOut);
			g1.addAction(mta);
		}
		if(keycode == Input.Keys.X) {
			
			ArrayList<Nokta> yeniNoktalar = new ArrayList<Nokta>();
			int NoktaSayisi = Noktalar.size();
			yeniNoktalar.add(Noktalar.get(0));
			for(int i = 0;i<NoktaSayisi-1;i++) {
				float AraUzunlukX = Noktalar.get(i).x - Noktalar.get(i+1).x;
				float AraUzunlukY = Noktalar.get(i).y - Noktalar.get(i+1).y;
				yeniNoktalar.add(new Nokta(Noktalar.get(i).x-AraUzunlukX/3,Noktalar.get(i).y-AraUzunlukY/3));
				yeniNoktalar.add(new Nokta(Noktalar.get(i).x-AraUzunlukX/3*2,Noktalar.get(i).y-AraUzunlukY/3*2));
			}
			yeniNoktalar.add(Noktalar.get(Noktalar.size()-1));
			Noktalar = yeniNoktalar;
			return true;
		}
		
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		Noktalar.add(new Nokta((float)screenX,(float)480-screenY));
		
		//Kameranin Konumunun Degismesiyle Ekrana Tiklama Noktasinda Olusan Kaymalari Engellemek Icin
		Ray ray = kamera.getPickRay(screenX, screenY); //Kmeradan Isin Gonderiyor.
		Plane plane = new Plane(); //Duzlem Olusturuyor
		plane.set(0,0,1,0); 
		Vector3 tiklananNokta = new Vector3(); //Vector Olusturuluyor
		Intersector.intersectRayPlane(ray, plane, tiklananNokta); //Duzlem ile Isinin Kesistigi Yeri Vectore Aktariyor
		
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}
	int ilkDokunusX;
	int ilkDokunusY;
	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		KameraKonumuDegistir( screenX, screenY);
		
		return true;
	}

	private void KameraKonumuDegistir(int screenX, int screenY) {
	//
		kamera.position.x -= screenX - ilkDokunusX ;
		kamera.position.y += screenY - ilkDokunusY;
		ilkDokunusX = screenX;
		ilkDokunusY = screenY;
		System.out.println(screenX );
		
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		ilkDokunusX = screenX;
		ilkDokunusY = screenY;
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		
		if(amount ==-1) {
			kamera.zoom -=0.01; 
			return true;
		}else {
			kamera.zoom +=0.01;
			return true;
		}
		
	}
}
