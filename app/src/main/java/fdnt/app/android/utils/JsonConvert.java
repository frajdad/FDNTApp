package fdnt.app.android.utils;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class JsonConvert {
	
	public static String serializeObject(Object obj) throws Exception {
		if(obj instanceof Enum) return ((Enum)obj).toString ();
		if(isValueType(obj)) return serializeValueTypeObject(obj);
		if(isMap(obj)) return serializeMap(obj);
		if(isArray(obj) || isCollection(obj)) return serializeArrayOrCollection(obj);
		return serializeSingleObject(obj);
	}
	
	private static <T> Boolean isValueType(T obj) {
		if(obj instanceof String) return true;
		if(obj instanceof Number) return true;
		if(obj instanceof Boolean) return true;
		if(obj instanceof Enum) return true;
		if(obj == null) return true;
		return false;
	}
	
	private static <T> String serializeValueTypeObject(T obj) throws Exception {
		if(!isValueType(obj)) throw new IllegalArgumentException("Given object is not value type! OBJECT: " + obj.getClass().getName());
		if(obj == null) return "null";
		if(obj instanceof String) return "\"" + obj.toString() + "\"";
		if(obj instanceof Boolean)
			if((Boolean)obj) return "true";
			else return "false";
		if(obj instanceof Number) return ((Number) obj).toString();
		throw new IllegalArgumentException("Given object is not value type! OBJECT: " + obj.getClass().getName());
	}
	
	private static <T> String serializeSingleObject(T obj) throws Exception {
		Field[] fields = obj.getClass().getDeclaredFields();
		String output = "{";
		for(Field f : fields) {
			output += "\"" + f.getName() + "\":";
			output += serializeObject(f.get(obj)) + ",";
		}
		output = output.substring(0, output.length() - 1) + "}";
		return output;
	}
	
	private static <T> String serializeArrayOrCollection(T obj) throws Exception {
		if(!isArray(obj) && !isCollection(obj)) throw new IllegalArgumentException("Given object is not collection or array! OBJECT: " + obj.getClass().getName());
		Object[] elements = (isArray(obj))? (Object[]) obj : ((Collection) obj).toArray();
		String output = "[";
		for(Object v : elements){
			output += serializeObject(v) + ",";
		}
		output = output.substring(0, output.length()-1) + "]";
		return output;
	}
	
	private static <T> String serializeMap (T obj) throws Exception {
		if(!isMap(obj)) throw new IllegalArgumentException("Given object is not Map! OBJECT: " + obj.getClass().getName());
		String output = "[";
		Map<Object, Object> temp = (Map<Object, Object>) obj;
		for(Object key : temp.keySet()){
			output += serializeObject(key) + ":";
			output += serializeObject(temp.get(key)) + ",";
		}
		output = output.substring(0, output.length()-1) + "]";
		return output;
	}
	
	private static <T> Boolean isArray(T obj){
		return obj.getClass().isArray();
	}
	
	private static <T> Boolean isCollection (T obj) {
		return obj instanceof Collection;
	}
	
	private static <T> Boolean isMap(T obj) {
		return obj instanceof Map;
	}
	
	public static <T> Object deserializeObject (String json, T obj) throws Exception {
		json = trimJson(json);
		if(isValueType(obj)) return deserializeValueTypeObject(json, obj);
		if(isMap(obj)) return deserializeMap(json, obj);
		if(isArray(obj) || isCollection(obj)) {
			ArrayList<T> temp = (ArrayList<T>) deserializeArrayOrCollection(json, obj);
			if(isArray(obj)){
				return temp.toArray();
			} else return (Collection) temp;
		}
		return deserializeSingleObject(json, obj);
	}
	
	private static <T> Object deserializeValueTypeObject(String json, T obj) throws Exception {
		if(!isValueType(obj)) throw new IllegalArgumentException("Given object is not value type! OBJECT: " + obj.getClass().getName());
		if(json == "null") return null;
		if(obj instanceof String) return json.substring(1, json.length()-1);
		if(obj instanceof Boolean) return json == "true";
		if(obj instanceof Number) return NumberFormat.getInstance().parse(json);
		throw new IllegalArgumentException("WTF?!");
	}
	
	private static <T> Object deserializeSingleObject(String json, T obj) throws Exception {
		Field[] fields = obj.getClass().getDeclaredFields();
		Object output = obj.getClass().getDeclaredConstructor().newInstance();
		final String[] pairs = splitObjectJsonIntoPairs(json);
		if(pairs.length != fields.length) throw new Exception("Wrong type!\nJSON: " + json + "\nTYPE: " + obj.getClass().getName());
		for(int i=0;i<fields.length;i++){
			String pair = pairs[i];
			String[] temp = splitPairIntoKeyAndValue(pair, false);
			Field field = getFieldWithName(fields, temp[0]);
			if(field == null) throw new Exception("Given field does not exist in given type. Field name: " + temp[0]);
			if(!temp[1].equals("null")) {
				if(!Enum.class.isAssignableFrom (field.getType ()))
					field.set (output, deserializeObject (temp[1], field.getType ().newInstance ()));
				else field.set (output, Enum.valueOf ((Class<Enum>) field.getType (),
						removeQuotes (temp[1])));
			} else field.set(output, null);
		}
		return output;
	}
	
	private static <T> Collection<T> deserializeArrayOrCollection(String json, T obj) throws Exception {
		if(!isArray(obj) || isCollection(obj)) throw new IllegalArgumentException("Given object is not array or collection! OBJECT: " + obj.getClass().getName());
		Class<?> type = (isArray(obj))? obj.getClass().getComponentType() : (Class<?>)((ParameterizedType)obj.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
		Collection output = new ArrayList<T>();
		String[] values = splitObjectJsonIntoPairs(json);
		for(String value : values)
			output.add((T) deserializeObject(value, type.getConstructor().newInstance()));
		return output;
	}
	
	private static <T> Object deserializeMap(String json, T obj) throws Exception {
		if(!isMap(obj)) throw new IllegalArgumentException("Given object is not Map! OBJECT: " + obj.getClass().getName());
		
		Class<?> keyClass = (Class<?>)((ParameterizedType)obj.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
		Class<?> valueClass = (Class<?>)((ParameterizedType)obj.getClass().getGenericSuperclass()).getActualTypeArguments()[1];
		Map output = new HashMap<Object, Object> ();
		String[] pairs = splitObjectJsonIntoPairs(json);
		for(String pair : pairs){
			String[] values = splitPairIntoKeyAndValue(pair, true);
			output.put(deserializeObject(values[0], keyClass), deserializeObject(values[1], valueClass));
		}
		return output;
	}
	
	private static String[] splitObjectJsonIntoPairs(String json) {
		ArrayList<String> list = new ArrayList<String>();
		json = json.substring(1, json.length()-1);
		int[] quotes = findAll(json, '\"');
		int[] opBraces = findAll(json, '{');
		int[] clBraces = findAll(json, '}');
		int start = 0;
		for(int i=0;i<json.length();i++){
			if(json.charAt(i) == ',' && howManyAreSmaller(quotes, i) % 2 == 0 && howManyAreSmaller(opBraces, i) == howManyAreSmaller(clBraces, i)){
				list.add(json.substring(start, i));
				start = i + 1;
			}
		}
		list.add(json.substring(start));
		return list.toArray(new String[list.size()]);
	}
	
	private static String[] splitPairIntoKeyAndValue(String json, Boolean isMap) {
		if(!isMap){
			String[] output = new String[]{
					removeQuotes(json.substring(0, json.indexOf(':'))),
					json.substring(json.indexOf(':')+1)
			};
			return output;
			
		} else {
			int[] quotes = findAll(json, '\"');
			int[] opBraces = findAll(json, '{');
			int[] clBraces = findAll(json, '}');
			int start = 0;
			String[] output = new String[2];
			for(int i=0;i<json.length();i++){
				if(json.charAt(i) == ':' && howManyAreSmaller(quotes, i) % 2 == 0 && howManyAreSmaller(opBraces, i) == howManyAreSmaller(clBraces, i)){
					output[0] = json.substring(start, i);
					start = i + 1;
					break;
				}
			}
			output[1] = json.substring(start);
			return output;
		}
	}
	
	private static String removeQuotes(String t) {
		String output = "";
		for(int i=0;i<t.length();i++)
			if(t.charAt(i) != '\"') output += t.charAt(i);
		return output;
	}
	
	private static int[] findAll(String json, char c) {
		ArrayList<Integer> temp = new ArrayList<Integer>();
		for(int i=0;i<json.length();i++)
			if(json.charAt(i) == c) temp.add(i);
		int[] output = new int[temp.size()];
		for(int i=0;i<output.length;i++)
			output[i] = temp.get(i);
		return output;
	}
	
	private static int howManyAreSmaller(int[] array, int value) {
		int output = 0;
		for(int i=0;i<array.length;i++)
			if(array[i] < value) output++;
		return output;
	}
	
	private static Field getFieldWithName(Field[] fields, String name) {
		for(int i=0;i<fields.length;i++)
			if(fields[i].getName().equals(name)) return fields[i];
		return null;
	}
	
	private static String trimJson(String json) {
		int[] quotes = findAll(json, '\"');
		String output = "";
		for(int i=0;i<json.length();i++) {
			if(Character.isWhitespace(json.charAt(i)) && howManyAreSmaller(quotes, i) % 2 == 0) continue;
			else output += json.charAt(i);
		}
		output = output.replace("\\n", "\n");
		return output;
	}
	
	public static <T> T[] rewriteArray (Object[] array, T[] out) {
		T[] output = (T[]) Array.newInstance(out.getClass().getComponentType(), out.length);
		for(int i=0;i<array.length;i++) {
			output[i] = (T) array[i];
		}
		return output;
	}
	
}
