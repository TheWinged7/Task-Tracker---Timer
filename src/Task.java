
public class Task {
	
	private Lap lap;
	public String title;
	
	public  Task( String label, int h, int m, int s)
	{
		
		title = label;
		lap = new Lap();

		
	}
	
	
		
	
	public String totalTaskTime()
	{
		String total="";
		int h, m, s;
		
		h=lap.getHours();
		m=lap.getMinutes();
		s=lap.getSeconds();
			
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
		
	public String lapToString()
	{
		
		return lap.lapToString();
	}
	
	public void tick()
	{
		lap.incMillis();
	}
	
	public boolean isLapTimeZero()
	{
				
		if ( 	lap.getSeconds() ==0	&&
				lap.getMinutes() ==0	&&
				lap.getHours() ==0
				)
		{
			return false;
		}
		
		return true;
	}
}