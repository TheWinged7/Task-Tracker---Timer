import java.io.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.regex.Pattern;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import java.awt.SystemColor;

public class MainWindow {

	private JFrame frmTasksTimer;
	private static int previousSelectedTab = 0;
	final static JTabbedPane taskTabs = new JTabbedPane(JTabbedPane.TOP);
	private static Gson gson = new Gson();
	private static ArrayList<Task> tabs = new ArrayList<Task>();
	private static ArrayList<JLabel> tabTimeLabels = new ArrayList<JLabel>();
	private static ArrayList<Timer> tabTimers = new ArrayList<Timer>();
	private final static PopupMenu trayMenu = new PopupMenu();

	private static Image icon = Toolkit.getDefaultToolkit().getImage(MainWindow.class.getResource("/icons/timer.png"));
	final static TrayIcon trayIcon = new TrayIcon(icon, "Task Timer - \"No Tasks\"");

	/**
	 * Launch the application.
	 */

	// update tooltip for tray icon
	private static void updateToolTip(int tabID) {
		String toolTip = "";
			// check if the selected timer is running and start tooltip with start/stop appropriatley
		if (tabTimers.get(tabID).isRunning()) {
			toolTip += "Stop ";
		} else {
			toolTip += "Start ";
		}
		
		// add the task title and timer time at time of generation
		toolTip += tabs.get(tabID).getTitle() + ": " + tabs.get(tabID).totalTaskTime();
		
		// update trayicon tooltip with the new one
		trayIcon.setToolTip(toolTip);
	}

	// check the title input by user to make sure its valid
	private static boolean checkTitle(String title) {
		if (title == null) // if nothing entered, return false and do nothing
		{
			return false;
		} else if (title.length() <= 0) // else if somehow not null but lengthis 0,
										// display error popup and return false
		{
			JOptionPane.showMessageDialog(null, "ERROR:\nEmpty titles are not allowed", "Title Error!",
					JOptionPane.WARNING_MESSAGE);
			return false;
		} else if (!Pattern.matches(".*[\\w].*", title)) // if only non-standard characters,
															// display error popup and return false
		{
			JOptionPane.showMessageDialog(null, "ERROR:\nTitle must contain at least one character other than space",
					"Title Error!", JOptionPane.WARNING_MESSAGE);
			return false;
		} else
		{
			// if title already exists, display error popup and return false
			for (int i = 0; i < tabs.size(); i++) {
				if (title.equals(tabs.get(i).getTitle())) {
					JOptionPane.showMessageDialog(null, "ERROR:\nTask with that title already exists", "Title Error!",
							JOptionPane.WARNING_MESSAGE);
					return false;
				}
			}
			// if none of the above are true, return true
			return true;
		}
	}

	// action listener for timers that start ticking when that timer is running
	private static class timerActionListener implements ActionListener {
		int taskID; 		// used to match the actionListener to the task and timer
							// label so only the correct running one is ticked

		// get the taskID in the constructor
		public timerActionListener(int taskNo) {
			taskID = taskNo;
		}
		
		public void actionPerformed(ActionEvent e) {
			// each tick, use Task.tick() to increment the tasks timer
			tabs.get(taskID).tick();
			// and update tooltip and label of the task to the new time
			tabTimeLabels.get(taskID).setText(tabs.get(taskID).totalTaskTime());
			updateToolTip(taskID);
		}

	}

	// update current timerActionListener taskIDs so that they can continue to
	// work after one has been deleted
	private static void updateTimerIDs() {
		// Iterate through all timers
		for (int i = 0; i < tabTimers.size(); i++) {
			ActionListener[] t = tabTimers.get(i).getActionListeners(); // get each ones list of action listeners
			boolean wasRunning = tabTimers.get(i).isRunning(); // check if the current task is running
			if (wasRunning) {
				tabTimers.get(i).stop(); // if it was, pause the timer while we update it
			}
			// iterate through all action listeners for the task 
			// (should only be 1:1, but better to be safe)
			for (int j = 0; j < t.length; j++) {
				tabTimers.get(i).removeActionListener(t[0]); 	// delete each one, which as we delete one,
																// the others become the first
																// meaning we want to delete the element at 0 every time
			}

			tabTimers.get(i).addActionListener(new timerActionListener(i)); // give the task a new actionListener
																			// with its now correct taskID

			if (wasRunning) {
				tabTimers.get(i).start(); 	// if the task was running when we started,
											// start it running again
			}
		}
	}

