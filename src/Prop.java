import java.util.ArrayList;

public class Prop {
	private String id, type, param01, param02;
	private boolean state;
	
	private ArrayList<Interaction> interactions = new ArrayList<Interaction>();
	
	
	/**
	 * Metodo especial para crear props de tipo door
	 * @param idProp
	 * @param typeProp
	 * @param stateProp
	 * @param scene01
	 * @param scene02
	 */
	public Prop(String idProp, String typeProp, boolean stateProp, String scene01, String scene02) {
		id = idProp;
		type = typeProp;
		state = stateProp;
		param01 = scene01;
		param02 = scene02;
	}
	
	/**
	 * Metodo especial para crear props de tipo box
	 * @param idProp
	 * @param typeProp
	 * @param scene
	 * @param unlock
	 */
	public Prop(String idProp, String typeProp, String scene, String unlock) {
		id = idProp;
		type = typeProp;
		param01 = scene;
		param02 = unlock;
	}
	
	public String getId() {return id;}
	public String getType() {return type;}
	public boolean getState() {return state;}
	public String getParam01() {return param01;}
	public String getParam02() {return param02;}
	public ArrayList<Interaction> getInteractions() {return interactions;}
	
	public void addInteraction(Interaction action) {
		interactions.add(action);
	}
	
	public void openProp() {
		if (!state) {
			state = true;
		}
	}
}