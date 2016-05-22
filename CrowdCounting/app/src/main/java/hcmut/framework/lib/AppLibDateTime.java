package hcmut.framework.lib;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;


public class AppLibDateTime {
	private static final int[] month31 = { 1, 3, 5, 7, 8, 10, 12 };
	private static final int[] month30 = { 4, 6, 9, 11 };
	private static final int[] month2829 = { 2 };
	
	public static final String yyyyMMddHHmmss = "yyyyMMddHHmmss";
	
	public static String generateDate(String day, String month, String year) {
		if(day.length()==0 || month.length()==0 || year.length()==0) return "";
		String dd = day;
		String MM = month;
		String yyyy = year;
		if(day.length()==1) {
			dd = "0" + day;
		}
		if(month.length()==1) {
			MM = "0" + month;
		}
		if(year.length()==1) {
			yyyy = "000" + year;
		}
		if(year.length()==2) {
			yyyy = "00" + year;
		}
		if(year.length()==3) {
			yyyy = "0" +year;
		}
		return yyyy + MM + dd;
	}
	
	public static boolean checkValidDate(String yyyyMMdd) {
		if(yyyyMMdd==null || yyyyMMdd.length()==0) return false;
    	boolean isValid = false;
    	int year = Integer.valueOf(extractYear(yyyyMMdd));
    	int month = Integer.valueOf(extractMonth(yyyyMMdd));
    	int day = Integer.valueOf(extractDay(yyyyMMdd));
    	
    	if(AppLibGeneral.isInArray(month31, month)) {
    		if(day>=0 && day<=31) {
    			isValid = true;
    		}
    	} else if(AppLibGeneral.isInArray(month30, month)) {
    		if(day>=0 && day<=30) {
    			isValid = true;
    		}
    	} else if(AppLibGeneral.isInArray(month2829, month)) {
    		if((year%100!=0&&year%4==0) || (year%100==0&&year%400==0)) {
    			if(day>=0 && day<=29) {
    				isValid = true;
    			}
    		} else {
    			if(day>=0 && day<=28) {
    				isValid = true;
    			}
    		}
    	}
    	
    	return isValid;
    }
	
	public static String extractYear(String yyyyMMdd) {
		if(yyyyMMdd==null || yyyyMMdd.length()==0) return "";
		return yyyyMMdd.substring(0, 4);
	}
	
	public static String extractMonth(String yyyyMMdd) {
		if(yyyyMMdd==null || yyyyMMdd.length()==0) return "";
		return yyyyMMdd.substring(4, 6);
	}
	
	public static String extractDay(String yyyyMMdd) {
		if(yyyyMMdd==null || yyyyMMdd.length()==0) return "";
		return yyyyMMdd.substring(6);
	}
	
	public static String convertFromyyyyMMdd2ReadableDate(String yyyyMMdd) {
		if(yyyyMMdd==null || yyyyMMdd.length()==0) {
			return "";
		}
		String year = extractYear(yyyyMMdd);
		String month = extractMonth(yyyyMMdd);
		String day = extractDay(yyyyMMdd);
		return day + "/" + month + "/" + year;
	}
	
