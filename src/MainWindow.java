import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.GridBagLayout;
import javax.swing.JButton;
import java.awt.GridBagConstraints;
import javax.swing.ImageIcon;
import javax.swing.UIManager;
import javax.swing.JToggleButton;
import java.awt.Insets;
import java.awt.Dimension;
import java.awt.Component;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTabbedPane;
import javax.swing.JPanel;
import java.awt.FlowLayout;
import java.awt.BorderLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import java.awt.CardLayout;
import javax.swing.SwingConstants;
import java.awt.GridLayout;
import net.miginfocom.swing.MigLayout;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.BoxLayout;
import javax.swing.Box;
import javax.swing.JScrollBar;
import javax.swing.JSeparator;
import javax.swing.JScrollPane;
import org.eclipse.wb.swing.FocusTraversalOnArray;
import javax.swing.SpringLayout;
import javax.swing.JToolBar;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JMenu;
import java.awt.Button;

public class MainWindow {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow window = new MainWindow();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 344);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new CardLayout(0, 0));
		
		JTabbedPane taskTabs = new JTabbedPane(JTabbedPane.TOP);
		taskTabs.setFont(new Font("Courier New", Font.PLAIN, 15));
		
		JPanel TASK1 = new JPanel();
		taskTabs.addTab("TASK NAME", null, TASK1, null);
		TASK1.setLayout(new BoxLayout(TASK1, BoxLayout.X_AXIS));
		
		Box contentBox = Box.createVerticalBox();
		TASK1.add(contentBox);
		
		Box taskNameBox = Box.createHorizontalBox();
		contentBox.add(taskNameBox);
		
		Component horizontalStrut_5 = Box.createHorizontalStrut(20);
		taskNameBox.add(horizontalStrut_5);
		
		JLabel taskLabel = new JLabel("TASK NAME");
		taskNameBox.add(taskLabel);
		taskLabel.setFont(new Font("Courier New", Font.BOLD, 20));
		
		Component horizontalStrut = Box.createHorizontalStrut(20);
		taskNameBox.add(horizontalStrut);
		
		JButton taskRenameButton = new JButton("Rename Task");
		taskNameBox.add(taskRenameButton);
		
		Component horizontalGlue_1 = Box.createHorizontalGlue();
		taskNameBox.add(horizontalGlue_1);
		
		Box currentLapBox = Box.createHorizontalBox();
		contentBox.add(currentLapBox);
		
		Component horizontalStrut_4 = Box.createHorizontalStrut(20);
		currentLapBox.add(horizontalStrut_4);
		
		JToggleButton timerToggleButton = new JToggleButton("");
		currentLapBox.add(timerToggleButton);
		timerToggleButton.setFocusable(false);
		timerToggleButton.setPreferredSize(new Dimension(50, 50));
		timerToggleButton.setSelectedIcon(new ImageIcon(MainWindow.class.getResource("/icons/buttons/pause.png")));
		timerToggleButton.setBorder(null);
		timerToggleButton.setContentAreaFilled(false);
		timerToggleButton.setBorderPainted(false);
		timerToggleButton.setIcon(new ImageIcon(MainWindow.class.getResource("/icons/buttons/play.png")));
		
		JButton timerLapButton = new JButton("");
		currentLapBox.add(timerLapButton);
		timerLapButton.setMaximumSize(new Dimension(50, 50));
		timerLapButton.setSize(new Dimension(50, 50));
		timerLapButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		timerLapButton.setFocusable(false);
		timerLapButton.setPreferredSize(new Dimension(50, 50));
		timerLapButton.setMinimumSize(new Dimension(50, 50));
		timerLapButton.setPressedIcon(new ImageIcon(MainWindow.class.getResource("/icons/buttons/lap_pressed.png")));
		timerLapButton.setOpaque(false);
		timerLapButton.setContentAreaFilled(false);
		timerLapButton.setBorderPainted(false);
		timerLapButton.setBorder(null);
		timerLapButton.setIcon(new ImageIcon(MainWindow.class.getResource("/icons/buttons/lap.png")));
		
		Component horizontalStrut_2 = Box.createHorizontalStrut(20);
		currentLapBox.add(horizontalStrut_2);
		
		JLabel taskTimer = new JLabel("HH:MM:SS");
		currentLapBox.add(taskTimer);
		taskTimer.setFont(new Font("Courier New", Font.BOLD, 25));
		
		Component horizontalGlue = Box.createHorizontalGlue();
		currentLapBox.add(horizontalGlue);
		
		Component verticalGlue_1 = Box.createVerticalGlue();
		contentBox.add(verticalGlue_1);
		
		JScrollPane scrollPane = new JScrollPane();
		contentBox.add(scrollPane);
		
		Box verticalBox = Box.createVerticalBox();
		scrollPane.setViewportView(verticalBox);
		
		Box horizontalBox = Box.createHorizontalBox();
		verticalBox.add(horizontalBox);
		
		Component horizontalStrut_6 = Box.createHorizontalStrut(20);
		horizontalBox.add(horizontalStrut_6);
		
		JLabel lapNumLabel = new JLabel("#");
		lapNumLabel.setFont(new Font("Courier New", Font.BOLD, 15));
		horizontalBox.add(lapNumLabel);
		
		Component horizontalStrut_3 = Box.createHorizontalStrut(20);
		horizontalBox.add(horizontalStrut_3);
		
		JLabel lapTime = new JLabel("HH:MM:SS");
		lapTime.setFont(new Font("Courier New", Font.BOLD, 15));
		horizontalBox.add(lapTime);
		
		Component horizontalGlue_2 = Box.createHorizontalGlue();
		horizontalBox.add(horizontalGlue_2);
		
		
		
		
		
		
	
		verticalBox.setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[]{horizontalBox, horizontalStrut_6, lapNumLabel, horizontalStrut_3, lapTime, horizontalGlue_2}));
		frame.getContentPane().add(taskTabs, "name_6289273657681");
		
		
		
		
	
		
		
		
		
		
	
	}

}
