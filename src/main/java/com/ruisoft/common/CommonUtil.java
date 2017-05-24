package com.ruisoft.common;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.regex.Pattern;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
//import org.json.JSONObject;

/**
 * 通用公共方法类
 * @author lenovo
 *
 */
public class CommonUtil {

    private static final Logger LOG = Logger.getLogger(CommonUtil.class);
    public static final Pattern PAT_PARAM = Pattern.compile("^(?:[^=&]+=[^=&]*&)*(?:[^=&]+=[^=&]*)$");

    /**
     * 获得一个UUID
     * @return String UUID
     */
    public static String getUUID() {
        return UUID.randomUUID().toString();
    }

    public static String get32UUID() {
        return getUUID().replaceAll("-", "");
    }

    public static String getPKUUID() {
        return getCurDateSortStr().concat("-").concat(getUUID().replaceAll("-", "").substring(0,16));

    }

    /**
     * Format : yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String getCurDateTime() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }
    /**
     * Format : yyyy-MM-dd
     * @return
     */
    public static String getCurDate() {
        return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    }

    /**
     * Format : yyyyMMddHHmmss
     * @return
     */
    public static String getCurDateSortStr() {
        return new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
    }
    /**
     * <p>
     * 输出POJO类中的所有属性及其值
     * </p>
     * 输出格式<code>[attr=value,...]</code>
     * <p>
     * POJO类属性值通过对应的<code>getter</code>方法获取， boolean型值也可以通过形如<code>isXXX</code>
     * 的方法获取
     * </p>
     *
     * @param pojo
     *            POJO类对象
     *
     * @return 格式化后的属性值字符串
     */
    public static <T> String pojoString(T pojo) {
        if (pojo == null)
            return "";

        String mName = null;
        StringBuffer str = new StringBuffer("[");
        Object[] params = null;

        for (Method md : pojo.getClass().getMethods()) {
            mName = md.getName();
            if (mName.startsWith("get") || mName.startsWith("is")) {
                if (mName.equals("getClass"))
                    continue;
                if (md.getParameterTypes().length > 0)
                    continue;

                try {
                    str.append(mName.substring(3, 4).toLowerCase().concat(mName.substring(4))).append('=').append(md.invoke(pojo, params));
                } catch (Exception e) {
                    LOG.info(e);
                } finally {
                    str.append(',');
                }
            }
        }
        if (str.length() > 1) {
            str.deleteCharAt(str.length() - 1);
        }
        str.append(']');

        return str.toString();
    }



    public static JSONObject getJsonData(String info){
        LOG.debug("请求参数：" + info);
        if (PAT_PARAM.matcher(info).matches()) {
            String[] param = info.split("&");
            String k = null, v = null;
            JSONObject json = new JSONObject();
            for (String p : param) {
                try {
                    k = p.split("=")[0];
                    v = p.split("=")[1];
                } catch (ArrayIndexOutOfBoundsException e) {
                    v = "";
                } finally {
                    json.put(k, v);
                }
            }
            return json;
        }
        return JSONObject.fromObject(info);
    }
    
