package zcf.group.Geo01;

import com.aliyun.odps.data.Record;
import com.aliyun.odps.mapred.Reducer;

import java.io.IOException;
import java.util.Iterator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
// 下面是用于时间转换
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
// 自然对数E
import java.lang.Math;

/**
 * Reducer模板。请用真实逻辑替换模板内容
 */
public class MyReducer implements Reducer {
    private Record result;

    public void setup(TaskContext context) throws IOException {
        result = context.createOutputRecord();
    }

    public void reduce(Record key, Iterator<Record> values, TaskContext context) throws IOException {
    	// 1. 信息丢失 && 有历史信息 --> 用历史信息恢复
    	// 2. 信息丢失 && 没有历史信息 --> 用交互过的服务的商品地理信息进行恢复，但这个时候不能考虑全集，全集的商品分布很广。
    	//    这里加一个dist不为Null就是服务的信息！
    	
    	// 存储用户历史信息中地理位置每bit的geohash
    	Map<Character, Long> g1 = new TreeMap<Character, Long>();
    	Map<Character, Long> g2 = new TreeMap<Character, Long>();
    	Map<Character, Long> g3 = new TreeMap<Character, Long>();
    	Map<Character, Long> g4 = new TreeMap<Character, Long>();
    	Map<Character, Long> g5 = new TreeMap<Character, Long>();
    	Map<Character, Long> g6 = new TreeMap<Character, Long>();
    	Map<Character, Long> g7 = new TreeMap<Character, Long>();
    	
    	// 存储用户交互过服务商品历史信息中地理位置每bit的geohash
    	Map<Character, Long> ig1 = new TreeMap<Character, Long>();
    	Map<Character, Long> ig2 = new TreeMap<Character, Long>();
    	Map<Character, Long> ig3 = new TreeMap<Character, Long>();
    	Map<Character, Long> ig4 = new TreeMap<Character, Long>();
    	Map<Character, Long> ig5 = new TreeMap<Character, Long>();
    	Map<Character, Long> ig6 = new TreeMap<Character, Long>();
    	Map<Character, Long> ig7 = new TreeMap<Character, Long>();
    	
    	
    	boolean is_miss = false;
    	boolean is_no_history_geohash = true; // 标记是否有用户历史行为
    	boolean is_no_item_history_geohash = true; // 标记服务商品的地理信息是否有
        while (values.hasNext()) {
            Record val = values.next();
            
            // 标记这条记录是否是缺失user_geohash,dist为-1表示缺失，否则不缺失
            // 只有dist不为null，说明是服务信息
            if (val.get("dist") != null) {
            	long dist = val.getBigint("dist");
                if (dist == -1) {
                	is_miss = true;
                }
                
                // 统计商品的地理信息
                if (val.get("item_geohash") != null) {
                	is_no_item_history_geohash = false;
                	char item_geohash []= val.getString("item_geohash").toCharArray();
                    
                	// 对item_geohash进行存储
                	char geoKey = item_geohash[0];
                    if (ig1.containsKey(geoKey)) {
                    	ig1.put(geoKey, ig1.get(geoKey) + 1L);
                    } else {
                    	ig1.put(geoKey, 1L);
                    }
                    
                	geoKey = item_geohash[1];
                    if (ig2.containsKey(geoKey)) {
                    	ig2.put(geoKey, ig2.get(geoKey) + 1L);
                    } else {
                    	ig2.put(geoKey, 1L);
                    }
                    
                	geoKey = item_geohash[2];
                    if (ig3.containsKey(geoKey)) {
                    	ig3.put(geoKey, ig3.get(geoKey) + 1L);
                    } else {
                    	ig3.put(geoKey, 1L);
                    }
                    
                	geoKey = item_geohash[3];
                    if (ig4.containsKey(geoKey)) {
                    	ig4.put(geoKey, ig4.get(geoKey) + 1L);
                    } else {
                    	ig4.put(geoKey, 1L);
                    }
                    
                	geoKey = item_geohash[4];
                    if (ig5.containsKey(geoKey)) {
                    	ig5.put(geoKey, ig5.get(geoKey) + 1L);
                    } else {
                    	ig5.put(geoKey, 1L);
                    }
                    
                	geoKey = item_geohash[5];
                    if (ig6.containsKey(geoKey)) {
                    	ig6.put(geoKey, ig6.get(geoKey) + 1L);
                    } else {
                    	ig6.put(geoKey, 1L);
                    }
                    
                	geoKey = item_geohash[6];
                    if (ig7.containsKey(geoKey)) {
                    	ig7.put(geoKey, ig7.get(geoKey) + 1L);
                    } else {
                    	ig7.put(geoKey, 1L);
                    }
    
                }
                
            }
            
            if (val.get("user_geohash") != null) {
            	is_no_history_geohash = false;
            	char user_geohash []= val.getString("user_geohash").toCharArray();
                // 对user_geohash进行存储
            	char geoKey = user_geohash[0];
                if (g1.containsKey(geoKey)) {
                	g1.put(geoKey, g1.get(geoKey) + 1L);
                } else {
                	g1.put(geoKey, 1L);
                }
                
            	geoKey = user_geohash[1];
                if (g2.containsKey(geoKey)) {
                	g2.put(geoKey, g2.get(geoKey) + 1L);
                } else {
                	g2.put(geoKey, 1L);
                }
                
                geoKey = user_geohash[2];
                if (g3.containsKey(geoKey)) {
                	g3.put(geoKey, g3.get(geoKey) + 1L);
                } else {
                	g3.put(geoKey, 1L);
                }
                
                geoKey = user_geohash[3];
                if (g4.containsKey(geoKey)) {
                	g4.put(geoKey, g4.get(geoKey) + 1L);
                } else {
                	g4.put(geoKey, 1L);
                }
                
                geoKey = user_geohash[4];
                if (g5.containsKey(geoKey)) {
                	g5.put(geoKey, g5.get(geoKey) + 1L);
                } else {
                	g5.put(geoKey, 1L);
                }
                
                geoKey = user_geohash[5];
                if (g6.containsKey(geoKey)) {
                	g6.put(geoKey, g6.get(geoKey) + 1L);
                } else {
                	g6.put(geoKey, 1L);
                }
                
                geoKey = user_geohash[6];
                if (g7.containsKey(geoKey)) {
                	g7.put(geoKey, g7.get(geoKey) + 1L);
                } else {
                	g7.put(geoKey, 1L);
                }    
            }
            
                      
            
        }
        if (is_miss == true && is_no_history_geohash == false) { // 信息丢失&&有历史信息 --> 用历史信息恢复
        	char user_geohash [] = {' ', ' ', ' ', ' ', ' ', ' ', ' '}; //最终的最大子串
        	char bigStringSet; // 记录每位的最大（数目最多）char
        	long num; // 标记最大次数
        	
        	// 查找最大串
        	bigStringSet = ' ';
        	num = 0L;
        	for(Map.Entry<Character, Long> entry : g1.entrySet()) {
        		if (num < entry.getValue()) {
        			num = entry.getValue();
        			bigStringSet = entry.getKey();
        		}
        	}
        	user_geohash[0] = bigStringSet;
        	
        	bigStringSet = ' ';
        	num = 0L;
        	for(Map.Entry<Character, Long> entry : g2.entrySet()) {
        		if (num < entry.getValue()) {
        			num = entry.getValue();
        			bigStringSet = entry.getKey();
        		}
        	}
        	user_geohash[1] = bigStringSet;
        	
        	bigStringSet = ' ';
        	num = 0L;
        	for(Map.Entry<Character, Long> entry : g3.entrySet()) {
        		if (num < entry.getValue()) {
        			num = entry.getValue();
        			bigStringSet = entry.getKey();
        		}
        	}
        	user_geohash[2] = bigStringSet;
        	
        	bigStringSet = ' ';
        	num = 0L;
        	for(Map.Entry<Character, Long> entry : g4.entrySet()) {
        		if (num < entry.getValue()) {
        			num = entry.getValue();
        			bigStringSet = entry.getKey();
        		}
        	}
        	user_geohash[3] = bigStringSet;
        	
        	bigStringSet = ' ';
        	num = 0L;
        	for(Map.Entry<Character, Long> entry : g5.entrySet()) {
        		if (num < entry.getValue()) {
        			num = entry.getValue();
        			bigStringSet = entry.getKey();
        		}
        	}
        	user_geohash[4] = bigStringSet;
        	
        	bigStringSet = ' ';
        	num = 0L;
        	for(Map.Entry<Character, Long> entry : g6.entrySet()) {
        		if (num < entry.getValue()) {
        			num = entry.getValue();
        			bigStringSet = entry.getKey();
        		}
        	}
        	user_geohash[5] = bigStringSet;
        	
        	bigStringSet = ' ';
        	num = 0L;
        	for(Map.Entry<Character, Long> entry : g7.entrySet()) {
        		if (num < entry.getValue()) {
        			num = entry.getValue();
        			bigStringSet = entry.getKey();
        		}
        	}
        	user_geohash[6] = bigStringSet;
	        result.set(0, key.get(0));
	        result.set(1, String.valueOf(user_geohash));
	        context.write(result);
        } else if (is_miss == true && is_no_item_history_geohash == false) { // 信息丢失&&有服务商品历史信息
        	char item_geohash [] = {' ', ' ', ' ', ' ', ' ', ' ', ' '}; //最终的最大子串
        	char bigStringSet; // 记录每位的最大（数目最多）char
        	long num; // 标记最大次数
        	
        	// 查找最大串
        	bigStringSet = ' ';
        	num = 0L;
        	for(Map.Entry<Character, Long> entry : ig1.entrySet()) {
        		if (num < entry.getValue()) {
        			num = entry.getValue();
        			bigStringSet = entry.getKey();
        		}
        	}
        	item_geohash[0] = bigStringSet;
        	
        	bigStringSet = ' ';
        	num = 0L;
        	for(Map.Entry<Character, Long> entry : ig2.entrySet()) {
        		if (num < entry.getValue()) {
        			num = entry.getValue();
        			bigStringSet = entry.getKey();
        		}
        	}
        	item_geohash[1] = bigStringSet;
        	
        	bigStringSet = ' ';
        	num = 0L;
        	for(Map.Entry<Character, Long> entry : ig3.entrySet()) {
        		if (num < entry.getValue()) {
        			num = entry.getValue();
        			bigStringSet = entry.getKey();
        		}
        	}
        	item_geohash[2] = bigStringSet;
        	
        	bigStringSet = ' ';
        	num = 0L;
        	for(Map.Entry<Character, Long> entry : ig4.entrySet()) {
        		if (num < entry.getValue()) {
        			num = entry.getValue();
        			bigStringSet = entry.getKey();
        		}
        	}
        	item_geohash[3] = bigStringSet;
        	
        	bigStringSet = ' ';
        	num = 0L;
        	for(Map.Entry<Character, Long> entry : ig5.entrySet()) {
        		if (num < entry.getValue()) {
        			num = entry.getValue();
        			bigStringSet = entry.getKey();
        		}
        	}
        	item_geohash[4] = bigStringSet;
        	
        	bigStringSet = ' ';
        	num = 0L;
        	for(Map.Entry<Character, Long> entry : ig6.entrySet()) {
        		if (num < entry.getValue()) {
        			num = entry.getValue();
        			bigStringSet = entry.getKey();
        		}
        	}
        	item_geohash[5] = bigStringSet;
        	
        	bigStringSet = ' ';
        	num = 0L;
        	for(Map.Entry<Character, Long> entry : ig7.entrySet()) {
        		if (num < entry.getValue()) {
        			num = entry.getValue();
        			bigStringSet = entry.getKey();
        		}
        	}
        	item_geohash[6] = bigStringSet;
	        result.set(0, key.get(0));
	        result.set(1, String.valueOf(item_geohash));
	        context.write(result);
        }
        
    }

    public void cleanup(TaskContext arg0) throws IOException {

    }
}
