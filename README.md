# arca
A modular Java scene-graph framework based on libgdx

```Java
//Implement a base SceneNode class
public class MyBaseSceneNode extends SceneNode<MyBaseSceneNode> {
	public void print(){
		System.out.println("My position relative to my parent is: " + getLocalPosition(new Vector3()));
		System.out.println("My position relative to world is: " + getWorldPosition(new Vector3()));
	}
}
```

```Java
//My scene-nodes
MyBaseSceneNode root, firstChild, secondChild;
root = new MyBaseSceneNode();
firstChild = new MyBaseSceneNode();
secondChild = new MyBaseSceneNode();

//Append all children to root
root.addChild(firstChild);
root.addChild(secondChild);

//Apply some transformation
root.setLocalPosition(100, 100, 0);
secondChild.setLocalPosition(50, 50, 0);

//Call print function from root
root.print();
//Tell all sub-nodes to call its print function
root.broadcast(new Consumer<MyBaseSceneNode>() {
  @Override
  public void accept(MyBaseSceneNode x) {
    x.print();
  }
});

/* Java 8 solution
 * root.broadcast(x -> x.print());
 */
```

```
Output
--Root:
My position relative to my parent is: (100.0,100.0,0.0)
My position relative to world is: (100.0,100.0,0.0)
--First Child:
My position relative to my parent is: (0.0,0.0,0.0)
My position relative to world is: (100.0,100.0,0.0)
--Second Child:
My position relative to my parent is: (50.0,50.0,0.0)
My position relative to world is: (150.0,150.0,0.0)
```
## Example with Libgdx
```Java
public abstract class GameObject extends SceneNode<GameObject> {
	public abstract void update(float pDeltaTime);
	public abstract void render(ShapeRenderer pShapeRenderer);
}
```
```Java
public class RectangleShape extends GameObject{
	private float mWidth, mHeight;
	private Color mColor;
	
	public RectangleShape(float pWidth, float pHeight, Color pColor){
		mWidth = pWidth;
		mHeight = pHeight;
		mColor = pColor;
	}

	@Override
	public void update(float pDeltaTime) {
		
	}

	@Override
	public void render(ShapeRenderer pShapeRenderer) {
		pShapeRenderer.begin(ShapeType.Filled);
		pShapeRenderer.setTransformMatrix(getWorldMatrix(new Matrix4()));
		pShapeRenderer.setColor(mColor);
		pShapeRenderer.rect(-mWidth / 2f, -mHeight / 2f, mWidth, mHeight);
		pShapeRenderer.end();
	}
}
```
```Java
public class CircleShape extends GameObject{
	private float mRadious;
	private Color mColor;
	
	public CircleShape(float pRadious, Color pColor){
		mRadious = pRadious;
		mColor = pColor;
	}

	@Override
	public void update(float pDeltaTime) {
		
	}

	@Override
	public void render(ShapeRenderer pShapeRenderer) {
		pShapeRenderer.begin(ShapeType.Filled);
		pShapeRenderer.setTransformMatrix(getWorldMatrix(new Matrix4()));
		pShapeRenderer.setColor(mColor);
		pShapeRenderer.circle(0, 0, mRadious);
		pShapeRenderer.end();
	}
}
```
```Java
public class MyGdxGame extends ApplicationAdapter {
	ShapeRenderer mShapeRenderer;
	GameObject mRoot;

	@Override
	public void create () {
		mShapeRenderer = new ShapeRenderer();

		mRoot = new GameObject() {

			@Override
			public void update(float pDeltaTime) {

			}

			@Override
			public void render(ShapeRenderer pShapeRenderer) {

			}
		};

		RectangleShape rec1,rec2;
		CircleShape circle;

		rec1 = new RectangleShape(100, 100, Color.GREEN.cpy());
		rec2 = new RectangleShape(50, 50, Color.RED.cpy()){
			@Override
			public void update(float pDeltaTime) {
				super.update(pDeltaTime);
				setLocalRotationZ(getLocalRotationZ() + 100 * pDeltaTime);
			}
		};
		circle = new CircleShape(40, Color.YELLOW.cpy());

		mRoot.addChild(rec1);
		mRoot.addChild(rec2);
		rec2.addChild(circle);

		mRoot.setLocalPosition(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f, 0);
		circle.setLocalPosition(30, 30, 0);

	}

	@Override
	public void render () {
		//Update
		mRoot.broadcast(new Consumer<GameObject>() {
			@Override
			public void accept(GameObject x) {
				x.update(Gdx.graphics.getDeltaTime());
			}
		});

		//Render
		Gdx.gl.glClearColor(0, 0.5f, 0.5f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		mRoot.broadcast(new Consumer<GameObject>() {
			@Override
			public void accept(GameObject x) {
				x.render(mShapeRenderer);
			}
		});
	}

	@Override
	public void dispose () {
		mShapeRenderer.dispose();
	}
}
```
![GIF](https://media.giphy.com/media/3o7bu4GLIX812USTKM/giphy.gif)
