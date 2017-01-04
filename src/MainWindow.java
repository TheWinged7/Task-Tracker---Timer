import java.io.*;
import javax.swing.*;
import java.awt.EventQueue;
import java.awt.Dimension;
import java.awt.Component;
import java.awt.Font;
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.regex.Pattern;
import com.google.gson.*;
//import com.google.gson.reflect.TypeToken;
import java.awt.SystemColor;


public class MainWindow {

	private JFrame frmTasksTimer;
	private static Gson gson = new Gson();
	private static ArrayList<Task>  tabs = new ArrayList<Task>();
	private static ArrayList<JLabel>  tabTimeLabels = new ArrayList<JLabel>();
	
	//put this in the addTab part so each tab has its own timer
	private Timer timer = new Timer (1000, new timerActionListener(0) );
	
	/**
	 * Launch the application.
	 */
	
	
	
	private static boolean checkTitle(String title)
	{
		
		
		if (title == null  )
		{
			return false;
		}
		else if(title.length()<=0){
			JOptionPane.showMessageDialog(null, "ERROR:\nEmpty titles are not allowed", "Title Error!", JOptionPane.WARNING_MESSAGE);
			return false;
		}
		else if (!Pattern.matches(".*[\\w].*", title))
		{
			JOptionPane.showMessageDialog(null, "ERROR:\nTitle must contain at least one character other than space", "Title Error!", JOptionPane.WARNING_MESSAGE);
			return false;
		}
		else if(tabs.contains(title))
		{
			JOptionPane.showMessageDialog(null, "ERROR:\nTask with that title already exists", "Title Error!", JOptionPane.WARNING_MESSAGE);
			return false;
		}
		else
		{
			return true;
		}
	}
	
