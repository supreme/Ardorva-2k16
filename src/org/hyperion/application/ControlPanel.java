package org.hyperion.application;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.hyperion.Server;
import org.hyperion.rs2.Constants;
import org.hyperion.rs2.model.World;
import org.hyperion.rs2.model.player.Player;
import org.hyperion.util.Language;

import javax.swing.DefaultListModel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListModel;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.JTextField;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * The control panel for the server.
 * @author Stephen Andrews
 */
public class ControlPanel extends JFrame {

	/**
	 * The main container for the application.
	 */
	private JPanel contentPane;
	
	/**
	 * The tabbed pane for the application.
	 */
	private JTabbedPane tabbedPane;
	
	/**
	 * The console for the application.
	 */
	private static JTextArea consoleArea;
	
	/**
	 * The label for the server's revision.
	 */
	private JLabel revisionLabel;
	
	/**
	 * The label for the server's uptime.
	 */
	private JLabel uptimeLabel;
	
	/**
	 * The label for the amount of players online.
	 */
	public static JLabel playersOnlineLabel;
	
	/**
	 * The label for the trades completed since the start of the server.
	 */
	private JLabel tradesCompletedLabel;
	
	/**
	 * The label for the duels completed since the start of the server.
	 */
	private JLabel duelsCompletedLabel;
	
	/**
	 * The list for the players online.
	 */
	public static DefaultListModel playersOnline;
	
	/**
	 * The panel for the main tab.
	 */
	private JPanel overviewPanel;
	
	/**
	 * The player tab.
	 */
	private JPanel playerPanel;
	
	/**
	 * The username of a selected player on the player tab.
	 */
	public static JTextField usernameField;

	/**
	 * Create the frame.
	 */
	public ControlPanel() {
		setTitle(Constants.SERVER_NAME);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 470, 341);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		overviewPanel = new JPanel();
		addComponents();
		overviewPanel.setLayout(null);
		
		JScrollPane consoleScrollPane = new JScrollPane();
		consoleScrollPane.setBounds(10, 155, 419, 99);
		overviewPanel.add(consoleScrollPane);
		
		consoleArea = new JTextArea();
		consoleScrollPane.setViewportView(consoleArea);
		
		revisionLabel = new JLabel("Revision: " + Server.VERSION);
		revisionLabel.setBounds(10, 11, 133, 14);
		overviewPanel.add(revisionLabel);
		
		uptimeLabel = new JLabel("Uptime: 1 hour");
		uptimeLabel.setBounds(10, 36, 133, 14);
		overviewPanel.add(uptimeLabel);
		
		playersOnlineLabel = new JLabel("Players online: 1");
		playersOnlineLabel.setBounds(10, 61, 133, 14);
		overviewPanel.add(playersOnlineLabel);
		
		tradesCompletedLabel = new JLabel("Trades completed: 1");
		tradesCompletedLabel.setBounds(10, 86, 133, 14);
		overviewPanel.add(tradesCompletedLabel);
		
		duelsCompletedLabel = new JLabel("Duels completed: 1");
		duelsCompletedLabel.setBounds(10, 111, 133, 14);
		overviewPanel.add(duelsCompletedLabel);
		
		JButton restartButton = new JButton("Restart Server");
		restartButton.setBounds(264, 7, 165, 36);
		overviewPanel.add(restartButton);
		
		JButton resetEntities = new JButton("Reset NPCs");
		resetEntities.setBounds(264, 57, 165, 36);
		overviewPanel.add(resetEntities);
		
		JLabel buildLabel = new JLabel("Build #" + Language.BUILD);
		buildLabel.setHorizontalAlignment(SwingConstants.CENTER);
		buildLabel.setBounds(264, 111, 165, 14);
		overviewPanel.add(buildLabel);
		
		playersOnline = new DefaultListModel();
		
		playerPanel = new JPanel();
		tabbedPane.addTab("Players", null, playerPanel, null);
		playerPanel.setLayout(null);
		
		JScrollPane playersOnlineScrollPane = new JScrollPane();
		playersOnlineScrollPane.setBounds(10, 11, 144, 243);
		playerPanel.add(playersOnlineScrollPane);
		
		final JList playersOnlineList = new JList(playersOnline);
		playersOnlineList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				System.out.println(playersOnlineList.getSelectedIndex());
				int index = playersOnlineList.getSelectedIndex() + 1;
				Player player = (Player) World.getWorld().getPlayers().get(index);
				PlayerData playerData = new PlayerData(player);
				playerData.getPlayerInfo();
			}
		});
		playersOnlineScrollPane.setViewportView(playersOnlineList);
		
		JLabel usernameLabel = new JLabel("Username:");
		usernameLabel.setBounds(164, 13, 76, 14);
		playerPanel.add(usernameLabel);
		
		usernameField = new JTextField();
		usernameField.setBounds(250, 10, 179, 20);
		playerPanel.add(usernameField);
		usernameField.setColumns(10);
	}
	
	/**
	 * Adds the components to the window.
	 */
	private void addComponents() {
		contentPane.add(tabbedPane, BorderLayout.CENTER);
		tabbedPane.addTab("Overview", null, overviewPanel, null);
	}
	
	public static JTextArea getConsole() {
		return consoleArea;
	}
}
