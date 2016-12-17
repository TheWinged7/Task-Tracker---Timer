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
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		frame.getContentPane().setLayout(gridBagLayout);
		
		JToggleButton timerToggleButton = new JToggleButton("");
		timerToggleButton.setFocusable(false);
		timerToggleButton.setPreferredSize(new Dimension(50, 50));
		timerToggleButton.setSelectedIcon(new ImageIcon(MainWindow.class.getResource("/icons/buttons/pause.png")));
		timerToggleButton.setBorder(null);
		timerToggleButton.setContentAreaFilled(false);
		timerToggleButton.setBorderPainted(false);
		timerToggleButton.setIcon(new ImageIcon(MainWindow.class.getResource("/icons/buttons/play.png")));
		GridBagConstraints gbc_timerToggleButton = new GridBagConstraints();
		gbc_timerToggleButton.insets = new Insets(0, 0, 0, 5);
		gbc_timerToggleButton.gridx = 2;
		gbc_timerToggleButton.gridy = 2;
		frame.getContentPane().add(timerToggleButton, gbc_timerToggleButton);
		
		JButton timerLapButton = new JButton("");
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
		GridBagConstraints gbc_timerLapButton = new GridBagConstraints();
		gbc_timerLapButton.insets = new Insets(0, 0, 0, 5);
		gbc_timerLapButton.gridx = 3;
		gbc_timerLapButton.gridy = 2;
		frame.getContentPane().add(timerLapButton, gbc_timerLapButton);
	}

}
