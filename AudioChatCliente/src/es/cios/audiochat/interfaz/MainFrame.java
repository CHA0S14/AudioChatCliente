package es.cios.audiochat.interfaz;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;

import es.cios.audiochat.entities.Canal;
import es.cios.audiochat.entities.Cliente;
import es.cios.audiochat.entities.SubCanal;
import es.cios.audiochat.interfaz.listeners.ActionListenerPer;
import es.cios.audiochat.interfaz.listeners.KeyListenerPer;
import es.cios.audiochat.servicios.AudioChatService;

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

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		AudioChatService.inciar();
	}

	/**
	 * Create the frame.
	 */
	public MainFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);

		JMenuBar menuBar = new JMenuBar();
		// TODO una menu bar en condiciones
		/*
		 * JMenu programa = new JMenu("Programa"); JMenu ayuda = new
		 * JMenu("Ayuda"); menuBar.add(programa); JMenuItem menuItem = new
		 * JMenuItem("hola"); programa.add(menuItem); menuBar.add(ayuda);
		 */
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

	private JTree crearJTree() {
		canales = new DefaultMutableTreeNode("Lista Canales");

		JTree tree = new JTree(canales);
		tree.setBorder(new LineBorder(new Color(0, 0, 0)));

		DefaultTreeCellRenderer renderer = (DefaultTreeCellRenderer) tree
				.getCellRenderer();

		renderer.setLeafIcon(null);
		renderer.setClosedIcon(null);
		renderer.setOpenIcon(null);

		return tree;
	}

	public void actualizarJTree() {
		this.canales=new DefaultMutableTreeNode("Lista Canales");
		List<Canal> canales = AudioChatService.getCanales();
		for (Canal canal : canales) {
			this.canal = new DefaultMutableTreeNode(canal.getName());
			this.canales.add(this.canal);
			List<Cliente> clientes = canal.getClientes();
			for (Cliente cliente : clientes) {
				this.cliente = new DefaultMutableTreeNode(cliente.getNombre());
				this.canal.add(this.cliente);
			}
			List<SubCanal> subCanales = canal.getSubCanales();
			for (SubCanal subCanal : subCanales) {
				this.subCanal = new DefaultMutableTreeNode(subCanal.getName());
				this.canal.add(this.subCanal);
				clientes = subCanal.getClientes();
				for (Cliente cliente : clientes) {
					this.cliente = new DefaultMutableTreeNode(
							cliente.getNombre());
					this.subCanal.add(this.cliente);
				}
			}
		}
		DefaultTreeModel model = new DefaultTreeModel(this.canales);
		tree.setModel(model);
	}

	public String getMensaje() {
		String texto = this.mensaje.getText();
		this.mensaje.setText("");
		return texto;
	}

	public void escribir(String text) {
		this.conversacion.append(text);
	}
}
