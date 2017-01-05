
public class Lap 	{
	private int hours;
	private int minutes;
	private int seconds;
	private int millis;



	
	public Lap ()
	{
		hours = 0;
		minutes = 0;
		seconds = 0;
		millis = 0;

	}
	
	public void incHours()
	{
		hours ++;
	}
	
	public void incMinutes()
	{
		minutes ++;
		if (minutes >=60)
		{
			minutes =0;
			incHours();
		}
	}
	
	public void incSeconds()
	{
		seconds ++;
		if (seconds >=60)
		{
			seconds =0;
			incMinutes();
		}
	}
	
	public void incMillis()
	{
		millis ++;
		if (millis >=10)
		{
			millis =0;
			incSeconds();
		}
	}


	public int getHours() 
	{
		return hours;
	}
	
	public int getMinutes() 
	{
		return minutes;
	}
	
	public int getSeconds() 
	{
		return seconds;
	}
	
	public int getMillis() 
	{
		return millis;
	}
	
	public String lapToString()
	{
		String time ="";
		if (hours<10)
		{
			time += "0" + hours  +":";
		}
		else
		{
			time +=  hours +":";
		}
		if (minutes<10)
		{
			time += "0" + minutes +":";
		}
		else
		{
			time +=  minutes +":";
		}
		if (seconds<10)
		{
			time += "0" + seconds;
		}
		else
		{
			time +=  seconds;
		}
		
		return time;
	}

	
}