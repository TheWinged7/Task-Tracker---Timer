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

//import com.google.gson.reflect.TypeToken;
import java.awt.SystemColor;


public class MainWindow {

	private JFrame frmTasksTimer;
	final static JTabbedPane taskTabs = new JTabbedPane(JTabbedPane.TOP);
	private static Gson gson = new Gson();
	private static ArrayList<Task>  tabs = new ArrayList<Task>();
	private static ArrayList<JLabel>  tabTimeLabels = new ArrayList<JLabel>();
	private static ArrayList<Timer> tabTimers = new ArrayList<Timer>();
	
	
	
	/**
	 * Launch the application.
	 */
	
	
	//check the title input by user to make sure its valid
	private static boolean checkTitle(String title)
	{
		//if title already exists, display error popup and return false
		for (int i=0; i<tabs.size(); i++)
		{
			if (title.equals( tabs.get(i).getTitle() ) )
			{
				JOptionPane.showMessageDialog(null, "ERROR:\nTask with that title already exists", "Title Error!", JOptionPane.WARNING_MESSAGE);
				return false;
			}
		}
		
		if (title == null  ) //if nothing entered, return false and do nothing
		{
			return false;
		}
		else if(title.length()<=0) //else if somehow not null but length is 0, display error popup and return false
		{
			JOptionPane.showMessageDialog(null, "ERROR:\nEmpty titles are not allowed", "Title Error!", JOptionPane.WARNING_MESSAGE);
			return false;
		}
		else if (!Pattern.matches(".*[\\w].*", title)) //if only non-standard characters, display error popup and return false
		{
			JOptionPane.showMessageDialog(null, "ERROR:\nTitle must contain at least one character other than space", "Title Error!", JOptionPane.WARNING_MESSAGE);
			return false;
		}
		/*
		else if(alreadyExists) //if the title already exists exactly, display error popup and return false
		{
			JOptionPane.showMessageDialog(null, "ERROR:\nTask with that title already exists", "Title Error!", JOptionPane.WARNING_MESSAGE);
			return false;
		}*/
		else //if none of the above are true, return true
		{
			return true;
		}
	}
	
	//action listener for timers that start ticking when that timer is running
	private static class timerActionListener implements ActionListener {
		int taskID;  //is used to match the actionListener to the task and timer label so only the correct running one is ticked
		
		public timerActionListener(int taskNo)
		{
			taskID = taskNo;
		}
		

		public void actionPerformed (ActionEvent e)
		{
			tabs.get(taskID).tick();
			tabTimeLabels.get(taskID).setText(tabs.get(taskID).totalTaskTime());
		}
		

	}
	
	//update current timerActionListener taskIDs so that they can continue to work after one has been deleted
	private static void updateTimerIDs ()
	{
		//Iterate through all timers
		for (int i=0; i< tabTimers.size(); i++)
		{
			ActionListener [] t = tabTimers.get(i).getActionListeners() ;  //get each ones list of action listeners
			boolean wasRunning= tabTimers.get(i).isRunning();  //check if the current task is running
			if (wasRunning)
				{
				tabTimers.get(i).stop(); //if it was, pause the timer while we update it
				}			
			//iterate through all action listeners for the task (should only be 1:1, but better to be safe)
			for (int j=0; j<t.length; j++)
			{
				 tabTimers.get(i).removeActionListener( t[0]) ; //delete each one, which as we delete one, the others become the first
				 												//meaning we want to delete at element 0 every time
			}
			 
			tabTimers.get(i).addActionListener(new timerActionListener(i)); //give the task a new actionListener with its now correct taskID
			
			if (wasRunning)
			{
				tabTimers.get(i).start(); //if the task was running when we started, start it running again
			}
		}
	}
	