	// add a new task in its own tab
	private static void addTab(final JTabbedPane tabbedPane, final String label, int h, int m, int s) {

		tabTimers.add(new Timer(100, new timerActionListener(tabs.size()))); // add a new timer with its own 
																			// timerActionListener to tabTimers
																				
		tabs.add(new Task(label, h, m, s)); // create a new task with the specified label and time to tabs
		JPanel contentPanel = new JPanel(); // create a new JPanel to hold this task
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.X_AXIS)); // set its layout to BoxLayour
																				
		Box newContentBox = Box.createVerticalBox(); 	// create a new verticalBox to hold the main elements

		contentPanel.add(newContentBox); // and add the verticalBox to the JPanel

		Box taskNameBox = Box.createHorizontalBox(); // create a horizontalBox to hold just the task name label
		newContentBox.add(taskNameBox); // and add it to the verticalBox

		Component horizontalStrut_5 = Box.createHorizontalStrut(20); // create and add spacing to the horizontalBox
		taskNameBox.add(horizontalStrut_5);
		
		JLabel taskLabel; // create JLabel for task name 
		
		// if label is long enough to need to wrap, setup wrapping in the JLabel using HTML
		if (label.length() >10)
		{
			String temp ="<html>";
			
			if (label.contains(" "))
			{
				String [] t = label.split(" ");
				for (int i=0; i< t.length; i++)
				{
					int l = 0;
					while (t[i].length() <10)
					{
						l++;
						t[i] += " " + t[i+l] ;
					}
					
					temp += t[i] + "<br>";
					i+=l;

				}
				temp += "</html>";
			}else 
			{
				for (int i=0; i<label.length(); i+=10 )
				{
					if (label.substring(i).length()<10 )
					{
						temp += label.substring(i) + "</html>";
					}else
					{
						temp += label.substring(i, i+10) +"<br>";
					}
				}
			}
			taskLabel = new JLabel(temp);
		}
		else // else just use the label as entered
		{
			taskLabel = new JLabel(label);
		}
		
		taskNameBox.add(taskLabel); // and add the task label to the taskNameBox
		taskLabel.setFont(new Font("Courier New", Font.BOLD, 20)); // set its font

		Component horizontalStrut = Box.createHorizontalStrut(20); // create and add spacing to the other side of the JLabel
		taskNameBox.add(horizontalStrut);

		JButton taskDeleteButton = new JButton("Delete Task"); // create a button that is for deleting the current task
		taskDeleteButton.addMouseListener(new MouseAdapter() { // add the buttons action listener
			@Override
			public void mouseClicked(MouseEvent e) {

				for (int i = 0; i < tabs.size(); i++) // iterate through all the tabs
				{
					if (tabs.get(i).getTitle() == label)	// and if its label matches this ones
					{ 										// delete it from tabs, tabTimeLabels, tabTimers
						tabs.remove(i); 					// and remove it from the tabBox too
						tabTimeLabels.remove(i);
						tabTimers.remove(i);
						taskTabs.removeTabAt(taskTabs.getSelectedIndex());
						updateTimerIDs(); 					// and then update all taskTimers
						break;
					}
				}

			}
		});
		taskNameBox.add(taskDeleteButton); // add the delete button to the taskNameBox

		Component horizontalGlue_1 = Box.createHorizontalGlue();
		taskNameBox.add(horizontalGlue_1);

		Box currentLapBox = Box.createHorizontalBox();
		newContentBox.add(currentLapBox);

		Component horizontalStrut_4 = Box.createHorizontalStrut(20);
		currentLapBox.add(horizontalStrut_4);

		final JToggleButton timerToggleButton = new JToggleButton(""); 	// create start/stop toggle button
		currentLapBox.add(timerToggleButton); 							// add it to the tab
		// and then set all of the settings for the button
		timerToggleButton.setFocusable(false);
		timerToggleButton.setPreferredSize(new Dimension(50, 50));
		timerToggleButton.setBorder(null);
		timerToggleButton.setContentAreaFilled(false);
		timerToggleButton.setBorderPainted(false);
		// set its running/stopped button icons
		timerToggleButton.setSelectedIcon(new ImageIcon(MainWindow.class.getResource("/icons/buttons/pause.png")));
		timerToggleButton.setIcon(new ImageIcon(MainWindow.class.getResource("/icons/buttons/play.png")));

		// setup buttons mouseListener and add it
		timerToggleButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// if timer is stopped, start it
				if (timerToggleButton.isSelected()) {
					for (int i = 0; i < tabs.size(); i++) {

						if (tabs.get(i).getTitle() == label) {
							tabTimers.get(i).start();
						}
					}
				// if timer is running, stop it
				} else {
					for (int i = 0; i < tabs.size(); i++) {

						if (tabs.get(i).getTitle() == label) {
							tabTimers.get(i).stop();
						}
					}
				}

			}
		});

		Component horizontalStrut_2 = Box.createHorizontalStrut(20);
		currentLapBox.add(horizontalStrut_2);
		
		// create label to display task timer, and add it to tabTimeLabels and to the JPanel
		tabTimeLabels.add(new JLabel(tabs.get(tabs.size() - 1).totalTaskTime()));
		currentLapBox.add(tabTimeLabels.get(tabTimeLabels.size() - 1));
		// set font for the JLabel
		tabTimeLabels.get(tabTimeLabels.size() - 1).setFont(new Font("Courier New", Font.BOLD, 25));

		Component horizontalGlue = Box.createHorizontalGlue();
		currentLapBox.add(horizontalGlue);

		// finally add the JPanel to the tabs
		tabbedPane.addTab(label, null, contentPanel, label);
		int tab = tabbedPane.indexOfTab(label); // get its index,
		previousSelectedTab = tab;				// set the last focused tab to it for 
												// if creating a tab is cancelled
		tabbedPane.setSelectedIndex(tab);		// and set it to be the focused tab

	}
	// *** end add tab ***
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow window = new MainWindow();
					window.frmTasksTimer.setVisible(true);
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

	
	private void LoadFile() {
		
		// create tray icon menu item to add more tabs at any time
		final MenuItem newTask = new MenuItem("New Task");
		// and set its ActionListener to call up the input popup, same as the default tab
		newTask.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				String title = (String) JOptionPane.showInputDialog("Please input task name", null);
				if (checkTitle(title)) {
					addTab(taskTabs, title, 0, 0, 0);
				}

			}
		}

		);
		
		// and add it to the tray menu
		trayMenu.add(newTask);

		// check if the JSon file we save to exists
		String homePath = System.getenv("APPDATA") + "\\TaskTimer";
		if (new File(homePath + "\\tasks.json").exists()) {
				// and if it does
			ArrayList<Task> oldTabs;
			try {
				// attempt to load it into a BufferedReader
				BufferedReader br = new BufferedReader(new FileReader(homePath + "\\tasks.json"));
				// before using GSON to change it from JSon into our ArrayList oldTabs
				oldTabs = gson.fromJson(br, new TypeToken<ArrayList<Task>>() {
				}.getType());
				
				// then iterate through oldTabs
				for (int i = 0; i < oldTabs.size(); i++) {
					// add each loaded task
					addTab(taskTabs, oldTabs.get(i).getTitle(), oldTabs.get(i).getHours(), oldTabs.get(i).getMinutes(),
							oldTabs.get(i).getSeconds());
					// stop, start, and then stop the timer again to make the button work initially 
					// ( Don't know why the button doesn't work on the first attempt to start it, but this fixes it )
					tabTimers.get(i).stop();
					tabTimers.get(i).start();
					tabTimers.get(i).stop();
					// and finally update the toolTip, whichever one is last loaded will be the one displayed
					updateToolTip(i);
				}
				 	// before finally closing our BufferedReader
				br.close();
					// and emptying old tabs, so that java handles less of the cleanup
				oldTabs.clear();
			} catch (IOException err) { 
				JOptionPane.showMessageDialog(null, "ERROR:\nProblem loading file:\n" + err.toString(), "File Loading Error!",
						JOptionPane.WARNING_MESSAGE);
			}

		}
	}

	/**
	 * Initialize the contents of the frame.
	 *
	 * 
	 */
	private void initialize() {
		// set a CloseListener to stop accidental closing of application
		CloseListener cl = new CloseListener("Are you sure you want to exit the application?", "Exit Application");

		// setup the JFrame holding everything
		frmTasksTimer = new JFrame();
		frmTasksTimer.setIconImage(	Toolkit.getDefaultToolkit().
									getImage(MainWindow.class.getResource("/icons/timer_transparent.png")));
		frmTasksTimer.setResizable(false);
		frmTasksTimer.getContentPane().setBackground(SystemColor.windowBorder);
		frmTasksTimer.setTitle("Tasks Timer");
		frmTasksTimer.setBounds(100, 100, 500, 350);
		frmTasksTimer.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frmTasksTimer.getContentPane().setLayout(new CardLayout(3, 3));

		// and add our CloseListener to it as a windowListener
		frmTasksTimer.addWindowListener(cl);

		// before setting up our tabbedPane
		taskTabs.setBorder(null);
		taskTabs.setBackground(SystemColor.controlHighlight);
		taskTabs.setFont(new Font("Courier New", Font.PLAIN, 15));

		// create our default tab JPanel that is used to add a task
		final JPanel TASK1 = new JPanel();
		TASK1.setBorder(null);

		
	

		// and then add the default tab into our tabbedPane
		taskTabs.addTab("+ Task", null, TASK1, null);

		// load any tabs from the previous session(s)
		LoadFile();
		
		//and setup our default tab
		TASK1.setLayout(new BorderLayout(0, 0));

		//create a button to fill the entire area of the default tab,  and do the setup of it
		JButton taskCreateButton = new JButton("New Task");
		taskCreateButton.setFont(new Font("Courier New", Font.BOLD, 50));
		taskCreateButton.setOpaque(false);
		taskCreateButton.setBorder(null);
		taskCreateButton.setContentAreaFilled(false);
		taskCreateButton.setFocusPainted(false);
		taskCreateButton.setFocusable(true);

		// add the button to the default tab
		TASK1.add(taskCreateButton);
		//and set its mouseListener to add a tab
		taskCreateButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {

				String title = (String) JOptionPane.showInputDialog("Please input task name", null);
				if (checkTitle(title)) {
					addTab(taskTabs, title, 0, 0, 0);

				}

			}
		});
		
		// add our tabbedPane to our JFrame
		frmTasksTimer.getContentPane().add(taskTabs, "name_6289273657681");

		
		trayIcon.setPopupMenu(trayMenu); // set the trayIcons right-click menu
		trayIcon.setImageAutoSize(true); // and set it to autoSize, so we dont get a wonky icon
		// before adding its mouseListener
		trayIcon.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// if double left-clicked
				if (e.getClickCount() == 2 && !e.isConsumed() && SwingUtilities.isLeftMouseButton(e)) {
					e.consume();
					// if shrunk to task tray return to normal, and visa versa
					if (frmTasksTimer.getState() == java.awt.Frame.NORMAL ) {
						frmTasksTimer.toBack();
						frmTasksTimer.setState(java.awt.Frame.ICONIFIED);
					} else 
					if (frmTasksTimer.getState() == java.awt.Frame.ICONIFIED) {
						frmTasksTimer.toFront();
						frmTasksTimer.repaint();
						frmTasksTimer.setState(java.awt.Frame.NORMAL);
					}
				} else 
				// if single left-click, stop timer if running, and start timer if it is stopped
				if (tabs.size() > 0) {
					if (tabTimers.get(previousSelectedTab - 1).isRunning()) {
						tabTimers.get(previousSelectedTab - 1).stop();
					} else {
						tabTimers.get(previousSelectedTab - 1).start();
					}
				// before updating the toolTip
					updateToolTip(previousSelectedTab - 1);
				}

			}

		});

		// the above sets up for adding system tray, but if it is not supported, then it never gets used
		// however, if it is supported
		if (SystemTray.isSupported()) {
			// setup a SystemTray
			final SystemTray tray = SystemTray.getSystemTray();
			try {
				// and attempt to add our idon to the tray
				tray.add(trayIcon);
			} catch (AWTException e) {
				JOptionPane.showMessageDialog(null, "ERROR:\nTrayIcon could not be added.", "Tray Icon Error!",
						JOptionPane.WARNING_MESSAGE);
			}
		}

		//add a change listener to check if the tab is changed to the default one by the user
		
		taskTabs.addChangeListener(new ChangeListener() {
			
			public void stateChanged(ChangeEvent e) {
				// so long as there exists at least one tab,
				if (taskTabs.getSelectedIndex() == 0 && tabs.size() > 0) {
					// Automatically prompt user for name of a task
					String title = (String) JOptionPane.showInputDialog("Please input task name", null);
					// and if it is valid, create the tab
					if (checkTitle(title)) {
						addTab(taskTabs, title, 0, 0, 0);
					} else { //if it is invalid,
						if (tabs.size() > 0) { //and if there is at least one task already created
							taskTabs.setSelectedIndex(previousSelectedTab); //switch back to the last focused tab
						} else { //otherwise, stay on the default tab
							taskTabs.setSelectedIndex(0);
						}
					}
				} else {
					if (tabs.size() > 0) {
						previousSelectedTab = taskTabs.getSelectedIndex();
						updateToolTip(previousSelectedTab - 1);
					}
				}
			}

		});

		//at shutdown, save tasks to a JSon file
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {

			public void run() {

				String homePath = System.getenv("APPDATA") + "\\TaskTimer";
				File dir = new File(homePath);
				if (!dir.exists()) {
					try {
						dir.mkdir();
					} catch (SecurityException se) {
						se.printStackTrace();
					}
				}
				try {
					File f = new File(homePath + "\\tasks.json");
					f.createNewFile();
					FileWriter fw = new FileWriter(f, false);
					gson.toJson(tabs, fw);
					fw.flush();
					fw.close();

				} catch (IOException err) {
					JOptionPane.showMessageDialog(null, "ERROR:\nProblem saving to file:\n" + err.toString(), "File Save Error!",
							JOptionPane.WARNING_MESSAGE);
				}
			}
		}));

	}

}
