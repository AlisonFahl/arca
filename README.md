# arca
A modular Java scene-graph framework based on libgdx

```Java
import com.arca.core.scenenode.SceneNode;
import com.badlogic.gdx.math.Vector3;

//Implement my base SceneNode class
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