	//add a new task in its own tab
	private static void addTab(final JTabbedPane tabbedPane, final String label, int h, int m, int s) {
					
			
			tabTimers.add(new Timer (100, new timerActionListener(tabs.size()) )); 	//add a new timer with its own timerActionListener to tabTimers
		 	tabs.add(new Task(label, h, m, s));										//create a new task with the specified label and time to tabs
		 	JPanel contentPanel = new JPanel();										//create a new JPanel to hold this task
		 	contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.X_AXIS));	//set its layout to boxLayout
		 	Box newContentBox = Box.createVerticalBox();							//create a new verticalBox to hold the main elements
			contentPanel.add(newContentBox);										//and add the verticalBox to the JPanel
		 	

						
			Box taskNameBox = Box.createHorizontalBox();							//create a horizontalBox to hold just the task name label
			newContentBox.add(taskNameBox);											//and add it to the verticalBox
			
			Component horizontalStrut_5 = Box.createHorizontalStrut(20);			//create and add spacing to the horizontalBox
			taskNameBox.add(horizontalStrut_5);
			
			JLabel taskLabel = new JLabel(label);									//create JLabel for task name and add it to the taskNameBox
			taskNameBox.add(taskLabel);
			taskLabel.setFont(new Font("Courier New", Font.BOLD, 20));				//set its font
			
			Component horizontalStrut = Box.createHorizontalStrut(20);				//create and add spacing to the other side of the JLabel
			taskNameBox.add(horizontalStrut);
			
			JButton taskCreateButton = new JButton("Delete Task");  				//create a button that is for deleting the current task
			taskCreateButton.addMouseListener(new MouseAdapter() {					//add the buttons action listener
				@Override
				public void mouseClicked(MouseEvent e) {
										
					for (int i=0; i< tabs.size(); i++) 								//iterate through all the tabs
					{	
						if (tabs.get(i).getTitle() == label ) 						//and if its label matches this ones
						{															//delete it from tabs, tabTimeLabels, tabTimers
							tabs.remove(i);											//and remove it from the tabBox too
							tabTimeLabels.remove(i);
							tabTimers.remove(i);
							taskTabs.removeTabAt(
									taskTabs.getSelectedIndex() );

							updateTimerIDs();										//and then update all taskTimers
							break;
						}
					}
					 
				}
			});
			taskNameBox.add(taskCreateButton);										//add the delete button to the taskNameBox
			
			Component horizontalGlue_1 = Box.createHorizontalGlue();
			taskNameBox.add(horizontalGlue_1);
			
			Box currentLapBox = Box.createHorizontalBox();
			newContentBox.add(currentLapBox);
			
			Component horizontalStrut_4 = Box.createHorizontalStrut(20);
			currentLapBox.add(horizontalStrut_4);
			
			final JToggleButton timerToggleButton = new JToggleButton("");
			currentLapBox.add(timerToggleButton);
			timerToggleButton.setFocusable(false);
			timerToggleButton.setPreferredSize(new Dimension(50, 50));
			timerToggleButton.setSelectedIcon(new ImageIcon(MainWindow.class.getResource("/icons/buttons/pause.png")));
			timerToggleButton.setBorder(null);
			timerToggleButton.setContentAreaFilled(false);
			timerToggleButton.setBorderPainted(false);
			timerToggleButton.setIcon(new ImageIcon(MainWindow.class.getResource("/icons/buttons/play.png")));
			
			timerToggleButton.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {

					if (timerToggleButton.isSelected())
					{
						for (int i=0; i< tabs.size(); i++)
						{
							
							if (tabs.get(i).getTitle() == label )
							{
								tabTimers.get(i).start();
								System.out.println("Starting Timer...");
							}
						}
					
					}
					else 
					{
						for (int i=0; i< tabs.size(); i++)
						{
							
							if (tabs.get(i).getTitle() == label )
							{
								tabTimers.get(i).stop();
								System.out.println("Stopping Timer...");
							}
						}
					}
					
				}
			}	);
			
						
			Component horizontalStrut_2 = Box.createHorizontalStrut(20);
			currentLapBox.add(horizontalStrut_2);
			
				
			tabTimeLabels.add(new JLabel(tabs.get(tabs.size()-1).totalTaskTime()));
			currentLapBox.add(tabTimeLabels.get( tabTimeLabels.size()-1));
			tabTimeLabels.get( tabTimeLabels.size()-1).setFont(new Font("Courier New", Font.BOLD, 25));
			
			Component horizontalGlue = Box.createHorizontalGlue();
			currentLapBox.add(horizontalGlue);
						
		    
		    tabbedPane.addTab(label , null, contentPanel, label);
		    int tab = tabbedPane.indexOfTab(label);
		    tabbedPane.setSelectedIndex(tab);
		    
		  }
	
	 


	
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

	

		private void LoadFile()
		{
			
			String homePath = System.getenv("APPDATA") + "\\TaskTimer";
			if (new File (homePath+"\\tasks.json").exists())
			{
				ArrayList<Task>  oldTabs;
				try{
					BufferedReader br = new BufferedReader 
								(new FileReader(homePath+"\\tasks.json") );
					
					
					oldTabs = gson.fromJson(br, new TypeToken<ArrayList<Task>>()
							{}.getType());


					for (int i=0; i<oldTabs.size(); i++)
					{
						System.out.println(oldTabs.get(i).getTitle() + "\t" + oldTabs.get(i).totalTaskTime());
						addTab(taskTabs, oldTabs.get(i).getTitle(), 
								oldTabs.get(i).getHours(), oldTabs.get(i).getMinutes(), oldTabs.get(i).getSeconds());
					}
					
					br.close();
					
					} catch(IOException err)
	        		{
		        		err.printStackTrace();
		        	}
				
				
				
				
					
			}
		}
	
	/**
	 * Initialize the contents of the frame.
	
	 *
	 */
	private void initialize() {
		
		
		
		CloseListener cl = new CloseListener("Are you sure you want to exit the application?",
			    "Exit Application");
		
		frmTasksTimer = new JFrame();
		frmTasksTimer.setIconImage(Toolkit.getDefaultToolkit().getImage(MainWindow.class.getResource("/icons/timer_transparent.png")));
		frmTasksTimer.setResizable(false);
		frmTasksTimer.getContentPane().setBackground(SystemColor.windowBorder);
		frmTasksTimer.setTitle("Tasks Timer");
		frmTasksTimer.setBounds(100, 100, 500, 350);
		frmTasksTimer.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frmTasksTimer.getContentPane().setLayout(new CardLayout(3, 3));
		
		frmTasksTimer.addWindowListener (cl);
		
		
		
		
		taskTabs.setBorder(null);
		taskTabs.setBackground(SystemColor.controlHighlight);
		taskTabs.setFont(new Font("Courier New", Font.PLAIN, 15));
		
		
		final JPanel TASK1 = new JPanel();
		TASK1.setBorder(null);
		
		
	    frmTasksTimer.getContentPane().add(taskTabs, "name_6289273657681");
		
		
		//this is the default tab, used only to add new tabs
		taskTabs.addTab("+ Task", null, TASK1, null);
		
		
		//this is the loading of old tabs 
		LoadFile();
		
		
		
		TASK1.setLayout(new BorderLayout(0, 0));
		
		JButton taskCreateButton = new JButton("New Task");
		taskCreateButton.setFont(new Font("Courier New", Font.BOLD, 50));
		taskCreateButton.setOpaque(false);
		taskCreateButton.setBorder(null);
		taskCreateButton.setContentAreaFilled(false);
		taskCreateButton.setFocusPainted(false);
		taskCreateButton.setFocusable(true);
		
		
		TASK1.add(taskCreateButton);
		taskCreateButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
				String title = (String) JOptionPane.showInputDialog("Please input task name", null);
				if (checkTitle(title))
				{
					addTab(taskTabs, title, 0, 0, 0);
					
					
				}

			}
		});
		frmTasksTimer.getContentPane().add(taskTabs, "name_6289273657681");
		
		
		
		
		final PopupMenu trayMenu = new PopupMenu();
		Image icon = Toolkit.getDefaultToolkit().getImage( MainWindow.class.getResource("/icons/timer.png"));
        final TrayIcon trayIcon = new TrayIcon( icon, 
        				"Task Timer" );  //add in image same as icon later
		
		
		final MenuItem currentTask = new MenuItem(taskTabs.getTitleAt( taskTabs.getSelectedIndex()));
		final MenuItem newTask = new MenuItem("New Task");
		
		currentTask.setEnabled(false);
		newTask.addActionListener(  new ActionListener() {
			
			public void actionPerformed (ActionEvent e) {
				String title = (String) JOptionPane.showInputDialog("Please input task name", null);
				if (checkTitle(title))
					{
						addTab(taskTabs, title, 0, 0, 0);
					}
			
				}
			}
				
				);
		

		trayMenu.add(newTask);
		
		
		trayIcon.setPopupMenu(trayMenu);
		trayIcon.setImageAutoSize(true);
		trayIcon.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
				if (frmTasksTimer.getState() == java.awt.Frame.NORMAL)
				{
					frmTasksTimer.toBack();
					frmTasksTimer.setState(java.awt.Frame.ICONIFIED);
				}
				else if (frmTasksTimer.getState() == java.awt.Frame.ICONIFIED)
				{
					frmTasksTimer.toFront();
					frmTasksTimer.repaint();
					frmTasksTimer.setState(java.awt.Frame.NORMAL);
				}
				

			}
		});
		
		if (SystemTray.isSupported())
		{
			final SystemTray tray = SystemTray.getSystemTray();
			try {
				tray.add(trayIcon);
			} 
		catch (AWTException e) {
            	System.out.println("TrayIcon could not be added.");
        	}
		}
		
		taskTabs.addChangeListener(new ChangeListener() {
			
			public void stateChanged (ChangeEvent e){
				if (taskTabs.getSelectedIndex() ==0)
				{
					String title = (String) JOptionPane.showInputDialog("Please input task name", null);
					if (checkTitle(title))
					{
						addTab(taskTabs, title, 0, 0, 0);
						
						
					}

				}
				else
				{
					currentTask.setLabel(taskTabs.getTitleAt( taskTabs.getSelectedIndex()));
				}
			}
			
		});
	
		
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {

	        public void run() {
	        	
	        	String homePath = System.getenv("APPDATA") + "\\TaskTimer";
	        	File dir = new File(homePath);
	        	if (!dir.exists())
	        	{
	        		try{
	        	        dir.mkdir();
	        	    } 
	        	    catch(SecurityException se){
	        	    	se.printStackTrace();
	        	    }        
	        	}
	        	try{
	        		File f = new File (homePath+"\\tasks.json");
	        		f.createNewFile();
	        		FileWriter fw = new FileWriter(f, false);
	        		gson.toJson(tabs, fw);
	        		fw.flush();
	        		fw.close();
	        	
	        	} catch(IOException err)
	        	{
	        		err.printStackTrace();
	        	}
	        }
	    }));
	
		
		
		
		
		
		
	}


}