    /*
     * 将小写的人民币转化成大写
     */
    public static String convertToChineseNumber(double number)
    {
    	StringBuffer chineseNumber = new StringBuffer();
    	String [] num={"零","壹","贰","叁","肆","伍","陆","柒","捌","玖"};
    	String [] unit = {"分","角","元","拾","佰","仟","万","拾","佰","仟","亿","拾","佰","仟","万"};
    	String tempNumber = String.valueOf(Math.round((number * 100)));
    	int tempNumberLength = tempNumber.length();
    	if ("0".equals(tempNumber))
    	{
    		return "零元整";
    	}
    	if (tempNumberLength > 15)
    	{
    		throw new RuntimeException("超出转化范围.");
    	}
    	boolean preReadZero = true;    //前面的字符是否读零
    	for (int i=tempNumberLength; i>0; i--)
    	{
    		if ((tempNumberLength - i + 2) % 4 == 0)
    		{
    			//如果在（圆，万，亿，万）位上的四个数都为零,如果标志为未读零，则只读零，如果标志为已读零，则略过这四位
    			if (i - 4 >= 0 && "0000".equals(tempNumber.substring(i - 4, i)))
    			{
    				if (!preReadZero)
    				{
    					chineseNumber.insert(0, "零");
    					preReadZero = true;
    				}
    				i -= 3;    //下面还有一个i--
    				continue;
    			}
    			//如果当前位在（圆，万，亿，万）位上，则设置标志为已读零（即重置读零标志）
    			preReadZero = true;
    		}
    		Integer digit = Integer.parseInt(tempNumber.substring(i - 1, i));
    		if (digit == 0)
    		{
    			//如果当前位是零并且标志为未读零，则读零，并设置标志为已读零
    			if (!preReadZero)
    			{
    				chineseNumber.insert(0, "零");
    				preReadZero = true;
    			}
    			//如果当前位是零并且在（圆，万，亿，万）位上，则读出（圆，万，亿，万）
    			if ((tempNumberLength - i + 2) % 4 == 0)
    			{
    				chineseNumber.insert(0, unit[tempNumberLength - i]);
    			}
    		}
    		//如果当前位不为零，则读出此位，并且设置标志为未读零
    		else
    		{
    			chineseNumber.insert(0, num[digit] + unit[tempNumberLength - i]);
    			preReadZero = false;
    		}
    	}
    	//如果分角两位上的值都为零，则添加一个“整”字
    	if (tempNumberLength - 2 >= 0 && "00".equals(tempNumber.substring(tempNumberLength - 2, tempNumberLength)))
    	{
    		chineseNumber.append("整");
    	}
    	return chineseNumber.toString();
    }
    
    public static String jwdToString (String jwd){
    	String str ="";
    	try{
    		if(!"".equals(jwd)&&jwd!=null){
    			String du = jwd.substring(0, jwd.indexOf("°"));
    			String min =  jwd.substring(jwd.indexOf("°")+1, jwd.indexOf("′"));
    			String ss =  jwd.substring(jwd.indexOf("′")+1, jwd.indexOf("″"));
    			BigDecimal du1 = new BigDecimal(du);
    			BigDecimal min1 = new BigDecimal(min);
    			BigDecimal ss1 = new BigDecimal(ss);
    			BigDecimal min22 = new BigDecimal("0");
    			if(min1.compareTo(new BigDecimal("0"))!=0){
    				min22 = min1.divide(new BigDecimal(60), new MathContext(6));
    			}
    			BigDecimal ss22 = new BigDecimal("0");
    			if(ss1.compareTo(new BigDecimal("0"))!=0){
    				ss22 = ss1.divide(new BigDecimal(3600), new MathContext(6));
    			}
    			BigDecimal jd2 = du1.add(min22).add(ss22).setScale(5, BigDecimal.ROUND_HALF_UP);
    			str = jd2.toString();
    		}
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	return str;
    }
    /**
     * 将JSONObjec集合换成Map集合
     * @param List<JSONObject>
     * @return
     */
    public static List<Map<String, Object>> jsonListToMapList(List<JSONObject> result) {
    	 List<Map<String, Object>> listmap = new ArrayList<Map<String, Object>>();
         for (int i = 0; i < result.size(); i++) {
        	 JSONObject jsonObj = result.get(i);
        	 Map<String, Object> map = new HashMap<String, Object>();
        	 map=jsonToMap(jsonObj);
        	 listmap.add(map);
         }
         return listmap;
    }
    /**
     * 将JSONObjec对象转换成Map集合
     * @param json
     * @return
     */
    public static HashMap<String, Object> jsonToMap(JSONObject json){
        HashMap<String, Object> map = new HashMap<String, Object>();
        Set keys = json.keySet();
        for(Object key : keys){
        	Object o = (String)json.get(key);
            map.put((String) key, o);
        }
        return map;
    }
    
	public static String jdTrans(String jd){
		String sjjd = "";
		if(isBigDecimal(jd)){
			BigDecimal jdb = new BigDecimal(jd);
			jdb = jdb.setScale(5, BigDecimal.ROUND_HALF_UP);
			sjjd = jdb.toString();
		}
		return sjjd;
		
	}
	
	public static boolean isBigDecimal(String str) {
		try {
			new BigDecimal(str);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
    
    public static void main(String args[]){
    	String ss = "123";
//    	System.out.println(ss);
//    	Map mm = new HashMap();
//    	mm.put("bbb", ss);
//    	System.out.println(mm.toString());
//    	 JSONObject json = new JSONObject();
//    	 json.put("aaa", ss);
//    	 System.out.println(json.toString());
//    	 System.out.println(json.get("aaa"));
    	System.out.println(jdTrans(ss));
    }
}
