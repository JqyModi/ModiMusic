package com.xfdream.music.observable;

import java.util.Observable;

/**
 * 瑙傚療鑰咃紝鐢ㄦ潵瑙傚療涓€浜涙搷浣滐紝濡備富棰橀鑹茬殑鏀瑰彉锛屼綍鏃跺脊鍑烘洿鏂扮殑绐楀彛绛夌瓑銆?
 * 
 * @author Administrator 
 */
public class ObserverManage extends Observable {

	private static ObserverManage myobserver = null;

	public static ObserverManage getObserver() {
		if (myobserver == null) {
			myobserver = new ObserverManage();
		}
		return myobserver;
	}

	public void setMessage(Object data) {
		myobserver.setChanged();
		myobserver.notifyObservers(data);
	}
}
