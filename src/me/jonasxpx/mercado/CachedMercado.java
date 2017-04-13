package me.jonasxpx.mercado;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;


public abstract class CachedMercado{

	
	private static ArrayList<SellItem> cached = new ArrayList<>();
	
	public static void save(File location){
		try(ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(location))){
			out.writeObject(cached);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	public static void loadCached(File location){
		if(location.exists()){
			try(ObjectInputStream oi = new ObjectInputStream(new FileInputStream(location))){
				Object ob;
				if((ob = oi.readObject()) instanceof ArrayList<?>){
					cached = (ArrayList<SellItem>) ob;
				}
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public static ArrayList<?> getCached(){
		return cached;
	}
}