	private static void addTab(final JTabbedPane tabbedPane, String label, int h, int m, int s) {
		 	tabs.add(new Task(label, h, m, s));
		 	JPanel contentPanel = new JPanel();
		 	contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.X_AXIS));
		 	Box newContentBox = Box.createVerticalBox();
			contentPanel.add(newContentBox);
		 	

						
			Box taskNameBox = Box.createHorizontalBox();
			newContentBox.add(taskNameBox);
			
			Component horizontalStrut_5 = Box.createHorizontalStrut(20);
			taskNameBox.add(horizontalStrut_5);
			
			JLabel taskLabel = new JLabel(label);
			taskNameBox.add(taskLabel);
			taskLabel.setFont(new Font("Courier New", Font.BOLD, 20));
			
			Component horizontalStrut = Box.createHorizontalStrut(20);
			taskNameBox.add(horizontalStrut);
			
			JButton taskCreateButton = new JButton("New Task");
			taskCreateButton.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					
					String title = (String) JOptionPane.showInputDialog("Please input task name", null);
					if (checkTitle(title))
					{
						addTab(tabbedPane, title, 0,0,0);
					}

				}
			});
			taskNameBox.add(taskCreateButton);
			
			Component horizontalGlue_1 = Box.createHorizontalGlue();
			taskNameBox.add(horizontalGlue_1);
			
			Box currentLapBox = Box.createHorizontalBox();
			newContentBox.add(currentLapBox);
			
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
			
				
			tabTimeLabels.add(new JLabel(tabs.get(0).totalTaskTime()));
			currentLapBox.add(tabTimeLabels.get( tabTimeLabels.size()-1));
			tabTimeLabels.get( tabTimeLabels.size()-1).setFont(new Font("Courier New", Font.BOLD, 25));
			
			Component horizontalGlue = Box.createHorizontalGlue();
			currentLapBox.add(horizontalGlue);
			
			Component verticalGlue_1 = Box.createVerticalGlue();
			newContentBox.add(verticalGlue_1);
			
			JScrollPane scrollPane = new JScrollPane();
			newContentBox.add(scrollPane);
			
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
			
			JLabel lapTime = new JLabel(tabs.get(0).laps.get(0).lapToString());
			lapTime.setFont(new Font("Courier New", Font.BOLD, 15));
			horizontalBox.add(lapTime);
			
			Component horizontalGlue_2 = Box.createHorizontalGlue();
			horizontalBox.add(horizontalGlue_2);
			
		    
		    tabbedPane.addTab(label , null, contentPanel, label);
		    int tab = tabbedPane.indexOfTab(label);
		    tabbedPane.setSelectedIndex(tab);
		    
		  }
	
	 
	private class timerActionListener implements ActionListener {
		int taskID;
		
		public timerActionListener(int taskNo)
		{
			taskID = taskNo;
		}
		
		public void actionPerformed (ActionEvent e)
		{
			tabs.get(taskID).tick();
			
		//	taskTimer.setText(tabs.get(taskID).totalTaskTime());
			tabTimeLabels.get(taskID).setText(tabs.get(taskID).totalTaskTime());
			System.out.println("tick\t"+taskID);
			System.out.println("Total:\t" + tabs.get(taskID).totalTaskTime() );
			for (int i=0; i< tabs.get(taskID).getNoLaps(); i++)
				System.out.println("Lap " + i + ":\t" +tabs.get(taskID).lapToString(i) );
		}
		

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

	

	
	
	/**
	 * Initialize the contents of the frame.
	
	 *
	 */
	private void initialize() {
		
		tabs.add(new Task("foo!", 3,5,21));
		
		CloseListener cl = new CloseListener("Are you sure you want to exit the application?",
			    "Exit Application");
		
		frmTasksTimer = new JFrame();
		frmTasksTimer.setResizable(false);
		frmTasksTimer.getContentPane().setBackground(SystemColor.windowBorder);
		frmTasksTimer.setTitle("Tasks Timer");
		frmTasksTimer.setBounds(100, 100, 500, 350);
		frmTasksTimer.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frmTasksTimer.getContentPane().setLayout(new CardLayout(3, 3));
		
		frmTasksTimer.addWindowListener (cl);
		
		
		
		final JTabbedPane taskTabs = new JTabbedPane(JTabbedPane.TOP);
		taskTabs.setBorder(null);
		taskTabs.setBackground(SystemColor.controlHighlight);
		taskTabs.setFont(new Font("Courier New", Font.PLAIN, 15));
		
		
		final JPanel TASK1 = new JPanel();
		TASK1.setBorder(null);
		
		
		//temp design code here
		
		
	 	TASK1.setLayout(new BoxLayout(TASK1, BoxLayout.X_AXIS));
	 	Box newContentBox = Box.createVerticalBox();
	 	newContentBox.setBackground(SystemColor.menu);
	 	TASK1.add(newContentBox);
	 	

					
		Box taskNameBox = Box.createHorizontalBox();
		newContentBox.add(taskNameBox);
		
		Component horizontalStrut_5 = Box.createHorizontalStrut(20);
		taskNameBox.add(horizontalStrut_5);
		
		JLabel taskLabel = new JLabel(tabs.get(0).title);
		taskNameBox.add(taskLabel);
		taskLabel.setFont(new Font("Courier New", Font.BOLD, 25));
		
		Component horizontalGlue_1 = Box.createHorizontalGlue();
		taskNameBox.add(horizontalGlue_1);
		
		Box currentLapBox = Box.createHorizontalBox();
		newContentBox.add(currentLapBox);
		
		Component horizontalStrut_4 = Box.createHorizontalStrut(20);
		currentLapBox.add(horizontalStrut_4);
		
		final JToggleButton timerToggleButton = new JToggleButton("");
		timerToggleButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {

				if (timerToggleButton.isSelected())
				{
					timer.start();
					System.out.println("Starting Timer...");
				}
				else 
				{
					timer.stop();
					System.out.println("Stopping Timer...");
				}
				
			}
		});
		
		
		
		
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
		
		tabTimeLabels.add(new JLabel(tabs.get(0).totalTaskTime()));
		currentLapBox.add(tabTimeLabels.get( tabTimeLabels.size()-1));
		tabTimeLabels.get( tabTimeLabels.size()-1).setFont(new Font("Courier New", Font.BOLD, 25));
		
		Component horizontalGlue = Box.createHorizontalGlue();
		currentLapBox.add(horizontalGlue);
		
		Component verticalGlue_1 = Box.createVerticalGlue();
		newContentBox.add(verticalGlue_1);
		
		JScrollPane scrollPane = new JScrollPane();
		newContentBox.add(scrollPane);
		
		Box verticalBox = Box.createVerticalBox();
		scrollPane.setViewportView(verticalBox);
		
		final Box horizontalBox = Box.createHorizontalBox();
		verticalBox.add(horizontalBox);
		
		Component horizontalStrut_6 = Box.createHorizontalStrut(20);
		horizontalBox.add(horizontalStrut_6);
		
		JLabel lapNumLabel = new JLabel(Integer.toBinaryString( tabs.get(0).laps.get(0).getLapNumber() ) );
		lapNumLabel.setFont(new Font("Courier New", Font.BOLD, 15));
		horizontalBox.add(lapNumLabel);
		
		Component horizontalStrut_3 = Box.createHorizontalStrut(20);
		horizontalStrut_3.setFocusable(false);
		horizontalBox.add(horizontalStrut_3);
		
		
		JLabel lapTime = new JLabel(tabs.get(0).laps.get(0).lapToString());
		lapTime.setFont(new Font("Courier New", Font.BOLD, 15));
		horizontalBox.add(lapTime);
		
		Component horizontalGlue_2 = Box.createHorizontalGlue();
		horizontalBox.add(horizontalGlue_2);
		
	    
	    taskTabs.addTab(tabs.get(0).title , null, TASK1, tabs.get(0).title);
		
	    frmTasksTimer.getContentPane().add(taskTabs, "name_6289273657681");
	    
	    
	    timerLapButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
				
				if (tabs.get(0).isLapTimeZero() )
				{
					tabs.get(0).newLap(); //replace 0 with correct tab no
				}
				
				JLabel lapTime = new JLabel(tabs.get(0).laps.get(0).lapToString());
				lapTime.setFont(new Font("Courier New", Font.BOLD, 15));
				horizontalBox.add(lapTime);
				
				Component horizontalGlue_2 = Box.createHorizontalGlue();
				horizontalBox.add(horizontalGlue_2);
				
			}
		});
		
		/*
		//this is the default tab, used only to add new tabs
		taskTabs.addTab("+ Task", null, TASK1, null);
		
		
		//this is the loading of old tabs 
		String homePath = System.getenv("APPDATA") + "\\TaskTimer";
		if (new File (homePath+"\\tasks.json").exists())
		{
			ArrayList<String>  oldTabs;
			try{
				BufferedReader br = new BufferedReader 
							(new FileReader(homePath+"\\tasks.json") );
				
				oldTabs = gson.fromJson(br, new TypeToken<ArrayList<String>>()
						{}.getType() );

				for (int i=0; i<oldTabs.size(); i++)
				{
					System.out.println(oldTabs.get(i) + "\t" + i);
					addTab(taskTabs, oldTabs.get(i));
				}
				
				} catch(IOException err)
        		{
	        		err.printStackTrace();
	        	}
			
			
			
			
				
		}
		
		
		
		
		
		
		
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
					addTab(taskTabs, title);
					
					
				}

			}
		});
		frmTasksTimer.getContentPane().add(taskTabs, "name_6289273657681");
		
		
		
		taskTabs.addChangeListener(new ChangeListener() {
			
			public void stateChanged (ChangeEvent e){
				if (taskTabs.getSelectedIndex() ==0)
				{
					String title = (String) JOptionPane.showInputDialog("Please input task name", null);
					if (checkTitle(title))
					{
						addTab(taskTabs, title);
						
						
					}

				}
			}
			
		});
	*/
		
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {

	        public void run() {
	        	
	        	String json = gson.toJson(tabs);
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
	        		fw.write(json);
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
