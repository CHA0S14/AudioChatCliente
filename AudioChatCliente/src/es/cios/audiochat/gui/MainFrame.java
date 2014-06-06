package es.cios.audiochat.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreeSelectionModel;

import es.cios.audiochat.entities.Canal;
import es.cios.audiochat.entities.Cliente;
import es.cios.audiochat.entities.SubCanal;
import es.cios.audiochat.gui.listeners.ActionListenerPer;
import es.cios.audiochat.gui.listeners.KeyListenerPer;
import es.cios.audiochat.gui.listeners.MouseListenerPer;
import es.cios.audiochat.gui.listeners.WindowListenerPer;
import es.cios.audiochat.servicios.AudioChatService;
/**
 * 
 * @author Chaos
 *
 */
@SuppressWarnings("serial")
public class MainFrame extends JFrame {

	private JPanel contentPane;
	private JTextField mensaje;
	private DefaultMutableTreeNode canales = null;
	private DefaultMutableTreeNode canal = null;
	private DefaultMutableTreeNode subCanal = null;
	private DefaultMutableTreeNode cliente = null;
	private JTree tree;
	private JTextArea conversacion;
	
	public JTree getTree() {
		return tree;
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try{
			AudioChatService.inciar();
		}catch(Exception e){
			new JOptionPane(e.getMessage());
			AudioChatService.finalizar();
		}
	}

	/**
	 * Create the frame.
	 */
	public MainFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		
		this.addWindowListener(new WindowListenerPer());

		JMenuBar menuBar = new JMenuBar();		
		JMenu programa = new JMenu("Editar"); 
		JMenuItem changeName = new JMenuItem("Cambiar Nombre");
		menuBar.add(programa); 
		//JMenu menuItem = new JMenuItem("hola"); 
		//programa.add(menuItem); 
		programa.add(changeName);
		
		changeName.addActionListener(new ActionListenerPer());
		changeName.setActionCommand("cambiarNombre");
		 
		setJMenuBar(menuBar);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));

		JPanel panelCentral = new JPanel();
		contentPane.add(panelCentral, BorderLayout.CENTER);
		panelCentral.setLayout(new BoxLayout(panelCentral, BoxLayout.X_AXIS));

		JPanel mainPanel = new JPanel();
		panelCentral.add(mainPanel);
		mainPanel.setLayout(new BorderLayout());

		tree = crearJTree();
		tree.setToggleClickCount(0);

		actualizarJTree();

		mainPanel.add(tree);

		JPanel chatPanel = new JPanel();
		panelCentral.add(chatPanel);
		chatPanel.setBorder(new LineBorder(new Color(0, 0, 0)));
		chatPanel.setLayout(new BorderLayout(0, 0));

		JPanel panelEscribir = new JPanel();
		chatPanel.add(panelEscribir, BorderLayout.SOUTH);
		panelEscribir.setLayout(new BoxLayout(panelEscribir, BoxLayout.X_AXIS));
		
		JButton btnGrabar = new JButton("Grabar");
		btnGrabar.addActionListener(new ActionListenerPer());
		btnGrabar.setActionCommand("grabar");
		panelEscribir.add(btnGrabar);

		mensaje = new JTextField();
		panelEscribir.add(mensaje);
		mensaje.setColumns(10);
		mensaje.addKeyListener(new KeyListenerPer());

		JButton btnEnviar = new JButton("Enviar");
		btnEnviar.addActionListener(new ActionListenerPer());
		btnEnviar.setActionCommand("enviar");
		panelEscribir.add(btnEnviar);

		conversacion = new JTextArea();
		chatPanel.add(conversacion, BorderLayout.CENTER);
		conversacion.setEditable(false);	
	}

	/**
	 * da forma inicial al jTree
	 * @return el JTree
	 */
	private JTree crearJTree() {
		canales = new DefaultMutableTreeNode("Lista Canales");

		JTree tree = new JTree(canales);
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		tree.setBorder(new LineBorder(new Color(0, 0, 0)));
		tree.addMouseListener(new MouseListenerPer());

		DefaultTreeCellRenderer renderer = (DefaultTreeCellRenderer) tree
				.getCellRenderer();

		renderer.setLeafIcon(null);
		renderer.setClosedIcon(null);
		renderer.setOpenIcon(null);

		return tree;
	}

	/**
	 * actualiza los datos del JTree
	 */
	public void actualizarJTree() {
		this.canales=new DefaultMutableTreeNode("Lista Canales");
		List<Canal> canales = AudioChatService.getCanales();
		for (Canal canal : canales) {
			this.canal = new DefaultMutableTreeNode(canal.getName());
			this.canales.add(this.canal);
			List<Cliente> clientes = canal.getClientes();
			for (Cliente cliente : clientes) {
				this.cliente = new DefaultMutableTreeNode(cliente.getNombre(),false);
				this.canal.add(this.cliente);
			}
			List<SubCanal> subCanales = canal.getSubCanales();
			for (SubCanal subCanal : subCanales) {
				this.subCanal = new DefaultMutableTreeNode(subCanal.getName());
				this.canal.add(this.subCanal);
				clientes = subCanal.getClientes();
				for (Cliente cliente : clientes) {
					this.cliente = new DefaultMutableTreeNode(
							cliente.getNombre(), false);
					this.subCanal.add(this.cliente);
				}
			}
		}
		DefaultTreeModel model = new DefaultTreeModel(this.canales);
		tree.setModel(model);
	}

	/**
	 * obtiene el mensaje que ha escrito el usuario
	 * @return el mensaje
	 */
	public String getMensaje() {
		String texto = this.mensaje.getText();
		this.mensaje.setText("");
		return texto;
	}

	/**
	 * escribe el mensaje recibido en pantalla
	 * @param text mensaje recibido
	 */
	public void escribir(String text) {
		this.conversacion.append(text);
	}
	
	/**
	 * devuelve el nodo del JTree marcado
	 * @return posicion en formato "x:y"
	 */
	public String getSelectedNodePosition(){
		DefaultMutableTreeNode node = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
		String position = getNodeIndex(tree, node); 
		return position;
	}
	
	/**
	 * devuelve el nodo del JTree marcado
	 * @param tree arbol de referencia
	 * @param node nodo al cual sacar el index
	 * @return posicion en formato "x:y"
	 */
	private String getNodeIndex(JTree tree, TreeNode node) {
	    TreeNode root = (TreeNode) tree.getModel().getRoot();
	    if (node == root) {
	        return "";
	    }
	    TreeNode parent = node.getParent();
	    if (parent == null) {
	        return null;
	    }
	    String parentIndex= getNodeIndex(tree, parent);
	    if (parentIndex == null) {
	        return null;
	    }
	    return parentIndex+":"+parent.getIndex(node);
	}
}
