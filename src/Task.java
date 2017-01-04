import java.util.ArrayList;

public class Task {
	
	public ArrayList<Lap> laps;
	public String title;
	
	public  Task( String label, int h, int m, int s)
	{
		
		title = label;
		laps = new ArrayList<Lap>();
		laps.add(new Lap(0)	);
		
	}
	
	public void newLap()
	{
		int lapNo = laps.size();
		laps.add(new Lap(lapNo) );
	}
		
	public int getNoLaps()
	{
		
		return laps.size();
	}
	
	public String totalTaskTime()
	{
		String total="";
		int h=0, m=0, s=0;
		
		for (Lap l : laps)
		{
			h+=l.getHours();
			m+=l.getMinutes();
			s+=l.getSeconds();
		}
		
		
		if (h<10)
		{
			total += "0" + h  +":";
		}
		else
		{
			total +=  h +":";
		}
		if (m<10)
		{
			total += "0" + m +":";
		}
		else
		{
			total +=  m +":";
		}
		if (s<10)
		{
			total += "0" + s;
		}
		else
		{
			total +=  s;
		}
		
		return total;
	}
	
	public String lapToString(int n)
	{
		
		return laps.get(n).lapToString();
	}
	
	public String lapToString()
	{
		
		return laps.get(laps.size()-1).lapToString();
	}
	
	public void tick()
	{
		laps.get(laps.size()-1).incSeconds();
	}
	
	public boolean isLapTimeZero()
	{
				
		if ( 	laps.get(laps.size()-1).getSeconds() ==0	&&
				laps.get(laps.size()-1).getMinutes() ==0	&&
				laps.get(laps.size()-1).getHours() ==0
				)
		{
			return false;
		}
		
		return true;
	}
}