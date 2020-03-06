public class Interaction {
	private String variable, msj;	// variable puede ser el destinatario (scenes) o la condicion (props tipo door)
	
	public Interaction(String var, String desc) {
		variable = var;
		msj = desc;
	}
	
	public String getVariable() {return variable;}
	public String getDescription() {return msj;}
}