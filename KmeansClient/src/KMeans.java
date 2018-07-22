import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;


/**
 * /**
 * <code>KMeans</code> è una classe che permette di definire ed inizializzare
 * l'interfaccia grafica e le funzionalità del client, permettendo di
 * utilizzarle. Questa classe inoltre si occupa del lancio dell'applicazione.
 * Estende la classe {@link JFrame} perchè rappresenta essa stessa un JFrame.
 * @author Francesco Ventura
 *
 */
public class KMeans extends JFrame{

	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	private ObjectOutputStream out;

	/**
	 * 
	 */
	private ObjectInputStream in;

	/**
	 * 
	 */
	private Socket socket;

	private static String ADDR = null;

	private static int PORT = -1;


	/**
	 * 
	 * @param args
	 */
	public static void main (String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				if(args.length == 2) {
					
					if(!args[0].equals("")) 
						ADDR = args[0];						
					else
						System.out.println("[IP] del server non corretto.");		
					
					try {
						PORT = Integer.parseInt(args[1]);						
					}catch(NumberFormatException e) {	
						System.out.println("[PORTA] non valida.");
					}
					
					if(!(PORT == -1 || ADDR == null))					
						new KMeans().init();
					
				}else{
					System.out.println("[IP] [PORTA] del server mancanti.");
					System.out.println("Digitare localhost come IP per connettersi in locale.");
				}

			}
		});	
	}

	public void init(){			
		try{
			InetAddress addr = InetAddress.getByName(ADDR);			
			socket = new Socket(addr, PORT);

			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());

			Container cp = this.getContentPane();
			TabbedPane tb = new TabbedPane();

			tb.setLayout(new GridLayout(1, 1));
			cp.add(tb);

			this.pack();
			this.setMinimumSize(this.getSize());
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			this.setLocationByPlatform(true);
			this.setTitle("K-MEANS");
			this.setVisible(true);

		}catch(IOException e) {
			JOptionPane.showMessageDialog(null, "Impossibile Connettersi al Server!");
			this.setVisible(false);	
			try {
				if(socket != null) {
					out.close();
					in.close();
					socket.close();
				}
			}catch (IOException e1) {	
			}finally{
				System.exit(0);
			}
		}

	}

	private class TabbedPane extends JPanel{

		private static final long serialVersionUID = 1L;
		private JPanelCluster panelDB;
		private JPanelCluster panelFile;

		TabbedPane(){
			JTabbedPane tabbedPane = new JTabbedPane();

			panelDB = new JPanelCluster("MINE", new java.awt.event.ActionListener() {
				public void actionPerformed(ActionEvent event) {
					try{
						learningFromDBAction();
					}catch (SocketException e) {
						JOptionPane.showMessageDialog(panelDB, "Impossibile Connettersi al Server:\n" + e.getMessage());
					}catch (IOException e) {
						JOptionPane.showMessageDialog(panelDB, e.getMessage());
					}catch (ClassNotFoundException e) {
						JOptionPane.showMessageDialog(panelDB, e.getMessage());
					} catch (ServerException e) {
						JOptionPane.showMessageDialog(panelDB, e.getMessage());
					}
				}
			});

			panelFile = new JPanelCluster("STORE FROM FILE", new java.awt.event.ActionListener() {
				public void actionPerformed(ActionEvent event) {
					try{
						learningFromFileAction();
					}catch (SocketException e) {
						JOptionPane.showMessageDialog(panelFile, "Impossibile Connettersi al Server:\n" + e.getMessage());
					}catch (IOException e) {
						JOptionPane.showMessageDialog(panelFile, e.getMessage());
					}catch (ClassNotFoundException e) {
						JOptionPane.showMessageDialog(panelFile, e.getMessage());						
					}catch (ServerException e) {
						JOptionPane.showMessageDialog(panelFile, e.getMessage());
					}					
				}
			});

			tabbedPane.addTab("DB", panelDB);
			tabbedPane.addTab("FILE", panelFile);
			tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
			add(tabbedPane);
		}

		private void learningFromDBAction()throws ServerException, SocketException, IOException, ClassNotFoundException{
			int k = 0;
			String tableName = panelDB.tableText.getText();
			String result = "";

			if(tableName.equals("")){
				JOptionPane.showMessageDialog(this, "Inserire il nome della tabella!");
			}else {
				try{
					k = Integer.parseInt(panelDB.kText.getText());
				}catch(NumberFormatException e){
					JOptionPane.showMessageDialog(this, "Valore K inserito non corretto.");
					return;
				}

				out.writeObject(0);
				out.writeObject(tableName);

				result = (String)in.readObject();

				if(!result.equals("OK"))
					throw new ServerException(result);
				else{
					out.writeObject(1);
					out.writeObject(k);

					result = (String)in.readObject();

					if(!result.equals("OK")){
						throw new ServerException(result);
					}else{

						Integer iterate = (Integer) in.readObject();
						String res = (String) in.readObject();

						panelDB.clusterOutput.setText(res + "Numero iterate: " + iterate + "\n");

						out.writeObject(2);

						result = (String)in.readObject();

						if(!result.equals("OK"))
							throw new ServerException(result);
						else
							JOptionPane.showMessageDialog(this, "File correttamente salvato: " + tableName + iterate);						
					}
				}
			}

		}

		private void learningFromFileAction() throws ServerException, SocketException, IOException, ClassNotFoundException{
			String tableName = panelFile.tableText.getText(), 
					k = panelFile.kText.getText();

			if(tableName.equals("")){
				JOptionPane.showMessageDialog(this, "Errore! - Inserire il nome della tabella!");
			}else
				if(k.equals(""))
					JOptionPane.showMessageDialog(this, "Errore! - Inserire un valore per k!");
				else {	
					out.writeObject(3);

					out.writeObject(tableName);
					out.writeObject(Integer.parseInt(k));

					String result = (String)in.readObject();

					if(!result.equals("OK"))
						throw new ServerException(result);
					else 						
						panelFile.clusterOutput.setText((String) in.readObject());					
				}
		}

		private class JPanelCluster extends JPanel{

			private static final long serialVersionUID = 1L;

			private JTextField tableText = new JTextField(20);

			private JTextField kText = new JTextField(10);

			private JTextArea clusterOutput = new JTextArea();

			private JButton executeButton;

			JPanelCluster(String buttonName, java.awt.event.ActionListener a){
				super(new FlowLayout());

				JPanel main = new JPanel();
				main.setPreferredSize(new Dimension(450, 600));
				main.setLayout(new GridLayout(3,1));

				JPanel upPanel = new JPanel();			
				upPanel.add(new JLabel("Table"));
				upPanel.add(tableText);
				upPanel.add(new JLabel("k"));
				upPanel.add(kText);

				JPanel centralPanel = new JPanel();		
				JScrollPane outputScroll = new JScrollPane(clusterOutput);
				clusterOutput.setEditable(false);
				clusterOutput.setLineWrap(true);
				centralPanel.setLayout(new BorderLayout());				
				centralPanel.add(outputScroll, BorderLayout.CENTER);

				JPanel downPanel = new JPanel();
				executeButton = new JButton(buttonName);
				downPanel.setLayout(new FlowLayout());	
				downPanel.add(executeButton);

				main.add(upPanel, BorderLayout.NORTH);
				main.add(centralPanel, BorderLayout.CENTER);
				main.add(downPanel, BorderLayout.SOUTH);

				add(main);

				executeButton.addActionListener(a);
			}
		}
	}

}
