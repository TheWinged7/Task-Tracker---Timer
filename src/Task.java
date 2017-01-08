
public class Task {
	
	private String title;
	private int hours;
	private int minutes;
	private int seconds;
	private int millis;
	
	
	public  Task( String label, int h, int m, int s)
	{
		
		title = label;
		hours =h;
		minutes=m;
		seconds=s;
		millis=0;
	}
	
	
	private void incHours()
	{
		hours ++;
	}
	
	private void incMinutes()
	{
		minutes ++;
		if (minutes >=60)
		{
			minutes =0;
			incHours();
		}
	}
	
	private void incSeconds()
	{
		seconds ++;
		if (seconds >=60)
		{
			seconds =0;
			incMinutes();
		}
	}
	
	private void incMillis()
	{
		millis ++;
		if (millis >=10)
		{
			millis =0;
			incSeconds();
		}
	}
		
	
	public String totalTaskTime()
	{
		String total="";
	
			
		if (hours<10)
		{
			total += "0" + hours  +":";
		}
		else
		{
			total +=  hours +":";
		}
		if (minutes<10)
		{
			total += "0" + minutes +":";
		}
		else
		{
			total +=  minutes +":";
		}
		if (seconds<10)
		{
			total += "0" + seconds;
		}
		else
		{
			total +=  seconds;
		}
		
		return total;
	}

	public String getTitle()
	{
		return title;
	}

	
	public int getSeconds()
	{
		return seconds;
	}
	
	public int getMinutes()
	{
		return minutes;
	}
	
	public int getHours()
	{
		return hours;
	}
	
	public void tick()
	{
		incMillis();
	}
	
	public boolean isLapTimeZero()
	{
				
		if ( 	seconds ==0	&&
				minutes ==0	&&
				hours ==0
				)
		{
			return false;
		}
		
		return true;
	}
}