import java.util.ArrayList;

public class Scene {
	private String id, msj;
	private int numDoors, numBoxes;
	
	private ArrayList<Interaction> interactions = new ArrayList<Interaction>();
	private ArrayList<Prop> props = new ArrayList<Prop>();
	
	
	public Scene(String idScene, int doors, int boxes, String desc) {
		id = idScene;
		numDoors = doors;
		numBoxes = boxes;
		msj = desc;
	}

	public String getId() {return id;}
	public int getNumDoors() {return numDoors;}
	public int getNumBoxes() {return numBoxes;}
	public String getDescription() {return msj;}
	public ArrayList<Interaction> getInteractions() {return interactions;}
	public ArrayList<Prop> getProps() {return props;}

	public void addInteraction(Interaction action) {
		interactions.add(action);
	}
	
	public void addProp(Prop newProp) {
		props.add(newProp);
	}

	public void removeProp(String idProp) {
		props.remove(getProp(idProp));
	}
	
	/**
	 * Metodo que devuelve el prop con id idProp (busca el prop dentro de una escena concreta)
	 * @param idProp
	 * @return
	 */
	public Prop getProp(String idProp) {
		for (int i=0; i<props.size(); i++) {
			if (props.get(i).getId().equals(idProp)) {
				return props.get(i);
			}
		}
		
		return null;
	}
}