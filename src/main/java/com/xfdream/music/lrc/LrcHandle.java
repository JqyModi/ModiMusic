package com.xfdream.music.lrc;

/**
 * Created by Modi on 2016/11/23.
 * 邮箱：1294432350@qq.com
 */

import com.xfdream.music.entity.Sentence;
import com.xfdream.music.util.LogUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LrcHandle {
    private List<Sentence> mWords = new ArrayList<Sentence>();
    private List<Integer> mTimeList = new ArrayList<Integer>();
    private int currentTime = 0;
    private int currentIndex = 0;

    public void readLRC(final String path) {
        File file = new File(path);
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            InputStreamReader inputStreamReader = new InputStreamReader(
                    fileInputStream, "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(
                    inputStreamReader);
            String s = "";
            while ((s = bufferedReader.readLine()) != null) {
                Sentence lSentence = new Sentence();
                LogUtil.e("s--------->" + s);
                if ((s.indexOf("[ti:") != -1) || (s.indexOf("[ar:") != -1)
                        || (s.indexOf("[al:") != -1) || (s.indexOf("[by:") != -1)) {
                    s = s.substring(s.indexOf(":") + 1, s.indexOf("]"));
                } else if (s.equals("")) {
                    //mWords.add(s);
                    continue;
                } else {
                    // [02:12.22]
                    String ss = s.substring(s.indexOf("["), s.indexOf("]") + 1);

                    String lS = addTimeToList(s);
                    int lI = timeHandler(lS);
                    lSentence.setToTime(lI);
                    s = s.replace(ss, "");
                    lSentence.setContent(s);

                }
                if (mWords.size()==0){
                    mWords.add(lSentence);
                }else {
                    lSentence.setFromTime(mWords.get(mWords.size()-1).getToTime());
                    mWords.add(lSentence);
                }
            }

            bufferedReader.close();
            inputStreamReader.close();
            fileInputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            mWords.add(new Sentence("歌词文件没有发现"));
        } catch (IOException e) {
            e.printStackTrace();
            mWords.add(new Sentence("歌词文件没有发现"));
        }
    }

    public List<Sentence> getWords() {
        return mWords;
    }
    public List<Sentence> getWordsByCurrentTime(int currentTime) {

        if (mWords != null && mWords.size()>0){
            for (int i=0;i<mWords.size();i++){
                boolean lInTime = mWords.get(i).isInTime(currentTime);
                if (mWords.get(i).getFromTime() == currentTime){
                    for (int j=0;j<i;j++){
                        mWords.remove(j);
                    }
                }else if (lInTime){
                    for (int j=0;j<i;j++){
                        mWords.remove(j);
                    }
                }
            }
        }
        return mWords;
    }

    public List<Integer> getTime() {
        return mTimeList;
    }
    private int timeHandler(String string) {
        string = string.replace(".", ":");
        String timeData[] = string.split(":");
        int minute = Integer.parseInt(timeData[0]);
        int second = Integer.parseInt(timeData[1]);
        int millisecond = Integer.parseInt(timeData[2]);

        int currentTime = (minute * 60 + second) * 1000 + millisecond * 10;

        return currentTime;
    }

    private String addTimeToList(String string) {
        Matcher matcher = Pattern.compile(
                "\\[\\d{1,2}:\\d{1,2}([\\.:]\\d{1,2})?\\]").matcher(string);
        if (matcher.find()) {
            String str = matcher.group();
            /*mTimeList.add(new LrcHandle().timeHandler(str.substring(1,
                    str.length() - 1)));*/
            return str.substring(1,
                    str.length() - 1);
        }
        return null;
    }

    public void setCurrentTime(int currentTime) {
        this.currentTime = currentTime;
    }

    public int getCurrentTime() {
        return currentTime;
    }

    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex;
    }
    public int getCurrentIndex() {
        return currentIndex;
    }
}