package com.xfdream.music.data;

public class MusicNum {
	public static int num=0;
	public static int play=0;
	public static  int _id=0;
	public static  boolean isok=false;
	private static boolean f=false;
	private static boolean d=false;
	private  static boolean c=false;
	private  static boolean b=false;
	private  static boolean a=false;
	private  static boolean e=false;
	private  static boolean g=false;
	private  static boolean h=false;
	private  static boolean i=false;
	private  static boolean j=false;
	private  static boolean k=false;
	private  static  boolean l=false;
	private  static  boolean m=false;
	private  static boolean n=false;
	private  static  boolean o=false;
	private  static boolean p=false;
	private  static boolean q=false;
	private  static  boolean r=false;
	private  static  boolean s=false;
	private  static  boolean t=false;
	private  static  boolean ser=false;
	private  static  boolean qq=false;
	private  static  boolean z=false;
	public   static boolean isbutton[]={a,b,c,d,e,f,g,h,i,j,k,l,m,n,o,p,q,r,s,t,qq,z};
public static void putint(int send){
	num=send;
}
public static int getint(){
	return num;
}

public static void putplay(int playfd){
	play=playfd;
}
public static int getplay(){
	return play;
	
}
public static void put_id(int sednd){
	_id=sednd;
}
public static int get_id(){
	return _id;
}
public static void putisok(boolean sednd){
	isok=sednd;
}
public static boolean getisok(){
	return isok;
}



public static void putser(boolean sa){
	ser=sa;
}
public static boolean getser(){
	return ser;
}





public static void putusbtn(int i,Boolean sa){
	isbutton[i]=sa;
}
public static boolean getbtn(int j){
	return isbutton[j];
}

}
