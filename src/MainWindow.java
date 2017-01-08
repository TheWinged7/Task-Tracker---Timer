import java.io.*;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.EventQueue;
import java.awt.Dimension;
import java.awt.Component;
import java.awt.Font;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.regex.Pattern;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

//import com.google.gson.reflect.TypeToken;
import java.awt.SystemColor;


public class MainWindow {

	private JFrame frmTasksTimer;
	final JTabbedPane taskTabs = new JTabbedPane(JTabbedPane.TOP);
	private static Gson gson = new Gson();
	private static ArrayList<Task>  tabs = new ArrayList<Task>();
	private static ArrayList<JLabel>  tabTimeLabels = new ArrayList<JLabel>();
	
	
	
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
	
	static private class timerActionListener implements ActionListener {
		int taskID;
		
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
	
	private static void addTab(final JTabbedPane tabbedPane, String label, int h, int m, int s) {
		
		
			final Timer timer = new Timer (100, new timerActionListener(tabs.size()) );
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
			
			JButton taskCreateButton = new JButton("Delete Task");
			taskCreateButton.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					//need to make this check which task this is, 
					//and then delete it from both task and taskLabel arrays, 
					//as well as remove its tab.

				}
			});
			taskNameBox.add(taskCreateButton);
			
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
						timer.start();
						System.out.println("Starting Timer...");
					}
					else 
					{
						timer.stop();
						System.out.println("Stopping Timer...");
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
