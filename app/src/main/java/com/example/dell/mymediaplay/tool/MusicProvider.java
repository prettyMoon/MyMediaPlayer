package com.example.dell.mymediaplay.tool;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import java.util.ArrayList;

/**
 * Created by DELL on 2016/5/22.
 */
public class MusicProvider {
    public static ArrayList<MusicInfo> getMusicInfos(Context context) {
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null,
                MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        ArrayList<MusicInfo> musicInfos = new ArrayList<MusicInfo>();
        for (int i = 0; i < cursor.getCount(); i++) {
            MusicInfo musicInfo = new MusicInfo();
            cursor.moveToNext();
            long id = cursor.getLong(cursor
                    .getColumnIndex(MediaStore.Audio.Media._ID));   //音乐id
            String title = cursor.getString((cursor
                    .getColumnIndex(MediaStore.Audio.Media.TITLE)));//音乐标题
            String artist = cursor.getString(cursor
                    .getColumnIndex(MediaStore.Audio.Media.ARTIST));//艺术家
            long duration = cursor.getLong(cursor
                    .getColumnIndex(MediaStore.Audio.Media.DURATION));//时长
            long size = cursor.getLong(cursor
                    .getColumnIndex(MediaStore.Audio.Media.SIZE));  //文件大小
            String url = cursor.getString(cursor
                    .getColumnIndex(MediaStore.Audio.Media.DATA));              //文件路径
            int isMusic = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.Audio.Media.IS_MUSIC));//是否为音乐
            if (isMusic != 0) {     //只把音乐添加到集合当中
                musicInfo.setId(id);
                musicInfo.setTitle(title);
                musicInfo.setArtist(artist);
                musicInfo.setDuration(duration);
                musicInfo.setSize(size);
                musicInfo.setUrl(url);
                musicInfos.add(musicInfo);
            }
        }
        return musicInfos;
    }
}
