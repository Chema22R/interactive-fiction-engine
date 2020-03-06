import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class Main {
	private static JFileChooser fc;
	private static DocumentBuilder builder;
	private static Document doc;
	private static int typeError;
	
	private static World world = World.getWorld();
	private static Player player = World.getPlayer();
	
	public static void main(String[] args) {
		init();
		validate();
		load();
		startGame(true);
		
		System.exit(0);
	}
	
	/**
	 * Metodo que inicializa las estrcuturas principales:
	 * 	- 1. Se inicializan los elementos necesarios para generar la ventana de seleccion de documentos xml
	 * 	- 2. Se inicializan los elementos necesarios para llevar a cabo el parseo del documento cargado
	 */
	private static void init() {
		// file chooser window
		fc = new JFileChooser();
		fc.setCurrentDirectory(new File(System.getProperty("user.dir")));
		fc.setDialogTitle("Seleccione un archivo xml");
	    
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fc.setAcceptAllFileFilterUsed(false);
	    FileNameExtensionFilter filter = new FileNameExtensionFilter("XML files (.xml)", "xml");
	    fc.setFileFilter(filter);
	    
	    
	    // xml parser
	    DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
	    domFactory.setValidating(true);
	    
	    try {
			builder = domFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
	    
		builder.setErrorHandler(new ErrorHandler() {
	        public void fatalError(SAXParseException exception) throws SAXException {typeError=0;throw exception;}
	        public void error(SAXParseException exception) throws SAXException {typeError=1;throw exception;}
	        public void warning(SAXParseException exception) throws SAXException {typeError=2;throw exception;}
	    });
	}

	/**
	 * Metodo que ejecuta la ventana de seleccion de documentos xml y que adminsitra las interacciones del usuario con esta ventana
	 */
	private static void validate() {
		String msj;
		int msjType;
		Object[] options = {"Si", "Salir"};

		while (true) {
			if (fc.showOpenDialog(fc) == JFileChooser.APPROVE_OPTION) {
				try {
					doc = builder.parse(fc.getSelectedFile().getAbsolutePath());
					msj = "El documento xml esta bien formado y es válido.\n";
					msjType = JOptionPane.QUESTION_MESSAGE;
				} catch (SAXException | IOException e) {
					if (typeError == 0) {
						msj = "El documento xml no está bien formado:\n";
					} else if (typeError == 1) {
						msj = "El documento xml no es válido:\n";
					} else {
						msj = "Ha ocurrido un problema durante la ejecución del programa:\n";
					}
					msj += "- " + e.getMessage() + "\n\n";
					msjType = JOptionPane.ERROR_MESSAGE;
				}

				if (msjType == JOptionPane.QUESTION_MESSAGE) {
					System.out.println("Documento XML cargado y parseado correctamente");
					break;
				} else if (JOptionPane.showOptionDialog(fc, msj + "¿Desea probar con otro documento?", "", JOptionPane.YES_NO_OPTION, msjType, null, options, options[0]) != 0) {
					break;
				}
			} else {
				break;
			}
		}
	}

	/**
	 * Metodo que realiza la lectura del documento xml cargado y genera todas las escenas, atrezo, jugadores, etc. Es decir, se encarga
	 * de crear todos los elementos de la estrcutura del xml y de configurarlos con los atributos que poseen.
	 */
	private static void load() {
		NodeList nodeList;
		NamedNodeMap attrList;
		NodeList actionList;
		
		String id, msj, variable, type, param01, param02;
		int numDoors, numBoxes;
		boolean state;
		Scene scene;
		Prop prop;
		
		doc.getDocumentElement().normalize();
		
		/*
		 * player
		 */
		
		nodeList = doc.getElementsByTagName("player");	// coge los elementos con tag player
		
		for (int i=0; i<nodeList.getLength(); i++) {
			attrList = nodeList.item(i).getAttributes();	// coge todos los atributos del nodo i
			
			player.setCurrentScene(attrList.item(0).getNodeValue());	// guarda la escena inicial del player cogiendo el valor del primer atributo (solo hay un atributo en player)
		}
		
		/*
		 * scenes
		 */
		
		nodeList = doc.getElementsByTagName("scene");	// coge los elementos con tag scene
		
		for (int i=0; i<nodeList.getLength(); i++) {
			if (nodeList.item(i).getNodeType() != Node.ELEMENT_NODE) continue;	// salta la iteraccion si el nodo no es de tipo elemento
			
			id = null;
			numDoors = 0;
			numBoxes = 0;
			msj = null;
			
			// attributes
			
			attrList = nodeList.item(i).getAttributes();	// coge todos los atributos del nodo i (de la scene i)
			
			for (int j=0; j<attrList.getLength(); j++) {
				switch (attrList.item(j).getNodeName()) {	// puesto que se pueden cargar desordenados los attributos, se deben guardar segun se van encontrando
					case "id": id = attrList.item(j).getNodeValue(); break;
					case "numDoors": numDoors = Integer.parseInt(attrList.item(j).getNodeValue()); break;
					case "numBoxes": numBoxes = Integer.parseInt(attrList.item(j).getNodeValue()); break;
					case "msj": msj = attrList.item(j).getNodeValue(); break;
				}
			}
			
			scene = new Scene(id, numDoors, numBoxes, msj);	// una vez cogidos los atributos, se crea la scene basica

			// actions
			
			actionList = nodeList.item(i).getChildNodes();	// coge todas las action (childs) del nodo i
			
			for (int j=0; j<actionList.getLength(); j++)  {
				if (actionList.item(j).getNodeType() != Node.ELEMENT_NODE) continue;	// salta la iteraccion si el nodo no es de tipo elemento
				
				variable = null;
				msj = null;

				attrList = actionList.item(j).getAttributes();	// coge todos los atributos del nodo j (del action j)
				
				for (int k=0; k<attrList.getLength(); k++) {
					switch (attrList.item(k).getNodeName()) {	// puesto que se pueden cargar desordenados los attributos, se deben guardar segun se van encontrando
						case "dest": variable = attrList.item(k).getNodeValue(); break;
						case "msj": msj = attrList.item(k).getNodeValue(); break;
					}
				}
				
				scene.addInteraction(new Interaction(variable, msj));	// una vez cogidos los atributos, se crea la interaction y se guarda en la escena a la que pertenece
			}
			
			world.addScene(scene);	// por ultimo, una vez cogida la escena entera (con sus atributos y actions), se guarda en world
		}
		
		/*
		 * props
		 */
		
		nodeList = doc.getElementsByTagName("prop");	// coge los elementos con tag prop
		
		for (int i=0; i<nodeList.getLength(); i++) {
			if (nodeList.item(i).getNodeType() != Node.ELEMENT_NODE) continue;	// salta la iteraccion si el nodo no es de tipo elemento
			
			id = null;
			type = null;
			state = true;
			param01 = null;
			param02 = null;
			
			// attributes
			
			attrList = nodeList.item(i).getAttributes();	// coge todos los atributos del nodo i (de la door i)
			
			for (int j=0; j<attrList.getLength(); j++) {
				switch (attrList.item(j).getNodeName()) {	// puesto que se pueden cargar desordenados los attributos, se deben guardar segun se van encontrando
					case "id": id = attrList.item(j).getNodeValue(); break;
					case "type": type = attrList.item(j).getNodeValue(); break;
					case "state": state = Boolean.parseBoolean(attrList.item(j).getNodeValue()); break;
					case "scene01": param01 = attrList.item(j).getNodeValue(); break;
					case "scene02": param02 = attrList.item(j).getNodeValue(); break;
					case "unlock": param02 = attrList.item(j).getNodeValue(); break;
				}
			}
			
			if (type.equals("door")) {	// una vez cogidos los atributos, se crea el prop basico
				prop = new Prop(id, type, state, param01, param02);
			} else if (type.equals("box")) {
				prop = new Prop(id, type, param01, param02);
			} else {
				break;
			}
			
			// actions
			
			actionList = nodeList.item(i).getChildNodes();	// coge todas las action (childs) del nodo i
			
			for (int j=0; j<actionList.getLength(); j++)  {
				if (actionList.item(j).getNodeType() != Node.ELEMENT_NODE) continue;	// salta la iteraccion si el nodo no es de tipo elemento
				
				variable = null;
				msj = null;

				attrList = actionList.item(j).getAttributes();	// coge todos los atributos del nodo j (del action j)
				
				for (int k=0; k<attrList.getLength(); k++) {
					switch (attrList.item(k).getNodeName()) {	// puesto que se pueden cargar desordenados los attributos, se deben guardar segun se van encontrando
						case "condition": variable = attrList.item(k).getNodeValue(); break;
						case "msj": msj = attrList.item(k).getNodeValue(); break;
					}
				}
				
				prop.addInteraction(new Interaction(variable, msj));	// una vez cogidos los atributos, se crea la interaction y se guarda en el prop al que pertenece
			}
			
			scene = world.getScene(param01);	// por ultimo, una vez cogido el prop entero (con sus atributos y actions), se guarda en las scene a las que pertenece (dos en el caso de door y una en el de box)
			scene.addProp(prop);
			
			if (type.equals("door") && !param01.equals(param02)) {
				scene = world.getScene(param02);
				scene.addProp(prop);
			}
		}
	}

	/**
	 * Metodo que lleva a cabo la ejecucion del juego por consola, mostrando los mensajes y llevando a cabo las acciones escogidas por el usuario.
	 * @param info (indica si se debe mostrar el mensaje y las opciones de la escena)
	 */
	private static void startGame(boolean info) {
		Scene scene;
		String dest, msj, type, param01, param02;
		boolean state, nextScene;
		Prop prop;
		ArrayList<Interaction> interactions;
		int input;
		
		scene = world.getScene(player.getCurrentScene());	// scene actual del jugador
		nextScene = true;
		
		msj = scene.getDescription();
		interactions = scene.getInteractions();
		
		/*
		 * imprimir el msj y opciones y leer la opcion elegida
		 */
		
		if (info) {	// solo imprime si info es true (para que no salgan continuamente el msj y las opciones)
			System.out.println("----------------------------------------------");
			System.out.println(msj);
			
			for (int i=0; i<interactions.size(); i++) {
				System.out.print("  " + i + ". ");
				System.out.println(interactions.get(i).getDescription());
			}
		}
		
		input = readInput(interactions.size());	// lectura de la opcion elegida
		
		/*
		 * aplicar la opcion elegida
		 */
		
		dest = interactions.get(input).getVariable();	// destinatario de la opcion elegida (door/box)
		
		prop = scene.getProp(dest);	// prop a modificar
		
		type = prop.getType();
		state = prop.getState();
		param01 = prop.getParam01();
		param02 = prop.getParam02();
		interactions = prop.getInteractions();
		
		if (type.equals("door")) {	// supuesto en el que el prop es una door
			if (state) {	// supuesto en el que la door esta abierta
				msj = "open";
				info = true;
				
				if (player.getCurrentScene().equals(param01)) {	// si la puerta esta abierta, se cambia al player de scene, a menos que la siguiente sea la final
					if (param02.equals(param01)) {	// caso especial en el que la puerta esta abierta pero no se puede cruzar
						info = false;
					} else {
						if (param02.equals("final")) {	// si la siguiente escena es la final, no se continua con el juego
							nextScene = false;
						}
						player.setCurrentScene(param02);
					}
				} else if (player.getCurrentScene().equals(param02)) {
					if (param01.equals(param02)) {	// caso especial en el que la puerta esta abierta pero no se puede cruzar
						info = false;
					} else {
						if (param01.equals("final")) {	// si la siguiente escena es la final, no se continua con el juego
							nextScene = false;
						}
						player.setCurrentScene(param01);
					}
				}
			} else {	// supuesto en el que la door esta cerrada
				msj = "closed";
				info = false;
			}
			
			for (int i=0; i<interactions.size(); i++) {	// se busca el msj entre las actions de la puerta y se coge el msj real
				if (interactions.get(i).getVariable().equals(msj)) {
					msj = interactions.get(i).getDescription();
					break;
				}
			}
		} else if (type.equals("box")) {	// supuesto en el que el prop es una box
			prop = world.getProp(param02);	// coge el prop que se debe cambiar (door a desbloquear)
			
			if (prop != null) {
				prop.openProp();	// se abre el prop cambiando su state a true
			}
			
			msj = interactions.get(0).getDescription();	// se coge el msj propio de la box
			info = false;
		}
		
		System.out.println(msj);
		
		if (nextScene) {
			startGame(info);
		} else {
			System.out.print(world.getScene(player.getCurrentScene()).getDescription());
		}
	}

	/**
	 * Metodo que lee por consola las ordenes introducidas por el usuario.
	 * @param options (limite superior del numero de opciones entre las que escoger)
	 * @return (retorna un integer con la opcion escogida por el usuario)
	 */
	private static int readInput(int options) {
		@SuppressWarnings("resource")
		java.util.Scanner in = new java.util.Scanner(System.in);
		int input;
		
		System.out.print("> ");
		
		try {
			input = in.nextInt();
			if (input < 0 || input >= options) throw new java.util.InputMismatchException();
			return input;
		} catch (java.util.InputMismatchException e) {	// se produce una excepcion si el valor introducido no es integer o no es un valor valido
			System.out.println("Error: debe introducirse un numero válido");
			return readInput(options);
		}
	}
}