package com.google;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/** A class used to represent a video. */
class Video implements Comparable<Video>{

  private final String title;
  private final String videoId;
  private final List<String> tags;
  String flag;

  Video(String title, String videoId, List<String> tags) {
    this.title = title;
    this.videoId = videoId;
    this.tags = Collections.unmodifiableList(tags);
    this.flag = null;
  }

  /** Returns the title of the video. */
  String getTitle() {
    return title;
  }

  /** Returns the video id of the video. */
  String getVideoId() {
    return videoId;
  }

  /** Returns a readonly collection of the tags of the video. */
  List<String> getTags() {
    return tags;
  }

  public void flag(String reason){
    flag = reason;
  }

  public boolean isFlagged(){
    return flag != null;
  }

  public String getReason(){
    return "(reason: "+ flag+")";
  }

  public void allow(){
    flag = null;
  }

  @Override
  public int compareTo(Video o) {
    if(this.title.compareTo(o.title) > 0) return 1;
    else if(this.title.compareTo(o.title) < 0) return -1;
    return 0;
  }

  @Override
  public String toString(){
    // Changing default lists to string into string where values are separated by spaces and changing prefix and suffix (Never knew this is possible thanks to Google now I know :D )
    String tags = getTags().stream().map(n -> String.valueOf(n)).collect(Collectors.joining(" ", "[", "]"));
    return (getTitle() + " (" + getVideoId() + ") "+ tags);
  }

}
