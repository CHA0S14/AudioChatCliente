package es.cios.audiochat.gui.listeners;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;

import es.cios.audiochat.servicios.AudioChatService;

public class MouseListenerPer extends MouseAdapter{
	
	
	

	@Override
	public void mousePressed(MouseEvent e) {
		Object obj = e.getSource();
		if (obj instanceof JTree){
			JTree tree = (JTree)obj;
			int row = tree.getRowForLocation(e.getX(), e.getY());
			tree.setSelectionRow(row);
		    if(row != -1) {
		        if(SwingUtilities.isRightMouseButton(e)) {		        	  
		        	DefaultMutableTreeNode node = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
		        	if(node.getAllowsChildren()){
		        		JPopupMenu popup = new JPopupMenu();
		        		
		        		if(node.getParent()==null || node.getParent().getParent()==null){
		        			JMenuItem crearSubCanal;
		        			
			        		if(node.getParent() == null){
			        			crearSubCanal = new JMenuItem("Crear canal");
			        		}else{
			        			crearSubCanal = new JMenuItem("Crear subcanal");
			        		}
			        		
			        		crearSubCanal.addActionListener(new ActionListenerPer());
			        		crearSubCanal.setActionCommand("crearSubCanal"); 		        		
			    			popup.add(crearSubCanal);
		        		}
		        		if(node.getParent() != null && !((String)node.getUserObject()).equals("C: Main chanel")){
			    			JMenuItem cambiarNombre = new JMenuItem("cambiar Nombre");
			    			cambiarNombre.addActionListener(new ActionListenerPer());
			    			cambiarNombre.setActionCommand("cambiarNombreCanal");
			    			popup.add(cambiarNombre);		    			
		        		}

		    			popup.show(tree, e.getX(), e.getY());
		        	}
		        }else{
		        	if(e.getClickCount()==2){
		        		AudioChatService.moverCanal();
		        	}
		        }
		    }
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}
}
