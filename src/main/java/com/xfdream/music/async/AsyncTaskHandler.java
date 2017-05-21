package com.xfdream.music.async;

import android.os.Handler;
import android.os.Message;

/**
 * 
 * 
 * @author Administrator
 *         <p>
 *         瀹炵幇AsyncTask鐩稿叧鐨勫紓姝ュ姞杞藉姛鑳?
 *         </p>
 *         <p>
 *         1.execute(Params... params)锛屾墽琛屼竴涓紓姝ヤ换鍔★紝闇€瑕佹垜浠湪浠ｇ爜涓皟鐢ㄦ鏂规硶锛岃Е鍙戝紓姝ヤ换鍔＄殑鎵ц銆?
 *         </p>
 *         <p>
 *         2.onPreExecute()锛屽湪execute(Params...
 *         params)琚皟鐢ㄥ悗绔嬪嵆鎵ц锛屼竴鑸敤鏉ュ湪鎵ц鍚庡彴浠诲姟鍓嶅UI鍋氫竴浜涙爣璁般€?
 *         </p>
 *         <p>
 *         3.doInBackground(Params...params)锛屽湪onPreExecute()瀹屾垚鍚庣珛鍗虫墽琛岋紝鐢ㄤ簬鎵ц杈冧负璐规椂鐨勬搷浣滐紝
 *         姝ゆ柟娉曞皢鎺ユ敹杈撳叆鍙傛暟鍜岃繑鍥炶绠楃粨鏋溿€?鍦ㄦ墽琛岃繃绋嬩腑鍙互璋冪敤publishProgress(Progress...
 *         values)鏉ユ洿鏂拌繘搴︿俊鎭€?
 *         </p>
 *         <p>
 *         4.onProgressUpdate(Progress... values)锛屽湪璋冪敤publishProgress(Progress...
 *         values)鏃讹紝姝ゆ柟娉曡鎵ц锛岀洿鎺ュ皢杩涘害淇℃伅鏇存柊鍒癠I缁勪欢涓娿€?5.onPostExecute(Result
 *         result)锛屽綋鍚庡彴鎿嶄綔缁撴潫鏃讹紝姝ゆ柟娉曞皢浼氳璋冪敤锛岃绠楃粨鏋滃皢鍋氫负鍙傛暟浼犻€掑埌姝ゆ柟娉曚腑锛岀洿鎺ュ皢缁撴灉鏄剧ず鍒癠I缁勪欢涓娿€?
 *         </p>
 *         <p>
 *         鐭ラ亾鐩稿叧鐨勫姛鑳藉悗锛屼究鍙互鐢╤andler鏉ョ畝鍗曞疄鐜?
 *         </p>
 */
public abstract class AsyncTaskHandler {

	private Handler mHandler;

	private final static int ACTION_ONPREEXECUTE = 1;
	private final static int ACTION_ONPROGRESS = 2;
	private final static int ACTION_ONSUCCESS = 3;
	private final static int ACTION_ONERROR = 4;

	public AsyncTaskHandler() {

		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case ACTION_ONPREEXECUTE:
					onPreExecute();
					break;
				case ACTION_ONPROGRESS:
					onProgressUpdate((Integer) msg.obj);
					break;
				case ACTION_ONSUCCESS:
					onPostExecute(msg.obj);
					break;
				case ACTION_ONERROR:
					onError((Exception) msg.obj);
					break;
				}
			}
		};
	}

	/**
	 * 鎵ц寮傛浠诲姟
	 */
	public void execute() {

		new Thread(new Runnable() {

			public void run() {
				try {
					sendOnPreExecute();
					Object result = doInBackground();
					sendOnSuccess(result);
				} catch (Exception e) {
					e.printStackTrace();
					sendOnError(e);
				}
			}
		}).start();

	}

	/**
	 * 浠诲姟鎵ц鍓?
	 */
	protected void onPreExecute() {
	}

	/**
	 * 鍦╫nPreExecute()瀹屾垚鍚庣珛鍗虫墽琛岋紝鐢ㄤ簬鎵ц杈冧负璐规椂鐨勬搷浣滐紝姝ゆ柟娉曞皢鎺ユ敹杈撳叆鍙傛暟鍜岃繑鍥炶绠楃粨鏋溿€?
	 * 鍦ㄦ墽琛岃繃绋嬩腑鍙互璋冪敤publishProgress(int progress)鏉ユ洿鏂拌繘搴︿俊鎭€?
	 * 
	 * @return
	 */
	protected abstract Object doInBackground() throws Exception;

	/**
	 * 姝ゆ柟娉曡鎵ц锛岀洿鎺ュ皢杩涘害淇℃伅鏇存柊鍒癠I缁勪欢涓娿€?
	 * 
	 * @param progress
	 */
	protected void onProgressUpdate(int progress) {

	}

	/**
	 * 褰撳悗鍙版搷浣滅粨鏉熸椂锛屾鏂规硶灏嗕細琚皟鐢紝璁＄畻缁撴灉灏嗗仛涓哄弬鏁颁紶閫掑埌姝ゆ柟娉曚腑锛岀洿鎺ュ皢缁撴灉鏄剧ず鍒癠I缁勪欢涓娿€?
	 * 
	 * @param result
	 */
	protected abstract void onPostExecute(Object result);

	/**
	 * 寮傛浠诲姟鎵ц澶辫触
	 * 
	 * @param e
	 */
	protected void onError(Exception e) {
	}

	private void sendOnPreExecute() {
		mHandler.sendEmptyMessage(ACTION_ONPREEXECUTE);
	}

	private void sendOnSuccess(Object result) {
		Message msg = mHandler.obtainMessage();
		msg.what = ACTION_ONSUCCESS;
		msg.obj = result;
		mHandler.sendMessage(msg);
	}

	private void sendOnError(Exception e) {
		Message msg = mHandler.obtainMessage();
		msg.what = ACTION_ONERROR;
		msg.obj = e;
		mHandler.sendMessage(msg);
	}

	protected void publishProgress(long progress) {
		Message msg = mHandler.obtainMessage();
		msg.what = ACTION_ONPROGRESS;
		msg.obj = progress;
		mHandler.sendMessage(msg);
	}

}
