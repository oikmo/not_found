package net.maniaticdevs.engine.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Calendar;

import net.maniaticdevs.main.Main;

/**
 * Logger class, logs whatever is given and saves to a log file
 * @author Oikmo
 */
public class Logger {
	
	/** Just the entire log ever accumulated during runtime */
	private static String completeLog = "";
	
	/** What level the log message is */
	public enum LogLevel {
		/** Safe just a general message, nothing to worry about */
		INFO,
		/** Something might be wrong but not THAT much to worry about */
		WARN,
		/** Be worried. */
		ERROR
	}
	
	/**
	 * Prints a log to the console based on given level. 
	 * Then it adds it to a large log in which when the program quits it will be saved.
	 * @param level Level of log
	 * @param message what log contains
	 */
	public static void log(LogLevel level, Object message) {
		String toLog = "";
		ThreadCall call = getThreadCall();
		if(!call.getClassCall().contains("$")) {
			toLog = getCurrentTime() + " [" + call.getThreadCall() + "/" + level.name() + "] "  + "[" + call.getClassCall() + "] " + message.toString();
		} else {
			toLog = getCurrentTime() + " [" + call.getThreadCall() + "/" + level.name() + "] " + message.toString();
		}
		
		completeLog += toLog + "\r\n";
		
		if(level == LogLevel.ERROR) {
			System.err.println(toLog);
		} else {
			System.out.println(toLog);
		}
	}
	
	/**
	 * Saves the large log.
	 */
	public static void saveLog() {
		if(completeLog.isEmpty()) { return; }
		File saveDirectory =  new File(Main.getWorkingDirectory() + "/logs/");
		if (!saveDirectory.exists()) {
	        try {
	        	saveDirectory.mkdir();
	        } catch (SecurityException e) {
	            System.out.println("nothing");
	        }
	    }
	    
		String path = saveDirectory +"/"+ getCurrentTimeFile() +".log";
		System.out.println(path);
	    File logFile = new File(path);
	    try {
	    	logFile.createNewFile();
	    	FileWriter logWriter = new FileWriter(path);
	    	
			logWriter.write(completeLog);
			logWriter.close();
	    } catch(IOException e)  {
	    	StringWriter ps = new StringWriter();
	    	e.printStackTrace(new PrintWriter(ps));
	    	System.out.println(ps.toString());
	    }
	}
	
	/* https://stackoverflow.com/questions/11306811/how-to-get-the-caller-class-in-java - Denys Seguret */
	/**
	 * returns who ran the function.
	 * @return class name
	 */
	private static ThreadCall getThreadCall() {
        StackTraceElement[] stElements = Thread.currentThread().getStackTrace();
        for(int i = 1; i < stElements.length; i++) {
            StackTraceElement ste = stElements[i];
            if(!ste.getClassName().equals(Logger.class.getName()) && ste.getClassName().indexOf("java.lang.Thread") != 0) {
            	String[] res = ste.getClassName().split("\\.");
                return new ThreadCall(Thread.currentThread().getName(), res[res.length-1]);
            }
        }
        return null;
     }
	
	/**
	 * returns currentTime in the form of [YYYY-MM-DD HH:MM:SS]
	 * @return string
	 */
	public static String getCurrentTime() {
		Calendar cal = Calendar.getInstance();
		
		int dy = cal.get(Calendar.DAY_OF_MONTH);
		int mon = cal.get(Calendar.MONTH) + 1;
		
		int hr = cal.get(Calendar.HOUR_OF_DAY);
		int min = cal.get(Calendar.MINUTE);
		int sec = cal.get(Calendar.SECOND);
		
		String day = "" + dy;
		String month = "" + mon;
		
		String hour = "" + hr;
		String minute = "" + min;
		String second = "" + sec;
		
		if(dy < 10) { day = "0" + dy; }
		if(mon < 10) { month = "0" + mon; }
		
		if(hr < 10) { hour = "0" + hr; }
		if(min < 10) { minute = "0" + min; }
		if(sec < 10) { second = "0" + sec; }
		
		String date = cal.get(Calendar.YEAR)+ "-" + month + "-" + day;
		String time = hour + ":" + minute + ":" + second;

		return "[" + date + " " + time + "]";
	}
	/**
	 * returns currentTime in the form of YYYY-MM-DD_HH.MM.SS
	 * @return {@link String} date
	 */
	public static String getCurrentTimeFile() {
		Calendar cal = Calendar.getInstance();
		
		int dy = cal.get(Calendar.DAY_OF_MONTH);
		int mon = cal.get(Calendar.MONTH) + 1;
		
		int hr = cal.get(Calendar.HOUR_OF_DAY);
		int min = cal.get(Calendar.MINUTE);
		int sec = cal.get(Calendar.SECOND);
		
		String day = "" + dy;
		String month = "" + mon;
		
		String hour = "" + hr;
		String minute = "" + min;
		String second = "" + sec;
		
		if(dy < 10) { day = "0" + dy; }
		if(mon < 10) { month = "0" + mon; }
		
		if(hr < 10) { hour = "0" + hr; }
		if(min < 10) { minute = "0" + min; }
		if(sec < 10) { second = "0" + sec; }
		
		String date = cal.get(Calendar.YEAR)+ "-" + month + "-" + day;
		String time = hour + "." + minute + "." + second;

		return date + "_" + time;
	}
	
	/**
	 * Just data to be able to log it in.
	 * @author Oikmo
	 */
	private static class ThreadCall {
		/** Thread that called */
		private String threadCall;
		/** Class that called */
		private String classCall;
		
		/** 
		 * ThreadCall contructor
		 * 
		 * @param threadCall Thread that called
		 * @param classCall Class that called
		 */
		public ThreadCall(String threadCall, String classCall) {
			this.threadCall = threadCall;
			this.classCall = classCall;
		}
		
		/** 
		 * Get the thread that called.
		 * @return {@link String}
		 */
		public String getThreadCall() {
			return threadCall;
		}
		
		/** 
		 * Get the class that called.
		 * @return {@link String}
		 */
		public String getClassCall() {
			return classCall;
		}
	}
}
