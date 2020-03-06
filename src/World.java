import java.util.ArrayList;

/**
 * Patron utilizado: singleton (para que se garantice la unicidad de world, solo existiendo una instancia)
 */
public class World {
	private static final World world = new World();
	private static final Player player = new Player("player00");
	
	private ArrayList<Scene> scenes = new ArrayList<Scene>();
	
	
	private World() {}
	
	public static World getWorld() {return world;}
	public static Player getPlayer() {return player;}
	
	
	public void addScene(Scene newScene) {
		scenes.add(newScene);
	}
	
	public void removeScene(String idScene) {
		scenes.remove(getScene(idScene));
	}
	
	/**
	 * Metodo que devuelve la scene con id idScene
	 * @param idScene
	 * @return
	 */
	public Scene getScene(String idScene) {
		for (int i=0; i<scenes.size(); i++) {
			if (scenes.get(i).getId().equals(idScene)) {
				return scenes.get(i);
			}
		}
		
		return null;
	}
	
	/**
	 * Metodo que devuelve el prop con id idProp (busca el prop entre todas las escenas)
	 * @param idProp
	 * @return
	 */
	public Prop getProp(String idProp) {
		Prop prop;
		
		for (int i=0; i<scenes.size(); i++) {
			prop = scenes.get(i).getProp(idProp);
			
			if (prop != null) {
				return prop;
			}
		}
		
		return null;
	}
}