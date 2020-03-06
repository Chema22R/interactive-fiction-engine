public class Player {
	private String id, currentScene;
	
	public Player(String idPlayer) {
		id = idPlayer;
	}
	
	public String getId() {return id;}
	public String getCurrentScene() {return currentScene;}
	
	public void setCurrentScene(String newScene) {
		currentScene = newScene;
	}
}