	/**
	 * 
	 * @param now : yyyyMMddHHmmss
	 * @param odate : yyyyMMddHHmmss
	 * @return : time in readable format
	 */
	public static String deltaDate2readableFormat(String now, String odate) {
		String result = "";
		int[] months = {0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
		String stt[] = new String[6];		
		String stts[] = new String[6];
		String ago = "";
		
//		if(!lang.equals("vi")) {
			stt[0] = "year";
			stt[1] = "month";
			stt[2] = "day";
			stt[3] = "hour";
			stt[4] = "minute";
			stt[5] = "second";
			
			stts[0] = "years";
			stts[1] = "months";
			stts[2] = "days";
			stts[3] = "hours";
			stts[4] = "minutes";
			stts[5] = "seconds";
			
			ago = "ago";
		/*} else {
			stt[0] = "năm";
			stt[1] = "tháng";
			stt[2] = "ngày";
			stt[3] = "giờ";
			stt[4] = "phút";
			stt[5] = "giây";
			
			stts[0] = "năm";
			stts[1] = "tháng";
			stts[2] = "ngày";
			stts[3] = "giờ";
			stts[4] = "phút";
			stts[5] = "giây";
			
			ago = "trước";
		}	*/	
			
		long longnow = Long.valueOf(odate);
		long longodate = Long.valueOf(now);
		
		double second1 = longnow - Math.floor(longnow/100)*100;
		double	second2 = longodate - Math.floor(longodate/100)*100;

		double	minute1 = Math.floor((longnow - Math.floor(longnow/10000)*10000)/100);
		double	minute2 = Math.floor((longodate - Math.floor(longodate/10000)*10000)/100);

		double	hour1 = Math.floor((longnow - Math.floor(longnow/1000000)*1000000)/10000);
		double	hour2 = Math.floor((longodate - Math.floor(longodate/1000000)*1000000)/10000);

		double	day1 = Math.floor((longnow - Math.floor(longnow/100000000)*100000000)/1000000);
		double	day2 = Math.floor((longodate - Math.floor(longodate/100000000)*100000000)/1000000);

		double	month1 = Math.floor((longnow - Math.floor(longnow/10000000000.0)*10000000000.0)/100000000);
		double	month2 = Math.floor((longodate - Math.floor(longodate/10000000000.0)*10000000000.0)/100000000);

		double	year1 = Math.floor(longnow/10000000000.0);
		double	year2 = Math.floor(longodate/10000000000.0);
			
		double delta_s = second2 - second1;
		double delta_m = (delta_s<0)?minute2-minute1-1:minute2-minute1;
		double delta_h = (delta_m<0)?hour2-hour1-1:hour2-hour1;
		double delta_d = (delta_h<0)?day2-day1-1:day2-day1;
		double delta_mo = (delta_d<0)?month2-month1-1:month2-month1;
		double delta_y = (delta_mo<0)?year2-year1-1:year2-year1;	
			
		delta_s += (delta_s<0)?60:0;
		delta_m += (delta_m<0)?60:0;
		delta_h += (delta_h<0)?24:0;
		delta_d += (delta_d<0)?((year1%4==0 && year1%100!=0 || year1%400==0)?months[(int)month1]+1:months[(int)month1]):0;
		delta_mo += (delta_mo<0)?12:0;
		delta_y += 0;
			
		int[] time = {(int)delta_y, (int)delta_mo, (int)delta_d, (int)delta_h, (int)delta_m, (int)delta_s};
		
		for(int i=0; i<time.length; i++) {  
			if(time[i]>0) {
				if(i<=2) { 
					result = date2readableFormat(odate);
					result += " " + time2readableFormatHourMinute(odate.substring(8));
					break;
				}
				int t = time[i];			
				if(t>1) result = Integer.toString(t) + " " + stts[i];
				else result = Integer.toString(t) + " " + stt[i];
				result += " " + ago;
				break;
			}
		}
		if(result=="") result = 1 + stt[5] + " " + ago;
		return result;
	}
	
	public static String date2readableFormat(String yyyyMMdd) {
		String yy = yyyyMMdd.substring(0, 4);
		String mm = yyyyMMdd.substring(4, 6);
		String dd = yyyyMMdd.substring(6, 8);
		String currentYear = AppLibGeneral.getTimeNow().substring(0, 4);
		mm = getMonth(mm, true);
		String date = (!yy.equals(currentYear))?mm + " " + dd + ", " + yy : mm + " " + dd;
		return date;
	}
	
	public static String time2readableFormat(String HHmmss) {
		String readableFormat = "";
		if(AppLibGeneral.isNotEmptyString(HHmmss) && HHmmss.length()==6) {
			String HH = HHmmss.substring(0, 2);
			String mm = HHmmss.substring(2, 4);
			String ss = HHmmss.substring(4);
			readableFormat =  HH + ":" + mm + ":" + ss;
		}
		return readableFormat;
	}
	
	public static String time2readableFormatHourMinute(String HHmmss) {
		String readableFormat = "";
		if(AppLibGeneral.isNotEmptyString(HHmmss) && HHmmss.length()==6) {
			String HH = HHmmss.substring(0, 2);
			String mm = HHmmss.substring(2, 4);
			readableFormat =  HH + ":" + mm;
		}
		return readableFormat;
	}
	
	public static String getMonth(int monthIdx, boolean is3chars) {
		return getMonth(String.valueOf(monthIdx), is3chars);
	}
	
	public static String getMonth(String monthIdx, boolean is3chars) {
		String result = getMonth(String.valueOf(monthIdx));
		if(is3chars) result = result.substring(0, 3);
		return result;
	}
	
	public static String getMonth(int monthIdx) {
		return getMonth(String.valueOf(monthIdx));
	}
	
	public static String getMonth(String monthIdx) {
		String month = "";
		if(monthIdx.equals("01") || monthIdx.equals("1")) { month = "January"; }
		else if (monthIdx.equals("02") || monthIdx.equals("2")) { month = "February"; }
		else if (monthIdx.equals("03") || monthIdx.equals("3")) { month = "March"; }
		else if (monthIdx.equals("04") || monthIdx.equals("4")) { month = "April"; }
		else if (monthIdx.equals("05") || monthIdx.equals("5")) { month = "May"; }
		else if (monthIdx.equals("06") || monthIdx.equals("6")) { month = "June"; }
		else if (monthIdx.equals("07") || monthIdx.equals("7")) { month = "July"; }
		else if (monthIdx.equals("08") || monthIdx.equals("8")) { month = "August"; }
		else if (monthIdx.equals("09") || monthIdx.equals("9")) { month = "September"; }
		else if (monthIdx.equals("10")) { month = "October"; }
		else if (monthIdx.equals("11")) { month = "November"; }
		else if (monthIdx.equals("12")) { month = "December"; }
		else month = monthIdx;
		return month;
	}
	
	/**
	 * 
	 * @param utcTime for example, System.currentTimeMillis()
	 * @param dateFormat for example, "yyyyMMddHHmmss"
	 * @return converted time in dateFormat
	 */
	public static String convertUTCTime(long utcTime, String dateFormat) {
		Date date = new Date(utcTime);  
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);		 
		Calendar calendar = Calendar.getInstance();
		TimeZone timezone = calendar.getTimeZone();		
		sdf.setTimeZone(timezone);
		return sdf.format(date);
	}
	
}
