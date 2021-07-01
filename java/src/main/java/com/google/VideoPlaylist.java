package com.google;

import java.util.ArrayList;

/** A class used to represent a Playlist */
class VideoPlaylist implements Comparable<VideoPlaylist>{

    private final String name;
    private ArrayList<Video> videos;

    public VideoPlaylist(String name){
        this.name = name;
        this.videos = new ArrayList<>();
    }

    public String getName(){
        return name;
    }

    public void add(Video video){
        videos.add(video);
    }

    public boolean contains(Video video){
        return videos.contains(video);
    }

    public ArrayList<Video> getVideos(){
        return videos;
    }

    public void remove(Video video){
        videos.remove(video);
    }

    public void clear(){
        videos.clear();
    }

    @Override
    public int compareTo(VideoPlaylist o) {
        if(this.name.compareTo(o.name) > 0) return 1;
        else if(this.name.compareTo(o.name) < 0) return -1;
        return 0;
    }
}
