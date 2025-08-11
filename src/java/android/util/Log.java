package android.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Log {
  
  private static final Logger Log = LoggerFactory.getLogger(Log.class);
  
    public static int d(String a, String b) {
        Log.info(a + ": " + b);
		return 0;
    }	
	
}